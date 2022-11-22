package model;

import java.util.Map;

public interface Database<T> {
    void insert(T element);

    Map<String, T> getAllPeek();
}
