package com.abcnv.nvone.lib_util.encryptDecrypt

import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


/**
 * AES加密解密工具
 * Created by LiuYi
 */
object NvAES {
    /**
     * 注意key和加密用到的字符串是不一样的 加密还要指定填充的加密模式和填充模式 AES密钥可以是128或者256，加密模式包括ECB, CBC等
     * ECB模式是分组的模式，CBC是分块加密后，每块与前一块的加密结果异或后再加密 第一块加密的明文是与IV变量进行异或
     */
    val KEY_ALGORITHM = "AES"
    val ECB_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding"
    val CBC_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding"
    val PLAIN_TEXT = "MANUTD is the greatest club in the world"

    /**
     * IV(Initialization Value)是一个初始值，对于CBC模式来说，它必须是随机选取并且需要保密的
     * 而且它的长度和密码分组相同(比如：对于AES 128为128位，即长度为16的byte类型数组)
     *
     */
    val IVPARAMETERS = byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)

    @JvmStatic
    fun main(arg: Array<String>) {
        val secretBytes = generateAESSecretKey()
        val key = restoreSecretKey(secretBytes)
        var encodedText = AesEcbEncode(PLAIN_TEXT.toByteArray(), key)
        println("AES ECB decoded: " + AesEcbDecode(encodedText, key)!!)
        encodedText = AesCbcEncode(PLAIN_TEXT.toByteArray(), key, IVPARAMETERS)
        println("AES CBC decoded: " + AesCbcDecode(encodedText, key, IVPARAMETERS)!!)
    }

    /**
     * 使用ECB模式进行加密。 加密过程三步走： 1. 传入算法，实例化一个加解密器 2. 传入加密模式和密钥，初始化一个加密器 3.
     * 调用doFinal方法加密
     *
     * @param plainText
     * @return
     */
    fun AesEcbEncode(plainText: ByteArray, key: SecretKey): ByteArray? {
        try {
            val cipher = Cipher.getInstance(ECB_CIPHER_ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, key)
            return cipher.doFinal(plainText)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 使用ECB解密，三步走，不说了
     *
     * @param decodedText
     * @param key
     * @return
     */
    fun AesEcbDecode(decodedText: ByteArray?, key: SecretKey): String? {
        try {
            val cipher = Cipher.getInstance(ECB_CIPHER_ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, key)
            return String(cipher.doFinal(decodedText))
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * CBC加密，三步走，只是在初始化时加了一个初始变量
     *
     * @param plainText
     * @param key
     * @param IVParameter
     * @return
     */
    fun AesCbcEncode(plainText: ByteArray, key: SecretKey, IVParameter: ByteArray): ByteArray? {
        try {
            val ivParameterSpec = IvParameterSpec(IVParameter)
            val cipher = Cipher.getInstance(CBC_CIPHER_ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec)
            return cipher.doFinal(plainText)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * CBC 解密
     *
     * @param decodedText
     * @param key
     * @param IVParameter
     * @return
     */
    fun AesCbcDecode(decodedText: ByteArray?, key: SecretKey, IVParameter: ByteArray): String? {
        val ivParameterSpec = IvParameterSpec(IVParameter)
        try {
            val cipher = Cipher.getInstance(CBC_CIPHER_ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec)
            return String(cipher.doFinal(decodedText))
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        }

        return null

    }

    /**
     * 1.创建一个KeyGenerator 2.调用KeyGenerator.generateKey方法
     * 由于某些原因，这里只能是128，如果设置为256会报异常，原因在下面文字说明
     *
     * @return
     */
    fun generateAESSecretKey(): ByteArray? {
        val keyGenerator: KeyGenerator
        try {
            keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM)
            return keyGenerator.generateKey().encoded
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 还原密钥
     *
     * @param secretBytes
     * @return
     */
    fun restoreSecretKey(secretBytes: ByteArray?): SecretKey {
        return SecretKeySpec(secretBytes, KEY_ALGORITHM)
    }
}
