package com.gempukku.libgdx.graph.demo.script;

public interface Action {
    float getStart();

    float getLength();

    void execute(float timeSinceStart);
}
