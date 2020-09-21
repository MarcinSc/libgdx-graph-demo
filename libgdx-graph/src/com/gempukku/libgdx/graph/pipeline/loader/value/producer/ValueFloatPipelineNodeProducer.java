package com.gempukku.libgdx.graph.pipeline.loader.value.producer;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.config.value.ValueFloatPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.loader.value.node.ValuePipelineNode;

import java.util.Map;

public class ValueFloatPipelineNodeProducer extends PipelineNodeProducerImpl {
    public ValueFloatPipelineNodeProducer() {
        super(new ValueFloatPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JsonValue data, Map<String, PipelineNode.FieldOutput<?>> inputFields) {
        return new ValuePipelineNode(configuration, "value", data.getFloat("v1"));
    }
}
