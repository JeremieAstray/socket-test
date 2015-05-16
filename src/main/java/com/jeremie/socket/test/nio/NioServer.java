package com.jeremie.socket.test.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
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
            Set readyKeys = selector.selectedKeys();

        }

    }
}
