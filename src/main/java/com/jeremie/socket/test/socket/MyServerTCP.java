/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jeremie.socket.test.socket;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <h>流程：  </h>
 * <p>创建服务器socket，需要在客户端连接前创建</p>
 * <p>接收客户端的连接，新建线程处理该客户端</p>
 * @author Administrator
 */
public class MyServerTCP {
    
    public static void main(String[] args){
        ServerSocket socket = null;
        try {
            //创建服务器socket
            socket = new ServerSocket(8000);
            Socket mes = null;
            while(true){
                //接收客户端的连接，新建线程处理该客户端
                mes = socket.accept();
                new Thread(new ServerRunnable(mes)).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(MyServerTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

    class ServerRunnable implements Runnable{
        Socket userSocket = null;
        public ServerRunnable() {
        }
        public ServerRunnable(Socket userSocket){
            this.userSocket = userSocket;
        }
        @Override
        public void run() {
            try {
                ObjectInputStream ois = new ObjectInputStream(userSocket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(userSocket.getOutputStream());
                while(true){
                    String msg = ois.readUTF();
                    oos.writeUTF("Server return:"+msg);
                    oos.flush();
                    if(msg==null||msg.equals("END"))
                        break ;
                }
                ois.close();
                oos.close();
                userSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(MyServerTCP.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }