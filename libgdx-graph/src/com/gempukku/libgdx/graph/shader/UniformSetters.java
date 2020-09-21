package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Matrix4;
import com.gempukku.libgdx.graph.shader.environment.GraphShaderEnvironment;
import com.gempukku.libgdx.graph.shader.models.GraphShaderModelInstanceImpl;

public class UniformSetters {
    private UniformSetters() {
    }

    public final static UniformRegistry.UniformSetter projViewTrans = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
            shader.setUniform(location, shaderContext.getCamera().combined);
        }
    };
    public final static UniformRegistry.UniformSetter cameraPosition = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
            Camera camera = shaderContext.getCamera();
            shader.setUniform(location, camera.position.x, camera.position.y, camera.position.z);
        }
    };
    public final static UniformRegistry.UniformSetter cameraDirection = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
            shader.setUniform(location, shaderContext.getCamera().direction);
        }
    };
    public final static UniformRegistry.UniformSetter worldTrans = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
            shader.setUniform(location, renderable.worldTransform);
        }
    };
    public final static UniformRegistry.UniformSetter ambientLight = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
            GraphShaderEnvironment environment = shaderContext.getGraphShaderEnvironment();
            if (environment != null && environment.getAmbientColor() != null) {
                Color ambientColor = environment.getAmbientColor();
                shader.setUniform(location, ambientColor.r, ambientColor.g, ambientColor.b);
            } else {
                shader.setUniform(location, 0f, 0f, 0f);
            }
        }
    };

    public static class Bones implements UniformRegistry.UniformSetter {
        private final static Matrix4 idtMatrix = new Matrix4();
        public final float bones[];

        public Bones(final int numBones) {
            this.bones = new float[numBones * 16];
        }

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
            Matrix4[] modelBones = renderable.bones;
            for (int i = 0; i < bones.length; i += 16) {
                final int idx = i / 16;
                if (modelBones == null || idx >= modelBones.length || modelBones[idx] == null)
                    System.arraycopy(idtMatrix.val, 0, bones, i, 16);
                else
                    System.arraycopy(modelBones[idx].val, 0, bones, i, 16);
            }
            shader.setUniformMatrixArray(location, bones);
        }
    }

    public static class MaterialFloat implements UniformRegistry.UniformSetter {
        private long type;
        private float defaultValue;

        public MaterialFloat(long type, float defaultValue) {
            this.type = type;
            this.defaultValue = defaultValue;
        }

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
            float value = defaultValue;
            FloatAttribute attribute = (FloatAttribute) renderable.material.get(type);
            if (attribute != null)
                value = attribute.value;
            shader.setUniform(location, value);
        }
    }

    public static class MaterialColor implements UniformRegistry.UniformSetter {
        private long type;
        private Color defaultColor;

        public MaterialColor(long type, Color defaultColor) {
            this.type = type;
            this.defaultColor = defaultColor;
        }

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
            Color color = defaultColor;
            ColorAttribute attribute = (ColorAttribute) renderable.material.get(type);
            if (attribute != null)
                color = attribute.color;
            shader.setUniform(location, color);
        }
    }

    public final static UniformRegistry.UniformSetter depthTexture = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
            Texture depthTexture = shaderContext.getDepthTexture();
            final int unit = shader.getContext().textureBinder.bind(depthTexture);
            shader.setUniform(location, unit);
        }
    };

    public static class MaterialTexture implements UniformRegistry.UniformSetter {
        private long type;

        public MaterialTexture(long type) {
            this.type = type;
        }

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
            TextureAttribute ta = (TextureAttribute) (renderable.material.get(type));
            if (ta != null) {
                final int unit = shader.getContext().textureBinder.bind(ta.textureDescription);
                shader.setUniform(location, unit);
            } else {
                int unit = shader.getContext().textureBinder.bind(shader.getDefaultTexture());
                shader.setUniform(location, unit);
            }
        }
    }

    public static class MaterialTextureUV implements UniformRegistry.UniformSetter {
        private long type;

        public MaterialTextureUV(long type) {
            this.type = type;
        }

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
            final TextureAttribute ta = (TextureAttribute) (renderable.material.get(type));
            if (ta != null) {
                shader.setUniform(location, ta.offsetU, ta.offsetV, ta.scaleU, ta.scaleV);
            } else {
                shader.setUniform(location, 0f, 0f, 1f, 1f);
            }
        }
    }
}
