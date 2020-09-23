package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.gempukku.libgdx.graph.TimeProvider;
import com.gempukku.libgdx.graph.shader.environment.GraphShaderEnvironment;

public interface ShaderContext {
    Texture getDepthTexture();

    Camera getCamera();

    GraphShaderEnvironment getGraphShaderEnvironment();

    TimeProvider getTimeProvider();
}
