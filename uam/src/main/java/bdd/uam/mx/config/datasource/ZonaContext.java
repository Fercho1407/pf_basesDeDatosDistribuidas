package bdd.uam.mx.config.datasource;

public class ZonaContext {

    private static final ThreadLocal<Zona> CONTEXT = new ThreadLocal<>();

    public static void setZona(Zona zona) {
        CONTEXT.set(zona);
    }

    public static Zona getZona() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}