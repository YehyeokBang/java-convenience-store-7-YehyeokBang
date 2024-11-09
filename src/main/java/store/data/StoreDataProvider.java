package store.data;

import java.util.List;

public interface StoreDataProvider<T> {

    List<T> getAll();
}
