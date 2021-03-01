//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.casnetvi.catbeacondemo;

import java.util.Arrays;

public class iBeaconData {
    public String macAddress = "";
    public String mac_status = "";
    public String beaconUuid = "00000000-0000-0000-0000-000000000000";
    public int major = 0;
    public int minor = 0;
    public byte oneMeterRssi = 0;
    public byte rssi = 0;
    public double batteryPower = 0.0D;
    static final byte[] iBeaconPostfix = new byte[]{26, -1, 76, 0, 2, 21};

    public iBeaconData() {
    }

    public static boolean isiBeaconData(byte[] scanRecord) {
        byte[] scanPostfixData = null;
        if (2 == scanRecord[0] && 1 == scanRecord[1]) {
            scanPostfixData = Arrays.copyOfRange(scanRecord, 3, 3 + iBeaconPostfix.length);
        } else {
            scanPostfixData = Arrays.copyOf(scanRecord, iBeaconPostfix.length);
        }

        return scanPostfixData != null && Arrays.equals(scanPostfixData, iBeaconPostfix);
    }

    public static iBeaconData generateiBeacon(byte[] scanRecord) {
//        if(!isiBeaconData(scanRecord)) {
//            return null;
//        } else {
        int uuidIndex = 9;
        int verIndex = 25;
        int rssiIndex = 29;
        int macIndex = 0;
        if (scanRecord[0] == 26) {
            uuidIndex = 6;
            verIndex = 22;
            rssiIndex = 26;
        }

        iBeaconData iData = new iBeaconData();
        byte[] uuidArr = Arrays.copyOfRange(scanRecord, uuidIndex, uuidIndex + 16);
        String strUuid = String.format("%02X%02X%02X%02X-%02X%02X-%02X%02X-%02X%02X-%02X%02X%02X%02X%02X%02X", new Object[]{Byte.valueOf(uuidArr[0]), Byte.valueOf(uuidArr[1]), Byte.valueOf(uuidArr[2]), Byte.valueOf(uuidArr[3]), Byte.valueOf(uuidArr[4]), Byte.valueOf(uuidArr[5]), Byte.valueOf(uuidArr[6]), Byte.valueOf(uuidArr[7]), Byte.valueOf(uuidArr[8]), Byte.valueOf(uuidArr[9]), Byte.valueOf(uuidArr[10]), Byte.valueOf(uuidArr[11]), Byte.valueOf(uuidArr[12]), Byte.valueOf(uuidArr[13]), Byte.valueOf(uuidArr[14]), Byte.valueOf(uuidArr[15])});
        iData.beaconUuid = strUuid;
        iData.major = (scanRecord[verIndex] << 8 & '\uff00') + (scanRecord[verIndex + 1] & 255);
        iData.minor = (scanRecord[verIndex + 2] << 8 & '\uff00') + (scanRecord[verIndex + 3] & 255);
        iData.oneMeterRssi = scanRecord[rssiIndex];
        return iData;
//        }
    }

    public static iBeaconData generateiBeacon1(byte[] scanRecord) {
        int uuidIndex = 9;
        int verIndex = 25;
        int rssiIndex = 29;
        int macIndex = 0;
        if (scanRecord[0] == 26) {
            uuidIndex = 6;
            verIndex = 22;
            rssiIndex = 26;
        }

        iBeaconData iData = new iBeaconData();
        byte[] uuidArr = Arrays.copyOfRange(scanRecord, uuidIndex, uuidIndex + 16);
        String strUuid = String.format("%02X%02X%02X%02X-%02X%02X-%02X%02X-%02X%02X-%02X%02X%02X%02X%02X%02X", new Object[]{Byte.valueOf(uuidArr[0]), Byte.valueOf(uuidArr[1]), Byte.valueOf(uuidArr[2]), Byte.valueOf(uuidArr[3]), Byte.valueOf(uuidArr[4]), Byte.valueOf(uuidArr[5]), Byte.valueOf(uuidArr[6]), Byte.valueOf(uuidArr[7]), Byte.valueOf(uuidArr[8]), Byte.valueOf(uuidArr[9]), Byte.valueOf(uuidArr[10]), Byte.valueOf(uuidArr[11]), Byte.valueOf(uuidArr[12]), Byte.valueOf(uuidArr[13]), Byte.valueOf(uuidArr[14]), Byte.valueOf(uuidArr[15])});
        iData.beaconUuid = strUuid;
        iData.major = (scanRecord[verIndex] << 8 & '\uff00') + (scanRecord[verIndex + 1] & 255);
        iData.minor = (scanRecord[verIndex + 2] << 8 & '\uff00') + (scanRecord[verIndex + 3] & 255);
        iData.oneMeterRssi = scanRecord[rssiIndex];
        return iData;
    }

    public static iBeaconData copyOf(iBeaconData iBeacon) {
        iBeaconData newBeacon = new iBeaconData();
        newBeacon.macAddress = iBeacon.macAddress;
        newBeacon.beaconUuid = iBeacon.beaconUuid;
        newBeacon.major = iBeacon.major;
        newBeacon.minor = iBeacon.minor;
        newBeacon.oneMeterRssi = iBeacon.oneMeterRssi;
        newBeacon.rssi = iBeacon.rssi;
        return newBeacon;
    }

    public boolean equals(iBeaconData iBeacon, boolean checkMacAddress) {
        return (!checkMacAddress || this.macAddress.equals(iBeacon.macAddress)) && this.beaconUuid.equals(iBeacon.beaconUuid) && this.major == iBeacon.major && this.minor == iBeacon.minor;
    }

    public double calDistance(double oneMeterRssi) {
        if (this.rssi == 0) {
            return -1.0D;
        } else {
            double ratio = (double) ((float) this.rssi) / oneMeterRssi;
            double accuracy = Math.pow(10.0D, ((double) this.rssi - oneMeterRssi) / -32.5D);
            return accuracy;
        }
    }

    public double calDistance(double oneMeterRssi, int rssi) {
        if (rssi == 0) {
            return -1.0D;
        } else {
            double ratio = (double) ((float) rssi) / oneMeterRssi;
            double accuracy = Math.pow(10.0D, ((double) rssi - oneMeterRssi) / -32.5D);
            return accuracy;
        }
    }

    public double calDistance() {
        return this.calDistance((double) this.oneMeterRssi);
    }
}
