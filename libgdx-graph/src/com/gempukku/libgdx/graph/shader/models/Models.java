package com.gempukku.libgdx.graph.shader.models;

import com.gempukku.libgdx.graph.shader.models.impl.GraphShaderModelsImpl;

public class Models {
    private Models() {
    }

    public static GraphShaderModels create() {
        return new GraphShaderModelsImpl();
    }
}
