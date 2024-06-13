package com.android.base.utils.security;

import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.blankj.utilcode.util.EncodeUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import timber.log.Timber;

/**
 * RSA 非对称加密。
 *
 * @see <a href='https://android-developers.googleblog.com/2018/03/cryptography-changes-in-android-p.html'>cryptography-changes-in-android-p<a/>
 * @see <a href='https://stackoverflow.com/questions/12471999/rsa-encryption-decryption-in-android/12474193'>rsa-encryption-decryption-in-android/12474193<a/>
 * @see <a href='https://proandroiddev.com/secure-data-in-android-encrypting-large-data-dda256a55b36'>secure-data-in-android-encrypting-large-data-dda256a55b36<a/>
 */
public final class RSAUtils {

    /**
     * 秘钥默认长度
     */
    public static final int DEFAULT_KEY_LENGTH = 1024;

    public static class Algorithm {

        /**
         * 默认。
         */
        public static final String RSA = "RSA";

        /**
         * JDK 标准。
         */
        public static final String RSA_ECB_PKCS1PADDING = "RSA/ECB/PKCS1Padding";

        /**
         * Android 标准。
         */
        public static final String RSA_ECB_NOPADDING = "RSA/ECB/NoPadding";

        /**
         * 签名校验算法：SHA256_RSA
         */
        public static final String SHA256_RSA = "SHA256withRSA";

        /**
         * 签名校验算法：SHA1_RSA
         */
        public static final String SHA1_RSA = "SHA1withRSA";
    }

    ///////////////////////////////////////////////////////////////////////////
    // 加解密
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 当加密的数据过长时，会出现 javax.crypto.IllegalBlockSizeException: Data must not be longer than 117 bytes 的异常。
     * RSA 算法规定一次加解密的数据不能超过生成密钥对时的 keyLength/8-11，比如 keyLength 一般是 1024 个字节，则加
     * 密的数据不能超过 117 个字节。
     */
    public static int calculateMaxRoundLength(int keyLength) {
        return (keyLength / 8) - 11;
    }

    /**
     * 使用公钥加密。
     *
     * @see #loadPublicKey(String)
     * @see #getPublicKey(byte[])
     */
    public static byte[] encryptData(
            String algorithm,
            byte[] data,
            PublicKey publicKey,
            int keyLength
    ) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        return doTransformWork(algorithm, data, publicKey, Cipher.ENCRYPT_MODE, keyLength);
    }

    /**
     * 使用私钥解密。
     *
     * @see #loadPrivateKey(String)
     * @see #getPrivateKey(byte[])
     */
    public static byte[] decryptData(
            String algorithm,
            byte[] data,
            PrivateKey privateKey,
            int keyLength
    ) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        return doTransformWork(algorithm, data, privateKey, Cipher.DECRYPT_MODE, keyLength);
    }

    /**
     * 使用私钥解密。
     *
     * @see #loadPrivateKey(String)
     * @see #getPrivateKey(byte[])
     */
    public static byte[] encryptData(
            String algorithm,
            byte[] data,
            PrivateKey privateKey,
            int keyLength
    ) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        return doTransformWork(algorithm, data, privateKey, Cipher.DECRYPT_MODE, keyLength);
    }

    /**
     * 使用公钥解密。
     *
     * @see #loadPublicKey(String)
     * @see #getPublicKey(byte[])
     */
    public static byte[] decryptData(
            String algorithm,
            byte[] data,
            PublicKey publicKey,
            int keyLength
    ) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        return doTransformWork(algorithm, data, publicKey, Cipher.DECRYPT_MODE, keyLength);
    }

    private static byte[] doTransformWork(
            String algorithm,
            byte[] data,
            Key key,
            int mode,
            int keyLength
    ) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        int roundLength = calculateMaxRoundLength(keyLength);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(mode, key);
            int inputLen = data.length;

            int offSet = 0;
            byte[] encryptedSegment;
            int roundCount = 0;

            while (inputLen - offSet > 0) {
                if (inputLen - offSet > roundLength) {
                    encryptedSegment = cipher.doFinal(data, offSet, roundLength);
                } else {
                    encryptedSegment = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(encryptedSegment, 0, encryptedSegment.length);
                roundCount++;
                offSet = roundCount * roundLength;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return encryptedData;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 生成新的私钥/密钥
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Generate a rsa key pair. the key length is 1024.
     */
    public static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        return generateRSAKeyPair(DEFAULT_KEY_LENGTH);
    }

    /**
     * Generate a rsa key pair.
     *
     * @param keyLength range：[512～2048], normally 1024.
     */
    public static KeyPair generateRSAKeyPair(int keyLength) throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(Algorithm.RSA);
        kpg.initialize(keyLength);
        return kpg.generateKeyPair();
    }

    /**
     * 随机生成 RSA 密钥对。这里需要提供一个新的 SecureRandom 实例用于初始化。在大多数情况下，这与使用 SDK 内部默认的 SecureRandom
     * 实例在安全性上是等效的，因为默认的 SecureRandom 通常已经足够安全。但是显式传入 SecureRandom可以确保每次生成密钥对时都使用新的随
     * 机性源，进一步提高安全性。
     *
     * @param keyLength 密钥长度，范围：512～2048，一般 1024。
     */
    public static KeyPair generateRSAKeyPair(int keyLength, @NonNull SecureRandom secureRandom) throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(Algorithm.RSA);
        kpg.initialize(keyLength, secureRandom);
        return kpg.generateKeyPair();
    }

    /**
     * Generate a rsa key pair.
     *
     * @param keyLength range：[512～2048], normally 1024.
     * @return the first of returned Pair is publicKey and the second is privateKey.
     * @see #generateRSAKeyPair(int)
     */
    public static Pair<String, String> generateRsaKeyPairInBase64(int keyLength) throws NoSuchAlgorithmException {
        KeyPair generatedKeyPair = generateRSAKeyPair(keyLength);
        String publicKey = EncodeUtils.base64Encode2String(generatedKeyPair.getPublic().getEncoded());
        String privateKey = EncodeUtils.base64Encode2String(generatedKeyPair.getPrivate().getEncoded());
        return new Pair<>(publicKey, privateKey);
    }

    /**
     * Generate a rsa key pair.
     *
     * @param keyLength range：[512～2048], normally 1024.
     * @return the first of returned Pair is publicKey and the second is privateKey.
     * @see #generateRSAKeyPair(int, SecureRandom)
     */
    public static Pair<String, String> generateRsaKeyPairInBase64(
            int keyLength,
            @NonNull SecureRandom secureRandom
    ) throws NoSuchAlgorithmException {
        KeyPair generatedKeyPair = generateRSAKeyPair(keyLength, secureRandom);
        String publicKey = EncodeUtils.base64Encode2String(generatedKeyPair.getPublic().getEncoded());
        String privateKey = EncodeUtils.base64Encode2String(generatedKeyPair.getPrivate().getEncoded());
        return new Pair<>(publicKey, privateKey);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 根据 key 创建 PublicKey 或 PrivateKey
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 通过公钥 byte[] 将公钥还原，适用于 RSA 算法。
     */
    public static PublicKey getPublicKey(@NonNull byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(Algorithm.RSA);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 通过私钥 byte[] 将私钥还原，适用于 RSA 算法。
     */
    public static PrivateKey getPrivateKey(@NonNull byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(Algorithm.RSA);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 使用 N、e 值还原公钥。
     */
    public static PublicKey getPublicKey(
            @NonNull String modulus,
            @NonNull String publicExponent
    ) throws InvalidKeySpecException, NoSuchAlgorithmException {
        BigInteger bigIntModulus = new BigInteger(modulus);
        BigInteger bigIntPrivateExponent = new BigInteger(publicExponent);
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntPrivateExponent);
        KeyFactory keyFactory = KeyFactory.getInstance(Algorithm.RSA);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 使用 N、d 值还原私钥。
     */
    public static PrivateKey getPrivateKey(
            @NonNull String modulus,
            @NonNull String privateExponent
    ) throws NoSuchAlgorithmException, InvalidKeySpecException {
        BigInteger bigIntModulus = new BigInteger(modulus);
        BigInteger bigIntPrivateExponent = new BigInteger(privateExponent);
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntPrivateExponent);
        KeyFactory keyFactory = KeyFactory.getInstance(Algorithm.RSA);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 从字符串中加载公钥。
     */
    public static PublicKey loadPublicKey(@NonNull String keyInBase64) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] buffer = Base64.decode(keyInBase64, Base64.DEFAULT);
        KeyFactory keyFactory = KeyFactory.getInstance(Algorithm.RSA);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 从字符串中加载私钥。
     *
     * @param keyInBase64 Base64 编码的私钥数据字符串
     */
    public static PrivateKey loadPrivateKey(@NonNull String keyInBase64) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] buffer = Base64.decode(keyInBase64, Base64.DEFAULT);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
        KeyFactory keyFactory = KeyFactory.getInstance(Algorithm.RSA);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 从文件中加载公钥。
     */
    public static PublicKey loadPublicKey(
            @NonNull InputStream in,
            boolean autoClose
    ) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return loadPublicKey(readKey(in, autoClose));
    }

    /**
     * 从文件中加载私钥。
     */
    public static PrivateKey loadPrivateKey(
            @NonNull InputStream in,
            boolean autoClose
    ) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return loadPrivateKey(readKey(in, autoClose));
    }

    /**
     * 读取密钥信息。
     *
     * <pre>
     * --------------------
     * CONTENT
     * --------------------
     * </pre>
     */
    private static String readKey(InputStream in, boolean autoClose) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(in));
            String readLine;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') {
                    continue;
                }
                sb.append(readLine);
                sb.append('\r');
            }
            return sb.toString();
        } catch (IOException e) {
            Timber.e(e, "readKey");
            return "";
        } finally {
            if (autoClose && br != null) {
                try {
                    br.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 签名与校验
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 用私钥签名。
     */
    public static byte[] signData(
            @NonNull String signatureAlgorithm,
            @NonNull byte[] data,
            @NonNull PrivateKey privateKey
    ) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(signatureAlgorithm);
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    /**
     * 用私钥签名。
     *
     * @param data data in bytes.
     */
    public static String signDataToBase64(
            @NonNull String signatureAlgorithm,
            @NonNull String data,
            @NonNull PrivateKey privateKey
    ) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        byte[] bytes = signData(signatureAlgorithm, data.getBytes(), privateKey);
        if (bytes == null) {
            return "";
        }
        return EncodeUtils.base64Encode2String(bytes);
    }

    /**
     * 用公钥验证签名。
     */
    public static boolean verifySign(
            @NonNull String signatureAlgorithm,
            @NonNull byte[] data, @NonNull byte[] sign,
            @NonNull PublicKey publicKey
    ) {
        try {
            Signature signature = Signature.getInstance(signatureAlgorithm);
            signature.initVerify(publicKey);
            signature.update(data);
            return signature.verify(sign);
        } catch (Exception e) {
            Timber.e(e, "verifySign:");
            return false;
        }
    }

}