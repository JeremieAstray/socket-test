package com.jeremie.socket.test.nio;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeremie.socket.test.VO.MessageVO;
import jdk.nashorn.internal.ir.debug.JSONWriter;

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
public class MyNioServer {
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
                        MessageVO messageVO = new MessageVO();
                        messageVO.setCreateTime(System.currentTimeMillis());
                        messageVO.setMessage("helloworld");
                        key2.attach(messageVO);
                    } else if (key.isWritable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        MessageVO messageVO  = (MessageVO)key.attachment();
                        ByteBuffer byteBuffer = ByteBuffer.wrap(JSON.toJSONString(messageVO).getBytes());
                        client.write(byteBuffer);
                        client.close();
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
