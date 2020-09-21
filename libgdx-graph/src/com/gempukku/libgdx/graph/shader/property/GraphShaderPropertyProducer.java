package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.PropertySource;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public interface GraphShaderPropertyProducer {
    ShaderFieldType getType();

    PropertySource createProperty(String name, JsonValue data, boolean designTime);
}
