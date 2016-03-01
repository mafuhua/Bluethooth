package com.yuenkeji.bluethooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textView;
    private Button dakai;
    private Button guanbi;
    private Button saomiao;
    private Button tingzhi;
    private ListView listView;
    private BluetoothAdapter mBluetoothAdapter;
    private MyReciver myReciver;
    private MyAdapter myAdapter;
    private ArrayList<BluetoothDevice> deviceList = new ArrayList<>();

    private void assignViews() {
        textView = (TextView) findViewById(R.id.textView);
        dakai = (Button) findViewById(R.id.dakai);
        guanbi = (Button) findViewById(R.id.guanbi);
        saomiao = (Button) findViewById(R.id.saomiao);
        tingzhi = (Button) findViewById(R.id.tingzhi);
        listView = (ListView) findViewById(R.id.listView);
        dakai.setOnClickListener(this);
        guanbi.setOnClickListener(this);
        saomiao.setOnClickListener(this);
        tingzhi.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter intentFilter = new IntentFilter();
        //开始扫描广播
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        //扫描完成
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        //找到了用蓝牙的设备
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        myReciver = new MyReciver();
        registerReceiver(myReciver, intentFilter);
        assignViews();
        //获取蓝牙适配器
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice bluetoothDevice = deviceList.get(position);
                Log.d("MainActivity", "连接");
               // ParcelUuid[] uuids = bluetoothDevice.getUuids();
                Log.d("MainActivity",bluetoothDevice.getName() );
                connetBlueTooth(bluetoothDevice);
            }
        });

    }

    private void connetBlueTooth(final BluetoothDevice bluetoothDevice) {
        new Thread() {
            @Override
            public void run() {
                try {
                    BluetoothSocket bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                    bluetoothSocket.connect();
                    OutputStream outputStream = bluetoothSocket.getOutputStream();
                    Log.d("MainActivity", "连接成功"+outputStream);
                   /* Looper.prepare();
                    Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();

                    Looper.loop();*/
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dakai:
                if (mBluetoothAdapter.isEnabled()) {
                    return;
                } else {
                    //打开蓝牙
                    mBluetoothAdapter.enable();
                }
                break;
            case R.id.guanbi:
                if (!mBluetoothAdapter.isEnabled()) {
                    return;
                } else {
                    mBluetoothAdapter.disable();
                }
                break;
            case R.id.saomiao:
                mBluetoothAdapter.startDiscovery();//开始扫描
                break;
            case R.id.tingzhi:
                break;
        }

    }

    class MyReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(intent.getAction())) {
                Toast.makeText(MainActivity.this, "开始扫描", Toast.LENGTH_SHORT).show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction())) {
                Toast.makeText(MainActivity.this, "扫描完成", Toast.LENGTH_SHORT).show();
            } else if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
                //扫描到可用蓝牙
                //获取蓝牙设备
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (deviceList == null) deviceList = new ArrayList<>();
                deviceList.add(bluetoothDevice);
                if (myAdapter == null) {
                    myAdapter = new MyAdapter(MainActivity.this, deviceList);
                }
                if (listView.getAdapter() == null) {
                    listView.setAdapter(myAdapter);
                } else {
                    myAdapter.setData(deviceList);
                    myAdapter.notifyDataSetChanged();
                }
            }







               /* Object[] array = mBluetoothAdapter.getBondedDevices().toArray();
                for (int i = 0; i < array.length; i++) {

                    deviceList = new ArrayList<>();
                    BluetoothDevice device = (BluetoothDevice) array[i];
                    deviceList.add(device);
                    Log.d("MyReciver", device.getName());
                }
               *//* Object[] lstDevice = btAdapt.getBondedDevices().toArray();
                for (int i = 0; i < lstDevice.length; i++) {
                    BluetoothDevice device = (BluetoothDevice) lstDevice[i];
                    String str = "已配对|" + device.getName() + "|"
                            + device.getAddress();
                    lstDevices.add(str); // 获取设备名称和mac地址
                    adtDevices.notifyDataSetChanged();*//*
            }*/
            // deviceList.add(bluetoothDevice);


            myAdapter = new MyAdapter(MainActivity.this, deviceList);
            Log.d("MyReciver", "deviceList:" + deviceList.toString());
            listView.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();


        }
    }
}

