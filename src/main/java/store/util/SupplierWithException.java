package store.util;

@FunctionalInterface
public interface SupplierWithException<T> {

    T get() throws IllegalArgumentException;
}
