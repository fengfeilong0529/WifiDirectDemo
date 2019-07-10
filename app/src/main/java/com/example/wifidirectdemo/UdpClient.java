package com.example.wifidirectdemo;


import android.util.Log;

public class UdpClient {
    private static final String TAG = "UdpClient";
    public static final int UDP_PORT = 6560;

    public static void sendData(final String serverIp, final String data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UdpSocket socket = new UdpSocket(serverIp, UDP_PORT, -1, 2000);
                boolean result = socket.sendData(data.getBytes());
                Log.i("ffl", "发送结果" + result + " " + serverIp + " " + UDP_PORT + " " + data);
            }
        }).start();
    }
}
