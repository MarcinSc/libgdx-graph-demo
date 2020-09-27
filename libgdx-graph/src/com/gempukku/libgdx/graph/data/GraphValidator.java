package com.gempukku.libgdx.graph.data;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;

public class GraphValidator<T extends GraphNode<W>, U extends GraphConnection, V extends GraphProperty<W>, W extends FieldType> {
    public ValidationResult<T, U, V, W> validateGraph(Graph<T, U, V, W> graph, String nodeEnd) {
        ValidationResult<T, U, V, W> result = new ValidationResult<>();

        T end = graph.getNodeById(nodeEnd);
        if (end == null)
            return result;

        // Check duplicate property names
        ObjectMap<String, V> propertyNames = new ObjectMap<>();
        for (V property : graph.getProperties()) {
            String propertyName = property.getName();
            if (propertyNames.containsKey(propertyName)) {
                result.addErrorProperty(property);
                result.addErrorProperty(propertyNames.get(propertyName));
            }
            propertyNames.put(propertyName, property);
        }

        boolean cyclic = isCyclic(result, graph, nodeEnd);
        if (!cyclic) {
            // Do other Validation
            validateNode(result, graph, nodeEnd, new ObjectMap<String, NodeOutputs<W>>());
        }
        return result;
    }

    private NodeOutputs<W> validateNode(ValidationResult<T, U, V, W> result, Graph<T, U, V, W> graph, String nodeId,
                                        ObjectMap<String, NodeOutputs<W>> nodeOutputs) {
        // Check if already validated
        NodeOutputs<W> outputs = nodeOutputs.get(nodeId);
        if (outputs != null)
            return outputs;

        T thisNode = graph.getNodeById(nodeId);
        ObjectSet<String> validatedFields = new ObjectSet<>();
        ObjectMap<String, W> inputsTypes = new ObjectMap<>();
        for (U incomingConnection : getIncomingConnections(graph, nodeId)) {
            String fieldTo = incomingConnection.getFieldTo();
            GraphNodeInput<W> input = thisNode.getConfiguration().getNodeInputs().get(fieldTo);
            T remoteNode = graph.getNodeById(incomingConnection.getNodeFrom());
            GraphNodeOutput<W> output = remoteNode.getConfiguration().getNodeOutputs().get(incomingConnection.getFieldFrom());

            // Validate the actual output is accepted by the input
            Array<W> acceptedPropertyTypes = input.getAcceptedPropertyTypes();
            if (!outputAcceptsPropertyType(output, acceptedPropertyTypes)) {
                result.addErrorConnection(incomingConnection);
            }

            validatedFields.add(fieldTo);
            NodeOutputs<W> outputFromRemoteNode = validateNode(result, graph, incomingConnection.getNodeFrom(), nodeOutputs);
            inputsTypes.put(fieldTo, outputFromRemoteNode.outputs.get(incomingConnection.getFieldFrom()));
        }

        for (GraphNodeInput<W> input : thisNode.getConfiguration().getNodeInputs().values()) {
            if (input.isRequired() && !validatedFields.contains(input.getFieldId())) {
                result.addErrorConnector(new NodeConnector(nodeId, input.getFieldId()));
            }
        }

        boolean valid = thisNode.getConfiguration().isValid(inputsTypes, graph.getProperties());
        if (!valid)
            result.addErrorNode(thisNode);

        ObjectMap<String, W> nodeOutputMap = new ObjectMap<>();
        for (GraphNodeOutput<W> value : thisNode.getConfiguration().getNodeOutputs().values()) {
            nodeOutputMap.put(value.getFieldId(), value.determineFieldType(inputsTypes));
        }

        NodeOutputs<W> nodeOutput = new NodeOutputs<>(nodeOutputMap);
        nodeOutputs.put(nodeId, nodeOutput);
        return nodeOutput;
    }

    private Iterable<U> getIncomingConnections(Graph<T, U, V, W> graph, String nodeId) {
        Array<U> result = new Array<>();
        for (U connection : graph.getConnections()) {
            if (connection.getNodeTo().equals(nodeId))
                result.add(connection);
        }
        return result;
    }

    private <W extends FieldType> boolean outputAcceptsPropertyType(GraphNodeOutput<W> output, Array<W> acceptedPropertyTypes) {
        Array<W> producablePropertyTypes = output.getProducableFieldTypes();
        for (W acceptedFieldType : acceptedPropertyTypes) {
            if (producablePropertyTypes.contains(acceptedFieldType, true))
                return true;
        }
        return false;
    }

    // This function is a variation of DFSUtil() in
    // https://www.geeksforgeeks.org/archives/18212
    private boolean isCyclicUtil(ValidationResult<T, U, V, W> validationResult, Graph<T, U, V, W> graph, String nodeId, ObjectSet<String> visited,
                                 ObjectSet<String> recStack) {
        // Mark the current node as visited and
        // part of recursion stack
        if (recStack.contains(nodeId)) {
            validationResult.addErrorNode(graph.getNodeById(nodeId));
            return true;
        }

        if (visited.contains(nodeId))
            return false;

        visited.add(nodeId);
        recStack.add(nodeId);

        ObjectSet<String> connectedNodes = new ObjectSet<>();
        for (U incomingConnection : getIncomingConnections(graph, nodeId)) {
            connectedNodes.add(incomingConnection.getNodeFrom());
        }

        for (String connectedNode : connectedNodes) {
            if (isCyclicUtil(validationResult, graph, connectedNode, visited, recStack)) {
                return true;
            }
        }
        recStack.remove(nodeId);

        return false;
    }

    private boolean isCyclic(ValidationResult<T, U, V, W> validationResult, Graph<T, U, V, W> graph, String start) {
        ObjectSet<String> visited = new ObjectSet<>();
        ObjectSet<String> recStack = new ObjectSet<>();

        // Call the recursive helper function to
        // detect cycle in different DFS trees
        if (isCyclicUtil(validationResult, graph, start, visited, recStack)) {
            return true;
        }

        for (T node : graph.getNodes()) {
            String nodeId = node.getId();
            if (!visited.contains(nodeId)) {
                validationResult.addWarningNode(node);
            }
        }
        return false;
    }

    private static class NodeOutputs<W> {
        private ObjectMap<String, W> outputs;

        public NodeOutputs(ObjectMap<String, W> outputs) {
            this.outputs = outputs;
        }
    }

    public static class ValidationResult<T extends GraphNode<W>, U extends GraphConnection, V extends GraphProperty<W>, W extends FieldType> {
        private final ObjectSet<T> errorNodes = new ObjectSet<>();
        private final ObjectSet<T> warningNodes = new ObjectSet<>();
        private final ObjectSet<U> errorConnections = new ObjectSet<>();
        private final ObjectSet<NodeConnector> errorConnectors = new ObjectSet<>();
        private final ObjectSet<V> errorProperties = new ObjectSet<>();

        public void addErrorNode(T node) {
            errorNodes.add(node);
        }

        public void addWarningNode(T node) {
            warningNodes.add(node);
        }

        public void addErrorConnection(U connection) {
            errorConnections.add(connection);
        }

        public void addErrorConnector(NodeConnector nodeConnector) {
            errorConnectors.add(nodeConnector);
        }

        public void addErrorProperty(V property) {
            errorProperties.add(property);
        }

        public ObjectSet<T> getErrorNodes() {
            return errorNodes;
        }

        public ObjectSet<T> getWarningNodes() {
            return warningNodes;
        }

        public ObjectSet<U> getErrorConnections() {
            return errorConnections;
        }

        public ObjectSet<NodeConnector> getErrorConnectors() {
            return errorConnectors;
        }

        public ObjectSet<V> getErrorProperties() {
            return errorProperties;
        }

        public boolean hasErrors() {
            return !errorNodes.isEmpty() || !errorConnections.isEmpty() || !errorConnectors.isEmpty() || !errorProperties.isEmpty();
        }

        public boolean hasWarnings() {
            return !warningNodes.isEmpty();
        }
    }
}
