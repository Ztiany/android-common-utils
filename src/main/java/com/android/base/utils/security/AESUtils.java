package com.android.base.utils.security;

import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import timber.log.Timber;


/**
 * AES 对称加密。
 * <p> <br/>
 * <b>关于 CBC 模式</b>：AES 算法是一种分块加密算法，它的分组大小（也称为块大小）为 128 位（16 字节）。在使用 CBC 模式进行加密时，每个分组都需要与前一个分组进行异或操作，
 * 因此需要指定一个初始向量（IV）来作为第一个分组的前一个分组。在使用 PKCS7Padding 填充方式时，如果明文长度不是 16 字节的整数倍，则需要使用填充字节将其填充到 16 字节的整
 * 数倍。因此，AES/CBC/PKCS7Padding 支持的明文长度范围为 1 到 2^36-1 字节（因为填充字节需要使用一个字节表示填充长度，因此填充长度最大为 255）。需要注意的是，由于使用 CBC
 * 模式进行加密时需要使用一个 IV 来作为第一个分组的前一个分组，因此在每次加密时都需要使用一个新的随机 IV。在解密时，需要使用相同的 IV 来解密密文。
 * <p> <br/>
 * 相关参考链接：
 * <ol>
 *     <li>
 *         <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/CryptoSpec.html#Introduction">Java Cryptography Architecture (JCA) Reference Guide</a>
 *     </li>
 *     <li>
 *         <a href="https://stackoverflow.com/questions/10935068/what-are-the-cipher-padding-strings-in-java/10935308#10935308">What are the cipher padding strings in java</a>
 *     </li>
 * </ol>
 */
public class AESUtils {

    @SuppressWarnings("unused")
    public static class Algorithm {

        //算法/加密模式/填充模式
        public static String AES = "AES";

        public static String AES_CBC_ISO10126PADDING = "AES/CBC/ISO10126Padding";
        public static String AES_CBC_NOPADDING = "AES/CBC/NoPadding";

        /**
         * 在 AES/CBC/PKCS5Padding 加密模式中，AES 算法使用的块大小为 128 位（16 字节），而 CBC 模式需要每个块都使用一个独立的初始向量，因此 IV 的长度应该与块大小相同，即 16 字节。
         */
        public static String AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5Padding";
        public static int AES_CBC_PKCS5PADDING_BLOCK_LEN = 16;

        /**
         * AES/CBC/PKCS5Padding 和 AES/CBC/PKCS7Padding 是两种常见的对称加密模式，它们的主要区别在于填充方式不同。在 PKCS#5 规范中，定义了一种填充方式，叫做 PKCS#5 填充（也称为 PKCS#5 padding），它是针对 8 字节块的加密算法而定义的，其填充方式与 PKCS#7 填充完全相同。
         * 因此，在 AES 算法使用的 16 字节块大小的 CBC 模式中，PKCS#5 填充和 PKCS#7 填充的效果是相同的，都是将明文填充到块大小的整数倍。在实际应用中，常常使用 PKCS#7 填充方式来替代 PKCS#5 填充方式，因为 PKCS#7 填充方式是针对所有块大小的加密算法而设计的，可以用于对不同块大小的加密算法进行填充，
         * 同时在  Java 中，也只提供了 PKCS#7 填充方式的实现。因此，在使用 Java 的加密 API 进行 AES/CBC/PKCS5Padding 填充时，实际上使用的是 PKCS#7 填充方式。 综上所述，AES/CBC/PKCS5Padding 和 AES/CBC/PKCS7Padding 的主要区别在于填充方式不同，但在 AES 算法的 16 字节块大小的 CBC 模式中，它们的效果是相同的。
         * <p><br/>
         * 具体参考：
         * <ol>
         *     <li><a href="https://crypto.stackexchange.com/questions/9043/what-is-the-difference-between-pkcs5-padding-and-pkcs7-padding">What is the difference between PKCS#5 padding and PKCS#7 padding</a>></li>
         * </ol>
         */
        public static String AES_CBC_PKCS7PADDING = "AES/CBC/PKCS7Padding";

        public static String AES_CFB_ISO10126PADDING = "AES/CFB/ISO10126Padding";
        public static String AES_CFB_NOPADDING = "AES/CFB/NoPadding";
        public static String AES_CFB_PKCS5PADDING = "AES/CFB/PKCS5Padding";
        public static String AES_CTR_ISO10126PADDING = "AES/CTR/ISO10126Padding";
        public static String AES_CTR_NOPADDING = "AES/CTR/NoPadding";
        public static String AES_CTR_PKCS5PADDING = "AES/CTR/PKCS5Padding";
        public static String AES_CTS_ISO10126PADDING = "AES/CTS/ISO10126Padding";
        public static String AES_CTS_NOPADDING = "AES/CTS/NoPadding";
        public static String AES_CTS_PKCS5PADDING = "AES/CTS/PKCS5Padding";
        public static String AES_ECB_ISO10126PADDING = "AES/ECB/ISO10126Padding";
        public static String AES_ECB_NOPADDING = "AES/ECB/NoPadding";
        public static String AES_ECB_PKCS5PADDING = "AES/ECB/PKCS5Padding";
        public static String AES_OFB_ISO10126PADDING = "AES/OFB/ISO10126Padding";
        public static String AES_OFB_NOPADDING = "AES/OFB/NoPadding";
        public static String AES_OFB_PKCS5PADDING = "AES/OFB/PKCS5Padding";
    }

    private static final int LIMIT_LEN = 16;

    /**
     * @throws InvalidKeyException 如果 password 的长度不等于 16，则抛出此异常。
     */
    @NonNull
    private static SecretKeySpec generateAESKey(String algorithm, String password) throws InvalidKeyException {
        byte[] passwordData = password.getBytes();
        if (passwordData.length != LIMIT_LEN) {
            throw new InvalidKeyException("password 的长度必须等于16");
        }
        return new SecretKeySpec(passwordData, algorithm);
    }

    /**
     * 生成 AES 算法的参数规范。IV 必须是随机生成的，每次加密时都需要使用一个新的 IV，以保证加密的安全性。
     *
     * @param length CBC 模式需要每个块都使用一个独立的初始向量，且因此  IV 的 长度应该与块大小相同，比如在 AES/CBC/PKCS5Padding 加密模式中，AES 算法使用的块大小为 128 位（16 字节），因此 IV 的长度应该为 16 字节。
     * @return AlgorithmParameterSpec 生成的参数规范。
     * @throws IllegalArgumentException 如果 length 小于等于 0，则抛出该异常。
     */
    @NonNull
    public static AlgorithmParameterSpec generateParameterSpec(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("length 必须大于 0");
        }
        SecureRandom random = new SecureRandom();
        byte[] ivBytes = new byte[length];
        random.nextBytes(ivBytes);
        Timber.d("generateParameterSpec: ivBytes = %s", Arrays.toString(ivBytes));
        return new IvParameterSpec(ivBytes);
    }

    ///////////////////////////////////////////////////////////////////////////
    // encryptData
    ///////////////////////////////////////////////////////////////////////////

    /**
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static byte[] encryptData(@NonNull byte[] content, @NonNull String algorithm, @NonNull String password, @Nullable AlgorithmParameterSpec parameterSpec) throws GeneralSecurityException {
        SecretKeySpec key = generateAESKey(algorithm, password);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
        return cipher.doFinal(content);
    }

    /**
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static byte[] encryptData(@NonNull byte[] content, @NonNull String algorithm, @NonNull String password) throws GeneralSecurityException {
        return encryptData(content, algorithm, password, null);
    }

    /**
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static byte[] encryptData(@NonNull String content, @NonNull String algorithm, @NonNull String password, @Nullable AlgorithmParameterSpec parameterSpec) throws GeneralSecurityException {
        return encryptData(content.getBytes(), algorithm, password, parameterSpec);
    }

    /**
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static byte[] encryptData(@NonNull String content, @NonNull String algorithm, @NonNull String password) throws GeneralSecurityException {
        return encryptData(content.getBytes(), algorithm, password);
    }

    /**
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static String encryptDataToBase64(@NonNull byte[] content, @NonNull String algorithm, @NonNull String password, @Nullable AlgorithmParameterSpec parameterSpec) throws GeneralSecurityException {
        byte[] input = encryptData(content, algorithm, password, parameterSpec);
        return Base64.encodeToString(input, Base64.NO_WRAP);
    }

    /**
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static String encryptDataToBase64(@NonNull byte[] content, @NonNull String algorithm, @NonNull String password) throws GeneralSecurityException {
        return encryptDataToBase64(content, algorithm, password, null);
    }

    /**
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static String encryptDataToBase64(@NonNull String content, @NonNull String algorithm, @NonNull String password, @Nullable AlgorithmParameterSpec parameterSpec) throws GeneralSecurityException {
        return encryptDataToBase64(content.getBytes(), algorithm, password, parameterSpec);
    }

    /**
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static String encryptDataToBase64(@NonNull String content, @NonNull String algorithm, @NonNull String password) throws GeneralSecurityException {
        return encryptDataToBase64(content.getBytes(), algorithm, password);
    }

    ///////////////////////////////////////////////////////////////////////////
    // decryptData
    ///////////////////////////////////////////////////////////////////////////

    /**
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static byte[] decryptData(@NonNull byte[] content, @NonNull String algorithm, @NonNull String password, @Nullable AlgorithmParameterSpec parameterSpec) throws GeneralSecurityException {
        SecretKeySpec key = generateAESKey(algorithm, password);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
        return cipher.doFinal(content);
    }

    /**
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static byte[] decryptData(@NonNull byte[] content, @NonNull String algorithm, @NonNull String password) throws GeneralSecurityException {
        return decryptData(content, algorithm, password, null);
    }

    /**
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static String decryptDataToString(@NonNull byte[] content, @NonNull String algorithm, @NonNull String password, @Nullable AlgorithmParameterSpec parameterSpec) throws GeneralSecurityException {
        byte[] bytes = decryptData(content, algorithm, password, parameterSpec);
        return new String(bytes);
    }

    /**
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static String decryptDataToString(@NonNull byte[] content, @NonNull String algorithm, @NonNull String password) throws GeneralSecurityException {
        return decryptDataToString(content, algorithm, password, null);
    }

    /**
     * @param content base64 编码的密文。
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static byte[] decryptDataFromBase64(@NonNull String content, @NonNull String algorithm, @NonNull String password, @Nullable AlgorithmParameterSpec parameterSpec) throws GeneralSecurityException {
        return decryptData(Base64.decode(content, Base64.NO_WRAP), algorithm, password, parameterSpec);
    }

    /**
     * @param content base64 编码的密文。
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static byte[] decryptDataFromBase64(@NonNull String content, @NonNull String algorithm, @NonNull String password) throws GeneralSecurityException {
        return decryptDataFromBase64(content, algorithm, password, null);
    }

    /**
     * @param content base64 编码的密文。
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static String decryptDataFromBase64ToString(@NonNull String content, @NonNull String algorithm, @NonNull String password, @Nullable AlgorithmParameterSpec parameterSpec) throws GeneralSecurityException {
        byte[] bytes = decryptDataFromBase64(content, algorithm, password, parameterSpec);
        return new String(bytes);
    }

    /**
     * @param content base64 编码的密文
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static String decryptDataFromBase64ToString(@NonNull String content, @NonNull String algorithm, @NonNull String password) throws GeneralSecurityException {
        return decryptDataFromBase64ToString(content, algorithm, password, null);
    }

}