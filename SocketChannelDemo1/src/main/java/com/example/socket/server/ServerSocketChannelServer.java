package com.example.socket.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Objects;

/**
 * nio服务器端
 */
public class ServerSocketChannelServer {

    /**
     * 1. 打开一个服务端通道
     * 2. 绑定对应的端口号
     * 3. 通道默认是阻塞的，需要设置为非阻塞
     * 4. 检查是否有客户端连接 有客户端连接会返回对应的通道
     * 5. 获取客户端传递过来的数据,并把数据放在byteBuffer这个缓冲区中
     * 6. 给客户端回写数据
     * 7. 释放资源
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws Exception {
        //打开一个服务端通道
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        //绑定对应的端口
        socketChannel.bind(new InetSocketAddress(8888));
        //设置通道为非阻塞
        socketChannel.configureBlocking(false);
        //处理连接请求
        while (true) {
            //处理连接的客户端，不存在则为null
            SocketChannel accept = socketChannel.accept();
            if (Objects.isNull(accept)) {
                //可做其他事儿
                System.out.println("暂时没有客户端连接~");
                Thread.sleep(5000);
                continue;
            }
            //分配内存缓冲区并读取数据
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            //-1: 读取到末尾
            //0：本次没有读取到字节
            //1：读到了末尾
            int read = accept.read(byteBuffer);
            System.out.println("当前接收到的消息：" + new String(byteBuffer.array(), 0, read));
            //回复消息
            accept.write(ByteBuffer.wrap("发送攻击".getBytes()));
            //关闭连接
            accept.close();
        }
    }
}
