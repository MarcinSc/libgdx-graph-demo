package com.gempukku.libgdx.graph.pipeline.loader.node;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.data.GraphNodeInput;

public class GraphNodeInputImpl<T extends FieldType> implements GraphNodeInput<T> {
    private String id;
    private String name;
    private Array<T> acceptedTypes;
    private boolean required;
    private boolean mainConnection;

    public GraphNodeInputImpl(String id, String name, T... acceptedType) {
        this(id, name, false, acceptedType);
    }

    public GraphNodeInputImpl(String id, String name, boolean required, T... acceptedType) {
        this(id, name, required, false, acceptedType);
    }

    public GraphNodeInputImpl(String id, String name, boolean required, boolean mainConnection, T... acceptedType) {
        this.id = id;
        this.name = name;
        this.required = required;
        this.mainConnection = mainConnection;
        this.acceptedTypes = new Array<>(acceptedType);
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public boolean isMainConnection() {
        return mainConnection;
    }

    @Override
    public String getFieldId() {
        return id;
    }

    @Override
    public String getFieldName() {
        return name;
    }

    @Override
    public Array<T> getAcceptedPropertyTypes() {
        return acceptedTypes;
    }
}
