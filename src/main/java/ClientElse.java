import java.io.*;
import java.net.Socket;

/**
 * Created by Jeremie on 2015/5/13.
 */
public class ClientElse {
    public static void main(String[] args) {
        Socket socket = null;
        Reader reader = null;
        BufferedReader brConsle = null;
        PrintWriter pw = null;
        BufferedReader br = null;
        try {
            socket = new Socket("127.0.0.1", 8000);
//            socket.getChannel().configureBlocking(false);
            reader = new InputStreamReader(System.in);
            brConsle = new BufferedReader(reader);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"));
            final BufferedReader finalbr = br;
            Thread thread = new Thread(() -> {
                try {
                    while (true) {
                        String msg = finalbr.readLine();
                        System.out.println(msg);
                        if ("server receive:END".equals(msg))
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
            while (true) {
                String message = brConsle.readLine();
                pw.println(message);
                pw.flush();
                if ("END".equals(message)) {
                    /*try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                    break;
                }
            }
            thread.interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.flush();
                pw.close();
            }
            try {
                if (br != null)
                    br.close();
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
