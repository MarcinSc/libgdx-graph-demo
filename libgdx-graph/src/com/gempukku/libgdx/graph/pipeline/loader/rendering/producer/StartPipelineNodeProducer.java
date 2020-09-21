package com.gempukku.libgdx.graph.pipeline.loader.rendering.producer;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.config.rendering.StartPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.node.StartPipelineNode;

import java.util.Map;

public class StartPipelineNodeProducer extends PipelineNodeProducerImpl {
    public StartPipelineNodeProducer() {
        super(new StartPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JsonValue data, Map<String, PipelineNode.FieldOutput<?>> inputFields) {
        return new StartPipelineNode(configuration, inputFields);
    }
}
