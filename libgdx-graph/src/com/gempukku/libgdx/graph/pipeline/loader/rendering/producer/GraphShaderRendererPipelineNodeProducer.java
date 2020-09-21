package com.gempukku.libgdx.graph.pipeline.loader.rendering.producer;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.GraphLoader;
import com.gempukku.libgdx.graph.WhitePixel;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.config.rendering.GraphShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderLoaderCallback;
import com.gempukku.libgdx.graph.shader.environment.GraphShaderEnvironment;
import com.gempukku.libgdx.graph.shader.models.GraphShaderModelInstanceImpl;
import com.gempukku.libgdx.graph.shader.models.GraphShaderModels;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GraphShaderRendererPipelineNodeProducer extends PipelineNodeProducerImpl {
    public GraphShaderRendererPipelineNodeProducer() {
        super(new GraphShaderRendererPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JsonValue data, Map<String, PipelineNode.FieldOutput<?>> inputFields) {
        final WhitePixel whitePixel = new WhitePixel();

        final ShaderContextImpl shaderContext = new ShaderContextImpl();

        JsonValue renderPassDefinitions = data.get("renderPasses");

        final List<RenderPass> renderPasses = new LinkedList<>();
        for (JsonValue renderPassDefinition : renderPassDefinitions) {
            renderPasses.add(new RenderPass(renderPassDefinition, whitePixel));
        }

        final PipelineNode.FieldOutput<GraphShaderModels> modelsInput = (PipelineNode.FieldOutput<GraphShaderModels>) inputFields.get("models");
        final PipelineNode.FieldOutput<GraphShaderEnvironment> lightsInput = (PipelineNode.FieldOutput<GraphShaderEnvironment>) inputFields.get("lights");
        final PipelineNode.FieldOutput<Camera> cameraInput = (PipelineNode.FieldOutput<Camera>) inputFields.get("camera");
        final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input");

        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            private final RenderContext renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.LRU, 1));

            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, Map<String, ? extends OutputValue> outputValues) {
                RenderPipeline renderPipeline = renderPipelineInput.getValue(pipelineRenderingContext);
                GraphShaderModels models = modelsInput.getValue(pipelineRenderingContext);
                Camera camera = cameraInput.getValue(pipelineRenderingContext);
                FrameBuffer currentBuffer = renderPipeline.getCurrentBuffer();
                int width = currentBuffer.getWidth();
                int height = currentBuffer.getHeight();
                float viewportWidth = camera.viewportWidth;
                float viewportHeight = camera.viewportHeight;
                if (width != viewportWidth || height != viewportHeight) {
                    camera.viewportWidth = width;
                    camera.viewportHeight = height;
                    camera.update();
                }
                renderContext.begin();
                GraphShaderEnvironment environment = lightsInput != null ? lightsInput.getValue(pipelineRenderingContext) : null;
                currentBuffer.begin();

                models.prepareForRendering(camera);

                shaderContext.setCamera(camera);
                shaderContext.setGraphShaderEnvironment(environment);
                shaderContext.setDepthTexture(renderPipeline.getDepthFrameBuffer().getColorBufferTexture());

                for (RenderPass renderPass : renderPasses) {
                    Array<GraphShader> opaqueShaders = renderPass.getOpaqueShaders();
                    Array<GraphShader> depthShaders = renderPass.getDepthShaders();
                    Array<GraphShader> transparentShaders = renderPass.getTransparentShaders();
                    // Initialize shaders for drawing
                    for (GraphShader shader : opaqueShaders) {
                        shader.setTimeProvider(pipelineRenderingContext.getTimeProvider());
                        shader.setEnvironment(environment);
                    }
                    for (GraphShader shader : depthShaders) {
                        shader.setTimeProvider(pipelineRenderingContext.getTimeProvider());
                        shader.setEnvironment(environment);
                    }
                    for (GraphShader shader : transparentShaders) {
                        shader.setTimeProvider(pipelineRenderingContext.getTimeProvider());
                        shader.setEnvironment(environment);
                    }

                    // First render opaque models
                    if (!opaqueShaders.isEmpty()) {
                        models.orderFrontToBack();
                        for (GraphShader shader : opaqueShaders) {
                            String tag = shader.getTag();
                            renderWithShaderOpaquePass(tag, shader, models, shaderContext);
                        }
                    }

                    // Then render transparent models
                    if (!transparentShaders.isEmpty()) {
                        models.orderBackToFront();
                        GraphShader lastShader = null;
                        for (GraphShaderModelInstanceImpl graphShaderModelInstance : models.getModels()) {
                            for (GraphShader shader : transparentShaders) {
                                String tag = shader.getTag();
                                if (graphShaderModelInstance.hasTag(tag)) {
                                    if (lastShader != shader) {
                                        if (lastShader != null)
                                            lastShader.end();
                                        shader.begin(shaderContext, renderContext);
                                    }
                                    shader.render(shaderContext, graphShaderModelInstance);
                                    lastShader = shader;
                                }
                            }
                        }
                        if (lastShader != null)
                            lastShader.end();
                    }

                    // Then if depth buffer is needed - draw to it
                    FrameBuffer depthFrameBuffer = renderPipeline.getDepthFrameBuffer();
                    if (depthFrameBuffer != null && !depthShaders.isEmpty()) {
                        currentBuffer.end();
                        depthFrameBuffer.begin();

                        models.orderFrontToBack();
                        for (GraphShader shader : depthShaders) {
                            String tag = shader.getTag();
                            renderWithShaderOpaquePass(tag, shader, models, shaderContext);
                        }

                        depthFrameBuffer.end();
                        currentBuffer.begin();
                    }
                }

                currentBuffer.end();
                renderContext.end();
                OutputValue<RenderPipeline> output = outputValues.get("output");
                if (output != null)
                    output.setValue(renderPipeline);
            }

            private void renderWithShaderOpaquePass(String tag, GraphShader shader, GraphShaderModels models, ShaderContext shaderContext) {
                boolean begun = false;
                for (GraphShaderModelInstanceImpl graphShaderModelInstance : models.getModelsWithTag(tag)) {
                    if (!begun) {
                        shader.begin(shaderContext, renderContext);
                        begun = true;
                    }
                    shader.render(shaderContext, graphShaderModelInstance);
                }
                if (begun)
                    shader.end();
            }

            @Override
            public void dispose() {
                for (RenderPass renderPass : renderPasses) {
                    renderPass.dispose();
                }
                whitePixel.dispose();
            }
        };
    }

    private GraphShader createShader(JsonValue shaderDefinition, Texture defaultTexture) {
        JsonValue shaderGraph = shaderDefinition.get("shader");
        return GraphLoader.loadGraph(shaderGraph, new ShaderLoaderCallback(defaultTexture));
    }

    private GraphShader createDepthShader(JsonValue shaderDefinition, Texture defaultTexture) {
        JsonValue shaderGraph = shaderDefinition.get("shader");
        return GraphLoader.loadGraph(shaderGraph, new ShaderLoaderCallback(defaultTexture, true));
    }

    private class RenderPass implements Disposable {
        private Array<GraphShader> opaqueShaders = new Array<>();
        private Array<GraphShader> depthShaders = new Array<>();
        private Array<GraphShader> transparentShaders = new Array<>();

        public RenderPass(JsonValue data, WhitePixel whitePixel) {
            final JsonValue shaderDefinitions = data.get("shaders");
            for (JsonValue shaderDefinition : shaderDefinitions) {
                String tag = shaderDefinition.getString("tag");
                GraphShader shader = createShader(shaderDefinition, whitePixel.texture);
                shader.setTag(tag);
                if (shader.getTransparency() == BasicShader.Transparency.opaque) {
                    opaqueShaders.add(shader);
                    GraphShader depthShader = createDepthShader(shaderDefinition, whitePixel.texture);
                    depthShader.setTag(tag);
                    depthShaders.add(depthShader);
                } else {
                    transparentShaders.add(shader);
                }
            }
        }

        public Array<GraphShader> getOpaqueShaders() {
            return opaqueShaders;
        }

        public Array<GraphShader> getDepthShaders() {
            return depthShaders;
        }

        public Array<GraphShader> getTransparentShaders() {
            return transparentShaders;
        }

        @Override
        public void dispose() {
            for (GraphShader shader : opaqueShaders) {
                shader.dispose();
            }
            for (GraphShader depthShader : depthShaders) {
                depthShader.dispose();
            }
            for (GraphShader shader : transparentShaders) {
                shader.dispose();
            }
        }
    }

}
