package com.abcly.swolf.lib_util.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic

object NYBluetoothData {

    val characteristicList = mutableListOf<BluetoothGattCharacteristic>()
    val map = mutableMapOf<String, BluetoothDevice>()
}