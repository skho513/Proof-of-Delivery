package com.example.podlibrary;

/**
 * Dependency provider to be used within this library
 *
 * @author milan
 */
public class PodInjector {
    private static PodComponent component;

    public static PodComponent getComponent() {
        return component;
    }

    public static void setComponent(PodComponent component) {
        PodInjector.component = component;
    }
}
