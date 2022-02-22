package org.spbstu.file_host.util;

public class FileSystemObjectUtil {
    public static String removeExtension(String fileNameWithExtension) {
        return fileNameWithExtension.lastIndexOf('.') != -1 ?
                fileNameWithExtension.substring(0, fileNameWithExtension.lastIndexOf('.'))
                : fileNameWithExtension;
    }

    public static String getExtension(String fileNameWithExtension) {
        return fileNameWithExtension.lastIndexOf('.') != -1 ?
                fileNameWithExtension.substring(fileNameWithExtension.lastIndexOf('.'))
                : "";
    }
}
