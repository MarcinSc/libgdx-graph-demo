package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.PropertySource;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class Vector2ShaderPropertyProducer implements GraphShaderPropertyProducer {
    @Override
    public ShaderFieldType getType() {
        return ShaderFieldType.Vector2;
    }

    @Override
    public PropertySource createProperty(String name, JsonValue data, boolean designTime) {
        final float x = data.getFloat("x");
        final float y = data.getFloat("y");
        return new PropertySource(name, ShaderFieldType.Vector2, new Vector2(x, y));
    }
}
