package com.socket.demo.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient1 {

    public static void main(String[] args) {
        try {
            createClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void createClient() throws IOException {
        Socket socket = new Socket("localhost", 8888);
        while (socket.isConnected()) {
            sendMsg(socket);
            receiveMsg(socket);
        }
    }

    private static void sendMsg(Socket socket) {
        OutputStream os = null;
        try {
            os = socket.getOutputStream();
            System.out.println("请输入内容：");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            os.write(line.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
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
            System.out.print("首长回复：");
            byte[] cache = new byte[1024];
            int count = 0;
            if ((count = is.read(cache)) > 0) {
                System.out.print(new String(cache, 0, count));
            }
            System.out.println();
            System.out.println("--------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
