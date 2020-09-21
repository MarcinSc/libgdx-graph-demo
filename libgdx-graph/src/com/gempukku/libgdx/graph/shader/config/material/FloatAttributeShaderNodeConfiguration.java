package com.gempukku.libgdx.graph.shader.config.material;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class FloatAttributeShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public FloatAttributeShaderNodeConfiguration(String type, String name) {
        super(type, name, "Material");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("value", "Value", ShaderFieldType.Float));
    }
}
