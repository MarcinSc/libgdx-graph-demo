package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.PropertySource;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class FloatShaderPropertyProducer implements GraphShaderPropertyProducer {
    @Override
    public ShaderFieldType getType() {
        return ShaderFieldType.Float;
    }

    @Override
    public PropertySource createProperty(String name, JsonValue data, boolean designTime) {
        final float x = data.getFloat("x");
        return new PropertySource(name, ShaderFieldType.Float, x);
    }
}
