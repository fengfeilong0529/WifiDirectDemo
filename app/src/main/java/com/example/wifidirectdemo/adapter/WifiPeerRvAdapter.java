package com.example.wifidirectdemo.adapter;

import android.net.wifi.p2p.WifiP2pDevice;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wifidirectdemo.R;

public class WifiPeerRvAdapter extends BaseQuickAdapter<WifiP2pDevice, BaseViewHolder> {
    private static final String TAG = "WifiPeerRvAdapter";

    public WifiPeerRvAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, WifiP2pDevice item) {
        helper.setText(R.id.tvDeviceName, item.deviceName)
                .setText(R.id.tvDeviceAddress, item.deviceAddress);
    }
}
