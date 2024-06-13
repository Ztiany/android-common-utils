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
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import timber.log.Timber;


/**
 * AES 对称加密。
 * <p> <br/>
 * <b>关于默认的加密模式</b>：默认情况下，Android 中的 AES 加密使用的是 CBC（Cipher Block Chaining）模式，并且填充模式是 PKCS7Padding。
 * 在 JDK 中，AES 加密的默认模式是 ECB（Electronic Codebook Mode），默认填充模式是 PKCS5Padding 或 PKCS7Padding（具体取决于 JDK
 * 的版本）。这两种填充模式实际上是相同的，只是 PKCS7Padding 是 PKCS5Padding 的超集，可以处理更多的块大小。
 * <p> <br/>
 * <b>关于 CBC 模式</b>：AES 算法是一种分块加密算法，它的分组大小（也称为块大小）为 128 位（16 字节）。在使用 CBC 模式进行加密时，每个分组
 * 都需要与前一个分组进行异或操作，因此需要指定一个初始向量（IV）来作为第一个分组的前一个分组。在使用 PKCS7Padding 填充方式时，如果明文
 * 长度不是 16 字节的整数倍，则需要使用填充字节将其填充到 16 字节的整数倍。因此，AES/CBC/PKCS7Padding 支持的明文长度范围为 1 到 2^36-1 字
 * 节（因为填充字节需要使用一个字节表示填充长度，因此填充长度最大为 255）。需要注意的是，由于使用 CBC 模式进行加密时需要使用一个 IV 来作
 * 为第一个分组的前一个分组，因此在每次加密时都需要使用一个新的随机 IV。在解密时，需要使用相同的 IV 来解密密文。
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

        /**
         * 指定算法为 AES，则表示使用 SDK 默认的加密模式和填充模式。
         */
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

        /**
         * PBKDF2WithHmacSHA1 是一种基于密码的密钥派生函数（Password-Based Key Derivation Function，PBKDF），它使用 HMAC-SHA1（Hash-based Message Authentication Code with Secure Hash Algorithm 1）算法作为其伪随机函数。
         * <p><br/>
         * PBKDF2 是一种密码学函数，用于从密码和盐值（salt）生成密钥。它的设计目的是增加从密码派生出来的密钥的强度，以抵御暴力破解等攻击。
         * <p><br/>
         * HMAC-SHA1 是一种基于哈希函数 SHA-1 和密钥的消息认证码（MAC）算法。它使用 SHA-1 哈希函数和密钥来生成消息的认证码，并且具有防止伪造认证码的安全特性。
         * <p><br/>
         * 将 PBKDF2 与 HMAC-SHA1 结合在一起，就得到了 PBKDF2WithHmacSHA1 算法，它使用 HMAC-SHA1 作为其伪随机函数来执行密钥派生。
         * <p><br/>
         * PBKDF2WithHmacSHA1 算法通常用于从密码和随机盐值派生出安全的密钥，用于加密或进行身份验证等安全目的。这种算法的强度取决于迭代次数和盐值的质量，通常会通过增加迭代次数和使用随机盐值来提高密钥的安全性。
         *
         * @see AESUtils#generatePBKDAESKey(String, PBKDF2Password)
         */
        private static final String PBKDF2_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA1";

    }

    @NonNull
    private static SecretKeySpec generateAESKey(
            @NonNull String algorithm,
            @NonNull Password password
    ) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (password instanceof GeneralPassword) {
            return generateGeneralAESKey(algorithm, ((GeneralPassword) password).getPassword());
        } else if (password instanceof PBKDF2Password) {
            return generatePBKDAESKey(algorithm, (PBKDF2Password) password);
        }
        throw new IllegalArgumentException("Unsupported password type: " + password.getClass().getName());
    }

    private static SecretKeySpec generateGeneralAESKey(@NonNull String algorithm, @NonNull String password) {
        byte[] passwordData = password.getBytes();
        return new SecretKeySpec(passwordData, algorithm);
    }

    /**
     * 生成基于 PBKDF2 的 AES 密钥
     *
     * @param algorithm 密钥算法，例如 "AES"
     * @param password  密钥密码
     * @return 生成的密钥
     * @throws NoSuchAlgorithmException 如果指定的算法不可用
     * @throws InvalidKeySpecException  如果生成密钥的规范无效
     */
    public static SecretKeySpec generatePBKDAESKey(
            @NonNull String algorithm,
            @NonNull PBKDF2Password password
    ) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 创建密钥工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(Algorithm.PBKDF2_DERIVATION_ALGORITHM);
        // 创建 PBEKeySpec 对象，指定密码、盐值、迭代次数和密钥长度
        PBEKeySpec keySpec = new PBEKeySpec(
                password.getPassword().toCharArray(),
                password.getSalt(),
                password.getIterationCount(),
                password.getKeyLength()
        );
        // 生成密钥并返回
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
        return new SecretKeySpec(keyBytes, algorithm);
    }

    /**
     * 在使用 AES 加密时，可以使用一组初始化向量（Initialization Vector，简称 IV）来增加加密的安全性。IV 的目的是在每次加密操作中引入随机性，即使相同的明文被多次加密，由于每次都使用不同的 IV，所生成的密文也会不同。
     * <p> <br/>
     * 该方法用于根据一组随机数组（即 IV）生成一个 IvParameterSpec（IvParameterSpec 是 AlgorithmParameterSpec 的一种）。按照规范，IV 必须是随机生成的，每次加密时都需要使用一个新的 IV，以保证加密的安全性。
     * <p> <br/>
     * 解密操作需要使用与加密操作相同的初始化向量（IV），通常情况下，在将密文发送给接收方时，通常会将 IV 与密文一起发送。在解密时，解密程序首先从密文中提取 IV，然后使用相同的 IV 和密钥对密文进行解密。你可以从 {@link IvParameterSpec#getIV()} 方法获取生成的 IV。
     *
     * @param length CBC 模式需要每个块都使用一个独立的初始向量，且  IV 的 长度应该与块大小相同，比如在 AES/CBC/PKCS5Padding 加密模式中，AES 算法使用的块大小为 128 位（16 字节），则 IV 的长度应该为 16 字节。
     * @return AlgorithmParameterSpec 生成的参数规范。
     * @throws IllegalArgumentException 如果 length 小于等于 0，则抛出该异常。
     */
    @NonNull
    public static IvParameterSpec generateParameterSpecByIV(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("length 必须大于 0");
        }
        SecureRandom random = new SecureRandom();
        byte[] ivBytes = new byte[length];
        random.nextBytes(ivBytes);
        Timber.d("generateParameterSpec: ivBytes = %s", Arrays.toString(ivBytes));
        return new IvParameterSpec(ivBytes);
    }

    @NonNull
    public static IvParameterSpec generateParameterSpecByIV(String ivContent) {
        Timber.d("generateParameterSpec: ivContent = %s", ivContent);
        return new IvParameterSpec(ivContent.getBytes());
    }

    @NonNull
    public static IvParameterSpec generateParameterSpecByIV(byte[] ivBytes) {
        Timber.d("generateParameterSpec: ivBytes = %s", Arrays.toString(ivBytes));
        return new IvParameterSpec(ivBytes);
    }

    /**
     * @see #generateAESKey(String, int)
     */
    public static String generateAESKeyToBase64(
            @NonNull String algorithm,
            int length
    ) throws NoSuchAlgorithmException {
        return Base64.encodeToString(generateAESKey(algorithm, length), Base64.NO_WRAP);
    }

    /**
     * @param length AES（Advanced Encryption Standard）的密钥长度通常是 128 比特（16 字节）、192 比特（24 字节）或 256 比特（32 字节）。这些密钥长度分别对应 AES 算法的三种变体：AES-128、AES-192和AES-256。AES-128 是最常用的，因为它提供了很高的安全性，并且速度相对较快。AES-192 和 AES-256 提供了更高的安全性，但可能会稍微慢一些，因为密钥长度更长。选择哪种密钥长度取决于应用的安全需求和性能要求。
     */
    public static byte[] generateAESKey(
            @NonNull String algorithm,
            int length
    ) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        keyGenerator.init(length);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    ///////////////////////////////////////////////////////////////////////////
    // encryptData
    ///////////////////////////////////////////////////////////////////////////

    /**
     * @param parameterSpec see {@link #generateParameterSpecByIV(int)} or {@link #generateParameterSpecByIV(String)}
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static byte[] encryptData(
            @NonNull byte[] content,
            @NonNull String algorithm,
            @NonNull Password password,
            @Nullable AlgorithmParameterSpec parameterSpec
    ) throws GeneralSecurityException {
        SecretKeySpec key = generateAESKey(algorithm, password);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
        return cipher.doFinal(content);
    }

    /**
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static byte[] encryptData(
            @NonNull byte[] content,
            @NonNull String algorithm,
            @NonNull Password password
    ) throws GeneralSecurityException {
        return encryptData(content, algorithm, password, null);
    }

    /**
     * @param parameterSpec see {@link #generateParameterSpecByIV(int)} or {@link #generateParameterSpecByIV(String)}
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static byte[] encryptData(
            @NonNull String content,
            @NonNull String algorithm,
            @NonNull Password password,
            @Nullable AlgorithmParameterSpec parameterSpec
    ) throws GeneralSecurityException {
        return encryptData(content.getBytes(), algorithm, password, parameterSpec);
    }

    /**
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static byte[] encryptData(
            @NonNull String content,
            @NonNull String algorithm,
            @NonNull Password password
    ) throws GeneralSecurityException {
        return encryptData(content.getBytes(), algorithm, password);
    }

    /**
     * @param parameterSpec see {@link #generateParameterSpecByIV(int)} or {@link #generateParameterSpecByIV(String)}
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static String encryptDataToBase64(
            @NonNull byte[] content,
            @NonNull String algorithm,
            @NonNull Password password,
            @Nullable AlgorithmParameterSpec parameterSpec
    ) throws GeneralSecurityException {
        byte[] input = encryptData(content, algorithm, password, parameterSpec);
        return Base64.encodeToString(input, Base64.NO_WRAP);
    }

    /**
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static String encryptDataToBase64(
            @NonNull byte[] content,
            @NonNull String algorithm,
            @NonNull Password password
    ) throws GeneralSecurityException {
        return encryptDataToBase64(content, algorithm, password, null);
    }

    /**
     * @param parameterSpec see {@link #generateParameterSpecByIV(int)} or {@link #generateParameterSpecByIV(String)}
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static String encryptDataToBase64(
            @NonNull String content,
            @NonNull String algorithm,
            @NonNull Password password,
            @Nullable AlgorithmParameterSpec parameterSpec
    ) throws GeneralSecurityException {
        return encryptDataToBase64(content.getBytes(), algorithm, password, parameterSpec);
    }

    /**
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static String encryptDataToBase64(
            @NonNull String content,
            @NonNull String algorithm,
            @NonNull Password password
    ) throws GeneralSecurityException {
        return encryptDataToBase64(content.getBytes(), algorithm, password);
    }

    ///////////////////////////////////////////////////////////////////////////
    // decryptData
    ///////////////////////////////////////////////////////////////////////////

    /**
     * @param parameterSpec see {@link #generateParameterSpecByIV(int)} or {@link #generateParameterSpecByIV(String)}
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static byte[] decryptData(
            @NonNull byte[] content,
            @NonNull String algorithm,
            @NonNull Password password,
            @Nullable AlgorithmParameterSpec parameterSpec
    ) throws GeneralSecurityException {
        SecretKeySpec key = generateAESKey(algorithm, password);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
        return cipher.doFinal(content);
    }

    /**
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static byte[] decryptData(
            @NonNull byte[] content,
            @NonNull String algorithm,
            @NonNull Password password
    ) throws GeneralSecurityException {
        return decryptData(content, algorithm, password, null);
    }

    /**
     * @param parameterSpec see {@link #generateParameterSpecByIV(int)} or {@link #generateParameterSpecByIV(String)}
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static String decryptDataToString(
            @NonNull byte[] content,
            @NonNull String algorithm,
            @NonNull Password password,
            @Nullable AlgorithmParameterSpec parameterSpec
    ) throws GeneralSecurityException {
        byte[] bytes = decryptData(content, algorithm, password, parameterSpec);
        return new String(bytes);
    }

    /**
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static String decryptDataToString(
            @NonNull byte[] content,
            @NonNull String algorithm,
            @NonNull Password password
    ) throws GeneralSecurityException {
        return decryptDataToString(content, algorithm, password, null);
    }

    /**
     * @param content       base64 编码的密文。
     * @param parameterSpec see {@link #generateParameterSpecByIV(int)} or {@link #generateParameterSpecByIV(String)}
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static byte[] decryptDataFromBase64(
            @NonNull String content,
            @NonNull String algorithm,
            @NonNull Password password,
            @Nullable AlgorithmParameterSpec parameterSpec
    ) throws GeneralSecurityException {
        return decryptData(Base64.decode(content, Base64.NO_WRAP), algorithm, password, parameterSpec);
    }

    /**
     * @param content base64 编码的密文。
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static byte[] decryptDataFromBase64(
            @NonNull String content,
            @NonNull String algorithm,
            @NonNull Password password
    ) throws GeneralSecurityException {
        return decryptDataFromBase64(content, algorithm, password, null);
    }

    /**
     * @param content       base64 编码的密文。
     * @param parameterSpec see {@link #generateParameterSpecByIV(int)} or {@link #generateParameterSpecByIV(String)}
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static String decryptDataFromBase64ToString(
            @NonNull String content,
            @NonNull String algorithm,
            @NonNull Password password,
            @Nullable AlgorithmParameterSpec parameterSpec
    ) throws GeneralSecurityException {
        byte[] bytes = decryptDataFromBase64(content, algorithm, password, parameterSpec);
        return new String(bytes);
    }

    /**
     * @param content base64 编码的密文
     * @throws GeneralSecurityException maybe maybe {@link NoSuchPaddingException}, {@link NoSuchAlgorithmException}, {@link InvalidAlgorithmParameterException}, {@link InvalidKeyException}, {@link IllegalBlockSizeException}, {@link BadPaddingException},
     */
    @NonNull
    public static String decryptDataFromBase64ToString(
            @NonNull String content,
            @NonNull String algorithm,
            @NonNull Password password
    ) throws GeneralSecurityException {
        return decryptDataFromBase64ToString(content, algorithm, password, null);
    }

}