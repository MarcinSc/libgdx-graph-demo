package com.gempukku.libgdx.graph;

import com.badlogic.gdx.math.MathUtils;

public class RandomIdGenerator implements IdGenerator {
    private static char[] characters = new String("0123456789ABCDEF").toCharArray();
    private int length;

    public RandomIdGenerator(int length) {
        this.length = length;
    }

    @Override
    public String generateId() {
        char[] result = new char[length];
        for (int i = 0; i < length; i++) {
            result[i] = characters[MathUtils.random(characters.length - 1)];
        }
        return new String(result);
    }
}
