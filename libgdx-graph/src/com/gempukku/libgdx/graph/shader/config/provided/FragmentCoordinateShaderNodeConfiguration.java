package com.gempukku.libgdx.graph.shader.config.provided;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class FragmentCoordinateShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public FragmentCoordinateShaderNodeConfiguration() {
        super("FragmentCoordinate", "Fragment coordinate", "Provided");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("output", "Output", ShaderFieldType.Color));
    }
}
