package com.gempukku.libgdx.graph.data;

import com.badlogic.gdx.utils.ObjectMap;

public interface NodeConfiguration<T extends FieldType> {
    String getType();

    String getName();

    String getMenuLocation();

    ObjectMap<String, GraphNodeInput<T>> getNodeInputs();

    ObjectMap<String, GraphNodeOutput<T>> getNodeOutputs();

    boolean isValid(ObjectMap<String, T> inputTypes, Iterable<? extends GraphProperty<T>> properties);
}
