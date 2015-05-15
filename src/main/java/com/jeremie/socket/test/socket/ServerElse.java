package com.jeremie.socket.test.socket;

import javafx.util.Callback;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jeremie on 2015/5/13.
 */
public class ServerElse {

    private static Map<String, ServerThread> onlineUsers;



    public static void main(String[] args) {
        onlineUsers = new HashMap<>();
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(8000);
            while (true) {
                Socket socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(socket, str -> {
                    for (Map.Entry<String, ServerThread> user : onlineUsers.entrySet()) {
                        PrintWriter bw = user.getValue().bw;
                        if (bw != null) {
                            bw.println(str);
                            bw.flush();
                        }
                    }
                    return true;
                });
                Thread thread = new Thread(serverThread);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null)
                    serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private static class ServerThread implements Runnable {
        private Socket socket;
        private String name = null;
        private Callback<String, Boolean> callback = null;
        public PrintWriter bw = null;
        public BufferedReader br = null;

        public ServerThread(Socket socket, Callback<String, Boolean> callback) {
            this.socket = socket;
            this.callback = callback;
        }

        @Override
        public void run() {
            try {
                bw = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(),"UTF-8"));
                br = new BufferedReader(new InputStreamReader(this.socket.getInputStream(),"UTF-8"));
                boolean firstTime = true;
                while (true) {
                    if (firstTime) {
                        firstTime = false;
                        name = br.readLine();
                        System.out.println("name=" + name);
                        onlineUsers.put(name, this);
                        continue;
                    }
                    String str = br.readLine();
                    System.out.println(name + ": " + str);
                    bw.println("server receive:" + str);
                    bw.flush();
                    if ("END".equals(str) || "null".equals(str)) break;
                    callback.call(name + ":" + str);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bw != null) {
                    bw.flush();
                    bw.close();
                }
                try {
                    if (br != null)
                        br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (!socket.isClosed())
                        socket.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    onlineUsers.remove(name);
                    System.out.println(name + socket.getInetAddress() + " close!");
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
