package com.abcly.swolf.lib_util.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.util.*

/**
 * @Description: TODO
 *
 * @Use:{非必须}
 *
 * @property TODO
 *
 * @Author liuyi

 * @DATE 2023/4/20 15:16
 */
class NYBluetoothGattCallback(
    var context: Context, var bluetoothCallback: BluetoothDataCallback,
    var uuidQppService: String = "0000FFF0-0000-1000-8000-00805f9b34fb",
    var uuidQppCharWrite: String = "0000FFF3-0000-1000-8000-00805f9b34fb",
    var uuidQppDescriptor: String = "00002902-0000-1000-8000-00805f9b34fb"
) : BluetoothGattCallback() {

    private var notifyEnabled = false

    interface BluetoothDataCallback {
        fun onReceiveData(
            bluetoothGatt: BluetoothGatt,
            qppUUIDForNotifyChar: String,
            qppData: ByteArray
        )
    }


    /**
     * //当连接上设备或者失去连接时会回调该函数
     *
     * @param gatt
     * @param status
     * @param newState
     */
    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Toast.makeText(context, "蓝牙连接成功", Toast.LENGTH_LONG).show()
                gatt.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Toast.makeText(context, "蓝牙连接失败", Toast.LENGTH_LONG).show()
                gatt.disconnect()
            }
        }
    }

    /**
     * //当设备是否找到服务时，会回调该函数
     *
     * @param gatt
     * @param status
     */
    @SuppressLint("WrongConstant")
    override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            Toast.makeText(context, "服务发现成功", Toast.LENGTH_LONG).show()
            qppEnable(gatt, uuidQppService, uuidQppCharWrite)
        } else {
        }
    }

    /**
     * //当读取设备中的数据时会回调该函数
     *
     * @param gatt
     * @param characteristic
     * @param status
     */
    override fun onCharacteristicRead(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ) {
        println("onCharacteristicRead")
        if (status == BluetoothGatt.GATT_SUCCESS) {
            //读取到的数据存在characteristic当中，可以通过characteristic.getValue();函数取出。然后再进行解析操作。
            val charaProp = characteristic.properties
            //表示可发出通知。  判断该Characteristic属性
            if (charaProp or BluetoothGattCharacteristic.PROPERTY_NOTIFY > 0) {
            } else {
                //获取到的值
                val bs = characteristic.value
                if (bluetoothCallback != null && bs != null && bs.size > 0) {
                    bluetoothCallback!!.onReceiveData(gatt, characteristic.uuid.toString(), bs)
                }
            }
        }
    }

    /**
     * //当向设备Descriptor中写数据时，会回调该函数
     *
     * @param gatt
     * @param descriptor
     * @param status
     */
    override fun onDescriptorWrite(
        gatt: BluetoothGatt?,
        descriptor: BluetoothGattDescriptor?,
        status: Int
    ) {
        notifyEnabled = true
    }

    /**
     * //当向Characteristic写数据时会回调该函数
     *
     * @param gatt
     * @param characteristic
     * @param status
     */
    override fun onCharacteristicWrite(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int
    ) {
    }

    /**
     * //设备发出通知时会调用到该接口
     *
     * @param gatt
     * @param characteristic
     */
    override fun onCharacteristicChanged(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?
    ) {
        updateValueForNotification(gatt, characteristic)
    }

    /**
     * 成功发现服务
     */
    private fun qppEnable(
        bluetoothGatt: BluetoothGatt?,
        qppServiceUUID: String,
        writeCharUUID: String
    ): Boolean {
        if (bluetoothGatt == null || qppServiceUUID.isEmpty() || writeCharUUID.isEmpty()) {
            return false
        }
        val qppService = bluetoothGatt.getService(UUID.fromString(qppServiceUUID))
            ?: return false
        NYBluetoothData.characteristicList.clear()
        NYBluetoothData.characteristicList.addAll(qppService.characteristics)
        val chara = qppService.getCharacteristic(UUID.fromString(writeCharUUID))
        return if (chara == null) {
            false
        } else {
            if (ActivityCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                bluetoothGatt.setCharacteristicNotification(chara, true)
                val descriptor = chara.getDescriptor(UUID.fromString(uuidQppDescriptor))
                descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                bluetoothGatt.writeDescriptor(descriptor)
                return true
            }
            false
        }
    }

    /**
     * 修改值关于通知
     */
    private fun updateValueForNotification(
        bluetoothGatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?
    ) {
        if (bluetoothGatt == null || characteristic == null) {
            return
        }
        if (!notifyEnabled) {
            return
        }
        val strUUIDForNotifyChar = characteristic.uuid.toString()
        val qppData = characteristic.value
        if (bluetoothCallback != null && qppData != null && qppData.size > 0) {
            bluetoothCallback!!.onReceiveData(bluetoothGatt, strUUIDForNotifyChar, qppData)
        }
    }
}