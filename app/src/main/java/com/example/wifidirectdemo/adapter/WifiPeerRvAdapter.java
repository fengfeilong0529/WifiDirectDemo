package com.example.wifidirectdemo.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wifidirectdemo.DeviceBean;
import com.example.wifidirectdemo.R;

public class WifiPeerRvAdapter extends BaseQuickAdapter<DeviceBean, BaseViewHolder> {
    private static final String TAG = "WifiPeerRvAdapter";

    public WifiPeerRvAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, DeviceBean item) {
        helper.setText(R.id.tvDeviceName, item.name)
                .setText(R.id.tvDeviceAddress, item.address);
    }
}
