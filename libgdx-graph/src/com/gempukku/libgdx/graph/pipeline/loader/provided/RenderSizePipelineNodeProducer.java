package com.gempukku.libgdx.graph.pipeline.loader.provided;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.provided.RenderSizePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;

public class RenderSizePipelineNodeProducer extends PipelineNodeProducerImpl {
    public RenderSizePipelineNodeProducer() {
        super(new RenderSizePipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, ObjectMap<String, ? extends OutputValue> outputValues) {
                int width = pipelineRenderingContext.getRenderWidth();
                int height = pipelineRenderingContext.getRenderHeight();
                OutputValue<Vector2> size = outputValues.get("size");
                if (size != null)
                    size.setValue(new Vector2(width, height));
                OutputValue<Float> widthValue = outputValues.get("width");
                if (widthValue != null)
                    widthValue.setValue(1f * width);
                OutputValue<Float> heightValue = outputValues.get("height");
                if (heightValue != null)
                    heightValue.setValue(1f * height);
            }
        };
    }
}
