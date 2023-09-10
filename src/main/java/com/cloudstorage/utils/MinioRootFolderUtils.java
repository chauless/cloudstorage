package com.cloudstorage.utils;

public class MinioRootFolderUtils {

    public static String getUserRootFolderPrefix(String username) {
        return "user-" + username + "-files/";
    }
}
