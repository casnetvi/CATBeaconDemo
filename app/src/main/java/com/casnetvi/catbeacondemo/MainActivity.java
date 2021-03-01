package com.casnetvi.catbeacondemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BleEngineV3 bleEngineV3;


    private BDeviceAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        }
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        bleEngineV3 = new BleEngineV3(this);
        bleEngineV3.setCallback(new BleEngineV3.Callback() {
            @Override
            public void onDevice(BDevice device) {
                mAdapter.addDevice(device);
            }
        });


        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new BDeviceAdapter();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();

        scan();
    }

    @Override
    protected void onStop() {
        super.onStop();

        bleEngineV3.stopScan();
    }


    public void scan() {
        if (checkBT()) {
            bleEngineV3.scan();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PermissionUtils.P_CODE_BLUE_TOOTH) {
                scan();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.P_CODE_BLUE_TOOTH) {
            if (PermissionUtils.checkGrantResults(grantResults)) {
                scan();
            }
        }
    }


    /**
     * 检查蓝牙
     */
    private boolean checkBT() {
        return checkBTPermission() && checkBTEnableAndOpen();
    }


    /**
     * 检查是否有权限，若无，则请求权限
     */
    private boolean checkBTPermission() {
        if (!PermissionUtils.checkHasPermission(this, PermissionUtils.PERMISSIONS_BLUE_TOOTH)) {
            PermissionUtils.requestPermissions(this, PermissionUtils.PERMISSIONS_BLUE_TOOTH, PermissionUtils.P_CODE_BLUE_TOOTH);
            return false;
        }
        return true;
    }

    /**
     * 检查蓝牙功能是否打开，若未打开，则请求打开
     */
    private boolean checkBTEnableAndOpen() {
        if (!checkBTEnable()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, PermissionUtils.P_CODE_BLUE_TOOTH);
            return false;
        }
        return true;
    }


    public boolean checkBTEnable() {
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled();
    }
}