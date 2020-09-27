package com.gempukku.libgdx.graph.shader.node.lighting;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.environment.SpotLight;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.UniformRegistry;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.lighting.SpotLightShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.environment.GraphShaderEnvironment;
import com.gempukku.libgdx.graph.shader.models.impl.GraphShaderModelInstance;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class SpotLightShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public SpotLightShaderNodeBuilder() {
        super(new SpotLightShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        final int index = data.getInt("index");

        ObjectMap<String, DefaultFieldOutput> result = new ObjectMap<>();
        if (producedOutputs.contains("position")) {
            String name = "u_spotLightPosition" + index;
            commonShaderBuilder.addUniformVariable(name, "vec3", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext, GraphShaderModelInstance graphShaderModelInstance, Renderable renderable) {
                            GraphShaderEnvironment environment = shaderContext.getGraphShaderEnvironment();
                            if (environment != null && environment.getSpotLights().size > index && environment.getSpotLights().get(index) != null) {
                                Array<SpotLight> spotLights = environment.getSpotLights();
                                SpotLight spotLight = spotLights.get(index);
                                shader.setUniform(location, spotLight.position);
                            } else {
                                shader.setUniform(location, 0f, 0f, 0f);
                            }
                        }
                    });
            result.put("position", new DefaultFieldOutput(ShaderFieldType.Vector3, name));
        }
        if (producedOutputs.contains("direction")) {
            String name = "u_spotLightDirection" + index;
            commonShaderBuilder.addUniformVariable(name, "vec3", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext, GraphShaderModelInstance graphShaderModelInstance, Renderable renderable) {
                            GraphShaderEnvironment environment = shaderContext.getGraphShaderEnvironment();
                            if (environment != null && environment.getSpotLights().size > index && environment.getSpotLights().get(index) != null) {
                                Array<SpotLight> spotLights = environment.getSpotLights();
                                SpotLight spotLight = spotLights.get(index);
                                shader.setUniform(location, spotLight.direction);
                            } else {
                                shader.setUniform(location, 0f, 0f, 0f);
                            }
                        }
                    });
            result.put("direction", new DefaultFieldOutput(ShaderFieldType.Vector3, name));
        }
        if (producedOutputs.contains("color")) {
            String name = "u_spotLightColor" + index;
            commonShaderBuilder.addUniformVariable(name, "vec4", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext, GraphShaderModelInstance graphShaderModelInstance, Renderable renderable) {
                            GraphShaderEnvironment environment = shaderContext.getGraphShaderEnvironment();
                            if (environment != null && environment.getSpotLights().size > index && environment.getSpotLights().get(index) != null) {
                                Array<SpotLight> spotLights = environment.getSpotLights();
                                SpotLight spotLight = spotLights.get(index);
                                shader.setUniform(location, spotLight.color);
                            } else {
                                shader.setUniform(location, 0f, 0f, 0f, 1f);
                            }
                        }
                    });
            result.put("color", new DefaultFieldOutput(ShaderFieldType.Color, name));
        }
        if (producedOutputs.contains("intensity")) {
            String name = "u_spotLightIntensity" + index;
            commonShaderBuilder.addUniformVariable(name, "float", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext, GraphShaderModelInstance graphShaderModelInstance, Renderable renderable) {
                            GraphShaderEnvironment environment = shaderContext.getGraphShaderEnvironment();
                            if (environment != null && environment.getSpotLights().size > index && environment.getSpotLights().get(index) != null) {
                                Array<SpotLight> spotLights = environment.getSpotLights();
                                SpotLight spotLight = spotLights.get(index);
                                shader.setUniform(location, spotLight.intensity);
                            } else {
                                shader.setUniform(location, 0f);
                            }
                        }
                    });
            result.put("intensity", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        if (producedOutputs.contains("cutOffAngle")) {
            String name = "u_spotLightCutOffAngle" + index;
            commonShaderBuilder.addUniformVariable(name, "float", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext, GraphShaderModelInstance graphShaderModelInstance, Renderable renderable) {
                            GraphShaderEnvironment environment = shaderContext.getGraphShaderEnvironment();
                            if (environment != null && environment.getSpotLights().size > index && environment.getSpotLights().get(index) != null) {
                                Array<SpotLight> spotLights = environment.getSpotLights();
                                SpotLight spotLight = spotLights.get(index);
                                shader.setUniform(location, spotLight.cutoffAngle);
                            } else {
                                shader.setUniform(location, 0f);
                            }
                        }
                    });
            result.put("cutOffAngle", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        if (producedOutputs.contains("exponent")) {
            String name = "u_spotLightExponent" + index;
            commonShaderBuilder.addUniformVariable(name, "float", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext, GraphShaderModelInstance graphShaderModelInstance, Renderable renderable) {
                            GraphShaderEnvironment environment = shaderContext.getGraphShaderEnvironment();
                            if (environment != null && environment.getSpotLights().size > index && environment.getSpotLights().get(index) != null) {
                                Array<SpotLight> spotLights = environment.getSpotLights();
                                SpotLight spotLight = spotLights.get(index);
                                shader.setUniform(location, spotLight.exponent);
                            } else {
                                shader.setUniform(location, 0f);
                            }
                        }
                    });
            result.put("exponent", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        return result;
    }
}
