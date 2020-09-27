package com.gempukku.libgdx.graph.shader.node.effect;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.LibGDXCollections;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.effect.IntensityShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class IntensityShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public IntensityShaderNodeBuilder() {
        super(new IntensityShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput aValue = inputs.get("color");

        commonShaderBuilder.addMainLine("// Intensity node");
        String name = "result_" + nodeId;
        ShaderFieldType resultType = ShaderFieldType.Float;
        commonShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = dot(" + aValue.getRepresentation() + ".rgb, vec3(0.2126729, 0.7151522, 0.0721750));");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(resultType, name));
    }
}
