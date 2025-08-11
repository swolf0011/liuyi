package com.abcnv.nvone.lib_util.encryptDecrypt

import java.io.IOException
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.crypto.*
import javax.crypto.spec.DESKeySpec
import javax.crypto.spec.SecretKeySpec

object NvEncryptDecryptUtil {

    /**
     * 信息摘要算法 MD5 (非对称): 让大容量信息在用数字签名软件签署私人密匙前被"压缩"成一种保密的格式。
     * 散列算法 SHA (非对称):散列算法，散列是信息的提炼，通常其长度要比信息小得多，且为一个固定长度。
     * 数据编码BASE64:采用Base64编码不仅比较简短，同时也具有不可读性，即所编码的数据不会被人用肉眼所直接看到。
     * 数据加密算法 DES (对称):DES是21世纪的加密标准，现新一代加密标准是AES。
     */
    /************* Base64 *****************/
    fun BASE64_encrypt2Bytes(data: ByteArray?): ByteArray? {
        return if (data == null) {
            null
        } else NvBase64Util.encode(data).toByteArray()
    }

    fun BASE64_encrypt2Bytes(data: String?): ByteArray? {
        return if (data == null) {
            null
        } else NvBase64Util.encode(data.toByteArray()).toByteArray()
    }

    fun BASE64_encrypt2String(data: ByteArray?): String? {
        return if (data == null) {
            null
        } else NvBase64Util.encode(data)
    }

    fun BASE64_encrypt2String(data: String?): String? {
        return if (data == null) {
            null
        } else NvBase64Util.encode(data.toByteArray())
    }

    fun BASE64_decrypt2Bytes(data: String?): ByteArray? {
        return if (data == null) {
            null
        } else NvBase64Util.decode(data)
    }

    fun BASE64_decrypt2String(data: String?): String? {
        return if (data == null) {
            null
        } else String(NvBase64Util.decode(data)!!)
    }

    /************* MD5 *****************/
    fun MD5_encrypt2Bytes(data: ByteArray?, md5_key: String): ByteArray? {
        if (data == null) {
            return null
        }
        try {
            val md5 = MessageDigest.getInstance(md5_key)
            md5.update(data)
            return md5.digest()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return null
    }

    fun MD5_encrypt2Bytes(data: String?, md5_key: String): ByteArray? {
        return if (data == null) {
            null
        } else MD5_encrypt2Bytes(data.toByteArray(), md5_key)
    }

    fun MD5_encrypt2String(data: ByteArray?, md5_key: String): String? {
        return if (data == null) {
            null
        } else byte2hex(MD5_encrypt2Bytes(data, md5_key))
    }

    fun MD5_encrypt2String(data: String?, md5_key: String): String? {
        return if (data == null) {
            null
        } else byte2hex(MD5_encrypt2Bytes(data, md5_key))
    }

    fun MD5_encrypt2String(data: ByteArray?, md5_key: String, n: Int): String? {
        var data: ByteArray? = data ?: return null
        try {
            for (i in 0 until n) {
                data = MD5_encrypt2Bytes(data, md5_key)
            }
            return byte2hex(data)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /************* SHA *****************/
    fun SHA_encrypt2Bytes(data: ByteArray?, sha_key: String): ByteArray? {
        if (data == null) {
            return null
        }
        try {
            val sha = MessageDigest.getInstance(sha_key)
            sha.update(data)
            return sha.digest()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return null
    }

    fun SHA_encrypt2Bytes(data: String?, sha_key: String): ByteArray? {
        return if (data == null) {
            null
        } else SHA_encrypt2Bytes(data.toByteArray(), sha_key)
    }

    fun SHA_encrypt2String(data: ByteArray?, sha_key: String): String? {
        return if (data == null) {
            null
        } else byte2hex(SHA_encrypt2Bytes(data, sha_key))
    }

    fun SHA_encrypt2String(data: String?, sha_key: String): String? {
        return if (data == null) {
            null
        } else SHA_encrypt2String(data.toByteArray(), sha_key)
    }

    /************* DES *****************/
    fun DES_encrypt2Bytes(desKeySpec_key: ByteArray?, data: ByteArray?, secretKeyFactory_key: String?): ByteArray? {
        if (desKeySpec_key == null || data == null || secretKeyFactory_key == null) {
            return null
        }
        try {
            val deskey = DESKeySpec(desKeySpec_key)
            val keyfactory = SecretKeyFactory.getInstance(secretKeyFactory_key)
            val securekey = keyfactory.generateSecret(deskey)
            val cipher = Cipher.getInstance(secretKeyFactory_key)
            val random = SecureRandom()
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random)
            return cipher.doFinal(data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun DES_encrypt2Bytes(desKeySpec_key: String?, data: ByteArray?, secretKeyFactory_key: String?): ByteArray? {
        return if (desKeySpec_key == null || data == null || secretKeyFactory_key == null) {
            null
        } else DES_encrypt2Bytes(desKeySpec_key.toByteArray(), data, secretKeyFactory_key)
    }

    fun DES_encrypt2Bytes(desKeySpec_key: ByteArray?, data: String?, secretKeyFactory_key: String?): ByteArray? {
        return if (desKeySpec_key == null || data == null || secretKeyFactory_key == null) {
            null
        } else DES_encrypt2Bytes(desKeySpec_key, data.toByteArray(), secretKeyFactory_key)
    }

    fun DES_encrypt2Bytes(desKeySpec_key: String?, data: String?, secretKeyFactory_key: String?): ByteArray? {
        return if (desKeySpec_key == null || data == null || secretKeyFactory_key == null) {
            null
        } else DES_encrypt2Bytes(desKeySpec_key.toByteArray(), data.toByteArray(), secretKeyFactory_key)
    }

    fun DES_encrypt2String(desKeySpec_key: ByteArray?, data: ByteArray?, secretKeyFactory_key: String?): String? {
        return if (desKeySpec_key == null || data == null || secretKeyFactory_key == null) {
            null
        } else byte2hex(DES_encrypt2Bytes(desKeySpec_key, data, secretKeyFactory_key))
    }

    fun DES_encrypt2String(desKeySpec_key: String?, data: ByteArray?, secretKeyFactory_key: String?): String? {
        return if (desKeySpec_key == null || data == null || secretKeyFactory_key == null) {
            null
        } else byte2hex(DES_encrypt2Bytes(desKeySpec_key, data, secretKeyFactory_key))
    }

    fun DES_encrypt2String(desKeySpec_key: ByteArray?, data: String?, secretKeyFactory_key: String?): String? {
        return if (desKeySpec_key == null || data == null || secretKeyFactory_key == null) {
            null
        } else byte2hex(DES_encrypt2Bytes(desKeySpec_key, data, secretKeyFactory_key))
    }

    fun DES_encrypt2String(desKeySpec_key: String?, data: String?, secretKeyFactory_key: String?): String? {
        return if (desKeySpec_key == null || data == null || secretKeyFactory_key == null) {
            null
        } else byte2hex(DES_encrypt2Bytes(desKeySpec_key, data, secretKeyFactory_key))
    }

    fun DES_decrypt2Bytes(desKeySpec_key: ByteArray?, data: ByteArray?, secretKeyFactory_key: String?): ByteArray? {
        if (desKeySpec_key == null || data == null || secretKeyFactory_key == null) {
            return null
        }
        try {
            val deskey = DESKeySpec(desKeySpec_key)
            val keyfactory = SecretKeyFactory.getInstance(secretKeyFactory_key)
            val securekey = keyfactory.generateSecret(deskey)
            val cipher = Cipher.getInstance(secretKeyFactory_key)
            val random = SecureRandom()
            cipher.init(Cipher.DECRYPT_MODE, securekey, random)
            return cipher.doFinal(data)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun DES_decrypt2Bytes(desKeySpec_key: String?, data: ByteArray?, secretKeyFactory_key: String?): ByteArray? {
        return if (desKeySpec_key == null || data == null || secretKeyFactory_key == null) {
            null
        } else DES_decrypt2Bytes(desKeySpec_key.toByteArray(), data, secretKeyFactory_key)
    }

    fun DES_decrypt2Bytes(desKeySpec_key: ByteArray?, data: String?, secretKeyFactory_key: String?): ByteArray? {
        return if (desKeySpec_key == null || data == null || secretKeyFactory_key == null) {
            null
        } else DES_decrypt2Bytes(desKeySpec_key, hex2byte(data), secretKeyFactory_key)
    }

    fun DES_decrypt2Bytes(desKeySpec_key: String?, data: String?, secretKeyFactory_key: String?): ByteArray? {
        return if (desKeySpec_key == null || data == null || secretKeyFactory_key == null) {
            null
        } else DES_decrypt2Bytes(desKeySpec_key.toByteArray(), hex2byte(data), secretKeyFactory_key)
    }

    fun DES_decrypt2String(desKeySpec_key: ByteArray?, data: ByteArray?, secretKeyFactory_key: String?): String? {
        return if (desKeySpec_key == null || data == null || secretKeyFactory_key == null) {
            null
        } else byte2hex(DES_decrypt2Bytes(desKeySpec_key, data, secretKeyFactory_key))
    }

    fun DES_decrypt2String(desKeySpec_key: String?, data: ByteArray?, secretKeyFactory_key: String?): String? {
        return if (desKeySpec_key == null || data == null || secretKeyFactory_key == null) {
            null
        } else byte2hex(DES_decrypt2Bytes(desKeySpec_key, data, secretKeyFactory_key))
    }

    fun DES_decrypt2String(desKeySpec_key: ByteArray?, data: String?, secretKeyFactory_key: String?): String? {
        return if (desKeySpec_key == null || data == null || secretKeyFactory_key == null) {
            null
        } else byte2hex(DES_decrypt2Bytes(desKeySpec_key, data, secretKeyFactory_key))
    }

    fun DES_decrypt2String(desKeySpec_key: String?, data: String?, secretKeyFactory_key: String?): String? {
        return if (desKeySpec_key == null || data == null || secretKeyFactory_key == null) {
            null
        } else byte2hex(DES_decrypt2Bytes(desKeySpec_key, data, secretKeyFactory_key))
    }

    //
    fun AESEncode(encodeRules: String, content: String): String? {
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            val keygen = KeyGenerator.getInstance("AES")
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            keygen.init(128, SecureRandom(encodeRules.toByteArray()))
            //3.产生原始对称密钥
            val original_key = keygen.generateKey()
            //4.获得原始对称密钥的字节数组
            val raw = original_key.encoded
            //5.根据字节数组生成AES密钥
            val key = SecretKeySpec(raw, "AES")
            //6.根据指定算法AES自成密码器
            val cipher = Cipher.getInstance("AES")
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.ENCRYPT_MODE, key)
            //8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            val byte_encode = content.toByteArray(charset("utf-8"))
            //9.根据密码器的初始化方式--加密：将数据加密
            val byte_AES = cipher.doFinal(byte_encode)
            //11.将字符串返回
            return String(byte_AES)
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
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return null
    }

    fun AESDncode(encodeRules: String, content: String): String? {
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            val keygen = KeyGenerator.getInstance("AES")
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            keygen.init(128, SecureRandom(encodeRules.toByteArray()))
            //3.产生原始对称密钥
            val original_key = keygen.generateKey()
            //4.获得原始对称密钥的字节数组
            val raw = original_key.encoded
            //5.根据字节数组生成AES密钥
            val key = SecretKeySpec(raw, "AES")
            //6.根据指定算法AES自成密码器
            val cipher = Cipher.getInstance("AES")
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.DECRYPT_MODE, key)
            //8.将加密并编码后的内容解码成字节数组
            val byte_content = content.toByteArray()
            // 解密
            val byte_decode = cipher.doFinal(byte_content)
            return String(byte_decode, Charset.defaultCharset())
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        }
        //如果有错就返加nulll
        return null
    }

    fun byte2hex(data: ByteArray?): String? {
        if (data == null) {
            return null
        }
        val hs = StringBuilder()
        var stmp: String
        for (n in data.indices) {
            stmp = Integer.toHexString(data[n] + 0 and 0XFF)
            if (stmp.length == 1) {
                hs.append('0')
            }
            hs.append(stmp)
        }
        return hs.toString().toUpperCase()
    }

    fun hex2byte(hex: String?): ByteArray? {
        if (hex == null) {
            return null
        }
        val bs = hex.toByteArray()
        if (bs.size % 2 != 0) {
            return null
        }
        val bh = ByteArray(bs.size / 2)
        var n = 0
        while (n < bs.size) {
            bh[n / 2] = Integer.parseInt(String(bs, n, 2), 16).toByte()
            n += 2
        }
        return bh
    }

}