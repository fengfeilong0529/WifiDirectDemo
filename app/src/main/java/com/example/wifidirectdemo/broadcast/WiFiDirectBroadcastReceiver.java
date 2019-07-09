package com.example.wifidirectdemo.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.util.Log;

import com.example.wifidirectdemo.DeviceBean;
import com.example.wifidirectdemo.MainActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "WiFiDirectBroadcastRece";

    private WifiP2pManager manager;
    private Channel channel;
    private MainActivity activity;

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel, MainActivity activity) {
        super();
        this.manager = manager;
        this.channel = channel;
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: 收到广播：" + intent.getAction());
        final String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            //检测 WIFI 功能是否被打开
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                //支持WiFi直连

            } else {
                //不支持WiFi直连
                activity.showToast("不支持WiFi直连");
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            //获取当前可用连接点的列表
            if (manager != null) {
                manager.requestPeers(channel, new WifiP2pManager.PeerListListener() {
                    @Override
                    public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
                        Collection<WifiP2pDevice> deviceList = wifiP2pDeviceList.getDeviceList();
                        Log.i(TAG, "deviceList: " + deviceList.size());
                        List<DeviceBean> list =new ArrayList<>();
                        for (WifiP2pDevice wifiP2pDevice : deviceList) {
                            Log.i(TAG, "deviceAddress: " + wifiP2pDevice.deviceAddress + "--name--" + wifiP2pDevice.deviceName);
                            list.add(new DeviceBean(wifiP2pDevice.deviceName,wifiP2pDevice.deviceAddress));
                        }
                        activity.refreshList(list);
                    }
                });
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            //建立或者断开连接

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            //当前设备的 WIFI 状态发生变化

        }
    }
}