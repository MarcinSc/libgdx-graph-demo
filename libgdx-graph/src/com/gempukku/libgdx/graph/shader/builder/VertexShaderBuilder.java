package com.gempukku.libgdx.graph.shader.builder;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.gempukku.libgdx.graph.shader.UniformRegistry;

public class VertexShaderBuilder extends CommonShaderBuilder {
    private ObjectMap<String, String> attributeVariables = new OrderedMap<>();

    public VertexShaderBuilder(UniformRegistry uniformRegistry) {
        super(uniformRegistry);
    }

    public void addAttributeVariable(String name, String type) {
        String existingType = attributeVariables.get(name);
        if (existingType != null && !existingType.equals(type))
            throw new IllegalStateException("Already contains vertex attribute of that name with different type");
        if (existingType == null) {
            uniformRegistry.registerAttribute(name);
            attributeVariables.put(name, type);
        }
    }

    private void appendAttributeVariables(StringBuilder stringBuilder) {
        for (ObjectMap.Entry<String, String> uniformDefinition : attributeVariables.entries()) {
            stringBuilder.append("attribute " + uniformDefinition.value + " " + uniformDefinition.key + ";\n");
        }
        if (!attributeVariables.isEmpty())
            stringBuilder.append("\n");
    }

    public String buildProgram() {
        StringBuilder result = new StringBuilder();

        appendInitial(result);
        appendStructures(result);
        appendAttributeVariables(result);
        appendUniformVariables(result);
        appendVaryingVariables(result);
        appendVariables(result);

        appendFunctions(result);

        appendMain(result);

        return result.toString();
    }
}
