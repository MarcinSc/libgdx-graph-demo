package com.gempukku.libgdx.graph.pipeline.loader.rendering.producer;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.environment.GraphShaderEnvironment;

public class ShaderContextImpl implements ShaderContext {
    private Camera camera;
    private GraphShaderEnvironment graphShaderEnvironment;
    private Texture depthTexture;

    @Override
    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    @Override
    public GraphShaderEnvironment getGraphShaderEnvironment() {
        return graphShaderEnvironment;
    }

    public void setGraphShaderEnvironment(GraphShaderEnvironment graphShaderEnvironment) {
        this.graphShaderEnvironment = graphShaderEnvironment;
    }

    @Override
    public Texture getDepthTexture() {
        return depthTexture;
    }

    public void setDepthTexture(Texture depthTexture) {
        this.depthTexture = depthTexture;
    }
}
