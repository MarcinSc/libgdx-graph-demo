package com.gempukku.libgdx.graph.pipeline.loader.node;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;

public interface PipelineNodeProducer {
    String getType();

    NodeConfiguration<PipelineFieldType> getConfiguration(JsonValue data);

    PipelineNode createNode(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields);
}
