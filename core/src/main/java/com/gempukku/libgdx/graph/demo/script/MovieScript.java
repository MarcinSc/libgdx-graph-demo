package com.gempukku.libgdx.graph.demo.script;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.shader.environment.GraphShaderEnvironment;
import com.gempukku.libgdx.graph.shader.models.GraphShaderModelInstance;
import com.gempukku.libgdx.graph.shader.models.GraphShaderModels;

public class MovieScript extends AbstractScript {
    private Label subtitleLabel;
    private GraphShaderModels models;
    private PipelineRenderer pipelineRenderer;

    public MovieScript(Label subtitleLabel, GraphShaderModels models, PipelineRenderer pipelineRenderer) {
        this.subtitleLabel = subtitleLabel;
        this.models = models;
        this.pipelineRenderer = pipelineRenderer;
    }

    public void addActorScript(ActorScript actorScript) {
        String modelId = actorScript.getModelId();
        float start = actorScript.getStart();
        float length = actorScript.getLength();
        addKeyframe(
                new Keyframe() {
                    @Override
                    public float getTime() {
                        return start;
                    }

                    @Override
                    public void performKeyframe() {
                        GraphShaderModelInstance modelInstance = models.createModelInstance(modelId);
                        actorScript.setGraphShaderModelInstance(modelInstance);
                        addAction(
                                new Action() {
                                    private float lastTimeSinceStart;

                                    @Override
                                    public float getStart() {
                                        return actorScript.getStart();
                                    }

                                    @Override
                                    public float getLength() {
                                        return actorScript.getLength();
                                    }

                                    @Override
                                    public void execute(float timeSinceStart) {
                                        actorScript.update(timeSinceStart - lastTimeSinceStart);
                                        lastTimeSinceStart = timeSinceStart;
                                    }
                                });
                        addKeyframe(
                                new Keyframe() {
                                    @Override
                                    public float getTime() {
                                        return start + length;
                                    }

                                    @Override
                                    public void performKeyframe() {
                                        models.destroyModelInstance(modelInstance.getId());
                                    }
                                }
                        );
                    }
                }
        );
    }

    public void setSubtitleText(float time, Color color, String text) {
        addKeyframe(
                new Keyframe() {
                    @Override
                    public float getTime() {
                        return time;
                    }

                    @Override
                    public void performKeyframe() {
                        subtitleLabel.setColor(color);
                        subtitleLabel.setText(text);
                    }
                });
    }

    public void setPipelineFloatProperty(String property, float fromTime, float length, float fromAmount, float toAmount) {
        setPipelineFloatProperty(property, fromTime, length, fromAmount, toAmount, Interpolation.linear);
    }

    public void setPipelineFloatProperty(String property, float fromTime, float length, float fromAmount, float toAmount, Interpolation interpolation) {
        addKeyframe(
                new Keyframe() {
                    @Override
                    public float getTime() {
                        return fromTime + length;
                    }

                    @Override
                    public void performKeyframe() {
                        pipelineRenderer.setPipelineProperty(property, toAmount);
                    }
                });
        addAction(
                new Action() {
                    @Override
                    public float getStart() {
                        return fromTime;
                    }

                    @Override
                    public float getLength() {
                        return length;
                    }

                    @Override
                    public void execute(float timeSinceStart) {
                        float value = fromAmount + (timeSinceStart / length) * (toAmount - fromAmount);
                        pipelineRenderer.setPipelineProperty(property, interpolation.apply(value));
                    }
                });
    }

    public void setPipelineCamera(float time, String property, Camera camera) {
        addKeyframe(
                new Keyframe() {
                    @Override
                    public float getTime() {
                        return time;
                    }

                    @Override
                    public void performKeyframe() {
                        pipelineRenderer.setPipelineProperty(property, camera);
                    }
                });
    }

    public void setPipelineLights(float time, String property, GraphShaderEnvironment lights) {
        addKeyframe(
                new Keyframe() {
                    @Override
                    public float getTime() {
                        return time;
                    }

                    @Override
                    public void performKeyframe() {
                        pipelineRenderer.setPipelineProperty(property, lights);
                    }
                });
    }

    public void setPipelineColor(float time, String property, Color color) {
        addKeyframe(
                new Keyframe() {
                    @Override
                    public float getTime() {
                        return time;
                    }

                    @Override
                    public void performKeyframe() {
                        pipelineRenderer.setPipelineProperty(property, color);
                    }
                });
    }
}
