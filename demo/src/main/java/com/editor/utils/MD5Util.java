package com.editor.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    //计算文件的md5值
    public static String getFileMD5(File file) throws IOException, NoSuchAlgorithmException {
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int len;
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        while ((len = fis.read(buffer)) != -1) {
            md5.update(buffer, 0, len);
        }
        fis.close();
        return new BigInteger(1, md5.digest()).toString(16);
    }
}
