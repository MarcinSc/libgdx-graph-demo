package com.gempukku.libgdx.graph.demo.script;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.demo.Script;

public class AbstractScript implements Script {
    private Array<Keyframe> keyframes = new Array<>();
    private Array<Action> actions = new Array<>();
    private float time = 0;

    protected void addKeyframe(Keyframe keyframe) {
        keyframes.add(keyframe);
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    @Override
    public void update(float delta) {
        time += delta;
        Array.ArrayIterator<Keyframe> iterator = keyframes.iterator();
        while (iterator.hasNext()) {
            Keyframe keyframe = iterator.next();
            if (keyframe.getTime() <= time) {
                iterator.remove();
                keyframe.performKeyframe();
            }
        }

        for (Action action : actions) {
            float start = action.getStart();
            float length = action.getLength();
            if (start <= time && time < start + length) {
                action.execute(time - start);
            }
        }
    }
}
