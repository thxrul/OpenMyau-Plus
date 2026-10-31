package myau.util;

import java.lang.reflect.Method;

/**
 * OptiFine detection and compatibility helper to prevent blur and shader pipeline
 * interference when OptiFine Fast Render or Shaderpacks are active.
 */
public final class OptifineUtil {
    private static Boolean optifineLoaded = null;
    private static Class<?> configClass = null;
    private static Method isFastRenderMethod = null;
    private static Method isShadersMethod = null;
    private static Method isAntialiasingMethod = null;

    private static void init() {
        if (optifineLoaded != null) return;
        try {
            configClass = Class.forName("net.optifine.Config");
            optifineLoaded = true;
        } catch (ClassNotFoundException e1) {
            try {
                configClass = Class.forName("Config");
                optifineLoaded = true;
            } catch (ClassNotFoundException e2) {
                optifineLoaded = false;
                return;
            }
        }

        try {
            isFastRenderMethod = configClass.getMethod("isFastRender");
        } catch (Throwable ignored) {}

        try {
            isShadersMethod = configClass.getMethod("isShaders");
        } catch (Throwable ignored) {}

        try {
            isAntialiasingMethod = configClass.getMethod("isAntialiasing");
        } catch (Throwable ignored) {}
    }

    public static boolean isOptifinePresent() {
        init();
        return Boolean.TRUE.equals(optifineLoaded);
    }

    public static boolean isFastRender() {
        init();
        if (!isOptifinePresent() || isFastRenderMethod == null) return false;
        try {
            Object result = isFastRenderMethod.invoke(null);
            return result instanceof Boolean && (Boolean) result;
        } catch (Throwable t) {
            return false;
        }
    }

    public static boolean isShaders() {
        init();
        if (!isOptifinePresent() || isShadersMethod == null) return false;
        try {
            Object result = isShadersMethod.invoke(null);
            return result instanceof Boolean && (Boolean) result;
        } catch (Throwable t) {
            return false;
        }
    }

    public static boolean isAntialiasing() {
        init();
        if (!isOptifinePresent() || isAntialiasingMethod == null) return false;
        try {
            Object result = isAntialiasingMethod.invoke(null);
            return result instanceof Boolean && (Boolean) result;
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * Determines whether custom framebuffer blur shaders should be safely bypassed
     * to avoid breaking or corrupting OptiFine rendering.
     */
    public static boolean shouldBypassBlur() {
        return isFastRender() || isShaders() || isAntialiasing();
    }

    private OptifineUtil() {}
}
