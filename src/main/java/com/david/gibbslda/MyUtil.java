package com.david.gibbslda;

class MyUtil {

    public static String getFolderPath (String filePathName) {

        int idx = filePathName.lastIndexOf ("/");
        String folderPath = filePathName.substring (0,idx);
        return folderPath;

    }
}
