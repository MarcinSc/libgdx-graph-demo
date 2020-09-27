package com.gempukku.libgdx.graph.shader.builder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.LibGDXCollections;

import java.nio.charset.Charset;

public class GLSLFragmentReader {
    private static ObjectMap<String, String> map = new ObjectMap<String, String>();

    private GLSLFragmentReader() {
    }

    public static String getFragment(String fragmentName) {
        return getFragment(fragmentName, LibGDXCollections.<String, String>emptyMap());
    }

    public static String getFragment(String fragmentName, ObjectMap<String, String> replacements) {
        String fragment = readFragment(fragmentName);
        for (ObjectMap.Entry<String, String> replacement : replacements.entries())
            fragment = fragment.replace(replacement.key, replacement.value);

        return fragment;
    }

    private static String readFragment(String fragmentName) {
        String result = map.get(fragmentName);
        if (result != null)
            return result;

        FileHandle fileHandle = Gdx.files.internal("glsl/fragment/" + fragmentName + ".glsl");
        String fragment = new String(fileHandle.readBytes(), Charset.forName("UTF-8"));
        map.put(fragmentName, fragment);

        return fragment;
    }
}
