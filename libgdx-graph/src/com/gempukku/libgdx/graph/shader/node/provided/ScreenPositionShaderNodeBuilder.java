package com.gempukku.libgdx.graph.shader.node.provided;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.UniformSetters;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.provided.ScreenPositionShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ScreenPositionShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public ScreenPositionShaderNodeBuilder() {
        super(new ScreenPositionShaderNodeConfiguration());
    }

    @Override
    public Map<String, ? extends FieldOutput> buildVertexNode(boolean designTime, String nodeId, JsonValue data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        throw new UnsupportedOperationException("Sampling of textures is not available in vertex shader in OpenGL ES");
    }

    @Override
    protected Map<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, Map<String, FieldOutput> inputs, Set<String> producedOutputs,
                                                                 CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        commonShaderBuilder.addUniformVariable("u_pixelSize", "vec2", true, UniformSetters.pixelSize);
        String name = "result_" + nodeId;
        commonShaderBuilder.addMainLine("// Screen Position node");
        commonShaderBuilder.addMainLine("vec2 " + name + " = gl_FragCoord.xy * u_pixelSize;");
        return Collections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Vector2, name));
    }
}
