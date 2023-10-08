package com.socket.demo.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer1 {

    public static void main(String[] args) {
        try {
            createServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static ServerSocket createServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        System.out.println("服务端已启动");
        while (true) {
            Socket socket = serverSocket.accept();
            executorService.execute(() -> {
                receiveMsg(socket);
                sendMsg(socket);
            });
        }
    }

    /**
     * socket 流接收数据是阻塞的，inputstream 读取结束后并不会返回-1
     *
     * @param socket
     */
    private static void receiveMsg(Socket socket) {
        InputStream is = null;
        try {
            is = socket.getInputStream();
            System.out.println("接收到的消息：");
            byte[] cache = new byte[1024];
            int count = 0;
            if ((count = is.read(cache)) > 0) {
                System.out.print(new String(cache, 0, count));
            }
            System.out.println();
            System.out.println("--------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendMsg(Socket socket) {
        OutputStream os = null;
        try {
            os = socket.getOutputStream();
            os.write("请发送攻击".getBytes());
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
