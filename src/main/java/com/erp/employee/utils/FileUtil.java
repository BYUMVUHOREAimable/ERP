package com.erp.employee.utils;


import com.erp.employee.enums.EFileSizeType;

import java.util.UUID;

public class FileUtil {

    public static EFileSizeType getFileSizeTypeFromFileSize(long size) {
        if (size >= (1024L * 1024 * 1024 * 1024))
            return EFileSizeType.TB;
        else if (size >= 1024 * 1024 * 1024)
            return EFileSizeType.GB;
        else if (size >= 1024 * 1024)
            return EFileSizeType.MB;
        else if (size >= 1024)
            return EFileSizeType.KB;
        else
            return EFileSizeType.B;
    }

    public static int getFormattedFileSizeFromFileSize(double size, EFileSizeType type ) {
        if (type == EFileSizeType.TB)
            return (int) (size / (1024L * 1024 * 1024 * 1024));
        else if (type == EFileSizeType.GB)
            return (int) (size / (1024 * 1024 * 1024));
        else if (type == EFileSizeType.MB)
            return (int) (size / (1024 * 1024));
        else if (type == EFileSizeType.KB)
            return (int) (size / (1024));
        else
            return (int) size;
    }

    public static String generateUUID(String fileName) {
        int period = fileName.indexOf(".");
        String prefix = fileName.substring(0, period);
        String suffix = fileName.substring(period);

        return prefix + "-" +  UUID.randomUUID().toString().replace("-", "")  + suffix;
    }



}
