package com.gempukku.libgdx.graph.demo.script;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.shader.models.GraphShaderModelInstance;

public class ActorScript extends AbstractScript {
    private String modelId;
    private float start;
    private float length;

    private GraphShaderModelInstance graphShaderModelInstance;

    private Vector3 position = new Vector3();
    private float scale;
    private Vector3 rotateAxis = new Vector3(0, 1f, 0);
    private float rotateDegrees;

    public ActorScript(String modelId, float start, float length) {
        this.modelId = modelId;
        this.start = start;
        this.length = length;
    }

    public String getModelId() {
        return modelId;
    }

    public float getStart() {
        return start;
    }

    public float getLength() {
        return length;
    }

    public void setGraphShaderModelInstance(GraphShaderModelInstance graphShaderModelInstance) {
        this.graphShaderModelInstance = graphShaderModelInstance;
    }

    public void setScale(float start, float length, float scaleStart, float scaleEnd) {
        addKeyframe(
                new Keyframe() {
                    @Override
                    public float getTime() {
                        return length;
                    }

                    @Override
                    public void performKeyframe() {
                        scale = scaleEnd;
                        updateTransform();
                    }
                });
        addAction(
                new Action() {
                    @Override
                    public float getStart() {
                        return start;
                    }

                    @Override
                    public float getLength() {
                        return length;
                    }

                    @Override
                    public void execute(float timeSinceStart) {
                        scale = scaleStart + (timeSinceStart / length) * (scaleEnd - scaleStart);
                        updateTransform();
                    }
                });
    }

    public void setPosition(float start, float length, Vector3 positionStart, Vector3 positionEnd) {
        setPosition(start, length, positionStart, positionEnd, Interpolation.linear);
    }

    public void setPosition(float start, float length, Vector3 positionStart, Vector3 positionEnd, Interpolation interpolation) {
        addKeyframe(
                new Keyframe() {
                    @Override
                    public float getTime() {
                        return length;
                    }

                    @Override
                    public void performKeyframe() {
                        position.set(positionEnd);
                        updateTransform();
                    }
                });
        addAction(
                new Action() {
                    @Override
                    public float getStart() {
                        return start;
                    }

                    @Override
                    public float getLength() {
                        return length;
                    }

                    @Override
                    public void execute(float timeSinceStart) {
                        float progress = interpolation.apply(timeSinceStart / length);
                        position.set(positionStart).add(
                                progress * (positionEnd.x - positionStart.x),
                                progress * (positionEnd.y - positionStart.y),
                                progress * (positionEnd.z - positionStart.z));
                        updateTransform();
                    }
                }
        );
    }

    public void setRotation(float start, float length, Vector3 axis, float degreesStart, float degreesEnd) {
        addKeyframe(
                new Keyframe() {
                    @Override
                    public float getTime() {
                        return length;
                    }

                    @Override
                    public void performKeyframe() {
                        rotateAxis = axis;
                        rotateDegrees = degreesEnd;
                        updateTransform();
                    }
                });
        addAction(
                new Action() {
                    @Override
                    public float getStart() {
                        return start;
                    }

                    @Override
                    public float getLength() {
                        return length;
                    }

                    @Override
                    public void execute(float timeSinceStart) {
                        rotateAxis = axis;
                        rotateDegrees = degreesStart + (timeSinceStart / length) * (degreesEnd - degreesStart);
                        updateTransform();
                    }
                });
    }

    private void updateTransform() {
        graphShaderModelInstance.getTransformMatrix().idt()
                .translate(position)
                .scale(scale, scale, scale)
                .rotate(rotateAxis.x, rotateAxis.y, rotateAxis.z, rotateDegrees);
    }
}