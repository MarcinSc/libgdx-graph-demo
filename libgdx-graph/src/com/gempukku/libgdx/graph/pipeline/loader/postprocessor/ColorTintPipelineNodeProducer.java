package com.gempukku.libgdx.graph.pipeline.loader.postprocessor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.config.postprocessor.ColorTintPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.loader.FloatFieldOutput;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;

import java.util.Map;

public class ColorTintPipelineNodeProducer extends PipelineNodeProducerImpl {
    public ColorTintPipelineNodeProducer() {
        super(new ColorTintPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JsonValue data, Map<String, PipelineNode.FieldOutput<?>> inputFields) {
        final ShapeRenderer shapeRenderer = new ShapeRenderer();

        PipelineNode.FieldOutput<Color> color = (PipelineNode.FieldOutput<Color>) inputFields.get("color");
        PipelineNode.FieldOutput<Float> strength = (PipelineNode.FieldOutput<Float>) inputFields.get("strength");

        if (color == null)
            color = new PipelineNode.FieldOutput<Color>() {
                @Override
                public PipelineFieldType getPropertyType() {
                    return PipelineFieldType.Color;
                }

                @Override
                public Color getValue(PipelineRenderingContext context) {
                    return Color.BLACK;
                }
            };
        if (strength == null)
            strength = new FloatFieldOutput(0f);

        final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input");

        final PipelineNode.FieldOutput<Float> finalStrength = strength;
        final PipelineNode.FieldOutput<Color> finalColor = color;
        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, Map<String, ? extends OutputValue> outputValues) {
                RenderPipeline renderPipeline = renderPipelineInput.getValue(pipelineRenderingContext);

                float strengthValue = finalStrength.getValue(pipelineRenderingContext);
                if (strengthValue > 0) {
                    Color colorValue = finalColor.getValue(pipelineRenderingContext);

                    FrameBuffer currentBuffer = renderPipeline.getCurrentBuffer();
                    currentBuffer.begin();

                    Gdx.gl.glEnable(GL20.GL_BLEND);
                    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.setColor(colorValue.r, colorValue.g, colorValue.b, colorValue.a * strengthValue);
                    shapeRenderer.rect(0, 0, currentBuffer.getWidth(), currentBuffer.getHeight());
                    shapeRenderer.end();
                    currentBuffer.end();
                }

                OutputValue<RenderPipeline> output = outputValues.get("output");
                if (output != null)
                    output.setValue(renderPipeline);
            }

            @Override
            public void dispose() {
                shapeRenderer.dispose();
            }
        };
    }
}
