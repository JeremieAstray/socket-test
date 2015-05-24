/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jeremie.socket.test.socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <h>流程： </h>
 * <p>
 * 根据服务器ip和端口创建socket</p>
 * <p>
 * while循环将数据发送到服务器</p>
 * <p>
 * 新建线程从服务器接收数据</p>
 * <h>注意事项</h>
 * <p>
 * 每次输出后要执行刷出</p>
 * <p>
 * 中文乱码，可能是个人的机子问题，连控制台输入都乱码</p>
 * @author Administrator
 */
public class MyClientTCP {

    public static void main(String[] args) {
        try {
            //服务器开启后，根据服务器ip和端口创建socket，此时已与服务器连接
            Socket socket = new Socket("127.0.0.1", 8000);
            //接收键盘输入，发送到服务器的信息
            BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
            //获取服务器输入输出流
            ObjectOutputStream bfo = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream bfi = new ObjectInputStream(socket.getInputStream());
            //新建线程接收服务器的数据
            new Thread(new Runnable() {
                public void run() {
                    try {
                        while (true) {
                            String msg = bfi.readUTF();
                            //测试从服务端接收的中文是否乱码，结果是
//                            BufferedOutputStream fileOutputStream = new BufferedOutputStream(new FileOutputStream("output.txt"));
//                            fileOutputStream.write(msg.getBytes());
//                            fileOutputStream.close();
//                            System.out.println(msg);
                            System.out.println(msg);
                            if (msg == null || "Server return:END".equals(msg)) {
                                bfi.close();
                                bfo.close();
                                socket.close();
                                System.exit(0);
                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(MyClientTCP.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            ).start();
            //向服务器发送数据，注意要执行flush()刷出
            while (true) {
                String msg = new String(bfr.readLine());
                            //测试控制台输入是否乱码，结果是
//                            BufferedOutputStream fileOutputStream = new BufferedOutputStream(new FileOutputStream("output.txt"));
//                            fileOutputStream.write(msg.getBytes());
//                            fileOutputStream.close();
//                            System.out.println(msg);

                System.out.println("clint:" + msg);
                bfo.writeUTF(msg);
                bfo.flush();
            }
        } catch (IOException ex) {
            Logger.getLogger(MyClientTCP.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
