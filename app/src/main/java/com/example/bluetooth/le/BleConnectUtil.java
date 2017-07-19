package com.example.bluetooth.le;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.lock.lib.api.Server;
import com.lock.lib.api.event.ResponseEvent;
import com.lock.lib.common.util.ByteUtil;
import com.lock.lib.common.util.DeviceUtil;
import com.lock.lib.common.util.FileUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by admin on 17/6/25.
 */

public class BleConnectUtil {

    public static String TAG = "BleConnectUtil";

    private int mOsVersion;

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
    public int status_unlock_success = 2;

    String HandShackKey_String = "ea8b2a7316e9b04945b339280ac3283c";

    byte[] HandShakeKey2;

    /**
     * 搜索BLE终端
     */
    private BluetoothAdapter mBluetoothAdapter;
    /**
     * 读写BLE终端
     */
    private BluetoothLeClass mBLE;
    private boolean mScanning;
    private Handler mHandler;
    BluetoothDevice mDevice;

    private String mMac;
    private int tryCount = 0;

    // Stops scanning after 15 seconds.
    private static final long SCAN_PERIOD = 15000;
    private static Context mContext;

    private List<DeviceModel> connectDevices = null;

    private Object lock = new Object();

    private int mDeviceConnectState = 1;

    public BleConnectUtil(Context context, BluetoothLeClass.OnServiceDiscoverListener discoverListener, BluetoothLeClass.OnDataAvailableListener dataAvailableListener) {
        FileUtil.deleteFile("", "");
        mOsVersion = DeviceUtil.getOsVersion();
        mContext = context;
        connectDevices = DeviceShare.getDevices(context);

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(mContext, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            return;
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(mContext, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            return;
        }

        //开启蓝牙
        mBluetoothAdapter.enable();

        mBLE = new BluetoothLeClass(mContext);
        if (!mBLE.initialize()) {
            Log.e(TAG, "Unable to initialize Bluetooth");
//            FileUtil.writeTxtToFile("Unable to initialize Bluetooth ");
            return;
        }
        //发现BLE终端的Service时回调
        mBLE.setOnServiceDiscoverListener(mOnServiceDiscover);
        //收到BLE终端数据交互的事件
        mBLE.setOnDataAvailableListener(mOnDataAvailable);

        mBLE.setOnDisconnectListener(mOnDisconnectListener);

        mHandler = new Handler();

        if (mOsVersion >= Build.VERSION_CODES.LOLLIPOP){
            initBleScan();
        }
    }

    public void connectDevice(String mac) {
        Log.e(TAG, "connectDevice mac " + mac);
        tryCount = 0;
//        FileUtil.writeTxtToFile("connectDevice mac");
        if (mBLE == null) {

            mBLE = new BluetoothLeClass(mContext);
            if (!mBLE.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
//                FileUtil.writeTxtToFile("Unable to initialize Bluetooth ");
                return;
            }
            //发现BLE终端的Service时回调
            mBLE.setOnServiceDiscoverListener(mOnServiceDiscover);
            //收到BLE终端数据交互的事件
            mBLE.setOnDataAvailableListener(mOnDataAvailable);

            mBLE.setOnDisconnectListener(mOnDisconnectListener);
//            return;
        }
        mMac = mac;

//        mScanner = mBluetoothAdapter.getBluetoothLeScanner();
        scanLeDevice(true);
    }

    public void disconnectDevice() {
        Log.e(TAG, "disconnectDevice");
        scanLeDevice(false);
        if (mBLE != null) {
            mBLE.disconnect();
            mBLE.close();
            mBLE = null;
        }
        mDevice = null;
    }

    public void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String mac = mDevice.getAddress();
        Log.e(TAG, "displayGattServices --> Device Mac Address " + mac);
//        FileUtil.writeTxtToFile("获取BLE设备服务相关信息 --> Device Mac Address  " + mac);
        mac = mac.replace(":", "");
        StringBuffer stringBufferMac = new StringBuffer(mac);
        String temp = ByteUtil.getStringRandom(20);
//        FileUtil.writeTxtToFile("获取BLE设备服务相关信息 --> 生成的随机数 " + temp);
        temp = temp + stringBufferMac;
        Log.e(TAG, "displayGattServices --> 16 byte data " + temp);
        Log.e(TAG, "displayGattServices --> 16 byte key 1 " + HandShackKey_String);
//        FileUtil.writeTxtToFile("获取BLE设备服务相关信息 --> 16 byte key 1 " + HandShackKey_String);
        byte[] bytes = ByteUtil.hexStringToBytes(temp);
        byte[] tempByte = ByteUtil.hexStringToBytes(HandShackKey_String);
        byte[] result = aesEncrypt(bytes, tempByte);
        byte[] result2 = new byte[4];
        final byte[] result3 = ByteUtil.addBytes(result, result2);
//        FileUtil.writeTxtToFile("获取BLE设备服务相关信息 --> aesEncrypt 秘钥1 加密和补0后数据 " + ByteUtil.bytesToHexString(result3));
        Log.e(TAG, "displayGattServices aesEncrypt----> result3 " + ByteUtil.bytesToHexString(result3));

        for (BluetoothGattService gattService : gattServices) {
            //-----Service的字段信息-----//
            int type = gattService.getType();
//            Log.e(TAG,"-->service type:"+Utils.getServiceType(type));
//            Log.e(TAG,"-->includedServices size:"+gattService.getIncludedServices().size());
//            Log.e(TAG,"-->service uuid:"+gattService.getUuid());
            if (gattService.getUuid().toString().equals(UUID_KEY_DATA_SERVICE)) {
                mGattService = gattService;
            }

            //-----Characteristics的字段信息-----//
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
//                Log.e(TAG,"---->char uuid:"+gattCharacteristic.getUuid());

                int permission = gattCharacteristic.getPermissions();
//                Log.e(TAG,"---->char permission:"+Utils.getCharPermission(permission));

                int property = gattCharacteristic.getProperties();
//                Log.e(TAG,"---->char property:"+Utils.getCharPropertie(property));

                //UUID_KEY_DATA是可以跟蓝牙模块串口通信的Characteristic
                if (gattCharacteristic.getUuid().toString().equals(UUID_KEY_DATA_CHARACTERISTIC_A)) {
                    mCharacteristicAa = gattCharacteristic;
                } else if (gattCharacteristic.getUuid().toString().equals(UUID_KEY_DATA_CHARACTERISTIC_C)) {
                    mCharacteristicAc = gattCharacteristic;
                    mBLE.setCharacteristicNotification(mCharacteristicAc, true);
                } else if (gattCharacteristic.getUuid().toString().equals(UUID_KEY_DATA_CHARACTERISTIC_B)) {
                    mCharacteristicAb = gattCharacteristic;
                }

            }
        }//

        //UUID_KEY_DATA是可以跟蓝牙模块串口通信的Characteristic
        if (mCharacteristicAa != null) {
            if (current_status == status_activation) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //设置数据内容
                        Log.e(TAG, "---->will write data  result3 " + ByteUtil.bytesToHexString(result3));
//                        FileUtil.writeTxtToFile("准备通过特征FFAA写入数据 -->  " + ByteUtil.bytesToHexString(result3));
                        mCharacteristicAa.setValue(result3);
                        //往蓝牙模块写入数据
                        mBLE.writeCharacteristic(mCharacteristicAa);
                    }
                }, 100);
            }
        }

    }

    private BluetoothLeClass.OnDisconnectListener mOnDisconnectListener = new BluetoothLeClass.OnDisconnectListener(){

        @Override
        public void onDisconnect(BluetoothGatt gatt) {
//            stopBleScan();
//            connectDevice(mMac);

            Log.e(TAG, "onDisconnect ");

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mDevice != null && mBLE != null) {
                        if ( ++tryCount <= 2){
                            try {
                                Log.e(TAG, "onDisconnect device mac " + mDevice.getAddress());
                                mBLE.connect(mDevice.getAddress());
                            }catch (Exception e){
                            }
                        }else{
                            mHandler.removeCallbacks(scanRunnable);
                            if (current_status == status_activation){
                                postResponseEvent(ResponseEvent.TYPE_UNLOCK_DEVICE_SUCCESS, Server.Code.FAIL, "Activation Garage failed", getShareDeviceModel(mDevice));
                            }else if (current_status == status_unlock){
                                postResponseEvent(ResponseEvent.TYPE_UNLOCK_DEVICE_SUCCESS, Server.Code.FAIL, "Garage opened failed", getShareDeviceModel(mDevice));
                            }
                        }
                    }
                }
            }, 500);
        }
    };

    /**
     * 搜索到BLE终端服务的事件
     */
    private BluetoothLeClass.OnServiceDiscoverListener mOnServiceDiscover = new BluetoothLeClass.OnServiceDiscoverListener() {
        @Override
        public void onServiceDiscover(BluetoothGatt gatt) {
            displayGattServices(mBLE.getSupportedGattServices());
        }
    };

    /**
     * 收到BLE终端数据交互的事件
     */
    private BluetoothLeClass.OnDataAvailableListener mOnDataAvailable = new BluetoothLeClass.OnDataAvailableListener() {

        /**
         * BLE终端数据被读的事件
         */
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e(TAG, "onCharRead " + gatt.getDevice().getName()
                        + " read "
                        + characteristic.getUuid().toString()
                        + " -> "
                        + Utils.bytesToHexString(characteristic.getValue()));

            }


        }

        /**
         * 收到BLE终端写入数据回调
         */
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          final BluetoothGattCharacteristic characteristic) {
            Log.e(TAG, "onCharacteristicWrite " + gatt.getDevice().getName() + " write " + characteristic.getUuid().toString() + "---current_status " + current_status);
//            FileUtil.writeTxtToFile("收到BLE返回的数据 设备名：" + gatt.getDevice().getName()  + " UUID " + characteristic.getUuid().toString() + " 当前状态 " + current_status);
            if (current_status == status_activation) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        byte[] resultByte = characteristic.getValue();
                        if (resultByte.length == 20) {
//                            FileUtil.writeTxtToFile("激活设备：激活第一步 "  );
                            String key2Temp = ByteUtil.bytesToHexString(resultByte);
                            byte[] key2TempByte = ByteUtil.hexStringToBytes(key2Temp.substring(0, 32));
                            byte[] tempKeyByte = ByteUtil.hexStringToBytes(HandShackKey_String);
                            HandShakeKey2 = resultByte = aesDecrypt(key2TempByte, tempKeyByte);

                            String mac = mDevice.getAddress();
                            Log.e(TAG, "onCharacteristicWrite--------> onCharacteristicWrite Device Mac Address " + mac + ", HandShakeKey2 " + ByteUtil.bytesToHexString(HandShakeKey2));

                            mac = mac.replace(":", "");
                            StringBuffer stringBufferMac = new StringBuffer(mac);
                            String temp = ByteUtil.getStringRandom(20);
                            temp = temp + stringBufferMac;
                            Log.e(TAG, "onCharacteristicWrite-------> 16 byte data " + temp);
                            Log.e(TAG, "onCharacteristicWrite-------> 16 byte key " + key2Temp);
                            byte[] bytes = ByteUtil.hexStringToBytes(temp);
                            byte[] result = aesEncrypt(bytes, resultByte);
                            Log.e(TAG, "onCharacteristicWrite-------> 16 byte aesEncrypt data " + ByteUtil.bytesToHexString(result));
                            byte[] result2 = new byte[4];
                            final byte[] result3 = ByteUtil.addBytes(result, result2);
//                            FileUtil.writeTxtToFile("激活设备：通过特征FFAA写入 "  );
                            mCharacteristicAa.setValue(result3);
                            //往蓝牙模块写入数据
                            if (mBLE != null){
                                mBLE.writeCharacteristic(mCharacteristicAa);
                            }
                        } else if (resultByte.length == 2) {
                            mHandler.removeCallbacks(scanRunnable);
                            mDeviceConnectState = 0;
                            String key2Temp = ByteUtil.bytesToHexString(resultByte);
                            if ("FF01".equals(key2Temp.toUpperCase())) {
                                Log.e(TAG, "onCharacteristicWrite-------> activation device success ");
                                String mac = mDevice.getAddress();
                                DeviceModel model = new DeviceModel();
//                                model.name = mDevice.getName();
                                model.name = "Digital Ant-" + "" + mac.substring(mac.length() - 2);

                                model.mac = mDevice.getAddress();
                                model.key = ByteUtil.bytesToHexString(HandShakeKey2);
                                DeviceShare.saveDevice(mContext, model);
                                current_status = status_unlock;
                                postResponseEvent(ResponseEvent.TYPE_ACTIVITY_DEVICE_SUCCESS, Server.Code.SUCCESS, "", model);
//                                FileUtil.writeTxtToFile("激活设备：激活第二步 验证成功："  + key2Temp);
                                disconnectDevice();
                            } else {
                                postResponseEvent(ResponseEvent.TYPE_ACTIVITY_DEVICE, Server.Code.FAIL, "Activation Garage failed", getShareDeviceModel(mDevice));
//                                FileUtil.writeTxtToFile("激活设备：激活第二步 验证失败："  + key2Temp);
                                disconnectDevice();
                            }
                        }

                    }
                }, 100);

            } else if (current_status == status_unlock) {
//                FileUtil.writeTxtToFile("开锁设备：" );
                Log.e(TAG, "onCharacteristicWrite-------> 开锁设备： " + mMac);
                byte[] temp = characteristic.getValue();
                if (temp.length == 16) {
                    Log.e(TAG, "开锁设备：开锁第一步： 密钥1 " + HandShackKey_String);
                    Log.e(TAG, "开锁设备：开锁第一步： 密钥2 " + HandShakeKey2);
//                    FileUtil.writeTxtToFile("开锁设备：开锁第一步 密钥1 "  + HandShackKey_String);
//                    FileUtil.writeTxtToFile("开锁设备：开锁第一步 密钥2 "  + HandShakeKey2);
                    byte[] tempKeyByte = ByteUtil.hexStringToBytes(HandShackKey_String);
                    byte[] decTemp = aesDecrypt(temp, tempKeyByte);
//                    FileUtil.writeTxtToFile("开锁设备：开锁第一步 解密后设备数据 decTemp "  + ByteUtil.bytesToHexString(decTemp));
                    byte[] aesTemp = aesEncrypt(decTemp, HandShakeKey2);
                    String decTempStr = ByteUtil.bytesToHexString(aesTemp);
//                    FileUtil.writeTxtToFile("开锁设备：开锁第一步 重新使用密钥2 加密 设备数据 decTemp "  + decTempStr);

                    decTempStr = decTempStr + "00000000";
                    final byte[] encrypData = ByteUtil.hexStringToBytes(decTempStr);
                    Log.e(TAG, "开锁设备：开锁第一步 FFAA写入 ");
//                            FileUtil.writeTxtToFile("开锁设备：开锁第一步 FFAA写入 "  + ByteUtil.bytesToHexString(encrypData));

                    mCharacteristicAb.setValue(encrypData);
                    //往蓝牙模块写入数据
                    if (mBLE != null){
                        mBLE.writeCharacteristic(mCharacteristicAb);
                    }
                    Log.e(TAG, "开锁设备：开锁第一步 FFAA写入 完成");
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 100);
                } else {
                    mHandler.removeCallbacks(scanRunnable);
                    mDeviceConnectState = 0;
                    String key2Temp = ByteUtil.bytesToHexString(temp);

                    stopBleScan();

                    Log.e(TAG, "onCharacteristicWrite-------> unlock device result  " + key2Temp);
                    if ("FF02".equals(key2Temp.toUpperCase())) {
                        current_status = status_unlock_success;
                        Log.e(TAG, "开锁设备 onCharacteristicWrite-------> unlock device success  ");
//                        FileUtil.writeTxtToFile("开锁设备：开锁第二步 开锁成功 "  + key2Temp);
                        postResponseEvent(ResponseEvent.TYPE_UNLOCK_DEVICE_SUCCESS, Server.Code.SUCCESS, "Garage has been opened successfully.", getShareDeviceModel(mDevice));
                    } else {
//                        FileUtil.writeTxtToFile("开锁设备：开锁第二步 开锁失败 "  + key2Temp);
                        postResponseEvent(ResponseEvent.TYPE_UNLOCK_DEVICE_SUCCESS, Server.Code.FAIL, "Garage opened failed", getShareDeviceModel(mDevice));
                    }
                }
            }
        }
    };

    Runnable scanRunnable = new Runnable() {
        @Override
        public void run() {
            mScanning = false;
            if (mOsVersion >= Build.VERSION_CODES.LOLLIPOP){
                if (mScanner != null && mScanCallback != null) {
                    disconnectDevice();
                    Log.e(TAG, "scanLeDevice mDeviceConnectState " + mDeviceConnectState);
                    if (mDeviceConnectState == 1){
                        postResponseEvent(ResponseEvent.TYPE_SCAN_TIME_OUT, Server.Code.FAIL, "Can not find the device", null);
                    }else{
                        postResponseEvent(ResponseEvent.TYPE_SCAN_TIME_OUT, Server.Code.SUCCESS, "Can not find the device", null);
                    }
                    FileUtil.writeTxtToFile("Scan time out, Can not find the device");
                }
            }else{
                disconnectDevice();
                if (mDeviceConnectState == 1){
                    postResponseEvent(ResponseEvent.TYPE_SCAN_TIME_OUT, Server.Code.FAIL, "Can not find the device", null);
                }else{
                    postResponseEvent(ResponseEvent.TYPE_SCAN_TIME_OUT, Server.Code.SUCCESS, "Can not find the device", null);
                }
            }
        }
    };

    private void scanLeDevice(final boolean enable) {
        Log.e(TAG, "scanLeDevice enable " + enable);
        if (enable) {
//            FileUtil.writeTxtToFile("开始扫描 ");
            mHandler.postDelayed(scanRunnable, SCAN_PERIOD);

            mScanning = true;
            if (mOsVersion >= Build.VERSION_CODES.LOLLIPOP){
                startBleScan();
            }else{
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            }
        } else {
//            FileUtil.writeTxtToFile("结束扫描 ");
            mScanning = false;
            if (mOsVersion >= Build.VERSION_CODES.LOLLIPOP){
                stopBleScan();
            }else{
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, final byte[] scanRecord) {
                    if ("SC".equalsIgnoreCase(device.getName())) {
//                        FileUtil.writeTxtToFile("4.4 扫描到设备  Mac " + device.getAddress() + ", name " + device.getName());
                        Log.e(TAG, "4.4 onScanResult-------->  Device Mac Address " + device.getAddress() + ", name " + device.getName());

                        String str = new String(ByteUtil.bytesToHexString(scanRecord)).toUpperCase();
//                        FileUtil.writeTxtToFile("扫描到 ble 设备  Mac " + device.getAddress() + ", name " + device.getName() + ", scanRecord " + str);
                        String tempMac = device.getAddress().replace(":", "").toUpperCase();
                        int s = str.indexOf(tempMac) + 12;
                        String string = str.substring(s, s+2);
                        Log.e(TAG, "onScanResult--------> scanRecord ble device " + str);

                        if (string.endsWith("00")) {
                            current_status = 0;
                        } else {
                            current_status = 1;
                        }

                        Log.e(TAG, "onScanResult-------->  ble device current_status " + current_status);
//                        FileUtil.writeTxtToFile("扫描到 ble 设备  ble device current_status " + current_status);
                        if (TextUtils.isEmpty(mMac)) {
                            if (current_status == 0){
                                // 激活
                                if (connectDevices.size() == 0) {
                                    scanLeDevice(false);
                                    mDevice = device;
                                    Log.e(TAG, "扫描到 ble 设备 1  开始连接 ");
//                                    FileUtil.writeTxtToFile("扫描到 ble 设备 1  开始连接 " + mDevice.getAddress());
                                    mBLE.connect(mDevice.getAddress());
                                } else {
                                    boolean flag = false;
                                    for (int i = 0, j = connectDevices.size(); i < j; i++) {
                                        DeviceModel deviceModel = connectDevices.get(i);
                                        if (device.getAddress().equals(deviceModel.mac)) {
                                            flag = true;
                                            break;
                                        }
                                    }
                                    if (!flag){
                                        scanLeDevice(false);
                                        mDevice = device;
                                        Log.e(TAG, "扫描到 ble 设备 2  开始连接 ");
//                                        FileUtil.writeTxtToFile("扫描到 ble 设备 2 开始连接 " + mDevice.getAddress());
                                        mBLE.connect(mDevice.getAddress());
                                    }else{
                                        Log.e(TAG, "本地存储设备已被重置 Mac " + device.getAddress());
//                                        FileUtil.writeTxtToFile("本地存储设备已被重置 Mac " + device.getAddress());
                                    }
                                }
                            }else{
//                                FileUtil.writeTxtToFile("扫描到 ble 设备  Please reset device! ");
                                Log.e(TAG, "扫描到 ble 设备  Please reset device! ");
                            }
                        } else {
                            if (device.getAddress().equalsIgnoreCase(mMac)){
                                if (current_status == status_unlock){
                                    scanLeDevice(false);
                                    if (connectDevices.size() > 0) {
                                        for (int i = 0, j = connectDevices.size(); i < j; i++) {
                                            DeviceModel deviceModel = connectDevices.get(i);
                                            if (device.getAddress().equals(deviceModel.mac)) {
                                                HandShakeKey2 = ByteUtil.hexStringToBytes(deviceModel.key);
                                                break;
                                            }
                                        }
                                    }
                                    mDevice = device;
                                    Log.e(TAG, "开锁： 扫描到 ble 设备 开始连接 " + mDevice.getAddress());
//                                    FileUtil.writeTxtToFile("开锁： 扫描到 ble 设备 开始开锁 " + mDevice.getAddress());
                                    mBLE.connect(mDevice.getAddress());
                                }else{
                                    scanLeDevice(false);
                                    mHandler.removeCallbacks(scanRunnable);
                                    mDeviceConnectState = 0;
                                    Log.e(TAG, "开锁： 扫描到 ble 设备  Device is reset,please delete record and re-activate it. " + device.getAddress());
//                                    FileUtil.writeTxtToFile("开锁： 扫描到 ble 设备  Device is reset,please delete record and re-activate it. ---- " + device.getAddress());
                                    postResponseEvent(ResponseEvent.TYPE_UNLOCK_DEVICE_SUCCESS, Server.Code.FAIL, "Device is reset,please delete record and re-activate it.", null);
                                }
                            }
                        }
                    }
                }
            };
    ScanCallback mScanCallback = null;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initBleScan(){
        mScanner = mBluetoothAdapter.getBluetoothLeScanner();
        mScanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, final ScanResult result) {
                super.onScanResult(callbackType, result);
                final BluetoothDevice device = result.getDevice();
//                FileUtil.writeTxtToFile("4.4+ 扫描到设备  Mac " + device.getAddress() + ", name " + device.getName());
                Log.e(TAG, "onScanResult 4.4+ -------->  Device Mac Address " + device.getAddress() + ", name " + device.getName());
                if ("SC".equalsIgnoreCase(device.getName()) && mDevice == null) {
//                    FileUtil.writeTxtToFile("扫描到 ble 设备  Mac " + device.getAddress() + ", name " + device.getName());
                    Log.e(TAG, "onScanResult-------->  ble device ");
                    SparseArray<byte[]> manufacturerSpecificData = result.getScanRecord().getManufacturerSpecificData();
                    byte[] b = manufacturerSpecificData.valueAt(manufacturerSpecificData.size() - 1);

                    String str = new String(ByteUtil.bytesToHexString(b));
                    if (str.endsWith("00")) {
                        current_status = 0;
                    } else {
                        current_status = 1;
                    }
                    Log.e(TAG, "onScanResult-------->  ble device current_status " + current_status);
//                    FileUtil.writeTxtToFile("扫描到 ble 设备  ble device current_status " + current_status);
                    if (TextUtils.isEmpty(mMac)) {
                        if (current_status == 0){
                            // 激活
                            if (connectDevices.size() == 0) {
                                scanLeDevice(false);
                                mDevice = device;
                                Log.e(TAG, "扫描到 ble 设备 1  开始连接 ");
//                                FileUtil.writeTxtToFile("扫描到 ble 设备 1  开始连接 " + mDevice.getAddress());
                                mBLE.connect(mDevice.getAddress());
                            } else {
                                boolean flag = false;
                                for (int i = 0, j = connectDevices.size(); i < j; i++) {
                                    DeviceModel deviceModel = connectDevices.get(i);
                                    if (device.getAddress().equals(deviceModel.mac)) {
                                        flag = true;
                                        break;
                                    }
                                }
                                if (!flag){
                                    scanLeDevice(false);
                                    mDevice = device;
                                    Log.e(TAG, "扫描到 ble 设备 2  开始连接 ");
//                                    FileUtil.writeTxtToFile("扫描到 ble 设备 2 开始连接 " + mDevice.getAddress());
                                    mBLE.connect(mDevice.getAddress());
                                }else{
                                    Log.e(TAG, "本地存储设备已被重置 Mac " + device.getAddress());
//                                    FileUtil.writeTxtToFile("本地存储设备已被重置 Mac " + device.getAddress());
//                                    postResponseEvent(ResponseEvent.TYPE_UNLOCK_DEVICE_SUCCESS, Server.Code.FAIL, "Device is reset,please delete record and re-activate it.", null);
                                }
                            }
                        }else{
//                            FileUtil.writeTxtToFile("扫描到 ble 设备  Please reset device! ");
                            Log.e(TAG, "扫描到 ble 设备  Please reset device! ");
                        }
                    } else {
                        if (device.getAddress().equalsIgnoreCase(mMac)){
                            if (current_status == status_unlock){
                                scanLeDevice(false);
                                if (connectDevices.size() > 0) {
                                    for (int i = 0, j = connectDevices.size(); i < j; i++) {
                                        DeviceModel deviceModel = connectDevices.get(i);
                                        if (device.getAddress().equals(deviceModel.mac)) {
                                            HandShakeKey2 = ByteUtil.hexStringToBytes(deviceModel.key);
                                            break;
                                        }
                                    }
                                }
                                mDevice = device;
                                Log.e(TAG, "开锁： 扫描到 ble 设备 开始连接 " + mDevice.getAddress());
//                                FileUtil.writeTxtToFile("开锁： 扫描到 ble 设备 开始开锁 " + mDevice.getAddress());
                                mBLE.connect(mDevice.getAddress());
                            }else{
                                scanLeDevice(false);
                                mHandler.removeCallbacks(scanRunnable);
                                mDeviceConnectState = 0;
                                Log.e(TAG, "开锁： 扫描到 ble 设备  Device is reset,please delete record and re-activate it. " + device.getAddress());
//                                FileUtil.writeTxtToFile("开锁： 扫描到 ble 设备  Device is reset,please delete record and re-activate it. ---- " + device.getAddress());
                                postResponseEvent(ResponseEvent.TYPE_UNLOCK_DEVICE_SUCCESS, Server.Code.FAIL, "Device is reset,please delete record and re-activate it.", null);
                            }
                        }
                    }
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
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startBleScan(){
        if (mScanner == null){
            mScanner = mBluetoothAdapter.getBluetoothLeScanner();
        }
        if (mScanner != null && mScanCallback != null) {
            mScanner.startScan(mScanCallback);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void stopBleScan(){
        if (mScanner != null && mScanCallback != null) {
            mScanner.stopScan(mScanCallback);
        }
    }

    private void postResponseEvent(int eventType, int errorCode, String errorMsg, DeviceModel deviceModel) {
        ResponseEvent event = new ResponseEvent();
        event.eventType = eventType;
        event.errorCode = errorCode;
        event.errorMsg = errorMsg;
        event.resultObj = deviceModel;
        EventBus.getDefault().post(event);
    }

    public DeviceModel getShareDeviceModel(BluetoothDevice device) {
        if (device == null) return null;
        DeviceModel deviceModel = null;
        if (connectDevices == null || connectDevices.size() == 0) {
            deviceModel = new DeviceModel();
            deviceModel.mac = device.getAddress();
            deviceModel.name = device.getName();
            deviceModel.key = ByteUtil.bytesToHexString(HandShakeKey2);
        } else {
            for (DeviceModel model : connectDevices) {
                if (model.mac.equals(device.getAddress())) {
                    deviceModel = model;
                }
            }
        }
        return deviceModel;
    }

    public native byte[] aesEncrypt(byte[] data, byte[] key);
    public native byte[] aesDecrypt(byte[] data, byte[] key);
}
