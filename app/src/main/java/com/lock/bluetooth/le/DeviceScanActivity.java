/*
 * Copyright (C) 2013 The Android Open Source Project
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

package com.lock.bluetooth.le;

import android.Manifest;
import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.lock.bluetooth.le.BluetoothLeClass.OnDataAvailableListener;
import com.lock.bluetooth.le.BluetoothLeClass.OnServiceDiscoverListener;
import com.lock.bluetooth.le.adapter.LeDeviceListAdapter;
import com.lock.lib.common.util.ByteUtil;
import com.lock.lib.common.util.ShareUtil;

/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
public class DeviceScanActivity extends Activity implements AdapterView.OnItemClickListener{
    private final static String TAG = DeviceScanActivity.class.getSimpleName();
    //	private final static String UUID_KEY_DATA = "0000ffe1-0000-1000-8000-00805f9b34fba";
    private final static String UUID_KEY_DATA = "00001800-0000-1000-8000-00805f9b34fb";
    private final static String UUID_KEY_DATA_SERVICE = "0000ffa9-0000-1000-8000-00805f9b34fb";
    private final static String UUID_KEY_DATA_CHARACTERISTIC_A = "0000ffaa-0000-1000-8000-00805f9b34fb";
    private final static String UUID_KEY_DATA_CHARACTERISTIC_B = "0000ffab-0000-1000-8000-00805f9b34fb";
    private final static String UUID_KEY_DATA_CHARACTERISTIC_C = "0000ffac-0000-1000-8000-00805f9b34fb";
    private final static String UUID_KEY_DATA_DESC = "00002902-0000-1000-8000-00805f9b34fb";

    public BluetoothGattService mGattService;
    public BluetoothGattCharacteristic mCharacteristicAa;
    public BluetoothGattCharacteristic mCharacteristicAb;
    public BluetoothGattCharacteristic mCharacteristicAc;
    BluetoothLeScanner mScanner = null;

    public int current_status = 0;
    public int status_activation = 0; //未激活
    public int status_unlock = 1; //已激活


    String HandShackKey_String = "ea8b2a7316e9b04945b339280ac3283c";

    byte[] HandShakeKey2;

    char[] AES_Key_Table = new char[]{};

    // 00001800-0000-1000-8000-00805f9b34fb

    private LeDeviceListAdapter mLeDeviceListAdapter;
    /**搜索BLE终端*/
    private BluetoothAdapter mBluetoothAdapter;
    /**读写BLE终端*/
    private BluetoothLeClass mBLE;
    private boolean mScanning;
    private Handler mHandler;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 100000;

    public String log;
    public TextView mTextView;

    public ListView mListView;
    public TextView mBackView, mMiddleView;

    static {
        System.loadLibrary("native_le-lib");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_list);

        mTextView = (TextView)findViewById(R.id.log);

        mListView = (ListView) findViewById(R.id.list_view);
        mBackView = (TextView) findViewById(R.id.head_left_view);
        mBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mMiddleView = (TextView) findViewById(R.id.head_middel_view);
        mMiddleView.setText("Setting");
        mHandler = new Handler();

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        //开启蓝牙
        mBluetoothAdapter.enable();

        mBLE = new BluetoothLeClass(this);
        if (!mBLE.initialize()) {
            Log.e(TAG, "Unable to initialize Bluetooth");
            finish();
        }
        //发现BLE终端的Service时回调
        mBLE.setOnServiceDiscoverListener(mOnServiceDiscover);
        //收到BLE终端数据交互的事件
        mBLE.setOnDataAvailableListener(mOnDataAvailable);

    }

    @Override
    protected void onResume() {
        super.onResume();

        mLeDeviceListAdapter = new LeDeviceListAdapter(this);
        mScanner = mBluetoothAdapter.getBluetoothLeScanner();
        mListView.setAdapter(mLeDeviceListAdapter);
        mListView.setOnItemClickListener(this);
        requestPermission();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        mLeDeviceListAdapter.clear();
        mBLE.disconnect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBLE.close();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
        if (device == null) return;
        if (mScanning) {
//            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mScanner.stopScan(mScanCallback);
            mScanning = false;
        }

        mBLE.connect(device.getAddress());
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
//                    mBluetoothAdapter.stopLeScan(mLeScanCallback);

                    mScanner.stopScan(mScanCallback);
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            Log.e(TAG, "scanLeDevice mScanner " + mScanner);
            mScanner.startScan(mScanCallback);
//            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
//            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mScanner.stopScan(mScanCallback);
        }
        invalidateOptionsMenu();
    }

    /**
     * 搜索到BLE终端服务的事件
     */
    private OnServiceDiscoverListener mOnServiceDiscover = new OnServiceDiscoverListener(){

        @Override
        public void onServiceDiscover(BluetoothGatt gatt) {
            displayGattServices(mBLE.getSupportedGattServices());
        }

    };

    /**
     * 收到BLE终端数据交互的事件
     */
    private OnDataAvailableListener mOnDataAvailable = new OnDataAvailableListener(){

        /**
         * BLE终端数据被读的事件
         */
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e(TAG,"onCharRead "+gatt.getDevice().getName()
                        +" read "
                        +characteristic.getUuid().toString()
                        +" -> "
                        +Utils.bytesToHexString(characteristic.getValue()));

            }


        }

        /**
         * 收到BLE终端写入数据回调
         */
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          final BluetoothGattCharacteristic characteristic) {
            Log.e(TAG,"onCharWrite "+gatt.getDevice().getName()
                    +" write "
                    +characteristic.getUuid().toString()
                    +" -> "
                    +new String(characteristic.getValue()));
            log = log  + "\r\n" + ("onCharWrite "+gatt.getDevice().getName()
                    +" write "
                    +characteristic.getUuid().toString());
//            mTextView.setText(log);
            if (current_status == status_activation){
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        byte[] resultByte = characteristic.getValue();
                        if (resultByte.length == 20){
                            String key2Temp = ByteUtil.bytesToHexString(resultByte);
                            byte[] key2TempByte = ByteUtil.hexStringToBytes(key2Temp.substring(0, 32));
                            byte[] tempKeyByte = ByteUtil.hexStringToBytes(HandShackKey_String);
                            HandShakeKey2 = resultByte = aesDecrypt_(key2TempByte, tempKeyByte);


                            String mac = mDevice.getAddress();
                            Log.e(TAG, "--------> Device Mac Address " + mac);

                            mac = mac.replace(":", "");
                            StringBuffer stringBufferMac = new StringBuffer(mac);
                            String temp = ByteUtil.getStringRandom(20);
                            temp = temp + stringBufferMac;
                            Log.e(TAG, "-------> 16 byte data " + temp);
                            Log.e(TAG, "-------> 16 byte key " + key2Temp);
                            byte[] bytes = ByteUtil.hexStringToBytes(temp);
                            byte[] result = aesEncrypt_(bytes, resultByte);
                            Log.e(TAG, "-------> 16 byte aesEncrypt_ data " + ByteUtil.bytesToHexString(result));
                            log = log  + "\r\n" + ("-------> 16 byte aesEncrypt_ data " + ByteUtil.bytesToHexString(result));
                            mTextView.setText(log);
                            byte[] result2 = new byte[4];
                            final byte[] result3 = ByteUtil.addBytes(result, result2);

                            mCharacteristicAa.setValue(result3);
                            //往蓝牙模块写入数据
                            mBLE.writeCharacteristic(mCharacteristicAa);
                        }else if (resultByte.length == 2){
                            String key2Temp = ByteUtil.bytesToHexString(resultByte);
                            log = log  + "\r\n" + ("activation key2Temp " + key2Temp);
                            mTextView.setText(log);
                            if ("FF01".equals(key2Temp.toUpperCase())){
                                Log.e(TAG, "-------> activation device success ");
                                ShareUtil.getInstance(DeviceScanActivity.this).save("mac", "" + mDevice.getAddress());
                                ShareUtil.getInstance(DeviceScanActivity.this).save("name", "" + mDevice.getName());
                                ShareUtil.getInstance(DeviceScanActivity.this).save("name", "" + mDevice.getName());
                            }
                            current_status = status_unlock;
                        }

                    }
                }, 3000);

            }else if (current_status == status_unlock){
                byte[] temp = characteristic.getValue();
                if (temp.length == 16){
                    byte[] tempKeyByte = ByteUtil.hexStringToBytes(HandShackKey_String);
                    byte[] decTemp = aesDecrypt_(temp, tempKeyByte);
                    byte[] aesTemp = aesEncrypt_(decTemp, HandShakeKey2);
                    String decTempStr = ByteUtil.bytesToHexString(aesTemp);

                    decTempStr = decTempStr + "00000000";
                    final byte[] encrypData = ByteUtil.hexStringToBytes(decTempStr);

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            log = log  + "\r\n" + "mCharacteristicAb write....";
                            mTextView.setText(log);
                            mCharacteristicAb.setValue(encrypData);
                            //往蓝牙模块写入数据
                            mBLE.writeCharacteristic(mCharacteristicAb);
                        }
                    }, 3000);
                }else{
                    String key2Temp = ByteUtil.bytesToHexString(temp);

                    Log.e(TAG, "-------> unlock device result  " + key2Temp);
                    if ("FF01".equals(key2Temp.toUpperCase())){

                    }
                    current_status = status_unlock;
                }

            }
        }
    };
    BluetoothDevice mDevice ;

    ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, final ScanResult result) {
            super.onScanResult(callbackType, result);
            final BluetoothDevice device = result.getDevice();

//            String str = new String(result.getScanRecord().getBytes());
            String str2 = ByteUtil.bytesToHexString(result.getScanRecord().getBytes());
            if ("SC".equals(device.getName())){
                Log.e(TAG, "onScanResult device " + device + ", scanRecord toString " + result.getScanRecord().toString() + " , \n\r str2 " + str2);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ("SC".equals(device.getName())){
                            mDevice = device;
                            SparseArray<byte[]> manufacturerSpecificData = result.getScanRecord().getManufacturerSpecificData();

                            byte[] b = manufacturerSpecificData.valueAt(manufacturerSpecificData.size() - 1);
                            String str = new String(ByteUtil.bytesToHexString(b));
                            log = log + "\r\n manufacturerSpecificData " + str;
                            mTextView.setText(log);
                            if (str.endsWith("00")){
                                current_status = 0;
                            }else{
                                current_status = 1;
                            }

                            log = log + "\r\n current status " + current_status;
                            mTextView.setText(log);

                            mLeDeviceListAdapter.addDevice(device);
                            mLeDeviceListAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }


        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.e(TAG, "onBatchScanResults ");
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.e(TAG, "onScanFailed ");
        }
    };

    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String mac = mDevice.getAddress();
        Log.e(TAG, "--> Device Mac Address " + mac);

        mac = mac.replace(":", "");
        StringBuffer stringBufferMac = new StringBuffer(mac);
        String temp = ByteUtil.getStringRandom(20);
        temp = temp + stringBufferMac;
        Log.e(TAG, "--> 16 byte data " + temp);
        Log.e(TAG, "--> 16 byte key " + HandShackKey_String);
        byte[] bytes = ByteUtil.hexStringToBytes(temp);
        byte[] tempByte = ByteUtil.hexStringToBytes(HandShackKey_String);
        byte[] result = aesEncrypt_(bytes, tempByte);
        Log.e(TAG, "--> 16 byte aesEncrypt_ data " + ByteUtil.bytesToHexString(result));
        byte[] result2 = new byte[4];
        final byte[] result3 = ByteUtil.addBytes(result, result2);
        Log.e(TAG,"----> result3 " + ByteUtil.bytesToHexString(result3));

        for (BluetoothGattService gattService : gattServices) {
            //-----Service的字段信息-----//
            int type = gattService.getType();
//            Log.e(TAG,"-->service type:"+Utils.getServiceType(type));
//            Log.e(TAG,"-->includedServices size:"+gattService.getIncludedServices().size());
//            Log.e(TAG,"-->service uuid:"+gattService.getUuid());
            if (gattService.getUuid().toString().equals(UUID_KEY_DATA_SERVICE)){
                mGattService = gattService;
            }

            //-----Characteristics的字段信息-----//
            List<BluetoothGattCharacteristic> gattCharacteristics =gattService.getCharacteristics();
            for (final BluetoothGattCharacteristic  gattCharacteristic: gattCharacteristics) {
//                Log.e(TAG,"---->char uuid:"+gattCharacteristic.getUuid());

                int permission = gattCharacteristic.getPermissions();
//                Log.e(TAG,"---->char permission:"+Utils.getCharPermission(permission));

                int property = gattCharacteristic.getProperties();
//                Log.e(TAG,"---->char property:"+Utils.getCharPropertie(property));

                byte[] data = gattCharacteristic.getValue();
                if (data != null && data.length > 0) {
                    Log.e(TAG,"---->char value:"+new String(data));
                }

                //UUID_KEY_DATA是可以跟蓝牙模块串口通信的Characteristic
                if(gattCharacteristic.getUuid().toString().equals(UUID_KEY_DATA_CHARACTERISTIC_A)){
                    mCharacteristicAa = gattCharacteristic;
                }else if (gattCharacteristic.getUuid().toString().equals(UUID_KEY_DATA_CHARACTERISTIC_C)){
                    mCharacteristicAc = gattCharacteristic;
                    //接受Characteristic被写的通知,收到蓝牙模块的数据后会触发mOnDataAvailable.onCharacteristicWrite()
                    mBLE.setCharacteristicNotification(mCharacteristicAc, true);
                }else if (gattCharacteristic.getUuid().toString().equals(UUID_KEY_DATA_CHARACTERISTIC_B)){
                    mCharacteristicAb = gattCharacteristic;
                    //接受Characteristic被写的通知,收到蓝牙模块的数据后会触发mOnDataAvailable.onCharacteristicWrite()
//                    mBLE.setCharacteristicNotification(mCharacteristicAb, true);
                }

            }
        }//

        //UUID_KEY_DATA是可以跟蓝牙模块串口通信的Characteristic
        if(mCharacteristicAa != null) {
            if (current_status == status_activation){
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                                mBLE.setCharacteristicNotification(mCharacteristicAc, true);
                        //设置数据内容
                        Log.e(TAG, "---->will write data  result3 " + ByteUtil.bytesToHexString(result3));
                        log = log + "\r\n will write data" + result3;
                        //mTextView.setText(log);
                        mCharacteristicAa.setValue(result3);
                        //往蓝牙模块写入数据
                         mBLE.writeCharacteristic(mCharacteristicAa);
                    }
                }, 5000);

            }
        }

    }

    private static final int REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;
    private void requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//如果 API level 是大于等于 23(Android 6.0) 时
            //判断是否具有权限
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //判断是否需要向用户解释为什么需要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
//                    showToast("自Android 6.0开始需要打开位置权限才可以搜索到Ble设备");
                }
                //请求权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_CODE_ACCESS_COARSE_LOCATION);
            }else{
                scanLeDevice(true);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_ACCESS_COARSE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户允许改权限，0表示允许，-1表示拒绝 PERMISSION_GRANTED = 0， PERMISSION_DENIED = -1
                //permission was granted, yay! Do the contacts-related task you need to do.
                //这里进行授权被允许的处理
                boolean flag = isLocationEnable(DeviceScanActivity.this);
                if (flag){
                    scanLeDevice(true);
                }else {
                    setLocationService();
                }
            } else {
                //permission denied, boo! Disable the functionality that depends on this permission.
                //这里进行权限被拒绝的处理
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Location service if enable
     *
     * @param context
     * @return location is enable if return true, otherwise disable.
     */
    public static final boolean isLocationEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean networkProvider = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean gpsProvider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (networkProvider || gpsProvider) return true;
        return false;
    }
//    如果定位已经打开，OK 很好，可以搜索到 ble 设备；如果定位没有打开，则需要用户去打开，像下面这样：

    private static final int REQUEST_CODE_LOCATION_SETTINGS = 2;

    private void setLocationService() {
        Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        this.startActivityForResult(locationIntent, REQUEST_CODE_LOCATION_SETTINGS);
    }
//    进入定位设置界面，让用户自己选择是否打开定位。选择的结果获取：

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_LOCATION_SETTINGS) {
            if (isLocationEnable(this)) {
                //定位已打开的处理
                scanLeDevice(true);
            } else {
                //定位依然没有打开的处理
            }
        } else super.onActivityResult(requestCode, resultCode, data);
    }

    public native byte[] aesEncrypt_(byte[] data, byte[] key);
    public native byte[] aesDecrypt_(byte[] data, byte[] key);

}