package com.gempukku.libgdx.graph.shader.models;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.utils.Disposable;

/**
 * Main interface that is used to operate on models rendered by GraphShaders. Any operation done on the models should
 * be done via use of this interface.
 */
public interface GraphShaderModels extends Disposable {
    /**
     * Registers the specified model with Graph Shaders.
     *
     * @param model Model to register
     * @return Returns modelId that is used further on, when referring to the model.
     */
    String registerModel(Model model);

    /**
     * Removes the specified model from Graph Shaders, freeing up the memory.
     * This method also removes all model instances created from that model.
     *
     * @param modelId modelId that was returned from the registerModel call.
     */
    void removeModel(String modelId);

    /**
     * Adds tag that will be added to every model instance that is subsequently created from the model.
     * Note, that adding the same tag multiple times with different TagPerformanceHint will result in using only the
     * last setting.
     *
     * @param modelId             ModelId that was returned from the registerModel call.
     * @param tag                 Tag that should be added to each subsequently created model instance.
     * @param tagOptimizationHint Specifies, how this tag will be used with model instances subsequently created.
     */
    void addModelDefaultTag(String modelId, String tag, TagOptimizationHint tagOptimizationHint);

    /**
     * Removes tag from list of tags that would be added to every model instance that is subsequently created from
     * the model. Please note - this DOES NOT remove the tag from model instances already created.
     *
     * @param modelId ModelId that was returned from the registerModel call.
     * @param tag     Tag that should be removed.
     */
    void removeModelDefaultTag(String modelId, String tag);

    /**
     * Creates model instance based on the specified model. This model instance will not be optimized by the engine.
     *
     * @param modelId ModelId that was returned from the registerModel call.
     * @return Returns modelInstanceId that is used further on, when referring to the model instance.
     */
    String createModelInstance(String modelId);

    /**
     * Creates model instance based on the specified model. Provides hints to the engine, on how this model will
     * be used, so that engine can optimize the rendering having those in mind.
     *
     * @param modelId                        ModelId that was returned from the registerModel call.
     * @param modelInstanceOptimizationHints Provides the engine some hints on how this model will be utilized.
     * @return Returns modelInstanceId that is used further on, when referring to the model instance.
     */
    String createModelInstance(String modelId, ModelInstanceOptimizationHints modelInstanceOptimizationHints);

    /**
     * Destroys and frees up any resources used by the specified model instance.
     *
     * @param modelInstanceId ModelInstanceId that was returned from the createModelInstance call.
     */
    void destroyModelInstance(String modelInstanceId);

    /**
     * Updates transform of the given model instance.
     *
     * @param modelInstanceId ModelInstanceId that was returned from the createModelInstance call.
     * @param transformUpdate An update operation that is performed on the transform of the model instance. Please note
     *                        - that the Matrix4 passed is NOT the actual instance of Matrix4 used by the model
     *                        internally.
     */
    void updateTransform(String modelInstanceId, TransformUpdate transformUpdate);

    /**
     * Creates animation controller for the specified model instance.
     *
     * @param modelInstanceId ModelInstanceId that was returned from the createModelInstance call.
     * @return AnimationController used to animate the model instance.
     */
    AnimationController createAnimationController(String modelInstanceId);

    /**
     * Adds tag to the model instance. These tags are used to specify, which shaders should render this model instance.
     * Note, that adding the same tag multiple times with different TagPerformanceHint will result in using only the
     * last setting, and adding a tag with different hint than is currently in use by the model instance, effectively
     * means that the "cost" of removal of the tag has to be "paid", based on the previous setting.
     *
     * @param modelInstanceId     ModelInstanceId that was returned from the createModelInstance call.
     * @param tag                 Tag to add.
     * @param tagOptimizationHint
     */
    void addTag(String modelInstanceId, String tag, TagOptimizationHint tagOptimizationHint);

    /**
     * Removes tag from the model instance.
     *
     * @param modelInstanceId ModelInstanceId that was returned from the createModelInstance call.
     * @param tag             Tag to remove.
     */
    void removeTag(String modelInstanceId, String tag);

    /**
     * Removes all tags from the model instance.
     *
     * @param modelInstanceId ModelInstanceId that was returned from the createModelInstance call.
     */
    void removeAllTags(String modelInstanceId);

    /**
     * Checks whether the model instance has the specified tag.
     *
     * @param modelInstanceId ModelInstanceId that was returned from the createModelInstance call.
     * @param tag             Tag to check the model instance for.
     * @return If the model instance has the tag.
     */
    boolean hasTag(String modelInstanceId, String tag);

    /**
     * Sets property on the given model instance.
     *
     * @param modelInstanceId ModelInstanceId that was returned from the createModelInstance call.
     * @param name            Name of the property.
     * @param value           Value of the property.
     */
    void setProperty(String modelInstanceId, String name, Object value);

    /**
     * Un-sets the property from the given model instance. If the property is un-set, the default value for that
     * property will be used (specified in the Graph editor).
     *
     * @param modelInstanceId ModelInstanceId that was returned from the createModelInstance call.
     * @param name            Name of the property.
     */
    void unsetProperty(String modelInstanceId, String name);

    /**
     * Returns the value of a property from the given model instance.
     * Please note - that if the property is not set on the model instance, a null is returned and NOT the default
     * value (from Graph editor).
     *
     * @param modelInstanceId ModelInstanceId that was returned from the createModelInstance call.
     * @param name            Name of the property.
     * @return Value of the property.
     */
    Object getProperty(String modelInstanceId, String name);
}
