package com.cloudstorage.utils;

public class MinioRootFolderUtils {

    public static String getUserRootFolderPrefix(String username) {
        return "user-" + username + "-files/";
    }

    public static String removeUserRootFolderPrefix(String path, String username) {
        String rootFolder = getUserRootFolderPrefix(username);

        return path.replaceAll(rootFolder, "");
    }
}
