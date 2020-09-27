package com.gempukku.libgdx.graph.shader.models.impl;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.IdGenerator;
import com.gempukku.libgdx.graph.RandomIdGenerator;
import com.gempukku.libgdx.graph.shader.models.GraphShaderModels;
import com.gempukku.libgdx.graph.shader.models.ModelInstanceOptimizationHints;
import com.gempukku.libgdx.graph.shader.models.TagOptimizationHint;
import com.gempukku.libgdx.graph.shader.models.TransformUpdate;

import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GraphShaderModelsImpl implements GraphShaderModels {
    private enum Order {
        Front_To_Back, Back_To_Front;

        public int result(float dst) {
            if (this == Front_To_Back)
                return dst > 0 ? 1 : (dst < 0 ? -1 : 0);
            else
                return dst < 0 ? 1 : (dst > 0 ? -1 : 0);
        }
    }

    private Vector3 cameraPosition = new Vector3();
    private Order order;
    private DistanceRenderableSorter sorter = new DistanceRenderableSorter();

    private ObjectMap<String, GraphShaderModel> graphShaderModels = new ObjectMap<>();
    private Array<GraphShaderModelInstance> models = new Array<>();

    private IdGenerator idGenerator = new RandomIdGenerator(16);
    private Matrix4 transformTempMatrix = new Matrix4();

    @Override
    public String registerModel(Model model) {
        String id = idGenerator.generateId();
        GraphShaderModel graphShaderModel = new GraphShaderModel(idGenerator, model);
        graphShaderModels.put(id, graphShaderModel);
        return id;
    }

    @Override
    public void removeModel(String modelId) {
        GraphShaderModel model = graphShaderModels.remove(modelId);
        Array.ArrayIterator<GraphShaderModelInstance> iterator = models.iterator();
        for (GraphShaderModelInstance graphShaderModelInstance : iterator) {
            if (graphShaderModelInstance.getModel() == model) {
                iterator.remove();
            }
        }
        model.dispose();
    }

    @Override
    public void addModelDefaultTag(String modelId, String tag, TagOptimizationHint tagOptimizationHint) {
        graphShaderModels.get(modelId).addDefaultTag(tag, tagOptimizationHint);
    }

    @Override
    public void removeModelDefaultTag(String modelId, String tag) {
        graphShaderModels.get(modelId).removeDefaultTag(tag);
    }

    @Override
    public String createModelInstance(String modelId) {
        return createModelInstance(modelId, ModelInstanceOptimizationHints.unoptimized);
    }

    @Override
    public String createModelInstance(String modelId, ModelInstanceOptimizationHints modelInstanceOptimizationHints) {
        GraphShaderModelInstance graphShaderModelInstance = graphShaderModels.get(modelId).createInstance(modelInstanceOptimizationHints);
        models.add(graphShaderModelInstance);
        return graphShaderModelInstance.getId();
    }

    @Override
    public void destroyModelInstance(String modelInstanceId) {
        Array.ArrayIterator<GraphShaderModelInstance> iterator = models.iterator();
        for (GraphShaderModelInstance graphShaderModelInstance : iterator) {
            if (graphShaderModelInstance.getId().equals(modelInstanceId)) {
                iterator.remove();
            }
        }
    }

    @Override
    public void updateTransform(String modelInstanceId, TransformUpdate transformUpdate) {
        ModelInstance modelInstance = getModelInstance(modelInstanceId).getModelInstance();
        transformTempMatrix.set(modelInstance.transform);
        transformUpdate.updateTransform(transformTempMatrix);
        modelInstance.transform.set(transformTempMatrix);
    }

    public AnimationController createAnimationController(String modelInstanceId) {
        return new AnimationController(getModelInstance(modelInstanceId).getModelInstance());
    }

    private GraphShaderModelInstance getModelInstance(String modelInstanceId) {
        for (GraphShaderModelInstance model : models) {
            if (model.getId().equals(modelInstanceId))
                return model;
        }
        return null;
    }

    @Override
    public void addTag(String modelInstanceId, String tag, TagOptimizationHint tagOptimizationHint) {
        getModelInstance(modelInstanceId).addTag(tag, tagOptimizationHint);
    }

    @Override
    public void removeTag(String modelInstanceId, String tag) {
        getModelInstance(modelInstanceId).removeTag(tag);
    }

    @Override
    public void removeAllTags(String modelInstanceId) {
        getModelInstance(modelInstanceId).removeAllTags();
    }

    @Override
    public void setProperty(String modelInstanceId, String name, Object value) {
        getModelInstance(modelInstanceId).setProperty(name, value);
    }

    @Override
    public void unsetProperty(String modelInstanceId, String name) {
        getModelInstance(modelInstanceId).unsetProperty(name);
    }

    @Override
    public boolean hasTag(String modelInstanceId, String tag) {
        return getModelInstance(modelInstanceId).hasTag(tag);
    }

    @Override
    public Object getProperty(String modelInstanceId, String name) {
        return getModelInstance(modelInstanceId).getProperty(name);
    }

    public void prepareForRendering(Camera camera) {
        cameraPosition.set(camera.position);
        order = null;
    }

    public void orderFrontToBack() {
        if (order == Order.Back_To_Front)
            models.reverse();
        if (order == null)
            sorter.sort(cameraPosition, models, Order.Front_To_Back);
        order = Order.Front_To_Back;
    }

    public void orderBackToFront() {
        if (order == Order.Front_To_Back)
            models.reverse();
        if (order == null)
            sorter.sort(cameraPosition, models, Order.Back_To_Front);
        order = Order.Back_To_Front;
    }

    public Iterable<? extends GraphShaderModelInstance> getModels() {
        return models;
    }

    public Iterable<? extends GraphShaderModelInstance> getModelsWithTag(final String tag) {
        return StreamSupport.stream(models.spliterator(), false)
                .filter(new Predicate<GraphShaderModelInstance>() {
                    @Override
                    public boolean test(GraphShaderModelInstance graphShaderModelInstance) {
                        return graphShaderModelInstance.hasTag(tag);
                    }
                }).collect(Collectors.<GraphShaderModelInstance>toList());
    }

    @Override
    public void dispose() {
        for (GraphShaderModel model : graphShaderModels.values()) {
            model.dispose();
        }
        graphShaderModels.clear();
        models.clear();
    }

    private static class DistanceRenderableSorter implements Comparator<GraphShaderModelInstance> {
        private Vector3 cameraPosition;
        private Order order;
        private final Vector3 tmpV1 = new Vector3();
        private final Vector3 tmpV2 = new Vector3();

        public void sort(Vector3 cameraPosition, Array<GraphShaderModelInstance> renderables, Order order) {
            this.cameraPosition = cameraPosition;
            this.order = order;
            renderables.sort(this);
        }

        private Vector3 getTranslation(Matrix4 worldTransform, Vector3 output) {
            worldTransform.getTranslation(output);
            return output;
        }

        @Override
        public int compare(GraphShaderModelInstance o1, GraphShaderModelInstance o2) {
            getTranslation(o1.getTransformMatrix(), tmpV1);
            getTranslation(o2.getTransformMatrix(), tmpV2);
            final float dst = (int) (1000f * cameraPosition.dst2(tmpV1)) - (int) (1000f * cameraPosition.dst2(tmpV2));
            return order.result(dst);
        }
    }
}
