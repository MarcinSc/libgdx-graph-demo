package com.gempukku.libgdx.graph.shader.node;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public abstract class ConfigurationShaderNodeBuilder implements GraphShaderNodeBuilder {
    private NodeConfiguration<ShaderFieldType> configuration;

    public ConfigurationShaderNodeBuilder(NodeConfiguration<ShaderFieldType> configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getType() {
        return configuration.getType();
    }

    @Override
    public NodeConfiguration<ShaderFieldType> getConfiguration(JsonValue data) {
        return configuration;
    }
}
