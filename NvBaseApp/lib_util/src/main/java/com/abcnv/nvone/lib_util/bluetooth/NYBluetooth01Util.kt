package com.abcly.swolf.lib_util.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.LeScanCallback
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
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

 * @DATE 2023/4/20 15:19
 */
class NYBluetooth01Util private constructor(var appContext: Context){
    var mBluetoothGatt: BluetoothGatt? = null
    companion object{
        private var bluetoothAdapter: BluetoothAdapter? = null
        private var bu: NYBluetooth01Util? = null
        /**
         * @param context
         * @return 设备不能使用蓝牙时返回null。
         */
        fun initBluetoothUtil(context: Context): NYBluetooth01Util? {
            if (bu == null) {
                val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
                bluetoothAdapter = bluetoothManager.adapter
                if (bluetoothAdapter == null) {
                    bu = null
                }
                bu = NYBluetooth01Util(context)
                if (bluetoothAdapter != null && !bluetoothAdapter!!.isEnabled) {
                    // 1:打开bluetoothAdapter设备 这个无提示效果
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        bluetoothAdapter!!.enable()
                        return bu
                    }
                    // 2:也可以这样,这个有提示效果
                    // Intent intent = new
                    // Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    // activity.startActivityForResult(intent, activity.RESULT_OK);
                }
            }
            return bu
        }
    }


    /**
     * 30秒中搜索蓝牙设备
     */
    fun scanLeDevice30000() {
        NYBluetoothData.map.clear()
        val handler = Handler()
        handler.postDelayed({
            if (ActivityCompat.checkSelfPermission(
                    appContext!!,
                    Manifest.permission.BLUETOOTH_SCAN
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                bluetoothAdapter!!.stopLeScan(leScanCallback)
            }
        }, 30000)
        bluetoothAdapter!!.startLeScan(leScanCallback)
    }

    private val leScanCallback =
        LeScanCallback { device, rssi, scanRecord -> NYBluetoothData.map[device.address] = device }

    fun connect(
        context: Context?,
        address: String?,
        bluetoothGattCallback: NYBluetoothGattCallback?
    ): Boolean {
        if (bluetoothAdapter == null || address == null) {
            return false
        }
        val device = bluetoothAdapter!!.getRemoteDevice(address) ?: return false
        if (ActivityCompat.checkSelfPermission(
                appContext!!,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            mBluetoothGatt = device.connectGatt(context, false, bluetoothGattCallback)
            return true
        }
        return true
    }

    /**
     * 发送数据到蓝牙
     */
    fun sendData(
        bluetoothGatt: BluetoothGatt?,
        characteristicUUID: UUID,
        bytes: ByteArray?
    ): Boolean {
        if (bluetoothGatt == null) {
            return false
        }
        if (bytes == null) {
            return false
        }
        for (c in NYBluetoothData.characteristicList) {
            if (c.uuid === characteristicUUID) {
                c.value = bytes
                if (ActivityCompat.checkSelfPermission(
                        appContext!!,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return bluetoothGatt.writeCharacteristic(c)
                }
            }
        }
        return false
    }

    /**
     * 关闭蓝牙设备
     */
    fun disconnect() {
        if (mBluetoothGatt != null) {
            if (ActivityCompat.checkSelfPermission(
                    appContext!!,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                mBluetoothGatt!!.disconnect()
            }
        }
    }

    fun close() {
        if (mBluetoothGatt != null) {
            if (ActivityCompat.checkSelfPermission(
                    appContext!!,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                mBluetoothGatt!!.close()
            }
        }
    }
}