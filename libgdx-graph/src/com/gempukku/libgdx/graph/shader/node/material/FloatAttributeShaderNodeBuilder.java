package com.gempukku.libgdx.graph.shader.node.material;

import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.UniformSetters;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.material.FloatAttributeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class FloatAttributeShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    private String alias;

    public FloatAttributeShaderNodeBuilder(String type, String name, String alias) {
        super(new FloatAttributeShaderNodeConfiguration(type, name));
        this.alias = alias;
    }

    @Override
    protected Map<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, Map<String, FieldOutput> inputs, Set<String> producedOutputs,
                                                                 CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        String name = "u_" + alias;

        float defaultValue = data.getFloat("default");

        long attributeType = Attribute.getAttributeType(alias);
        commonShaderBuilder.addUniformVariable(name, "float", false, new UniformSetters.MaterialFloat(attributeType, defaultValue));

        return Collections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }
}
