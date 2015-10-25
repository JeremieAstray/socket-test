package com.jeremie.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * @author guanhong 15/10/20 下午5:15.
 */
public class JabberClient1 {
    private static int clPrt = 8000;
    private static int sPort = 8001;

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        Selector selector = Selector.open();
        try {
            socketChannel.configureBlocking(false);
            socketChannel.socket().bind(new InetSocketAddress(clPrt));
            socketChannel.register(selector, SelectionKey.OP_READ |
                    SelectionKey.OP_WRITE | SelectionKey.OP_CONNECT);
            int i = 0;
            // Because of the asynchronous nature you do not know
            // when reading and writing is done, hence you need to
            // keep track of this, boolean written is used to
            // alternate between read and write. Whatever is written
            // is echoed and should be read.
            // boolean done is used to check when to break out of
            // the loop
            boolean written = false, done = false;
            //JabberServer.java to which this client connects writes
            // using BufferedWriter.println(). This method performs
            // encoding according to the defualt charset
            String encoding = System.getProperty("file.encoding");
            Charset cs = Charset.forName(encoding);
            ByteBuffer buf = ByteBuffer.allocate(16);
            InetAddress addr = InetAddress.getLocalHost();
            System.out.println(addr.toString());
            boolean success = socketChannel.connect(
                    new InetSocketAddress(addr, sPort));
            if (!success) socketChannel.finishConnect();
            while (!done) {
                selector.select();
                Iterator it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey key = (SelectionKey) it.next();
                    it.remove();
                    socketChannel = (SocketChannel) key.channel();
                    if (key.isConnectable() && !socketChannel.isConnected()) {

                    }
                    if (key.isReadable() && written) {
                        if (socketChannel.read((ByteBuffer) buf.clear()) > 0) {
                            written = false;
                            String response = cs.decode((ByteBuffer) buf.flip()).toString();
                            System.out.print(response);
                            if (response.indexOf("END") != -1)
                                done = true;
                        }
                    }
                    if (key.isWritable() && !written) {
                        if (i < 10) socketChannel.write(ByteBuffer.wrap(
                                ("howdy " + i + '\n').getBytes()));
                        else if (i == 10) socketChannel.write(ByteBuffer.wrap(
                                "END\n".getBytes()));
                        written = true;
                        i++;
                    }
                }
            }
        } finally {
            socketChannel.close();
            selector.close();
        }
    }
}
