package com.gempukku.libgdx.graph.pipeline.property;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.impl.WritablePipelineProperty;

import java.util.function.Supplier;

public class StagePipelinePropertyProducer implements PipelinePropertyProducer {
    @Override
    public PipelineFieldType getType() {
        return PipelineFieldType.Stage;
    }

    @Override
    public WritablePipelineProperty createProperty(JsonValue data) {
        return new WritablePipelineProperty(PipelineFieldType.Stage,
                new Supplier<Stage>() {
                    @Override
                    public Stage get() {
                        return null;
                    }
                });
    }
}
