package com.example.conduit.shared.utils;

import java.util.Arrays;
import java.util.List;

public class StringUtils {

    public static String getSlugTitle(String title) {
        List<String> titleList = Arrays.stream(title
                .toLowerCase()
                .split(" "))
                .toList();
        return String.join("-", titleList);
    }
}
