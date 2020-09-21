package com.gempukku.libgdx.graph.shader.node;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;

import java.util.Map;
import java.util.Set;

public interface GraphShaderNodeBuilder {
    String getType();

    NodeConfiguration<ShaderFieldType> getConfiguration(JsonValue data);

    Map<String, ? extends FieldOutput> buildVertexNode(boolean designTime, String nodeId, JsonValue data, Map<String, FieldOutput> inputs, Set<String> producedOutputs,
                                                       VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader);

    Map<String, ? extends FieldOutput> buildFragmentNode(boolean designTime, String nodeId, JsonValue data, Map<String, FieldOutput> inputs, Set<String> producedOutputs,
                                                         VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader);

    interface FieldOutput {
        ShaderFieldType getFieldType();

        String getRepresentation();

        String getSamplerRepresentation();
    }
}
