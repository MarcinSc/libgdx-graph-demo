package com.gempukku.libgdx.graph.pipeline.loader.rendering.producer;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.config.rendering.EndPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.node.EndPipelineNode;

import java.util.Map;

public class EndPipelineNodeProducer extends PipelineNodeProducerImpl {
    public EndPipelineNodeProducer() {
        super(new EndPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JsonValue data, Map<String, PipelineNode.FieldOutput<?>> inputFields) {
        return new EndPipelineNode(
                (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input"));
    }
}
