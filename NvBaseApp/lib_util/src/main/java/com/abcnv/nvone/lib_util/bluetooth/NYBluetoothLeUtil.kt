package com.abcly.swolf.lib_util.bluetooth

import android.Manifest
import android.app.Activity
import android.bluetooth.*
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.util.*

/**
 * <uses-permission android:name="android.permission.BLUETOOTH_SCAN" />
 * <uses-permission android:name="android.permission.BLUETOOTH" />
 * <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
 * <!--如果你想让你的应用程序只在低功耗蓝牙手机上工作，那你可以如下设置：required = true-->
 * <uses-feature android:name="android.hardware.bluetooth_le" android:required="false"/>
 * 蓝牙4.0工具
 * @Description: TODO
 *
 * @Use:{非必须}
 *
 * @property TODO
 *
 * @Author liuyi

 * @DATE 2023/4/20 15:17
 */
class NYBluetoothLeUtil {
    var mBluetoothAdapter: BluetoothAdapter? = null
    var bluetoothLeScanner: BluetoothLeScanner? = null
    var mBluetoothGatt: BluetoothGatt? = null
    var activity: Activity? = null

    fun NYBluetoothLeUtil(activity: Activity) {
        this.activity = activity
        if (!activity.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(activity, "设备不支持蓝牙4.0", Toast.LENGTH_SHORT).show()
            return
        }
        val bluetoothManager =
            activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = bluetoothManager.adapter
        if (mBluetoothAdapter == null) {
            Toast.makeText(activity, "设备没有检测到蓝牙设备,不支持蓝牙", Toast.LENGTH_SHORT).show()
            return
        }
        if (!mBluetoothAdapter!!.isEnabled) {
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                mBluetoothAdapter!!.enable()
                bluetoothLeScanner = mBluetoothAdapter!!.bluetoothLeScanner
                return
            }
        }
    }

    fun startScanLeDevice() {
        NYBluetoothData.map.clear()
        val handler = Handler()
        handler.postDelayed({ stopScanLeDevice() }, 30000) //在这里可以自己进行时间的设置，比如搜索10秒
        if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            bluetoothLeScanner!!.startScan(scanCallback) //开始搜索
            return
        }
    }

    fun stopScanLeDevice() {
        if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            bluetoothLeScanner!!.stopScan(scanCallback)
            return
        }
    }

    private val scanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            NYBluetoothData.map[result.device.address] =
                result.device
        }
    }

    fun connect(address: String?, bluetoothGattCallback: BluetoothGattCallback?): Boolean {
        if (mBluetoothAdapter == null || address == null) {
            return false
        }
        val device = mBluetoothAdapter!!.getRemoteDevice(address) ?: return false
        //这里才是真正连接
        if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            mBluetoothGatt = device.connectGatt(activity, false, bluetoothGattCallback)
            return mBluetoothGatt != null
        }
        return false
    }


    fun disconnect() {
        if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (mBluetoothGatt != null) {
                mBluetoothGatt!!.disconnect()
                mBluetoothGatt!!.close()
                mBluetoothGatt = null
            }
            if (mBluetoothAdapter != null && mBluetoothAdapter!!.isDiscovering) {
                mBluetoothAdapter!!.cancelDiscovery()
                mBluetoothAdapter = null
            }
            return
        }
    }

    /**
     * 发送数据到蓝牙 ,可写的UUID
     */
    fun wirteData(characteristicUUID: UUID, bytes: ByteArray?): Boolean {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            return false
        }
        for (c in NYBluetoothData.characteristicList) {
            if (c.uuid === characteristicUUID) {
                c.value = bytes
                if (ActivityCompat.checkSelfPermission(
                        activity!!,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return mBluetoothGatt!!.writeCharacteristic(c)
                }
            }
        }
        return false
    }

    /**
     * 可读蓝牙数据,可读的UUID
     */
    fun readCharacteristic(characteristicUUID: UUID) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            return
        }
        var c1: BluetoothGattCharacteristic? = null
        for (c in NYBluetoothData.characteristicList) {
            if (c.uuid === characteristicUUID) {
                c1 = c
                break
            }
        }
        if (c1 != null) {
            if (ActivityCompat.checkSelfPermission(
                    activity!!,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                mBluetoothGatt!!.readCharacteristic(c1)
                return
            }
        }
    }

}