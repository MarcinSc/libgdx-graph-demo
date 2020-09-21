package com.gempukku.libgdx.graph.shader.config.provided;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class PixelSizeShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public PixelSizeShaderNodeConfiguration() {
        super("PixelSize", "Pixel size", "Provided");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("size", "Size", ShaderFieldType.Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("x", "X", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("y", "Y", ShaderFieldType.Float));
    }
}
