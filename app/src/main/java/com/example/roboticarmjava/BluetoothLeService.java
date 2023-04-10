package com.example.roboticarmjava;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

//johnny imports
import android.app.Activity;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;


public class BluetoothLeService extends GATT_UUIDS {
    private int REQUEST_ENABLE_BT = 1;
    //A static object to record current connection status.(Also used in MainActivity)
    static public boolean connected = false;
    static public boolean calibrated = false;
    static public BluetoothLeService bluetoothLeService;
    public BluetoothGatt mBluetoothGatt;
    private BluetoothAdapter mBluetoothAdapter;
    private Context context;

    private boolean mScanning;
    Handler mHandler;

    private List<BluetoothDevice> mBluetoothDevices = new ArrayList<>();
    List<String> mBluetoothDeviceList = new ArrayList<>();
    private ArrayAdapter<String> mBluetoothListAdapter;
    private BluetoothLeScanner scanner;

    private boolean firstRead = true;
    private boolean initializing = true;
    int MTU = 0;
    public static int deviceInfo = -1;

    public final static String ACTION_GATT_CONNECTED = "ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SCAN_COMPLETE = "ACTION_SCAN_COMPLETE";
    public final static String ACTION_ANGLES_READ = "ACTION_ANGLES_READ";
    public final static String ACTION_TEMP_UPDATE = "ACTION_TEMP_UPDATE";
    public final static String ACTION_TEMPRAW_UPDATE = "ACTION_TEMPRAW_UPDATE";
    public final static String ACTION_FORCE_UPDATE = "ACTION_FORCE_UPDATE";
    public final static String ACTION_DATA_DOWNLOAD = "ACTION_DATA_DOWNLOAD";
    public final static String ACTION_DATA_ERASE = "ACTION_DATA_ERASE";
    public final static String ACTION_RESTARTAPP = "ACTION_RESTARTAPP";

    @SuppressLint("MissingPermission")
    public BluetoothLeService(Context context) {
        mBluetoothListAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, mBluetoothDeviceList);
        this.context = context;
        BluetoothManager bluetoothManClass = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);

        // if adpter is null then quit
        mBluetoothAdapter = bluetoothManClass.getAdapter();
        if (mBluetoothAdapter==null){
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("No bluetooth in this device");
            builder.setMessage("There is no bluetooth function in this device");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    System.exit(0);
                }
            });
            builder.show();
        }
        scanner = mBluetoothAdapter.getBluetoothLeScanner();
        mBluetoothAdapter.enable();
        mHandler = new Handler();
        initBluetooth();
    }

    @SuppressLint("MissingPermission")
    private boolean initBluetooth() {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ((Activity) context).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return false;
        }
        return true;
    }

    @SuppressLint("MissingPermission")
    public void scanBluetooth(boolean enable) {
        final long SCAN_PERIOD = 8000;
        if (enable) {
            mBluetoothDeviceList.clear();
            mBluetoothDevices.clear();
            if (mBluetoothGatt != null){
                mBluetoothGatt.close();
            }
            mBluetoothListAdapter.notifyDataSetChanged();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    scanner.stopScan(scan_callback);
                    broadcastUpdate(ACTION_GATT_SCAN_COMPLETE, "");
                }
            }, SCAN_PERIOD);
            mScanning = true;
            scanner.startScan(scan_callback);
        } else {
            mBluetoothDeviceList.clear();
            mBluetoothDevices.clear();
            mBluetoothListAdapter.notifyDataSetChanged();
            mScanning = false;
            scanner.stopScan(scan_callback);
        }
    }

    private ScanCallback scan_callback = new ScanCallback() {
        @SuppressLint("MissingPermission")
        public void onScanResult(int callbackType, final ScanResult result) {
            if (!mScanning) {
                return;
            }
            try{
                Log.e("Found device", result.getDevice().getName());
                if (result.getDevice().getName()!=null) {
                    int rssi = result.getRssi();
                    if (mBluetoothDevices.contains(result.getDevice())){
                        int index = mBluetoothDevices.indexOf(result.getDevice());
                        if (index < mBluetoothDeviceList.size() && index >= 0){
                            mBluetoothDeviceList.set(index,result.getDevice().getName()+"        " +rssi+"dbm");
                            mBluetoothListAdapter.notifyDataSetChanged();
                        }
                    }else{
                        mBluetoothDeviceList.add(result.getDevice().getName()+"        " +rssi+"dbm");
                        mBluetoothDevices.add(result.getDevice());
                        mBluetoothListAdapter.notifyDataSetChanged();
                    }
                }
            }
            catch (NullPointerException e){

            }
        }
    };
    BluetoothGattCallback mGattCallback =  new BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            gatt.discoverServices();
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            List<BluetoothGattService> servicesList = gatt.getServices();
            for (BluetoothGattService service: servicesList){
                Log.v("service",String.valueOf(service.getUuid()));
                for (BluetoothGattCharacteristic characteristic:service.getCharacteristics()){
                    Log.v("characteristic",String.valueOf(characteristic.getUuid()));
                }
            }
        }

        @Override
        public void onCharacteristicRead(@NonNull BluetoothGatt gatt, @NonNull BluetoothGattCharacteristic characteristic, @NonNull byte[] value, int status) {
            super.onCharacteristicRead(gatt, characteristic, value, status);
            if (characteristic.getUuid().equals(UUID_tx_char)){
                byte[] emptyArray = new byte[]{-1,-1,-1,-1,-1,-1};
                byte[] rollDataB = new byte[]{characteristic.getValue()[0],characteristic.getValue()[1]};
                float rollData =  convertADC(rollDataB);
                byte[] pitchDataB = new byte[]{characteristic.getValue()[2],characteristic.getValue()[3]};
                float pitchData =  convertADC(pitchDataB);
                byte[] yawDataB = new byte[]{characteristic.getValue()[4],characteristic.getValue()[5]};
                float yawData =  convertADC(yawDataB);
                float[] arr = new float[]{pitchData, rollData, yawData};
                broadcastUpdate(ACTION_ANGLES_READ, arr);

            }
        }

    };
    @SuppressLint("MissingPermission")
    public void initConnection(int deviceNum) {
        scanner.stopScan(scan_callback);
        mScanning = false;
        initializing = true;
        BluetoothDevice device = mBluetoothDevices.get(deviceNum);
        if (initBluetooth()) {
            if (mBluetoothGatt == null) {
                mBluetoothGatt = device.connectGatt(context, false,
                        mGattCallback);

            }
            else{
                terminateConnection();
                initBluetooth();
                mBluetoothGatt = device.connectGatt(context, false, mGattCallback);
            }
        }
    }

    public float convertADC(byte[] adcRawVal) {
        int digits = adcRawVal[0];
        if (digits < 0) {
            digits += 256;
        }
        int tens = adcRawVal[1] < 0?adcRawVal[1]+256:adcRawVal[1];
        float rawVal = digits + tens/100;
        return rawVal;
    }
    @SuppressLint("MissingPermission")
    public void terminateConnection() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
            connected = false;
            calibrated = false;
            mBluetoothGatt.close();
        }
    }
    @SuppressLint("MissingPermission")
    public void sendWriteRequest(byte[] arr){
        if (mBluetoothGatt != null) {
            BluetoothGattCharacteristic characteristic =
                    mBluetoothGatt.getService(UUID_IDENTIFIER_SERV).getCharacteristic(UUID_rx_char);
            if (characteristic != null) {
                try{
                    characteristic.setValue(arr);

                    mBluetoothGatt.writeCharacteristic(characteristic);
                }
                catch (Exception e) {

                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void sendReadRequest(){
        if (mBluetoothGatt != null) {

            BluetoothGattCharacteristic characteristic =
                    mBluetoothGatt.getService(UUID_IDENTIFIER_SERV).getCharacteristic(UUID_tx_char);
            mBluetoothGatt.readCharacteristic(characteristic);

        }
    }

    private void broadcastUpdate(final String action, String data) {
        final Intent intent = new Intent(action);
        intent.putExtra(action, data);
        context.sendBroadcast(intent);
    }
    private void broadcastUpdate(final String action, float data) {
        final Intent intent = new Intent(action);
        intent.putExtra(action, data);
        context.sendBroadcast(intent);
    }
    private void broadcastUpdate(final String action, float[] data) {
        final Intent intent = new Intent(action);
        intent.putExtra(action, data);
        context.sendBroadcast(intent);
    }
    public ArrayAdapter<String> getmBluetoothListAdapter() {
        return mBluetoothListAdapter;
    }

}
