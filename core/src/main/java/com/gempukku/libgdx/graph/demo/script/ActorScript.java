package com.gempukku.libgdx.graph.demo.script;

import com.gempukku.libgdx.graph.shader.models.GraphShaderModelInstance;

public class ActorScript extends AbstractScript {
    private String modelId;
    private float start;
    private float length;

    private GraphShaderModelInstance graphShaderModelInstance;

    public ActorScript(String modelId, float start, float length) {
        this.modelId = modelId;
        this.start = start;
        this.length = length;
    }

    public String getModelId() {
        return modelId;
    }

    public float getStart() {
        return start;
    }

    public float getLength() {
        return length;
    }

    public void setGraphShaderModelInstance(GraphShaderModelInstance graphShaderModelInstance) {
        this.graphShaderModelInstance = graphShaderModelInstance;
    }
}
