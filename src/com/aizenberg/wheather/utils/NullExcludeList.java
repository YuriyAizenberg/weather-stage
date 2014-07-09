package com.aizenberg.wheather.utils;

import java.util.ArrayList;
import java.util.Collection;

public class NullExcludeList<T> extends ArrayList<T> {

    public NullExcludeList(Collection<? extends T> collection) {
        if (collection != null) {
            for (T t : collection) {
                add(t);
            }
        }
    }

    public NullExcludeList() {
    }

    @Override
    public boolean add(T object) {
        return object != null && super.add(object);
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size()) return null;
        return super.get(index);
    }
}
