package com.gempukku.libgdx.graph.shader.models;

import com.badlogic.gdx.math.Matrix4;

public class Transforms {
    private Transforms() {
    }

    public static TransformUpdate translate(final float x, final float y, final float z) {
        return new TransformUpdate() {
            @Override
            public void updateTransform(Matrix4 transform) {
                transform.translate(x, y, z);
            }
        };
    }

    public static TransformUpdate rotate(final float axisX, final float axisY, final float axisZ, final float degrees) {
        return new TransformUpdate() {
            @Override
            public void updateTransform(Matrix4 transform) {
                transform.rotate(axisX, axisY, axisZ, degrees);
            }
        };
    }

    public static TransformUpdate scale(final float scaleX, final float scaleY, final float scaleZ) {
        return new TransformUpdate() {
            @Override
            public void updateTransform(Matrix4 transform) {
                transform.scale(scaleX, scaleY, scaleZ);
            }
        };
    }
}
