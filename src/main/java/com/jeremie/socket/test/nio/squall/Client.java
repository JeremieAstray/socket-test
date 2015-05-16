package com.jeremie.socket.test.nio.squall;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jeremie on 15/5/16.
 */
public class Client {


    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 20; i++) {
            Student stu = new Student();
            stu.setId(i);
            stu.setName("Squall");
            service.execute(() -> {
                try {
                    write(stu);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        service.shutdown();
    }


    public static void write(Object obj) throws IOException {
        SocketChannel channel = null;
        try {
            channel = SocketChannel.open(new InetSocketAddress(
                    "127.0.0.1", 8000));
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();

            ObjectOutputStream out = new ObjectOutputStream(bOut);
            out.writeObject(obj);
            out.flush();
            byte[] arr = bOut.toByteArray();
            System.out.println("Object in " + arr.length + " bytes");
            out.writeObject(obj);
            out.flush();
            arr = bOut.toByteArray();
            System.out.println("Object1+2 in " + arr.length + " bytes");
            ByteBuffer bb = ByteBuffer.wrap(arr);
            out.close();
            //Socket socket = channel.socket();
            // SocketChannel sc = socket.getChannel();
            channel.write(bb);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.close();
        }
    }
}
