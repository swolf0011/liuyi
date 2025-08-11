package com.abcnv.nvone.lib_framework

import android.content.Context
import java.io.InputStream
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.*
/**
 * @Description:
 *
 *
 * @Use:{非必须}
 *
 * @property
 *
 * @Author liuyi
 */
object NvSSLUtil {
    // 生成SSLContext对象
    val notCerSSLContext: SSLContext?
        get() {
            var sslContext: SSLContext? = null
            try {
                sslContext = SSLContext.getInstance("TLS")
                sslContext!!.init(null, arrayOf<TrustManager>(object : X509TrustManager {
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate?> {
                        return arrayOfNulls(0)
                    }
                }), SecureRandom())
                return sslContext
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }

    fun getHavCerSSLContext(context: Context, assetsCerFileName: String): SSLContext? {
        // 生成SSLContext对象
        var sslContext: SSLContext? = null
        var inputStream: InputStream? = null
        try {
            sslContext = SSLContext.getInstance("TLS")
            // 从assets中加载证书
            inputStream = context.assets.open(assetsCerFileName)
            // 证书工厂
            val cerFactory = CertificateFactory.getInstance("X.509")
            val cer = cerFactory.generateCertificate(inputStream)
            // 密钥库
            val kStore = KeyStore.getInstance("PKCS12")
            kStore.load(null, null)
            kStore.setCertificateEntry("trust", cer)// 加载证书到密钥库中
            // 密钥管理器
            val keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
            keyFactory.init(kStore, null)// 加载密钥库到管理器
            // 信任管理器
            val tFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            tFactory.init(kStore)// 加载密钥库到信任管理器
            // 初始化
            sslContext!!.init(keyFactory.keyManagers, tFactory.trustManagers, SecureRandom())

            return sslContext
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                    inputStream = null
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
        return null
    }
}