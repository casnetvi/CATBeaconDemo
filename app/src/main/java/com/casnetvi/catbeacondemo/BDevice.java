package com.casnetvi.catbeacondemo;

/**
 * Created by wzx on 2018/10/22.
 */

public class BDevice {
    private String name;
    private int rssi;
    private String address;
    private byte[] record;

    private String beaconUuid;
    private int major;
    private int minor;

    private int broadCastInterval;
    private int testPower;
    private int transPower;
    private String bleName;
    private int battery;


    public BDevice(String name, String address, int rssi, byte[] record) {
        this.name = name;
        this.address = address;
        this.rssi = rssi;
        this.record = record;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getRecord() {
        return record;
    }

    public void setRecord(byte[] record) {
        this.record = record;
    }

    public String getBeaconUuid() {
        return beaconUuid;
    }

    public void setBeaconUuid(String beaconUuid) {
        this.beaconUuid = beaconUuid;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getBroadCastInterval() {
        return broadCastInterval;
    }

    public void setBroadCastInterval(int broadCastInterval) {
        this.broadCastInterval = broadCastInterval;
    }

    public int getTestPower() {
        return testPower;
    }

    public void setTestPower(int testPower) {
        this.testPower = testPower;
    }

    public int getTransPower() {
        return transPower;
    }

    public void setTransPower(int transPower) {
        this.transPower = transPower;
    }

    public String getBleName() {
        return bleName;
    }

    public void setBleName(String bleName) {
        this.bleName = bleName;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }
}
