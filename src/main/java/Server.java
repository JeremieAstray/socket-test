import javafx.util.Callback;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jeremie on 2015/5/13.
 */
public class Server {

    private static Map<String, ServerThread> onlineUsers;



    public static void main(String[] args) {
        onlineUsers = new HashMap<>();
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(8000);
            while (true) {
                Socket socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(socket, str -> {
                    try {
                        for (Map.Entry<String, ServerThread> user : onlineUsers.entrySet()) {
                            ObjectOutputStream oos = user.getValue().oos;
                            if (oos != null) {
                                oos.writeUTF(str);
                                oos.flush();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                });
                Thread thread = new Thread(serverThread);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null)
                    serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private static class ServerThread implements Runnable {
        private Socket socket;
        private String name = null;
        private Callback<String, Boolean> callback = null;
        public ObjectOutputStream oos = null;
        public ObjectInputStream ois = null;

        public ServerThread(Socket socket, Callback<String, Boolean> callback) {
            this.socket = socket;
            this.callback = callback;
        }

        @Override
        public void run() {
            try {
                oos = new ObjectOutputStream(this.socket.getOutputStream());
                ois = new ObjectInputStream(this.socket.getInputStream());
                boolean firstTime = true;
                while (true) {
                    if (firstTime) {
                        firstTime = false;
                        name = ois.readUTF();
                        System.out.println("name=" + name);
                        onlineUsers.put(name, this);
                        continue;
                    }
                    String str = ois.readUTF();
                    System.out.println(name + ": " + str);
                    oos.writeUTF("server receive:" + str);
                    if ("END".equals(str) || "null".equals(str)) break;
                    callback.call(name + ":" + str);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (oos != null) {
                        oos.flush();
                        oos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (ois != null)
                        ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (!socket.isClosed())
                        socket.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    onlineUsers.remove(name);
                    System.out.println(name + socket.getInetAddress() + " close!");
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
