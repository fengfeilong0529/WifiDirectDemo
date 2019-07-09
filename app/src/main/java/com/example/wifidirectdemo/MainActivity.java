package com.example.wifidirectdemo;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.wifidirectdemo.adapter.WifiPeerRvAdapter;
import com.example.wifidirectdemo.broadcast.WiFiDirectBroadcastReceiver;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private WiFiDirectBroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter;
    private RecyclerView mRvDevices;
    private WifiPeerRvAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initView();
    }

    private void initView() {
        mRvDevices = (RecyclerView) findViewById(R.id.rvDevices);
        mRvDevices.setLayoutManager(new LinearLayoutManager(this));
        mRvDevices.setItemAnimator(new DefaultItemAnimator());
        mRvDevices.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter = new WifiPeerRvAdapter(R.layout.item_peer_device);
        mRvDevices.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                connectDevice(mAdapter.getData().get(position));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    public void init() {
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    /**
     * 开始扫描
     *
     * @param view
     */
    public void startScan(View view) {
        mManager.discoverPeers(mChannel, listener);
    }

    public void connectDevice(WifiP2pDevice device) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                //TODO 进行socket通讯

            }

            @Override
            public void onFailure(int i) {
                Toast.makeText(MainActivity.this, "连接失败，code：" + i, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void disconnect() {
        mManager.removeGroup(mChannel, listener);
    }

    WifiP2pManager.ActionListener listener = new WifiP2pManager.ActionListener() {
        @Override
        public void onSuccess() {
            Log.i(TAG, "onSuccess: ");
        }

        @Override
        public void onFailure(int i) {
            Log.i(TAG, "onFailure: " + i);

        }
    };

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void refreshList(List<WifiP2pDevice> list) {
        mAdapter.setNewData(list);
    }
}
