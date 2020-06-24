package com.example.devutils.utils.net;

import com.example.devutils.utils.io.NioChannelUtils;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * NIO下的TCP操作工具类
 * Created by AMe on 2020-06-16 14:41.
 */
public class NioNetTcpUtils {

    public static SocketChannel getSocketChannel() throws IOException {
        return NioChannelUtils.getSocketChannel();
    }

    public static SocketChannel getSocketChannel(SocketAddress remote) throws IOException {
        return NioChannelUtils.getSocketChannel(remote);
    }

    public static ServerSocketChannel getServerSocketChannel() throws IOException {
        return NioChannelUtils.getServerSocketChannel();
    }

}
