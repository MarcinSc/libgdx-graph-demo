package com.gempukku.libgdx.graph.data;

import com.badlogic.gdx.utils.JsonValue;

public interface GraphNode<T extends FieldType> {
    String getId();

    String getType();

    JsonValue getData();

    boolean isInputField(String fieldId);

    NodeConfiguration<T> getConfiguration();
}
