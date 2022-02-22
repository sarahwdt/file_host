package org.spbstu.file_host.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumUtil {
    public static byte[] getChecksum(InputStream inputStream) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        try (DigestInputStream digestInputStream = new DigestInputStream(inputStream, messageDigest)) {
            digestInputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return messageDigest.digest();
    }
}
