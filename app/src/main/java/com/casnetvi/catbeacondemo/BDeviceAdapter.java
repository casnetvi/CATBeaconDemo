/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.casnetvi.catbeacondemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BDeviceAdapter extends RecyclerView.Adapter<BDeviceAdapter.ViewHolder> {
    private List<BDevice> mDataSet = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mac;
        public final TextView rssi;
        public final TextView content;

        public ViewHolder(View v) {
            super(v);
            mac = v.findViewById(R.id.mac);
            rssi = v.findViewById(R.id.rssi);
            content = v.findViewById(R.id.content);
        }
    }

    public BDeviceAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_beacon, viewGroup, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.mac.setText(mDataSet.get(position).getAddress());
        viewHolder.rssi.setText(String.valueOf(mDataSet.get(position).getRssi()));
        viewHolder.content.setText(generateContent(mDataSet.get(position)));
    }


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


    public void addDevice(BDevice device) {
        if (device == null) {
            return;
        }
        boolean isNewDevice = true;
        for (int i = 0; i < mDataSet.size(); i++) {
            BDevice item = mDataSet.get(i);
            if (item.getAddress().equals(device.getAddress())) {
                isNewDevice = false;
                mDataSet.set(i, device);
                break;
            }
        }
        if (isNewDevice) {
            mDataSet.add(device);
        }
        notifyDataSetChanged();
    }

    private String generateContent(BDevice bDevice) {
        String contentDesc = "";
        contentDesc += byteArrayToString(bDevice.getRecord());

        contentDesc += "\n\nuuid : " + bDevice.getBeaconUuid();
        contentDesc += "\nmajor : " + bDevice.getMajor();
        contentDesc += "\nminor : " + bDevice.getMinor();
        contentDesc += "\n广播间隔 : " + bDevice.getBroadCastInterval();
        contentDesc += "\n测试功率 : " + bDevice.getTestPower();
        contentDesc += "\n传输功率 : " + bDevice.getTransPower();
        contentDesc += "\n名称 : " + bDevice.getBleName();
        contentDesc += "\n电池电量 : " + bDevice.getBattery();
        return contentDesc;
    }


    private static String byteArrayToString(byte[] buffer) {
        if (buffer == null) {
            return "";
        }
        final StringBuilder stringBuilder = new StringBuilder(buffer.length);
        if (buffer.length > 0) {
            for (byte byteChar : buffer)
                stringBuilder.append(String.format("%02X ", byteChar));
        }
        return stringBuilder.toString();
    }
}
