package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;

public class GraphShaderAttribute extends Attribute {
    public static final String GraphShaderAlias = "graphShader";
    public static final long GraphShader = register(GraphShaderAlias);

    private ObjectSet<String> tags = new ObjectSet<>();
    private ObjectMap<String, Object> properties = new ObjectMap<>();

    public GraphShaderAttribute() {
        super(GraphShader);
    }

    public void addShaderTag(String tag) {
        tags.add(tag);
    }

    public void removeShaderTag(String tag) {
        tags.remove(tag);
    }

    public void setProperty(String name, Object value) {
        properties.put(name, value);
    }

    public void removeProperty(String name) {
        properties.remove(name);
    }

    public boolean hasTag(String tag) {
        return tags.contains(tag);
    }

    public Object getProperty(String name) {
        return properties.get(name);
    }

    @Override
    public Attribute copy() {
        GraphShaderAttribute result = new GraphShaderAttribute();
        result.tags = new ObjectSet<>(tags);
        result.properties = new ObjectMap<>(properties);
        return result;
    }

    @Override
    public int compareTo(Attribute o) {
        if (type != o.type) return (int) (type - o.type);
        GraphShaderAttribute other = (GraphShaderAttribute) o;
        return tags.hashCode() + properties.hashCode() - other.tags.hashCode() - other.properties.hashCode();
    }
}
