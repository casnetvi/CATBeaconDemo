package com.casnetvi.catbeacondemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;

/**
 * Created by wzx on 17/3/10.
 */

public class BleEngineV3 {
    public interface Callback {
        void onDevice(BDevice device);
    }


    private Context mContext;
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private Callback mCallback;


    /**
     * 设备扫描回调
     */
    private final BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    BDevice bDevice = generate(device.getName(), device.getAddress(), rssi, scanRecord);
                    if (bDevice != null && mCallback != null) {
                        mCallback.onDevice(bDevice);
                    }
                }
            };


    public BleEngineV3(Context context) {
        mContext = context;
        initialize();
    }


    private void initialize() {
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                return;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            return;
        }
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public void scan() {
        mBluetoothAdapter.startLeScan(mLeScanCallback);
    }

    public void stopScan() {
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
    }


    private BDevice generate(String name, String address, int rssi, byte[] record) {
//        02 01 06 1A FF 59 00 02 15 E2 C5 66 5E C9 FA B5 DF A4 3A 3C F8 55 28 1D F8 27 12 14 58 C3 1C 16 4B 43 23 0A 00 C8 C3 06 6B 63 6C 6F 75 64 2E B8 DB DF 19 C1 39 F7 3C 3F 42 45 48 00 00 00
        if (record.length < 9) {
            return null;
        }

        if (record[0] == 0x02
                && record[1] == 0x01
//                && record[2] == 0x06
                && record[3] == 0x1A
                && record[4] == (byte) 0xFF
                && record[5] == 0x59
                && record[6] == 0x00
                && record[7] == 0x02
                && record[8] == 0x15
        ) {

            int index = -1;
            for (int i = 0; i < record.length - 1; i++) {
                if (record[i] == 0x4B && record[i + 1] == 0x43) {
                    index = i + 2;
                    break;
                }
            }

            if (index > -1 && index + 14 < record.length) {
                BDevice device = new BDevice(name, address, rssi, record);
                iBeaconData iBeacon = iBeaconData.generateiBeacon(record);
                String beaconUuid = iBeacon.beaconUuid.replace("-", "");
                int major = iBeacon.major;
                int minor = iBeacon.minor;
                device.setBeaconUuid(beaconUuid);
                device.setMajor(major);
                device.setMinor(minor);

//        System.out.println(ByteUtils.byteArrayToString(record));
//        System.out.println("index : " + index);
//
//        //电源 加密
//        System.out.println(ByteUtils.byteArrayToString(new byte[]{record[index], record[index + 1]}));
//
//        //广播间隔 1000
//        System.out.println(ByteUtils.byteArrayToString(new byte[]{record[index + 2], record[index + 3]}));
//
//        //测试功率 61
//        System.out.println(ByteUtils.byteArrayToString(new byte[]{record[index + 4]}));
//
//        //传输功率 1-8
//        System.out.println(ByteUtils.byteArrayToString(new byte[]{record[index + 5]}));
//
//        //名称  [6,7,8,9,10,11]
//        System.out.println(ByteUtils.byteArrayToString(new byte[]{record[index + 6], record[index + 7], record[index + 8], record[index + 9], record[index + 10], record[index + 11]}));
//
//        //电池电量
//        System.out.println(ByteUtils.byteArrayToString(new byte[]{record[index + 12], record[index + 13]}));
//
//        //mac地址  [14,15,16,17,18,19]
//        System.out.println(byteArrayToString(new byte[]{record[index + 14],record[index + 15],record[index + 16],record[index + 17],record[index + 18],record[index + 19]});

                int broadCastInterval = record[index + 2] * 256 + (record[index + 3] & 0xFF);

                int testPower = record[index + 4];
                int transPower = record[index + 5];
                String bleName = new String(new byte[]{record[index + 6], record[index + 7], record[index + 8], record[index + 9], record[index + 10], record[index + 11]});
                int battery = (record[index + 12] * 256 + (record[index + 13] & 0xFF)) - (record[index] * 256 + (record[index + 1] & 0xFF));
                String mac = byteArrayToString(new byte[]{record[index + 14], record[index + 15], record[index + 16], record[index + 17], record[index + 18], record[index + 19]});

//        System.out.println("beaconUuid : " + beaconUuid);
//        System.out.println("major : " + major);
//        System.out.println("minor : " + minor);
//        System.out.println("broadCastInterval : " + broadCastInterval);
//        System.out.println("testPower : " + testPower);
//        System.out.println("transPower : " + transPower);
//        System.out.println("bleName : " + bleName);
//        System.out.println("battery : " + battery);
//        System.out.println(mac);

                device.setBroadCastInterval(broadCastInterval);
                device.setTestPower(-Math.abs(testPower));
                device.setTransPower(transPower);
                device.setBleName(bleName);
                device.setBattery(battery);
                device.setAddress(mac);

                return device;
            }
        }
        return null;
    }


    private static String byteArrayToString(byte[] buffer) {
        if (buffer == null) {
            return "";
        }
        final StringBuilder stringBuilder = new StringBuilder(buffer.length);
        if (buffer.length > 0) {
            for (int i = 0; i < buffer.length; i++) {
                if (i == 0) {
                    stringBuilder.append(String.format("%02X", buffer[i]));
                } else {
                    stringBuilder.append(String.format(":%02X", buffer[i]));
                }
            }
        }
        return stringBuilder.toString();
    }
}
