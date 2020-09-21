package com.gempukku.libgdx.graph.pipeline.loader.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.PropertyNodeConfiguration;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducer;

import java.util.Map;

public class PropertyPipelineNodeProducer implements PipelineNodeProducer {
    @Override
    public String getType() {
        return "Property";
    }

    @Override
    public NodeConfiguration<PipelineFieldType> getConfiguration(JsonValue data) {
        final String name = data.getString("name");
        final PipelineFieldType fieldType = PipelineFieldType.valueOf(data.getString("type"));
        return new PropertyNodeConfiguration<PipelineFieldType>(name, fieldType);
    }

    @Override
    public PipelineNode createNode(JsonValue data, Map<String, PipelineNode.FieldOutput<?>> inputFields) {
        final String propertyName = data.getString("name");
        final PipelineFieldType fieldType = PipelineFieldType.valueOf(data.getString("type"));

        return new PipelineNode() {
            @Override
            public FieldOutput<?> getFieldOutput(String name) {
                return new FieldOutput<Object>() {
                    @Override
                    public PipelineFieldType getPropertyType() {
                        return fieldType;
                    }

                    @Override
                    public Object getValue(PipelineRenderingContext context) {
                        return context.getPipelinePropertySource().getPipelineProperty(propertyName).getValue();
                    }
                };
            }

            @Override
            public void startFrame(float delta) {

            }

            @Override
            public void endFrame() {

            }

            @Override
            public void dispose() {

            }
        };
    }
}
