package com.example.conduit.shared.utils;

import java.util.Arrays;
import java.util.List;

public class StringUtils {

    public static String getSlugTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return "";
        }
        List<String> titleList = Arrays.stream(title
                        .toLowerCase()
                        .trim()
                        .split(" "))
                .filter(s -> !s.isEmpty())
                .toList();
        return String.join("-", titleList);
    }
}
