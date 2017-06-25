package com.example.bluetooth.le;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
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
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.lock.lib.api.Server;
import com.lock.lib.api.event.RequestEvent;
import com.lock.lib.api.event.ResponseEvent;
import com.lock.lib.common.constants.Constants;
import com.lock.lib.common.util.ByteUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

/**
 * Created by oriolexia on 17/6/25.
 */

public class BleConnectUtil {

    public static String TAG = "BleConnectUtil";
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
    public int status_unlock_success = 2;


    String HandShackKey_String = "ea8b2a7316e9b04945b339280ac3283c";

    byte[] HandShakeKey2;

    char[] AES_Key_Table = new char[]{};

    /**搜索BLE终端*/
    private BluetoothAdapter mBluetoothAdapter;
    /**读写BLE终端*/
    private BluetoothLeClass mBLE;
    private boolean mScanning;
    private Handler mHandler;
    BluetoothDevice mDevice ;

    private String mMac;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 15000;
    private static Context mContext;

    private List<DeviceModel> connectDevices = null;

    static {
        System.loadLibrary("native_le-lib");
    }

    public BleConnectUtil(Context context, BluetoothLeClass.OnServiceDiscoverListener discoverListener, BluetoothLeClass.OnDataAvailableListener dataAvailableListener){
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
            return;
        }
        //发现BLE终端的Service时回调
        mBLE.setOnServiceDiscoverListener(mOnServiceDiscover);
        //收到BLE终端数据交互的事件
        mBLE.setOnDataAvailableListener(mOnDataAvailable);


        mHandler = new Handler();
    }

    public void connectDevice(String mac){
        Log.e(TAG, "connectDevice mac " + mac);
        if (mBLE == null){
            return;
        }
        mMac = mac;

        mScanner = mBluetoothAdapter.getBluetoothLeScanner();
        scanLeDevice(true);

    }

    public void disconnectDevice(){
        scanLeDevice(false);
        mBLE.disconnect();
        mBLE.close();
        mBLE = null;
    }

    public void displayGattServices(List<BluetoothGattService> gattServices) {
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
        byte[] result = aesEncrypt(bytes, tempByte);
        Log.e(TAG, "--> 16 byte aesEncrypt data " + ByteUtil.bytesToHexString(result));
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

//                byte[] data = gattCharacteristic.getValue();
//                if (data != null && data.length > 0) {
//                    Log.e(TAG,"---->char value:"+new String(data));
//                }

                //UUID_KEY_DATA是可以跟蓝牙模块串口通信的Characteristic
                if(gattCharacteristic.getUuid().toString().equals(UUID_KEY_DATA_CHARACTERISTIC_A)){
                    mCharacteristicAa = gattCharacteristic;
                }else if (gattCharacteristic.getUuid().toString().equals(UUID_KEY_DATA_CHARACTERISTIC_C)){
                    mCharacteristicAc = gattCharacteristic;
                    mBLE.setCharacteristicNotification(mCharacteristicAc, true);
                }else if (gattCharacteristic.getUuid().toString().equals(UUID_KEY_DATA_CHARACTERISTIC_B)){
                    mCharacteristicAb = gattCharacteristic;
                }

            }
        }//

        //UUID_KEY_DATA是可以跟蓝牙模块串口通信的Characteristic
        if(mCharacteristicAa != null) {
            if (current_status == status_activation){
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //设置数据内容
                        Log.e(TAG, "---->will write data  result3 " + ByteUtil.bytesToHexString(result3));
                        mCharacteristicAa.setValue(result3);
                        //往蓝牙模块写入数据
                        mBLE.writeCharacteristic(mCharacteristicAa);
                    }
                }, 5000);
            }
        }

    }


    /**
     * 搜索到BLE终端服务的事件
     */
    private BluetoothLeClass.OnServiceDiscoverListener mOnServiceDiscover = new BluetoothLeClass.OnServiceDiscoverListener(){

        @Override
        public void onServiceDiscover(BluetoothGatt gatt) {
            displayGattServices(mBLE.getSupportedGattServices());
        }

    };

    /**
     * 收到BLE终端数据交互的事件
     */
    private BluetoothLeClass.OnDataAvailableListener mOnDataAvailable = new BluetoothLeClass.OnDataAvailableListener(){

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
                    +characteristic.getUuid().toString());
            if (current_status == status_activation){
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        byte[] resultByte = characteristic.getValue();
                        if (resultByte.length == 20){
                            String key2Temp = ByteUtil.bytesToHexString(resultByte);
                            byte[] key2TempByte = ByteUtil.hexStringToBytes(key2Temp.substring(0, 32));
                            byte[] tempKeyByte = ByteUtil.hexStringToBytes(HandShackKey_String);
                            HandShakeKey2 = resultByte = aesDecrypt(key2TempByte, tempKeyByte);


                            String mac = mDevice.getAddress();
                            Log.e(TAG, "--------> Device Mac Address " + mac);

                            mac = mac.replace(":", "");
                            StringBuffer stringBufferMac = new StringBuffer(mac);
                            String temp = ByteUtil.getStringRandom(20);
                            temp = temp + stringBufferMac;
                            Log.e(TAG, "-------> 16 byte data " + temp);
                            Log.e(TAG, "-------> 16 byte key " + key2Temp);
                            byte[] bytes = ByteUtil.hexStringToBytes(temp);
                            byte[] result = aesEncrypt(bytes, resultByte);
                            Log.e(TAG, "-------> 16 byte aesEncrypt data " + ByteUtil.bytesToHexString(result));
                            byte[] result2 = new byte[4];
                            final byte[] result3 = ByteUtil.addBytes(result, result2);

                            mCharacteristicAa.setValue(result3);
                            //往蓝牙模块写入数据
                            mBLE.writeCharacteristic(mCharacteristicAa);
                        }else if (resultByte.length == 2){
                            String key2Temp = ByteUtil.bytesToHexString(resultByte);
                            if ("FF01".equals(key2Temp.toUpperCase())){
                                Log.e(TAG, "-------> activation device success ");
                                DeviceModel model = new DeviceModel();
                                model.name = mDevice.getName();
                                model.mac = mDevice.getAddress();
                                model.key = ByteUtil.bytesToHexString(HandShakeKey2);
                                DeviceShare.saveDevice(mContext, model);
                                current_status = status_unlock;
                                postResponseEvent(ResponseEvent.TYPE_ACTIVITY_DEVICE, Server.Code.SUCCESS, "", getShareDeviceModel(mDevice));
                            }else{
                                postResponseEvent(ResponseEvent.TYPE_ACTIVITY_DEVICE, Server.Code.FAIL, "Activation device failed", getShareDeviceModel(mDevice));
                            }
                        }

                    }
                }, 2000);

            }else if (current_status == status_unlock){
                byte[] temp = characteristic.getValue();
                if (temp.length == 16){
                    byte[] tempKeyByte = ByteUtil.hexStringToBytes(HandShackKey_String);
                    byte[] decTemp = aesDecrypt(temp, tempKeyByte);
                    byte[] aesTemp = aesEncrypt(decTemp, HandShakeKey2);
                    String decTempStr = ByteUtil.bytesToHexString(aesTemp);

                    decTempStr = decTempStr + "00000000";
                    final byte[] encrypData = ByteUtil.hexStringToBytes(decTempStr);

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mCharacteristicAb.setValue(encrypData);
                            //往蓝牙模块写入数据
                            mBLE.writeCharacteristic(mCharacteristicAb);
                        }
                    }, 3000);
                }else{
                    String key2Temp = ByteUtil.bytesToHexString(temp);

                    Log.e(TAG, "-------> unlock device result  " + key2Temp);
                    if ("FF02".equals(key2Temp.toUpperCase())){
                        current_status = status_unlock_success;
                        Log.e(TAG, "-------> unlock device success  ");

                        postResponseEvent(ResponseEvent.TYPE_UNLOCK_DEVICE, Server.Code.SUCCESS, "", getShareDeviceModel(mDevice));
                    }else {
                        postResponseEvent(ResponseEvent.TYPE_UNLOCK_DEVICE, Server.Code.FAIL, "Unlock device failed", getShareDeviceModel(mDevice));
                    }
                }
            }
        }
    };


    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mScanner.stopScan(mScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mScanner.startScan(mScanCallback);
        } else {
            mScanning = false;
            mScanner.stopScan(mScanCallback);
        }
    }

    ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, final ScanResult result) {
            super.onScanResult(callbackType, result);
            final BluetoothDevice device = result.getDevice();
            Log.e(TAG, "--------> onScanResult Device Mac Address " + device.getAddress() + ", name " + device.getName());
            if ("SC".equals(device.getName())){
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                            SparseArray<byte[]> manufacturerSpecificData = result.getScanRecord().getManufacturerSpecificData();

                            byte[] b = manufacturerSpecificData.valueAt(manufacturerSpecificData.size() - 1);
                            String str = new String(ByteUtil.bytesToHexString(b));

                            if (str.endsWith("00")){
                                current_status = 0;
                            }else{
                                current_status = 1;
                            }

                            if (TextUtils.isEmpty(mMac)){
                                // 激活
                                if (connectDevices.size() == 0){
                                    scanLeDevice(false);
                                    mDevice = device;
                                    mBLE.connect(mDevice.getAddress());
                                }else{
                                    for (int i = 0, j= connectDevices.size(); i<j; i++){
                                        DeviceModel deviceModel = connectDevices.get(i);
                                        if (!device.getAddress().equals(deviceModel.mac)){
                                            scanLeDevice(false);
                                            mDevice = device;
                                            mBLE.connect(mDevice.getAddress());

                                        }
                                    }
                                }

                            }else {
                                if (device.getAddress().equals(mMac)) {
                                    scanLeDevice(false);
                                    if (connectDevices.size() > 0){
                                        for (int i = 0, j= connectDevices.size(); i<j; i++){
                                            DeviceModel deviceModel = connectDevices.get(i);
                                            if (device.getAddress().equals(deviceModel.mac)){
                                                HandShakeKey2 = ByteUtil.hexStringToBytes(deviceModel.key);
                                            }
                                        }
                                    }

                                    mDevice = device;
                                    mBLE.connect(mDevice.getAddress());

                                }
                            }

                        }

                }, 100);
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


    private void postResponseEvent(int eventType, int errorCode, String errorMsg, DeviceModel deviceModel) {
        ResponseEvent event = new ResponseEvent();
        event.eventType = eventType;
        event.errorCode = errorCode;
        event.errorMsg = errorMsg;
        event.resultObj = deviceModel;
        EventBus.getDefault().post(event);
    }

    public DeviceModel getShareDeviceModel(BluetoothDevice device){

        DeviceModel deviceModel = null;

        if (connectDevices == null || connectDevices.size() == 0){
            deviceModel.mac = device.getAddress();
            deviceModel.name = device.getName();
            deviceModel.key = ByteUtil.bytesToHexString(HandShakeKey2);
        }else{
            for (DeviceModel model : connectDevices){
                if (model.mac.equals(device.getAddress())){
                    deviceModel = model;
                }
            }
        }

        return deviceModel;
    }

    public native byte[] aesEncrypt(byte[] data, byte[] key);
    public native byte[] aesDecrypt(byte[] data, byte[] key);


}
