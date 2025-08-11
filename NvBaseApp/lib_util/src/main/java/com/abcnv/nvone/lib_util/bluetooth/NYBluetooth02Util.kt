package com.abcly.swolf.lib_util.bluetooth

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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

 * @DATE 2023/4/20 15:18
 */
class NYBluetooth02Util {
    var map: Map<String, BluetoothDevice> = HashMap()
    var mBluetoothGatt: BluetoothGatt? = null

    private var bluetoothAdapter: BluetoothAdapter? = null

    private var activity: Activity? = null

    fun NYBluetooth02Util(activity: Activity?) {
        this.activity = activity
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        bluetoothIsNull()
        registerReceiver()
    }

    private fun registerReceiver() {
        // 注册Receiver来获取蓝牙设备相关的结果 将action指定为：ACTION_FOUND
        val intent = IntentFilter()
        intent.addAction(BluetoothDevice.ACTION_FOUND) // 用BroadcastReceiver来取得搜索结果
        intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED) // bluetooth不可用
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED) //
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        // 注册广播接收器
        activity!!.registerReceiver(searchDevices, intent)
    }

    /**
     * 广播
     */
    private val searchDevices: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            var device: BluetoothDevice? = null
            // 搜索远程蓝牙设备时，取得设备的MAC地址
            if (BluetoothDevice.ACTION_FOUND == action) {
                // 代表远程蓝牙适配器的对象取出
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                NYBluetoothData.map[device!!.address] =
                    device
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
//                Log.i("NYBluetooth02Util","bluetooth不可用");
                NYBluetoothData.map.clear()
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED == action) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                if (ActivityCompat.checkSelfPermission(
                        activity!!,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    when (device!!.bondState) {
                        BluetoothDevice.BOND_BONDING -> {}
                        BluetoothDevice.BOND_BONDED -> {}
                        BluetoothDevice.BOND_NONE -> {}
                        else -> {}
                    }
                }
            }
        }
    }

    /**
     * 判断是否有蓝牙设备
     */
    fun bluetoothIsNull(): Boolean {
        // 判断是否有Bluetooth设备
        if (bluetoothAdapter == null) {
            Toast.makeText(activity, "设备没有检测到蓝牙设备", Toast.LENGTH_SHORT).show()
            return true
        }
        if (bluetoothAdapter != null && !bluetoothAdapter!!.isEnabled) {
            // 1:打开bluetoothAdapter设备 这个无提示效果
            if (ActivityCompat.checkSelfPermission(
                    activity!!,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                bluetoothAdapter!!.enable()
            }

            // 2:也可以这样,这个有提示效果
            // Intent intent = new
            // Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            // activity.startActivityForResult(intent, activity.RESULT_OK);
        }
        return false
    }

    /**
     * 搜索蓝牙设备
     */
    fun discoveryDevice() {
        if (bluetoothIsNull()) {
            return
        }
        // 扫描蓝牙设备 最少要12秒，功耗也非常大（电池等） 是异步扫描意思就是一调用就会扫描
        if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            bluetoothAdapter!!.startDiscovery()
        }
    }

    /**
     * 销毁
     */
    fun destroy() {
        if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (bluetoothAdapter != null && bluetoothAdapter!!.isDiscovering) {
                bluetoothAdapter!!.cancelDiscovery()
            }
            activity!!.unregisterReceiver(searchDevices)
        }
    }

    /**
     * 销毁
     */
    fun connectBluetooth(address: String, bluetoothGattCallback: NYBluetoothGattCallback?) {
        val device = map[address]
        if (device != null) {
            if (ActivityCompat.checkSelfPermission(
                    activity!!,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                mBluetoothGatt = device.connectGatt(activity, true, bluetoothGattCallback)
            }
        } else {
            Toast.makeText(activity, "蓝牙地址没有找到对应的设备", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * 发送数据到蓝牙
     *
     * @param bluetoothGatt
     * @param characteristicUUID
     * @return
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
                        activity!!,
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
                    activity!!,
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
                    activity!!,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                mBluetoothGatt!!.close()
            }
        }
    }
}