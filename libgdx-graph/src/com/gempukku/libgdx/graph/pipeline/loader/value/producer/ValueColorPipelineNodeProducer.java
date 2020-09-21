package com.gempukku.libgdx.graph.pipeline.loader.value.producer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.config.value.ValueColorPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.loader.value.node.ValuePipelineNode;

import java.util.Map;

public class ValueColorPipelineNodeProducer extends PipelineNodeProducerImpl {
    public ValueColorPipelineNodeProducer() {
        super(new ValueColorPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JsonValue data, Map<String, PipelineNode.FieldOutput<?>> inputFields) {
        return new ValuePipelineNode(configuration, "value", Color.valueOf(data.getString("color")));
    }
}
