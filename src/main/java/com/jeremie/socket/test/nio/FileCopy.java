package com.jeremie.socket.test.nio;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by jeremie on 15/5/16.
 */
public class FileCopy {
    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        copy("/volumes/Macintosh HD 2/未来日记/未来日记/[4hun][Mirai_Nikki][BDRIP][18][1080P][BIG5_GB][X264_AACx2](C94CBF7E).mkv");
        nioPaste2("/Users/jeremie/Desktop/test.mkv");
        System.out.println(System.currentTimeMillis() - time);
    }

    //被复制的文件路径
    public static String filePath = null;

    /**
     * 跟某个复制的按钮相关（点击时将对应文件路径传到filePath中）
     *
     * @param path
     */
    public static void copy(String path) {
        filePath = path;
    }

    /**
     * 粘贴（粘贴之前请检测输出文件是否已经存在，是否进行覆盖）
     * 利用NIO文件通道方法
     *
     * @param outputPath 输出的路径，包括文件名
     * @return 是否成功粘贴对象
     */
    public static boolean nioPaste(String outputPath) {
        //必须要有文件路径才能调用此方法，界面控制
        File inputFile = new File(filePath);
        File outputFile = new File(outputPath);
        FileChannel out = null;
        FileChannel in = null;
        try {
            in = new FileInputStream(inputFile).getChannel();
            out = new FileOutputStream(outputFile).getChannel();
            out.transferFrom(in, 0, in.size());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return outputFile.exists();

    }

    /**
     * 粘贴（粘贴之前请检测输出文件是否已经存在，是否进行覆盖）
     * 利用NIO文件内存映射及文件通道
     *
     * @param outputPath 输出的路径，包括文件名
     * @return 是否成功粘贴对象
     */
    public static boolean nioPaste2(String outputPath) {
        //必须要有文件路径才能调用此方法，界面控制
        File inputFile = new File(filePath);
        File outputFile = new File(outputPath);
        FileChannel out = null;
        FileChannel in = null;
        try {
            in = new FileInputStream(inputFile).getChannel();
            out = new FileOutputStream(outputFile).getChannel();
            MappedByteBuffer mbb = in.map(FileChannel.MapMode.READ_ONLY, 0, in.size());
            out.write(mbb);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return outputFile.exists();

    }

    /**
     * 粘贴（粘贴之前请检测输出文件是否已经存在，是否进行覆盖）
     * 传统IO方法
     *
     * @param outputPath 输出的路径，包括文件名
     * @return 是否成功粘贴对象
     */
    public static boolean tPaste(String outputPath) {
        //必须要有文件路径才能调用此方法，界面控制
        File inputFile = new File(filePath);
        File outputFile = new File(outputPath);
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(inputFile));
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
            int size = 1024 * 1024;
            byte[] buffer = new byte[size];
            int readSize;
            while ((readSize = bufferedInputStream.read(buffer, 0, size)) != -1) {
                bufferedOutputStream.write(buffer, 0, readSize);
                bufferedOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (bufferedInputStream != null)
                    bufferedInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return outputFile.exists();

    }
}
