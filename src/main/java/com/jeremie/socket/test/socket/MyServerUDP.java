/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jeremie.socket.test.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class MyServerUDP {

    static byte[] buffer = new byte[1024];
    static InetSocketAddress socketAddress;
    static DatagramSocket ds;
    static DatagramPacket packet;

    public static void main(String[] args) {
        try {
            //绑定端口，建立连接
            socketAddress = new InetSocketAddress("127.0.0.1", 3344);
            ds = new DatagramSocket(socketAddress);

            //建立报表数据缓冲，等待接收报表
            packet = new DatagramPacket(buffer, buffer.length);
//            packet.setSocketAddress(socketAddress);
//                //输出主机名IP及端口
//                System.out.println("Host: " + packet.getAddress().getHostName() + packet.getAddress().getHostAddress() 
//                        +  "Port: " + packet.getPort());

            while (true) {
                //怎么获取请求的地址端口,接收请求后，packet中保存有请求IP及端口
                ds.receive(packet);

                //输出请求的主机名IP及端口
                System.out.println("Host: " + packet.getAddress().getHostName() + packet.getAddress().getHostAddress()
                        + "Port: " + packet.getPort());

                //获取报表内容
                String in = new String(packet.getData(), 0, packet.getLength());
                System.out.println("server receive:" + in);

                /**
                 * 多线程思路 克隆接收报表放到一个线程处理 每接收一个就回送一个报表
                 */
                byte[] outputBuffer = new byte[1024];
                String outputInf = "server had received";
                //error!!!
                DatagramPacket dp = new DatagramPacket(outputBuffer, outputBuffer.length, packet.getSocketAddress());
                dp.setData(outputInf.getBytes("UTF-8"));
                ds.send(dp);
            }
        } catch (SocketException ex) {
            Logger.getLogger(MyServerUDP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MyServerUDP.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
