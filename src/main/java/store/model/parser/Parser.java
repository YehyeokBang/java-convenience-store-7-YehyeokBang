package store.model.parser;

public interface Parser<K, V> {

    V parse(K input);
}
