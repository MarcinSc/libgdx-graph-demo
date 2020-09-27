package com.gempukku.libgdx.graph;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectSet;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GraphLoader {
    private static final int VERSION_MAJOR = 0;
    private static final int VERSION_MINOR = 1;
    private static final int VERSION_PATCH = 0;
    public static final String VERSION = VERSION_MAJOR + "." + VERSION_MINOR + "." + VERSION_PATCH;

    public static <T> T loadGraph(InputStream inputStream, GraphLoaderCallback<T> graphLoaderCallback) throws IOException {
        try {
            JsonReader parser = new JsonReader();
            JsonValue graph = (JsonValue) parser.parse(new InputStreamReader(inputStream));
            return loadGraph(graph, graphLoaderCallback);
        } catch (Exception exp) {
            throw new IOException("Unable to parse graph", exp);
        }
    }

    public static <T> T loadGraph(JsonValue graph, GraphLoaderCallback<T> graphLoaderCallback) {
        // Assuming default
        String version = graph.has("version") ? graph.getString("version") : "0.1.0";
        if (!canReadVersion(version)) {
            throw new IllegalArgumentException("Unable to read a graph of version " + version);
        }
        if (!VERSION.equals(version))
            Gdx.app.debug("GraphLoader", "Reading a graph from different version " + VERSION + " != " + version);

        graphLoaderCallback.start();
        for (JsonValue object : graph.get("nodes")) {
            String type = object.getString("type");
            String id = object.getString("id");
            float x = object.getFloat("x");
            float y = object.getFloat("y");
            JsonValue data = object.get("data");
            graphLoaderCallback.addPipelineNode(id, type, x, y, data);
        }
        for (JsonValue connection : graph.get("connections")) {
            String fromNode = connection.getString("fromNode");
            String fromField = connection.getString("fromField");
            String toNode = connection.getString("toNode");
            String toField = connection.getString("toField");
            graphLoaderCallback.addPipelineVertex(fromNode, fromField, toNode, toField);
        }
        for (JsonValue property : graph.get("properties")) {
            String type = property.getString("type");
            String name = property.getString("name");
            JsonValue data = (JsonValue) property.get("data");
            graphLoaderCallback.addPipelineProperty(type, name, data);
        }
        JsonValue groups = graph.get("groups");
        if (groups != null) {
            for (JsonValue group : groups) {
                String name = group.getString("name");
                JsonValue nodes = group.get("nodes");
                ObjectSet<String> nodeIds = new ObjectSet<>();
                for (JsonValue node : nodes) {
                    nodeIds.add(node.asString());
                }
                graphLoaderCallback.addNodeGroup(name, nodeIds);
            }
        }

        return graphLoaderCallback.end();
    }

    private static boolean canReadVersion(String version) {
        String[] split = version.split("\\.");
        if (split.length != 3)
            return false;

        int major = Integer.parseInt(split[0]);
        int minor = Integer.parseInt(split[1]);
        int patch = Integer.parseInt(split[2]);

        // Breaking compatibility on major version
        if (major != VERSION_MAJOR)
            return false;
        // Should support every previous minor version
        if (minor > VERSION_MINOR)
            return false;
        // Should support same minor if previous patch version
        if (minor == VERSION_MINOR && patch > VERSION_PATCH)
            return false;

        return true;
    }
}
