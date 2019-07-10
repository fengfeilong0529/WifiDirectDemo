package com.example.wifidirectdemo;

import android.os.SystemClock;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by Administrator on 2017-06-09.
 */
public class UdpServer extends IThread {
    public static final int UDP_PORT = 6560;
    private UdpListener listener;
    private final int port;
    DatagramSocket socket = null;
    private boolean isRunning;

    public UdpServer(int port, UdpListener listener) {
        this.port = port;
        this.listener = listener;
    }

    @Override
    public void IRun() {
        synchronized (this) {
            if (isRunning) {
                return;
            }
            isRunning = true;
            byte[] buf = new byte[1024];    //存储发来的消息
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket = new DatagramSocket(port);
                while (isRunning) {
                    try {
                        System.out.println("监听广播端口打开：");
                        socket.receive(packet);
                        String recvStr = new String(packet.getData(), 0, packet.getLength());
                        listener.onData(recvStr);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    SystemClock.sleep(50);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    socket.close();
                }
            }
            isRunning = false;
        }
    }

    public void close() {
        isRunning = false;
        if (socket != null) {
            socket.close();
        }
    }

    public interface UdpListener {
        void onData(String data);
    }
}
