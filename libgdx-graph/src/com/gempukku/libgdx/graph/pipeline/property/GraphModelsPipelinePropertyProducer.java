package com.gempukku.libgdx.graph.pipeline.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.impl.WritablePipelineProperty;
import com.gempukku.libgdx.graph.shader.models.GraphShaderModels;

import java.util.function.Supplier;

public class GraphModelsPipelinePropertyProducer implements PipelinePropertyProducer {
    @Override
    public PipelineFieldType getType() {
        return PipelineFieldType.GraphModels;
    }

    @Override
    public WritablePipelineProperty createProperty(JsonValue data) {
        return new WritablePipelineProperty(PipelineFieldType.GraphModels,
                new Supplier<GraphShaderModels>() {
                    @Override
                    public GraphShaderModels get() {
                        return null;
                    }
                });
    }
}
