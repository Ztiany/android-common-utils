package com.android.base.utils.security

interface Password

/** 常规密码 */
class GeneralPassword(
    /** 密钥密码：在使用 AES 加密算法时，密钥的长度通常应该符合 AES 要求的长度。对于 AES，密钥的长度可以是 128 比特（16 字节）、192 比特（24 字节）或 256 比特（32 字节）。 */
    val password: String,
) : Password

/** 用于 PBKDF2 算法的密码 */
class PBKDF2Password(
    /** 密钥密码：在使用 AES 加密算法时，密钥的长度通常应该符合 AES 要求的长度。对于 AES，密钥的长度可以是 128 比特（16 字节）、192 比特（24 字节）或 256 比特（32 字节）。 */
    val password: String,
    /** 盐值 */
    val salt: ByteArray,
    /** 迭代次数，迭代次数越多，生成的密钥越安全，但会增加计算成本。一般来说，推荐的迭代次数为数千或上万次，以确保生成的密钥足够安全。但实际选择应根据具体情况进行评估，可能需要进行一些性能测试来确定合适的迭代次数。 */
    val iterationCount: Int,
    /** 生成的密钥长度（以位为单位），常见的密钥长度包括 128 位、192 位和 256 位，即分别对应 16 字节、24 字节和 32 字节。通常情况下，越长的密钥长度提供的安全性越高，但也可能会降低性能。选择密钥长度时，应根据安全需求和性能要求进行权衡。一般来说，推荐使用 128 位或更长的密钥长度以确保足够的安全性。 */
    val keyLength: Int,
) : Password
