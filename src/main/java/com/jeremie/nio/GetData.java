package com.jeremie.nio;

import java.nio.ByteBuffer;


/**
 * @author guanhong 15/10/20 下午4:23.
 */
public class GetData {
    public static void main(String[] args){
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1000);
        int i = 0;
        while (i++ < byteBuffer.limit()){
            if (byteBuffer.get()!=0)
                System.out.println("nonzero");
        }
        System.out.println(" i = " + i);
        byteBuffer.rewind();
        byteBuffer.asLongBuffer().put(123456789555555l);
        System.out.println(byteBuffer.getLong());
        byteBuffer.rewind();

    }
}
