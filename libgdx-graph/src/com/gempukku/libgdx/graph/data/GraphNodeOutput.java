package com.gempukku.libgdx.graph.data;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public interface GraphNodeOutput<T extends FieldType> {
    boolean isMainConnection();

    String getFieldName();

    String getFieldId();

    Array<T> getProducableFieldTypes();

    T determineFieldType(ObjectMap<String, T> inputs);

    boolean supportsMultiple();
}
