package com.gempukku.libgdx.graph.shader.node.provided;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.UniformSetters;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.provided.PixelSizeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PixelSizeShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public PixelSizeShaderNodeBuilder() {
        super(new PixelSizeShaderNodeConfiguration());
    }

    @Override
    protected Map<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        commonShaderBuilder.addUniformVariable("u_pixelSize", "vec2", true, UniformSetters.pixelSize);
        Map<String, DefaultFieldOutput> result = new HashMap<>();
        if (producedOutputs.contains("size")) {
            result.put("size", new DefaultFieldOutput(ShaderFieldType.Vector2, "u_pixelSize"));
        }
        if (producedOutputs.contains("x")) {
            result.put("x", new DefaultFieldOutput(ShaderFieldType.Vector2, "u_pixelSize.x"));
        }
        if (producedOutputs.contains("y")) {
            result.put("y", new DefaultFieldOutput(ShaderFieldType.Vector2, "u_pixelSize.y"));
        }
        return result;
    }
}
