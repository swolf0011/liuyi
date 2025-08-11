package com.abcnv.nvone.lib_util.encryptDecrypt

import java.security.*
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.HashMap
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException

object NvRSA {
    val KEY_ALGORITHM = "RSA"
    /**
     * 貌似默认是RSA/NONE/PKCS1Padding，未验证
     */
    val CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding"
    val PUBLIC_KEY = "publicKey"
    val PRIVATE_KEY = "privateKey"

    /**
     * RSA密钥长度必须是64的倍数，在512~65536之间。默认是1024
     */
    val KEY_SIZE = 2048

    val PLAIN_TEXT = "MANUTD is the greatest club in the world看的"



    /**
     * 生成密钥对。注意这里是生成密钥对KeyPair，再由密钥对获取公私钥
     *
     * @return
     */
    fun generateKeyBytes(): Map<String, ByteArray>? {
        try {
            val keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM)
            keyPairGenerator.initialize(KEY_SIZE)
            val keyPair = keyPairGenerator.generateKeyPair()
            val publicKey = keyPair.public as RSAPublicKey
            val privateKey = keyPair.private as RSAPrivateKey
            val keyMap = HashMap<String, ByteArray>()
            keyMap[PUBLIC_KEY] = publicKey.encoded
            keyMap[PRIVATE_KEY] = privateKey.encoded
            return keyMap
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 还原公钥，X509EncodedKeySpec 用于构建公钥的规范
     *
     * @param keyBytes
     * @return
     */
    fun restorePublicKey(keyBytes: ByteArray): PublicKey? {
        val x509EncodedKeySpec = X509EncodedKeySpec(keyBytes)
        try {
            val factory = KeyFactory.getInstance(KEY_ALGORITHM)
            return factory.generatePublic(x509EncodedKeySpec)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidKeySpecException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 还原私钥，PKCS8EncodedKeySpec 用于构建私钥的规范
     *
     * @param keyBytes
     * @return
     */
    fun restorePrivateKey(keyBytes: ByteArray): PrivateKey? {
        val pkcs8EncodedKeySpec = PKCS8EncodedKeySpec(keyBytes)
        try {
            val factory = KeyFactory.getInstance(KEY_ALGORITHM)
            return factory.generatePrivate(pkcs8EncodedKeySpec)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidKeySpecException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 加密，三步走。
     *
     * @param key
     * @param plainText
     * @return
     */
    fun RSAEncode(key: PublicKey?, plainText: ByteArray): ByteArray? {
        try {
            val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
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
     * 解密，三步走。
     *
     * @param key
     * @param encodedText
     * @return
     */
    fun RSADecode(key: PrivateKey?, encodedText: ByteArray?): String? {
        try {
            val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, key)
            return String(cipher.doFinal(encodedText))
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

}
