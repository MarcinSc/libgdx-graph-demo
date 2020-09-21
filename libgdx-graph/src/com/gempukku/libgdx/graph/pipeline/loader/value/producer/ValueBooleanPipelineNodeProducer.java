package com.gempukku.libgdx.graph.pipeline.loader.value.producer;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.config.value.ValueBooleanPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.loader.value.node.ValuePipelineNode;

import java.util.Map;

public class ValueBooleanPipelineNodeProducer extends PipelineNodeProducerImpl {
    public ValueBooleanPipelineNodeProducer() {
        super(new ValueBooleanPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JsonValue data, Map<String, PipelineNode.FieldOutput<?>> inputFields) {
        return new ValuePipelineNode(configuration, "value", data.getBoolean("value"));
    }
}
