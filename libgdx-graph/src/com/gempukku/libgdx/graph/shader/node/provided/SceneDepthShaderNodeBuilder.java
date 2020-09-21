package com.gempukku.libgdx.graph.shader.node.provided;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.UniformSetters;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.GLSLFragmentReader;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.provided.SceneDepthShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class SceneDepthShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public SceneDepthShaderNodeBuilder() {
        super(new SceneDepthShaderNodeConfiguration());
    }

    @Override
    public Map<String, ? extends FieldOutput> buildVertexNode(boolean designTime, String nodeId, JsonValue data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        throw new UnsupportedOperationException("Sampling of textures is not available in vertex shader in OpenGL ES");
    }

    @Override
    protected Map<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, Map<String, FieldOutput> inputs, Set<String> producedOutputs,
                                                                 CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        if (designTime) {
            return Collections.singletonMap("depth", new DefaultFieldOutput(ShaderFieldType.Float, "0.0"));
        } else {
            commonShaderBuilder.addUniformVariable("u_cameraClipping", "vec2", true, UniformSetters.cameraClipping);

            commonShaderBuilder.addUniformVariable("u_sceneDepthTexture", "sampler2D", true, UniformSetters.depthTexture);
            if (!commonShaderBuilder.containsFunction("unpackVec3ToFloat")) {
                commonShaderBuilder.addFunction("unpackVec3ToFloat", GLSLFragmentReader.getFragment("unpackVec3ToFloat"));
            }

            FieldOutput screenPosition = inputs.get("screenPosition");
            String screenPositionValue = screenPosition != null ? screenPosition.getRepresentation() : "gl_FragCoord";
            String name = "depth_" + nodeId;
            commonShaderBuilder.addMainLine("// Scene depth node");
            commonShaderBuilder.addMainLine("float " + name + " = unpackVec3ToFloat(texture2D(u_sceneDepthTexture, " + screenPositionValue + ".xy).rgb);");
            return Collections.singletonMap("depth", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
    }
}
