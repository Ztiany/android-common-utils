package com.android.base.utils.security;

import androidx.annotation.Nullable;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Ztiany
 */
public class SHAUtils {

    private static final String SHA256 = "SHA256";

    @Nullable
    public static byte[] toSHA256(String content) {
        return toSHA256(content.getBytes(StandardCharsets.UTF_8));
    }

    @Nullable
    public static byte[] toSHA256(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance(SHA256);
            md.update(bytes);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
