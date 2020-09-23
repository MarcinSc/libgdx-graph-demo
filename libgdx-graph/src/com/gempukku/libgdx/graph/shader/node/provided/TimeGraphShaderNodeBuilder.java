package com.gempukku.libgdx.graph.shader.node.provided;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.UniformSetters;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.provided.TimeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TimeGraphShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public TimeGraphShaderNodeBuilder() {
        super(new TimeShaderNodeConfiguration());
    }

    @Override
    protected Map<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, Map<String, FieldOutput> inputs, Set<String> producedOutputs,
                                                                 CommonShaderBuilder commonShaderBuilder, final GraphShaderContext graphShaderContext, GraphShader graphShader) {
        Map<String, FieldOutput> result = new HashMap<>();
        if (producedOutputs.contains("time")) {
            commonShaderBuilder.addUniformVariable("u_time", "float", true, UniformSetters.time);
            result.put("time", new DefaultFieldOutput(ShaderFieldType.Float, "u_time"));
        }
        if (producedOutputs.contains("sinTime")) {
            commonShaderBuilder.addUniformVariable("u_sinTime", "float", true, UniformSetters.sinTime);
            result.put("sinTime", new DefaultFieldOutput(ShaderFieldType.Float, "u_sinTime"));
        }
        if (producedOutputs.contains("cosTime")) {
            commonShaderBuilder.addUniformVariable("u_cosTime", "float", true, UniformSetters.cosTime);
            result.put("cosTime", new DefaultFieldOutput(ShaderFieldType.Float, "u_cosTime"));
        }
        if (producedOutputs.contains("deltaTime")) {
            commonShaderBuilder.addUniformVariable("u_deltaTime", "float", true, UniformSetters.deltaTime);
            result.put("deltaTime", new DefaultFieldOutput(ShaderFieldType.Float, "u_deltaTime"));
        }
        return result;
    }
}
