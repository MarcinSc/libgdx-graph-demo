package com.gempukku.libgdx.graph.pipeline.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.impl.WritablePipelineProperty;

public interface PipelinePropertyProducer {
    PipelineFieldType getType();

    WritablePipelineProperty createProperty(JsonValue data);
}
