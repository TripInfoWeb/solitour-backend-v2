package solitour_backend.solitour.config.routing;

public class DataSourceContextHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setMaster() {
        contextHolder.set("master");
    }

    public static void setReplica() {
        contextHolder.set("replica");
    }

    public static String getCurrentDb() {
        return contextHolder.get();
    }

    public static void clear() {
        contextHolder.remove();
    }
}
