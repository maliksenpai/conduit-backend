package com.example.conduit.shared.utils;

import java.util.function.Consumer;

public class ObjectUtils {
    public static <T> void updateFieldIfPresent(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
