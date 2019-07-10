package com.example.wifidirectdemo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by Administrator on 2017-06-09
 * Udp  发送
 */
public class UdpSocket {
    DatagramSocket dSocket = null;
    private String serverIP = "";
    private int serverPort = 0;
    private int LocalPort = 0;
    private int soTimeOut = 2000;

    public UdpSocket(String serverIP, int serverPort, int LocalPort, int soTimeOut) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.LocalPort = LocalPort;
        this.soTimeOut = soTimeOut;

        try {
            this.dSocket = LocalPort > 0 ? new DatagramSocket(LocalPort) : new DatagramSocket();
            this.dSocket.setSoTimeout(this.soTimeOut);
        } catch (SocketException var6) {
            var6.printStackTrace();
        }
    }

    //是否发送成功
    public boolean sendData(byte[] data) {
        boolean result;
        try {
            InetAddress serverAddress = InetAddress.getByName(this.serverIP);

            DatagramPacket datagramPacket = new DatagramPacket(data, data.length, serverAddress, this.serverPort);
            dSocket.send(datagramPacket);
            result = true;
        } catch (IOException var5) {
            var5.printStackTrace();
            result = false;
        }
        return result;
    }

    public void close() {
        if (!dSocket.isClosed())
            dSocket.close();
    }

    public static void receiver(int port, ParseCallBack callBack) {
        DatagramSocket ds = null;
        byte[] buf = new byte[1024];//存储发来的消息
        StringBuffer buffer = new StringBuffer();
        try {
            ds = new DatagramSocket(port);
            while (true) {
                try {
                    DatagramPacket dp = new DatagramPacket(buf, buf.length);
                    ds.receive(dp);
                    for (int i = 0; i < buf.length; i++) {
                        if (buf[i] == 0) {
                            break;
                        }
                        buffer.append((char) buf[i]);
                    }
                    String remoteip = dp.getAddress().getHostAddress();
                    callBack.parse(remoteip, buffer.toString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                buffer.delete(0, buffer.length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ds != null) {
                ds.close();
            }
        }
    }

    interface ParseCallBack {
        void parse(String ip, String data);
    }
}
