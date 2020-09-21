package com.gempukku.libgdx.graph.demo.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gempukku.libgdx.graph.demo.LibgdxGraphDemo;

/**
 * Launches the desktop (LWJGL) application.
 */
public class DesktopLauncher {
    public static void main(String[] args) {
        createApplication();
    }

    private static LwjglApplication createApplication() {
        return new LwjglApplication(new LibgdxGraphDemo(), getDefaultConfiguration());
    }

    private static LwjglApplicationConfiguration getDefaultConfiguration() {
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.title = "libGDX Graph demo";
        configuration.width = 1440;
        configuration.height = 810;
        //// This prevents a confusing error that would appear after exiting normally.
        configuration.forceExit = false;

        for (int size : new int[]{128, 64, 32, 16}) {
            configuration.addIcon("libgdx" + size + ".png", FileType.Internal);
        }
        return configuration;
    }
}