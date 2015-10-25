package com.jeremie.nio;

import java.net.InetAddress;

/**
 * @author guanhong 15/10/20 下午4:51.
 */
public class WhoAmI {
    public static void main(String[] args)
            throws Exception {
        InetAddress a = InetAddress.getLocalHost();
        System.out.println(a);
    }
}
