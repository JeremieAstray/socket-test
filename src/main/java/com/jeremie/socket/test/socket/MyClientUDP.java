/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jeremie.socket.test.socket;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class MyClientUDP {

    public static void main(String[] args) {
        try {
            //必须调用无参的构造方法，产生本地的socket
            DatagramSocket ds = new DatagramSocket();

            //发送的目标IP、端口及内容由dp负责
            InetSocketAddress sa = new InetSocketAddress("127.0.0.1", 3344);
            String inputString;
            BufferedReader dis = new BufferedReader(new InputStreamReader(System.in));
            
            //接收缓冲
            byte[] buffer = new byte[1024];
            while (true) {
                inputString = dis.readLine();
                DatagramPacket dp = new DatagramPacket(inputString.getBytes(), inputString.length(), sa);
                ds.send(dp);

                DatagramPacket dpReceive = new DatagramPacket(buffer, buffer.length);
                ds.receive(dpReceive);
                String receiveString = new String(dpReceive.getData(),0,dpReceive.getLength());
                System.out.println(receiveString);
                
            }

        } catch (SocketException ex) {
            Logger.getLogger(MyClientUDP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MyClientUDP.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
