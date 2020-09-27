package com.gempukku.libgdx.graph.shader.models;

/**
 * Configures how the model instance will behave, which helps the engine to best optimize the rendering.
 */
public class ModelInstanceOptimizationHints {
    /**
     * Defines for how long this model instance will exist within the context.
     */
    public enum Existence {
        Permanent,
        Temporary,
        Flash;
    }

    /**
     * Defines if this model instance will be animated or not.
     */
    public enum Animation {
        Off, On;
    }

    /**
     * Defines how often this model instance will have its transform (translation, rotation and/or scale) modified.
     */
    public enum Transform {
        Never, Sometimes, Often;
    }

    /**
     * Defines how often graph shader properties of this model instance will be modified.
     */
    public enum PropertyModification {
        Never, Sometimes, Often;
    }

    public static final ModelInstanceOptimizationHints unoptimized = ModelInstanceOptimizationHints.create().build();

    private final Existence existence;
    private final Animation animation;
    private final Transform transform;
    private final PropertyModification propertyModification;

    private ModelInstanceOptimizationHints(Existence existence, Animation animation, Transform transform, PropertyModification propertyModification) {
        this.existence = existence;
        this.animation = animation;
        this.transform = transform;
        this.propertyModification = propertyModification;
    }

    public static final Builder create() {
        return new Builder();
    }

    public Existence getExistence() {
        return existence;
    }

    public Animation getAnimation() {
        return animation;
    }

    public Transform getTransform() {
        return transform;
    }

    public PropertyModification getPropertyModification() {
        return propertyModification;
    }

    public static class Builder {
        private Existence existence = Existence.Flash;
        private Animation animation = Animation.On;
        private Transform transform = Transform.Often;
        private PropertyModification propertyModification = PropertyModification.Often;

        private Builder() {

        }

        public Builder existence(Existence existence) {
            this.existence = existence;
            return this;
        }

        public Builder animation(Animation animation) {
            this.animation = animation;
            return this;
        }

        public Builder transform(Transform transform) {
            this.transform = transform;
            return this;
        }

        public Builder propertyModification(PropertyModification propertyModification) {
            this.propertyModification = propertyModification;
            return this;
        }

        public ModelInstanceOptimizationHints build() {
            return new ModelInstanceOptimizationHints(existence, animation, transform, propertyModification);
        }
    }
}
