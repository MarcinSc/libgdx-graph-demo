package com.gempukku.libgdx.graph.shader.node.value;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.SimpleNumberFormatter;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.value.ValueVector2ShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ValueVector2ShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public ValueVector2ShaderNodeBuilder() {
        super(new ValueVector2ShaderNodeConfiguration());
    }

    @Override
    protected Map<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        float v1 = data.getFloat("v1");
        float v2 = data.getFloat("v2");

        return Collections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.Vector2, "vec2(" + format(v1) + ", " + format(v2) + ")"));
    }

    private String format(float component) {
        return SimpleNumberFormatter.format(component);
    }
}
