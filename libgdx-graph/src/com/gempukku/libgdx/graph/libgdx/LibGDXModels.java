package com.gempukku.libgdx.graph.libgdx;

import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererModels;

import java.util.Map;
import java.util.WeakHashMap;

public class LibGDXModels implements PipelineRendererModels {
    private ObjectSet<RenderableProvider> models = new ObjectSet<>();

    private Map<Material, ObjectSet<String>> tags = new WeakHashMap<>();

    public void addRenderableProvider(RenderableProvider renderableProvider) {
        models.add(renderableProvider);
    }

    public void removeRenderableProvider(RenderableProvider renderableProvider) {
        models.remove(renderableProvider);
    }

    public void addShaderTag(Material material, String tag) {
        ObjectSet<String> tagSet = tags.get(material);
        if (tagSet == null) {
            tagSet = new ObjectSet<>();
            tags.put(material, tagSet);
        }
        tagSet.add(tag);
    }

    public void removeShaderTag(Material material, String tag) {
        ObjectSet<String> tagSet = tags.get(material);
        if (tagSet != null) {
            tagSet.remove(tag);
        }
    }

    @Override
    public ObjectSet<RenderableProvider> getModels() {
        return models;
    }
}
