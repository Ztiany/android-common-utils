package com.android.base.utils.security;

import android.util.Base64;

import androidx.annotation.Nullable;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


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

    private static final int LIMIT_LEN = 16;

    private static SecretKeySpec generateAESKey(String algorithm, String password) {
        byte[] passwordData = password.getBytes();
        if (passwordData.length != LIMIT_LEN) {
            throw new IllegalArgumentException("password 长度必须等于16");
        }
        return new SecretKeySpec(passwordData, algorithm);
    }

    public static IvParameterSpec generateIvParameterSpec(int length) {
        SecureRandom random = new SecureRandom();
        byte[] ivBytes = new byte[length]; // 16字节的初始向量
        random.nextBytes(ivBytes); // 生成随机的初始向量
        return new IvParameterSpec(ivBytes);
    }

    ///////////////////////////////////////////////////////////////////////////
    // encryptData
    ///////////////////////////////////////////////////////////////////////////

    @Nullable
    public static byte[] encryptData(byte[] content, String algorithm, String password, IvParameterSpec ivParameterSpec) {
        try {
            SecretKeySpec key = generateAESKey(algorithm, password);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static byte[] encryptData(byte[] content, String algorithm, String password) {
        return encryptData(content, algorithm, password, null);
    }

    @Nullable
    public static byte[] encryptData(String content, String algorithm, String password, IvParameterSpec ivParameterSpec) {
        return encryptData(content.getBytes(), algorithm, password);
    }

    @Nullable
    public static byte[] encryptData(String content, String algorithm, String password) {
        return encryptData(content.getBytes(), algorithm, password);
    }

    @Nullable
    public static String encryptDataToBase64(byte[] content, String algorithm, String password, IvParameterSpec ivParameterSpec) {
        byte[] input = encryptData(content, algorithm, password, ivParameterSpec);
        if (input == null) return null;
        return Base64.encodeToString(input, Base64.NO_WRAP);
    }

    @Nullable
    public static String encryptDataToBase64(byte[] content, String algorithm, String password) {
        return encryptDataToBase64(content, algorithm, password, null);
    }

    @Nullable
    public static String encryptDataToBase64(String content, String algorithm, String password, IvParameterSpec ivParameterSpec) {
        return encryptDataToBase64(content.getBytes(), algorithm, password, ivParameterSpec);
    }

    @Nullable
    public static String encryptDataToBase64(String content, String algorithm, String password) {
        return encryptDataToBase64(content.getBytes(), algorithm, password);
    }

    ///////////////////////////////////////////////////////////////////////////
    // decryptData
    ///////////////////////////////////////////////////////////////////////////

    @Nullable
    public static byte[] decryptData(byte[] content, String algorithm, String password, IvParameterSpec ivParameterSpec) {
        try {
            SecretKeySpec key = generateAESKey(algorithm, password);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static byte[] decryptData(byte[] content, String algorithm, String password) {
        return decryptData(content, algorithm, password, null);
    }

    public static String decryptDataToString(byte[] content, String algorithm, String password, IvParameterSpec ivParameterSpec) {
        byte[] bytes = decryptData(content, algorithm, password, ivParameterSpec);
        return bytes == null ? "" : new String(bytes);
    }

    public static String decryptDataToString(byte[] content, String algorithm, String password) {
        return decryptDataToString(content, algorithm, password, null);
    }

    /**
     * @param content base64 编码的密文
     */
    @Nullable
    public static byte[] decryptDataFromBase64(String content, String algorithm, String password, IvParameterSpec ivParameterSpec) {
        return decryptData(Base64.decode(content, Base64.NO_WRAP), algorithm, password, ivParameterSpec);
    }

    /**
     * @param content base64 编码的密文
     */
    @Nullable
    public static byte[] decryptDataFromBase64(String content, String algorithm, String password) {
        return decryptDataFromBase64(content, algorithm, password, null);
    }

    /**
     * @param content base64 编码的密文
     */
    @Nullable
    public static String decryptDataFromBase64ToString(String content, String algorithm, String password, IvParameterSpec ivParameterSpec) {
        byte[] bytes = decryptDataFromBase64(content, algorithm, password, ivParameterSpec);
        return bytes == null ? null : new String(bytes);
    }

    /**
     * @param content base64 编码的密文
     */
    public static String decryptDataFromBase64ToString(String content, String algorithm, String password) {
        return decryptDataFromBase64ToString(content, algorithm, password, null);
    }

}