package com.jeremie.socket.test.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Jeremie on 2015/5/15.
 */
public class NioServer {
    public static void main(String[] args) {
        ServerSocketChannel serverSocketChannel;
        Selector selector = null;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            ServerSocket serverSocket = serverSocketChannel.socket();
            serverSocket.bind(new InetSocketAddress(8000));
            serverSocketChannel.configureBlocking(false);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                if (selector == null)
                    break;
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                try {
                    if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        System.out.println("Accepted connection from " + client);
                        client.configureBlocking(false);
                        SelectionKey key2 = client.register(selector, SelectionKey.OP_WRITE);
                        ByteBuffer buffer = ByteBuffer.wrap("helloworld".getBytes());
                        buffer.flip();
                        key2.attach(buffer);
                    } else if (key.isWritable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        if (!buffer.hasRemaining()) {
                            buffer.rewind();
                            int first = buffer.get();
                            buffer.rewind();
                            int position = first - ' ' + 1;
                            buffer.put("helloworld".getBytes(),position,10);
                            buffer.put((byte)'\r');
                            buffer.put((byte)'\n');
                            buffer.flip();
                        }
                        client.write(buffer);
                    }
                } catch (IOException e) {
                    key.cancel();
                    try {
                        key.channel().close();
                    }catch (IOException e2){
                    }
                }
            }

        }

    }
}
