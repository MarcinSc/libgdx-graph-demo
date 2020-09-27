package com.gempukku.libgdx.graph.shader.node.math.value;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.LibGDXCollections;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.math.value.RemapShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class RemapShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public RemapShaderNodeBuilder() {
        super(new RemapShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput inputValue = inputs.get("input");
        FieldOutput fromValue = inputs.get("from");
        FieldOutput toValue = inputs.get("to");
        ShaderFieldType resultType = inputValue.getFieldType();

        String functionName = "remap_" + resultType.getShaderType();

        if (!commonShaderBuilder.containsFunction(functionName)) {
            commonShaderBuilder.addFunction(functionName, resultType.getShaderType() + " " + functionName + "(" + resultType.getShaderType() + " value, vec2 from, vec2 to) {\n" +
                    "  return to.x + (value - from.x) * (to.y - to.x) / (from.y - from.x);\n" +
                    "}\n");
        }
        commonShaderBuilder.addMainLine("// Remap node");
        String name = "result_" + nodeId;
        commonShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = " + functionName + "(" + inputValue.getRepresentation() + ", " + fromValue.getRepresentation() + ", " + toValue.getRepresentation() + ");");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(resultType, name));
    }
}
