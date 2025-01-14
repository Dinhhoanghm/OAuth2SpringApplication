package vn.aivhub.oauth.config.annotation;

@FunctionalInterface
public interface SupplierThrowable<T> {
    T get() throws Exception;
}
