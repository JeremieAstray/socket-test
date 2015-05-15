package com.jeremie.socket.test.socket;

import java.io.*;
import java.net.Socket;

/**
 * Created by Jeremie on 2015/5/13.
 */
public class Client {
    public static void main(String[] args) {
        Socket socket = null;
        Reader reader = null;
        BufferedReader brConsle = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            socket = new Socket("127.0.0.1", 8000);
//            socket.getChannel().configureBlocking(false);
            reader = new InputStreamReader(System.in);
            brConsle = new BufferedReader(reader);
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            final ObjectInputStream finalOis = ois;
            Thread thread = new Thread(() -> {
                try {
                    while (true) {
                        String msg = finalOis.readUTF();
                        System.out.println(msg);
                        if ("receive:END".equals(msg))
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
            while (true) {
                String message = brConsle.readLine();
                oos.writeUTF(message);
                oos.flush();
                if ("END".equals(message))
                    break;
            }
            thread.interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.flush();
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (ois != null)
                    ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (brConsle != null)
                    brConsle.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
