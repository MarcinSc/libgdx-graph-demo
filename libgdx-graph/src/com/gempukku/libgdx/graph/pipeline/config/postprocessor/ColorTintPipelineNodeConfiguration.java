package com.gempukku.libgdx.graph.pipeline.config.postprocessor;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.Color;
import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.RenderPipeline;

public class ColorTintPipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public ColorTintPipelineNodeConfiguration() {
        super("ColorTint", "Color tint", "Post-processing");
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("color", "Color", Color));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("strength", "Strength", Float));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("input", "Input", true, true, RenderPipeline));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("output", "Output", true, RenderPipeline));
    }
}
