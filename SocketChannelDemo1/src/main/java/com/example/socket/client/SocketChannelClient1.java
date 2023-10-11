package com.example.socket.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * nio客户端
 */
public class SocketChannelClient1 {

    /**
     * 1. 打开通道
     * 2. 设置连接IP和端口号
     * 3. 写出数据
     * 4. 读取服务器写回的数据
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        //打开一个通道
        SocketChannel socketChannel = SocketChannel.open();
        //设置连接的IP和端口号
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8888));
        //发送数据
        socketChannel.write(ByteBuffer.wrap("请首长指示".getBytes()));
        //分配内存缓冲区并获取数据
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int read = socketChannel.read(byteBuffer);
        System.out.println("收到的回复:" + new String(byteBuffer.array(),0,read));
        //释放资源
        socketChannel.close();
    }
}
