package com.gempukku.libgdx.graph.shader.models;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.IdGenerator;
import com.gempukku.libgdx.graph.RandomIdGenerator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GraphShaderModels implements Disposable {
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

    private Map<String, GraphShaderModel> graphShaderModels = new HashMap<>();
    private Array<GraphShaderModelInstanceImpl> models = new Array<>();

    private IdGenerator idGenerator = new RandomIdGenerator(16);

    public String registerModel(Model model) {
        String id = idGenerator.generateId();
        GraphShaderModel graphShaderModel = new GraphShaderModel(idGenerator, model);
        graphShaderModels.put(id, graphShaderModel);
        return id;
    }

    public void removeModel(String modelId) {
        GraphShaderModel model = graphShaderModels.remove(modelId);
        Array.ArrayIterator<GraphShaderModelInstanceImpl> iterator = models.iterator();
        for (GraphShaderModelInstanceImpl graphShaderModelInstance : iterator) {
            if (graphShaderModelInstance.getModel() == model) {
                iterator.remove();
            }
        }
        model.dispose();
    }

    public void addModelDefaultTag(String modelId, String tag) {
        graphShaderModels.get(modelId).addDefaultTag(tag);
    }

    public void removeModelDefaultTag(String modelId, String tag) {
        graphShaderModels.get(modelId).removeDefaultTag(tag);
    }

    public GraphShaderModelInstance createModelInstance(String modelId) {
        GraphShaderModelInstanceImpl graphShaderModelInstance = graphShaderModels.get(modelId).createInstance();
        models.add(graphShaderModelInstance);
        return graphShaderModelInstance;
    }

    public void destroyModelInstance(String modelInstanceId) {
        Array.ArrayIterator<GraphShaderModelInstanceImpl> iterator = models.iterator();
        for (GraphShaderModelInstanceImpl graphShaderModelInstance : iterator) {
            if (graphShaderModelInstance.getId().equals(modelInstanceId)) {
                iterator.remove();
            }
        }
    }

    public AnimationController createAnimationController(String modelInstanceId) {
        for (GraphShaderModelInstanceImpl model : models) {
            if (model.getId().equals(modelInstanceId))
                return new AnimationController(model.getModelInstance());
        }
        return null;
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

    public Iterable<? extends GraphShaderModelInstanceImpl> getModels() {
        return models;
    }

    public Iterable<? extends GraphShaderModelInstanceImpl> getModelsWithTag(final String tag) {
        return StreamSupport.stream(models.spliterator(), false)
                .filter(new Predicate<GraphShaderModelInstanceImpl>() {
                    @Override
                    public boolean test(GraphShaderModelInstanceImpl graphShaderModelInstance) {
                        return graphShaderModelInstance.hasTag(tag);
                    }
                }).collect(Collectors.<GraphShaderModelInstanceImpl>toList());
    }

    @Override
    public void dispose() {
        for (GraphShaderModel model : graphShaderModels.values()) {
            model.dispose();
        }
        graphShaderModels.clear();
        models.clear();
    }

    private static class DistanceRenderableSorter implements Comparator<GraphShaderModelInstanceImpl> {
        private Vector3 cameraPosition;
        private Order order;
        private final Vector3 tmpV1 = new Vector3();
        private final Vector3 tmpV2 = new Vector3();

        public void sort(Vector3 cameraPosition, Array<GraphShaderModelInstanceImpl> renderables, Order order) {
            this.cameraPosition = cameraPosition;
            this.order = order;
            renderables.sort(this);
        }

        private Vector3 getTranslation(Matrix4 worldTransform, Vector3 output) {
            worldTransform.getTranslation(output);
            return output;
        }

        @Override
        public int compare(GraphShaderModelInstanceImpl o1, GraphShaderModelInstanceImpl o2) {
            getTranslation(o1.getTransformMatrix(), tmpV1);
            getTranslation(o2.getTransformMatrix(), tmpV2);
            final float dst = (int) (1000f * cameraPosition.dst2(tmpV1)) - (int) (1000f * cameraPosition.dst2(tmpV2));
            return order.result(dst);
        }
    }
}
