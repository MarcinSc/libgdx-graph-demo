package com.gempukku.libgdx.graph.pipeline.loader.node;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.data.GraphNodeOutput;

import java.util.function.Function;

public class GraphNodeOutputImpl<T extends FieldType> implements GraphNodeOutput<T> {
    private String id;
    private String name;
    private boolean mainConnection;
    private Function<ObjectMap<String, T>, T> outputTypeFunction;
    private Array<T> propertyTypes;

    public GraphNodeOutputImpl(String id, String name, final T producedType) {
        this(id, name, false, producedType);
    }

    public GraphNodeOutputImpl(String id, String name, boolean mainConnection, final T producedType) {
        this(id, name, mainConnection, new Function<ObjectMap<String, T>, T>() {
            @Override
            public T apply(ObjectMap<String, T> stringTMap) {
                return producedType;
            }
        }, producedType);
    }

    public GraphNodeOutputImpl(String id, String name, Function<ObjectMap<String, T>, T> outputTypeFunction, T... producedType) {
        this(id, name, false, outputTypeFunction, producedType);
    }

    public GraphNodeOutputImpl(String id, String name, boolean mainConnection, Function<ObjectMap<String, T>, T> outputTypeFunction, T... producedType) {
        this.id = id;
        this.name = name;
        this.mainConnection = mainConnection;
        this.outputTypeFunction = outputTypeFunction;
        this.propertyTypes = new Array<>(producedType);
    }

    @Override
    public String getFieldId() {
        return id;
    }

    @Override
    public boolean isMainConnection() {
        return mainConnection;
    }

    @Override
    public String getFieldName() {
        return name;
    }

    @Override
    public Array<T> getProducableFieldTypes() {
        return propertyTypes;
    }

    @Override
    public boolean supportsMultiple() {
        return !mainConnection;
    }

    @Override
    public T determineFieldType(ObjectMap<String, T> inputs) {
        return outputTypeFunction.apply(inputs);
    }
}
