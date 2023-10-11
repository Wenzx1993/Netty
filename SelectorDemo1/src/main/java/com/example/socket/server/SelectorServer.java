package com.example.socket.server;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO服务端
 */
public class SelectorServer {

    /**
     * 1. 打开一个服务端通道
     * 2. 绑定对应的端口号
     * 3. 通道默认是阻塞的，需要设置为非阻塞
     * 4. 创建选择器
     * 5. 将服务端通道注册到选择器上,并指定注册监听的事件为OP_ACCEPT
     * 6. 检查选择器是否有事件
     * 7. 获取事件集合
     * 8. 判断事件是否是客户端连接事件SelectionKey.isAcceptable()
     * 9. 得到客户端通道,并将通道注册到选择器上, 并指定监听事件为OP_READ
     * 10. 判断是否是客户端读就绪事件SelectionKey.isReadable()
     * 11. 得到客户端通道,读取数据到缓冲区
     * 12. 给客户端回写数据
     * 13. 从集合中删除对应的事件, 因为防止二次处理
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //打开一个服务端通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //绑定一个端口
        serverSocketChannel.bind(new InetSocketAddress(8888));
        //设置通道为非阻塞
        serverSocketChannel.configureBlocking(false);
        //创建一个选择器
        Selector selector = Selector.open();
        //服务端通道注册到选择器上，并指定监听事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            //阻塞 2000 毫秒，监控所有注册的通道,当有对应的事件操作时, 会将SelectionKey放入集合内部并返回
            int select = selector.select(2000);
            if (select == 0) {
                continue;
            }
            //获取事件集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();
                //如果是连接事件
                if (next.isAcceptable()) {
                    //获取客户端通道
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("收到连接请求");
                    //将通道设置为非阻塞
                    socketChannel.configureBlocking(false);
                    //通道注册到selector上，并监听OP_READ事件
                    socketChannel.register(selector, SelectionKey.OP_READ);
                }
                //如果是读事件
                if (next.isReadable()) {
                    //获取客户通道
                    SocketChannel socketChannel = (SocketChannel) next.channel();
                    //分配内存缓冲区并读取数据
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int read = socketChannel.read(byteBuffer);
                    System.out.println(new String(byteBuffer.array(), 0, read));
                    //回复
                    socketChannel.write(ByteBuffer.wrap("发动攻击".getBytes()));
                }
                //移除事件避免重复消费
                iterator.remove();
            }
        }
    }
}
