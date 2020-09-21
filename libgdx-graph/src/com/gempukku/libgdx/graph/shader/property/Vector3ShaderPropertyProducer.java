package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.PropertySource;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class Vector3ShaderPropertyProducer implements GraphShaderPropertyProducer {
    @Override
    public ShaderFieldType getType() {
        return ShaderFieldType.Vector3;
    }

    @Override
    public PropertySource createProperty(String name, JsonValue data, boolean designTime) {
        final float x = data.getFloat("x");
        final float y = data.getFloat("y");
        final float z = data.getFloat("z");
        return new PropertySource(name, ShaderFieldType.Vector3, new Vector3(x, y, z));
    }
}
