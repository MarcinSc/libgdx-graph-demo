package com.gempukku.libgdx.graph.shader.node;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.EndShaderNodeConfiguration;

import java.util.Map;
import java.util.Set;

public class EndGraphShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public EndGraphShaderNodeBuilder() {
        super(new EndShaderNodeConfiguration());
    }

    @Override
    public Map<String, ? extends FieldOutput> buildVertexNode(boolean designTime, String nodeId, JsonValue data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        // Dummy node
        return null;
    }

    @Override
    public Map<String, ? extends FieldOutput> buildFragmentNode(boolean designTime, String nodeId, JsonValue data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        // Dummy node
        return null;
    }
}
