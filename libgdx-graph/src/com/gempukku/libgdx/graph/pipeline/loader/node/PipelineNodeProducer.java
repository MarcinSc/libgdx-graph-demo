package com.gempukku.libgdx.graph.pipeline.loader.node;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;

import java.util.Map;

public interface PipelineNodeProducer {
    String getType();

    NodeConfiguration<PipelineFieldType> getConfiguration(JsonValue data);

    PipelineNode createNode(JsonValue data, Map<String, PipelineNode.FieldOutput<?>> inputFields);
}
