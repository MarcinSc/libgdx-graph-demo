package com.gempukku.libgdx.graph;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.FieldType;

import java.util.function.Function;

public class SameTypeOutputTypeFunction<T extends FieldType> implements Function<ObjectMap<String, T>, T> {
    private String[] inputs;

    public SameTypeOutputTypeFunction(String... input) {
        this.inputs = input;
    }

    @Override
    public T apply(ObjectMap<String, T> map) {
        T resolvedType = null;
        for (String input : inputs) {
            T type = map.get(input);
            if (type == null)
                return null;
            if (resolvedType != null && resolvedType != type)
                return null;
            resolvedType = type;
        }

        return resolvedType;
    }
}
