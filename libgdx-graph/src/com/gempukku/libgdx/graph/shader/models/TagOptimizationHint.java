package com.gempukku.libgdx.graph.shader.models;

/**
 * Used to specify what a relationship between a model instance and a tag is.
 */
public enum TagOptimizationHint {
    /**
     * Specifies, that this tag will be on the model instance pretty much permanently. This allows for greatest amount
     * of performance optimization, however removal of the tag might be costly.
     */
    Always(2),
    /**
     * Specifies, that this tag will be on the model instance for some time. This allows for a lot of performance
     * optimization, however removal of the tag might require a bit of work.
     */
    Temporary(1),
    /**
     * Specifies, that this tag will be on the model for a very short amount of time. Usually this is not going to be
     * optimized at all, and a removal of such tag has no cost at all.
     */
    Flash(0);

    private int value;

    TagOptimizationHint(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
