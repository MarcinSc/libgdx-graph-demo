package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.PropertySource;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class ColorShaderPropertyProducer implements GraphShaderPropertyProducer {
    @Override
    public ShaderFieldType getType() {
        return ShaderFieldType.Color;
    }

    @Override
    public PropertySource createProperty(String name, JsonValue data, boolean designTime) {
        final Color color = Color.valueOf(data.getString("color"));
        return new PropertySource(name, ShaderFieldType.Color, color);
    }
}
