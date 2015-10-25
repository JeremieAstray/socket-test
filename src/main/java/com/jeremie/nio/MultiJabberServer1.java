package com.jeremie.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.concurrent.Executors;

/**
 * @author guanhong 15/10/20 下午5:19.
 */
public class MultiJabberServer1 {
    public static final int PORT = 8001;

    public static void main(String[] args) throws IOException {
        //Channel read from data will be in ByteBuffer form
        // written by PrintWriter.println(). Decoding of this
        // byte stream requires character set of default encoding.
        String encoding = System.getProperty("file.encoding");
        // Had to initialized here since we do not wish to create
        // a new instance of Charset everytime it is required
        //Charset cs = Charset.forName(
        // System.getProperty("file.encoding"));
        Charset cs = Charset.forName(encoding);
        ByteBuffer buffer = ByteBuffer.allocate(16);
        SocketChannel ch = null;
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        Selector selector = Selector.open();
        try {
            serverSocketChannel.configureBlocking(false);
            //Local address on which it will listen for connections
            // Note: Socket.getChannel() returns null unless a channel
            // is associated with it as shown below.
            //i.e the expression (ssc.socket().getChannel() != null) is true
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
            // Channel is interested in OP_ACCEPT events SelectionKey key =
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Server on port: " + PORT);
            while (true) {
                selector.select();
                Iterator it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey skey = (SelectionKey) it.next();
                    it.remove();
                    if (skey.isAcceptable()) {
                        ch = serverSocketChannel.accept();
                        System.out.println(
                                "Accepted connection from:" + ch.socket());
                        ch.configureBlocking(false);
                        ch.register(selector, SelectionKey.OP_READ);
                    } else {
                        // Note no check performed if the channel
                        // is writable or readable - to keep it simple
                        ch = (SocketChannel) skey.channel();
                        ch.read(buffer);
                        CharBuffer cb = cs.decode(
                                (ByteBuffer) buffer.flip());
                        String response = cb.toString();
                        System.out.print("Echoing : " + response);
                        ch.write((ByteBuffer) buffer.rewind());
                        if (response.indexOf("END") != -1)
                            ch.close();
                        buffer.clear();
                    }
                }
            }
        } finally {
            if (ch != null) ch.close();
            serverSocketChannel.close();
            selector.close();
        }
    }
}
