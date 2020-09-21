package com.gempukku.libgdx.graph.shader.config.provided;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class ScreenPositionShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public ScreenPositionShaderNodeConfiguration() {
        super("ScreenPosition", "Screen position", "Provided");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("output", "Output", ShaderFieldType.Vector2));
    }
}
