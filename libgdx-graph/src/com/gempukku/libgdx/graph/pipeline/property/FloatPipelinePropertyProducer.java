package com.gempukku.libgdx.graph.pipeline.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.impl.WritablePipelineProperty;

import java.util.function.Supplier;

public class FloatPipelinePropertyProducer implements PipelinePropertyProducer {
    @Override
    public PipelineFieldType getType() {
        return PipelineFieldType.Float;
    }

    @Override
    public WritablePipelineProperty createProperty(JsonValue data) {
        final float x = data.getFloat("x");
        return new WritablePipelineProperty(PipelineFieldType.Float,
                new Supplier<Float>() {
                    @Override
                    public Float get() {
                        return x;
                    }
                });
    }
}
