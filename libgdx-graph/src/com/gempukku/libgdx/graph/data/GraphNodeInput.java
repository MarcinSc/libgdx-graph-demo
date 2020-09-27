package com.gempukku.libgdx.graph.data;

import com.badlogic.gdx.utils.Array;

public interface GraphNodeInput<T extends FieldType> {
    boolean isRequired();

    boolean isMainConnection();

    String getFieldName();

    String getFieldId();

    Array<T> getAcceptedPropertyTypes();
}
