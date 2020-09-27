package com.gempukku.libgdx.graph;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.data.GraphNodeInput;
import com.gempukku.libgdx.graph.data.GraphNodeOutput;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.data.NodeConfiguration;

public class NodeConfigurationImpl<T extends FieldType> implements NodeConfiguration<T> {
    private String type;
    private String name;
    private String menuLocation;
    private ObjectMap<String, GraphNodeInput<T>> nodeInputs = new OrderedMap<>();
    private ObjectMap<String, GraphNodeOutput<T>> nodeOutputs = new OrderedMap<>();

    public NodeConfigurationImpl(String type, String name, String menuLocation) {
        this.type = type;
        this.name = name;
        this.menuLocation = menuLocation;
    }

    public void addNodeInput(GraphNodeInput<T> input) {
        nodeInputs.put(input.getFieldId(), input);
    }

    public void addNodeOutput(GraphNodeOutput<T> output) {
        nodeOutputs.put(output.getFieldId(), output);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getMenuLocation() {
        return menuLocation;
    }

    @Override
    public ObjectMap<String, GraphNodeInput<T>> getNodeInputs() {
        return nodeInputs;
    }

    @Override
    public ObjectMap<String, GraphNodeOutput<T>> getNodeOutputs() {
        return nodeOutputs;
    }

    @Override
    public boolean isValid(ObjectMap<String, T> inputTypes, Iterable<? extends GraphProperty<T>> properties) {
        for (GraphNodeOutput<T> nodeOutput : nodeOutputs.values()) {
            T output = nodeOutput.determineFieldType(inputTypes);
            if (output == null)
                return false;
        }

        return true;
    }
}
