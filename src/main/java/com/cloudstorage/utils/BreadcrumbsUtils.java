package com.cloudstorage.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BreadcrumbsUtils {

    public static List<String> getBreadcrumbLinksForPath(String path) {
        if (path.isEmpty()) {
            return List.of(path);
        }

        List<String> links = new ArrayList<>();

        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) == '/') {
                links.add(path.substring(0, i));
            }
        }

        return links;
    }

    public static List<String> getFolderNamesForPath(String path) {
        if (path.isEmpty()) {
            return List.of(path);
        }
        return Arrays.stream(path.split("/")).toList();
    }
}
