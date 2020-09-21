package com.gempukku.libgdx.graph.pipeline.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererModels;
import com.gempukku.libgdx.graph.pipeline.impl.WritablePipelineProperty;

import java.util.function.Supplier;

public class ModelsPipelinePropertyProducer implements PipelinePropertyProducer {
    @Override
    public PipelineFieldType getType() {
        return PipelineFieldType.Models;
    }

    @Override
    public WritablePipelineProperty createProperty(JsonValue data) {
        return new WritablePipelineProperty(PipelineFieldType.Models,
                new Supplier<PipelineRendererModels>() {
                    @Override
                    public PipelineRendererModels get() {
                        return null;
                    }
                });
    }
}
