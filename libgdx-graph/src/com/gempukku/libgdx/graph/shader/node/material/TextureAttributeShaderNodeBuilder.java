package com.gempukku.libgdx.graph.shader.node.material;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.LibGDXCollections;
import com.gempukku.libgdx.graph.WhitePixel;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.UniformRegistry;
import com.gempukku.libgdx.graph.shader.UniformSetters;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.material.TextureAttributeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.models.impl.GraphShaderModelInstance;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class TextureAttributeShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    private String alias;

    public TextureAttributeShaderNodeBuilder(String type, String name, String alias) {
        super(new TextureAttributeShaderNodeConfiguration(type, name));
        this.alias = alias;
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
                                                                       CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        String textureName = "u_" + alias;
        String transformName = "u_UV" + alias;
        if (designTime) {
            Texture texture = null;
            if (data != null) {
                String previewPath = data.getString("previewPath");
                if (previewPath != null) {
                    try {
                        texture = new Texture(Gdx.files.absolute(previewPath));
                        graphShaderContext.addManagedResource(texture);
                    } catch (Exception exp) {
                        // Ignore
                    }
                }
            }
            if (texture == null)
                texture = WhitePixel.sharedInstance.texture;


            final Texture finalTexture = texture;
            commonShaderBuilder.addUniformVariable(textureName, "sampler2D", false,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext, GraphShaderModelInstance graphShaderModelInstance, Renderable renderable) {
                            shader.setUniform(location, finalTexture);
                        }
                    });
            commonShaderBuilder.addUniformVariable(transformName, "vec4", false,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext, GraphShaderModelInstance graphShaderModelInstance, Renderable renderable) {
                            shader.setUniform(location, 0f, 0f, 1f, 1f);
                        }
                    });
        } else {
            long attributeType = Attribute.getAttributeType(alias);
            commonShaderBuilder.addUniformVariable(textureName, "sampler2D", false, new UniformSetters.MaterialTexture(attributeType));
            commonShaderBuilder.addUniformVariable(transformName, "vec4", false, new UniformSetters.MaterialTextureUV(attributeType));
        }

        return LibGDXCollections.singletonMap("texture", new DefaultFieldOutput(ShaderFieldType.TextureRegion, transformName, textureName));
    }
}
