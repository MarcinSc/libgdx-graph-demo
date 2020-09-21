package com.gempukku.libgdx.graph;

import com.badlogic.gdx.utils.JsonValue;

import java.util.Set;

public interface GraphLoaderCallback<T> {
    void start();

    void addPipelineNode(String id, String type, float x, float y, JsonValue data);

    void addPipelineVertex(String fromNode, String fromField, String toNode, String toField);

    void addPipelineProperty(String type, String name, JsonValue data);

    void addNodeGroup(String name, Set<String> nodeIds);

    T end();
}
