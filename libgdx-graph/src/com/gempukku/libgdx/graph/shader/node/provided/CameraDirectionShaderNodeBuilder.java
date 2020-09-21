package com.gempukku.libgdx.graph.shader.node.provided;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.UniformSetters;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.provided.CameraDirectionShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class CameraDirectionShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public CameraDirectionShaderNodeBuilder() {
        super(new CameraDirectionShaderNodeConfiguration());
    }

    @Override
    protected Map<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        commonShaderBuilder.addUniformVariable("u_cameraDirection", "vec3", true, UniformSetters.cameraDirection);
        return Collections.singletonMap("direction", new DefaultFieldOutput(ShaderFieldType.Vector3, "u_cameraDirection"));
    }
}
