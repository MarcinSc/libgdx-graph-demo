package com.gempukku.libgdx.graph.shader.node.noise;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.LibGDXCollections;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.GLSLFragmentReader;
import com.gempukku.libgdx.graph.shader.config.noise.SimplexNoise2DNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class SimplexNoise2DShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public SimplexNoise2DShaderNodeBuilder() {
        super(new SimplexNoise2DNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
                                                                       CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput uvValue = inputs.get("uv");
        FieldOutput scaleValue = inputs.get("scale");

        if (!commonShaderBuilder.containsFunction("simplexNoise2d")) {
            commonShaderBuilder.addFunction("simplexNoise2d", GLSLFragmentReader.getFragment("simplexNoise2d"));
        }

        commonShaderBuilder.addMainLine("// Simplex noise 2D node");
        String name = "result_" + nodeId;
        if (uvValue.getFieldType() == ShaderFieldType.Vector2) {
            commonShaderBuilder.addMainLine("float " + name + " = simplexNoise2d(" + uvValue.getRepresentation() + " * " + scaleValue.getRepresentation() + ");");
        } else {
            commonShaderBuilder.addMainLine("float " + name + " = simplexNoise2d(vec2(" + uvValue.getRepresentation() + ", 0.0) * " + scaleValue.getRepresentation() + ");");
        }

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }
}
