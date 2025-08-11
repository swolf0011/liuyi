package com.abcly.swolf.lib_util.bluetooth

import android.bluetooth.BluetoothGatt
import android.content.Context
import android.widget.Toast
/**
 * @Description: TODO
 *
 * @Use:{非必须}
 *
 * @property TODO
 *
 * @Author liuyi

 * @DATE 2023/4/20 15:23
 */
class NYBluetooth01UtilTest {
    /**
     * 蓝牙连接
     */
    fun testConnentBluetooth(context: Context, bluetoothAddress: String) {
        val callback = object : NYBluetoothGattCallback.BluetoothDataCallback{
            override fun onReceiveData(
                bluetoothGatt: BluetoothGatt,
                qppUUIDForNotifyChar: String,
                qppData: ByteArray
            ) {
            }
        }
        val uuidQppService = "0000FFF0-0000-1000-8000-00805f9b34fb"
        val uuidQppCharWrite = "0000FFF3-0000-1000-8000-00805f9b34fb"
        val uuidQppDescriptor = "00002902-0000-1000-8000-00805f9b34fb"
        val bluetoothGattCallback = NYBluetoothGattCallback(
            context,
            callback,
            uuidQppService,
            uuidQppCharWrite,
            uuidQppDescriptor
        )
        val bu = NYBluetooth01Util.initBluetoothUtil(context)
        bu?.connect(context, bluetoothAddress, bluetoothGattCallback)
            ?: Toast.makeText(context, "蓝牙不可用", Toast.LENGTH_LONG).show()
    }
}