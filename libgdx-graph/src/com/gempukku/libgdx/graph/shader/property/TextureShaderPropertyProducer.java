package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.WhitePixel;
import com.gempukku.libgdx.graph.shader.PropertySource;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class TextureShaderPropertyProducer implements GraphShaderPropertyProducer {
    @Override
    public ShaderFieldType getType() {
        return ShaderFieldType.TextureRegion;
    }

    @Override
    public PropertySource createProperty(String name, JsonValue data, boolean designTime) {
        if (designTime)
            return new PropertySource(name, ShaderFieldType.TextureRegion, WhitePixel.sharedInstance.textureRegion);
        else
            return new PropertySource(name, ShaderFieldType.TextureRegion, null);
    }
}
