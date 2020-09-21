package com.gempukku.libgdx.graph.data;

import com.badlogic.gdx.utils.JsonValue;

public interface GraphProperty<T extends FieldType> {
    String getName();

    T getType();

    JsonValue getData();
}
