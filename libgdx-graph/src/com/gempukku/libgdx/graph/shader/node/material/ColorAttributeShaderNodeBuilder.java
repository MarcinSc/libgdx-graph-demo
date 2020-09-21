package com.gempukku.libgdx.graph.shader.node.material;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.UniformSetters;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.material.ColorAttributeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ColorAttributeShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    private String alias;

    public ColorAttributeShaderNodeBuilder(String type, String name, String alias) {
        super(new ColorAttributeShaderNodeConfiguration(type, name));
        this.alias = alias;
    }

    @Override
    protected Map<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, Map<String, FieldOutput> inputs, Set<String> producedOutputs,
                                                                 CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        String name = "u_" + alias;

        Color defaultColor = Color.valueOf(data.getString("default"));

        long attributeType = Attribute.getAttributeType(alias);
        commonShaderBuilder.addUniformVariable(name, "vec4", false, new UniformSetters.MaterialColor(attributeType, defaultColor));

        return Collections.singletonMap("color", new DefaultFieldOutput(ShaderFieldType.Color, name));
    }
}
