package com.yuenkeji.bluethooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/1.
 */
public class MyAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<BluetoothDevice> deviceList;

    public MyAdapter(Context context, ArrayList<BluetoothDevice> deviceList) {
        this.context = context;
        this.deviceList = deviceList;
    }

    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return deviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.lv_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BluetoothDevice bluetoothDevice = deviceList.get(position);
        Log.d("MyAdapter", bluetoothDevice.getName()+"***");
        viewHolder.tvname.setText(bluetoothDevice.getName());
        viewHolder.tvaddress.setText(bluetoothDevice.getAddress());
        return convertView;
    }

public void setData(ArrayList<BluetoothDevice> deviceList){

    this.deviceList = deviceList;
}
    public class ViewHolder {
        public final TextView tvname;
        public final TextView tvaddress;
        public final View root;

        public ViewHolder(View root) {
            this.root = root;
            tvname = (TextView) root.findViewById(R.id.tv_name);
            tvaddress = (TextView) root.findViewById(R.id.tv_address);
        }
    }
}

