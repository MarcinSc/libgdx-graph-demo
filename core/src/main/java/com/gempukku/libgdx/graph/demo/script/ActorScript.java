package com.gempukku.libgdx.graph.demo.script;

import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.shader.models.GraphShaderModelInstance;

public class ActorScript extends AbstractScript {
    private String modelId;
    private float start;
    private float length;

    private GraphShaderModelInstance graphShaderModelInstance;

    private Vector3 position = new Vector3();
    private float scale = 1f;
    private Vector3 rotateAxis = new Vector3(0, 1f, 0);
    private float rotateDegrees;
    private AnimationController animationController;

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

    public ActorScript setScale(float start, float length, float scaleStart, float scaleEnd) {
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
        return this;
    }

    public ActorScript setPosition(float start, float length, Vector3 positionStart, Vector3 positionEnd) {
        return setPosition(start, length, positionStart, positionEnd, Interpolation.linear);
    }

    public ActorScript setPosition(float start, float length, Vector3 positionStart, Vector3 positionEnd, Interpolation interpolation) {
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
        return this;
    }

    public ActorScript setRotation(float start, float length, Vector3 axis, float degreesStart, float degreesEnd) {
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
        return this;
    }

    private void updateTransform() {
        graphShaderModelInstance.getTransformMatrix().idt()
                .translate(position)
                .scale(scale, scale, scale)
                .rotate(rotateAxis.x, rotateAxis.y, rotateAxis.z, rotateDegrees);
    }

    public ActorScript setFloatProperty(String property, float start, float length, float fromAmount, float toAmount) {
        addKeyframe(
                new Keyframe() {
                    @Override
                    public float getTime() {
                        return start + length;
                    }

                    @Override
                    public void performKeyframe() {
                        graphShaderModelInstance.setProperty(property, toAmount);
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
                        float value = fromAmount + (timeSinceStart / length) * (toAmount - fromAmount);
                        graphShaderModelInstance.setProperty(property, value);
                    }
                });
        return this;
    }

    public ActorScript removeTag(float time, String tag) {
        addKeyframe(
                new Keyframe() {
                    @Override
                    public float getTime() {
                        return time;
                    }

                    @Override
                    public void performKeyframe() {
                        graphShaderModelInstance.removeTag(tag);
                    }
                });
        return this;
    }

    public ActorScript addTag(float time, String tag) {
        addKeyframe(
                new Keyframe() {
                    @Override
                    public float getTime() {
                        return time;
                    }

                    @Override
                    public void performKeyframe() {
                        graphShaderModelInstance.addTag(tag);
                    }
                });
        return this;
    }

    public void setAnimationController(AnimationController animationController) {
        this.animationController = animationController;
    }

    public ActorScript setAnimation(float time, String animation, int loopCount, float transitionTime) {
        addKeyframe(
                new Keyframe() {
                    @Override
                    public float getTime() {
                        return time;
                    }

                    @Override
                    public void performKeyframe() {
                        animationController.animate(animation, loopCount, null, transitionTime);
                    }
                });
        return this;
    }
}
