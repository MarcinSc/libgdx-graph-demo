package com.gempukku.libgdx.graph.shader.node.value;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.SimpleNumberFormatter;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.value.ValueFloatShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ValueFloatShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public ValueFloatShaderNodeBuilder() {
        super(new ValueFloatShaderNodeConfiguration());
    }

    @Override
    protected Map<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        float value = data.getFloat("v1");

        return Collections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.Float, format(value)));
    }

    private String format(float component) {
        return SimpleNumberFormatter.format(component);
    }
}
