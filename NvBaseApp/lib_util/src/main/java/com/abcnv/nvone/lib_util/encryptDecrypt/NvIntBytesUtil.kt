package com.abcnv.nvone.lib_util.encryptDecrypt

object NvIntBytesUtil {

    object HLIntBytesUtil2 {
        /**
         * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。
         */
        fun intToBytes(value: Int): ByteArray {
            val src = ByteArray(2)
            src[0] = (value shr 8 and 0xFF).toByte()
            src[1] = (value and 0xFF).toByte()
            return src
        }

        /**
         * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。
         */
        fun bytesToInt(src: ByteArray): Int {
            return (src[0]+0 and 0xFF shl 8 or (src[1]+0 and 0xFF)).toInt()
        }
    }

    object LHIntBytesUtil2 {
        /**
         * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。
         */
        fun intToBytes(value: Int): ByteArray {
            val src = ByteArray(2)
            src[1] = (value shr 8 and 0xFF).toByte()
            src[0] = (value and 0xFF).toByte()
            return src
        }

        /**
         * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序。
         */
        fun bytesToInt(src: ByteArray): Int {
            return (src[0]+0 and 0xFF or (src[1]+0 and 0xFF shl 8)).toInt()
        }
    }

    object HLIntBytesUtil4 {
        /**
         * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。
         */
        fun intToBytes(value: Int): ByteArray {
            val src = ByteArray(4)
            src[0] = (value shr 24 and 0xFF).toByte()
            src[1] = (value shr 16 and 0xFF).toByte()
            src[2] = (value shr 8 and 0xFF).toByte()
            src[3] = (value and 0xFF).toByte()
            return src
        }

        /**
         * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。
         */
        fun bytesToInt(src: ByteArray): Int {
            val value: Int
            value = (src[0]+0 and 0xFF shl 24
                    or (src[1]+0 and 0xFF shl 16)
                    or (src[2]+0 and 0xFF shl 8)
                    or (src[3]+0 and 0xFF)).toInt()
            return value
        }
    }

    object LHIntBytesUtil4 {
        /**
         * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。
         */
        fun intToBytes(value: Int): ByteArray {
            val src = ByteArray(4)
            src[3] = (value shr 24 and 0xFF).toByte()
            src[2] = (value shr 16 and 0xFF).toByte()
            src[1] = (value shr 8 and 0xFF).toByte()
            src[0] = (value and 0xFF).toByte()
            return src
        }

        /**
         * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序。
         */
        fun bytesToInt(src: ByteArray): Int {
            val value: Int
            value = (src[0]+0 and 0xFF
                    or (src[1]+0 and 0xFF shl 8)
                    or (src[2]+0 and 0xFF shl 16)
                    or (src[3]+0 and 0xFF shl 24)).toInt()
            return value
        }
    }

    object HexBytesUtil {

        /**
         * 字节转十六进制
         *
         * @param b 需要进行转换的byte字节
         * @return 转换后的Hex字符串
         */
        fun byteToHex(b: Byte): String {
            var hex = Integer.toHexString(b+0 and 0xFF)
            if (hex.length < 2) {
                hex = "0$hex"
            }
            return hex
        }

        /**
         * 字节数组转16进制
         *
         * @param bytes 需要转换的byte数组
         * @return 转换后的Hex字符串
         */
        fun bytesToHex(bytes: ByteArray): String {
            val sb = StringBuffer()
            for (i in bytes.indices) {
                val hex = Integer.toHexString(bytes[i]+0 and 0xFF)
                if (hex.length < 2) {
                    sb.append(0)
                }
                sb.append(hex)
            }
            return sb.toString()
        }

        /**
         * Hex字符串转byte
         *
         * @param inHex 待转换的Hex字符串
         * @return 转换后的byte
         */
        fun hexToByte(inHex: String): Byte {
            return Integer.parseInt(inHex, 16).toByte()
        }

        /**
         * hex字符串转byte数组
         *
         * @param inHex 待转换的Hex字符串
         * @return 转换后的byte数组结果
         */
        fun hexToByteArray(inHex: String): ByteArray {
            var inHex = inHex
            var hexlen = inHex.length
            val result: ByteArray
            if (hexlen % 2 == 1) {
                //奇数
                hexlen++
                result = ByteArray(hexlen / 2)
                inHex = "0$inHex"
            } else {
                //偶数
                result = ByteArray(hexlen / 2)
            }
            var j = 0
            var i = 0
            while (i < hexlen) {
                result[j] = hexToByte(inHex.substring(i, i + 2))
                j++
                i += 2
            }
            return result
        }
    }
}
