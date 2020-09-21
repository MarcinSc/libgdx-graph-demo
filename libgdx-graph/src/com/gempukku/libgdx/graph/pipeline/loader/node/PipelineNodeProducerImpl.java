package com.gempukku.libgdx.graph.pipeline.loader.node;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;

public abstract class PipelineNodeProducerImpl implements PipelineNodeProducer {
    protected NodeConfiguration<PipelineFieldType> configuration;

    public PipelineNodeProducerImpl(NodeConfiguration<PipelineFieldType> configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getType() {
        return configuration.getType();
    }

    @Override
    public final NodeConfiguration<PipelineFieldType> getConfiguration(JsonValue data) {
        return configuration;
    }
}
