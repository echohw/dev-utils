package com.example.devutils.utils.net;

import com.example.devutils.utils.io.NioChannelUtils;
import java.io.IOException;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Created by AMe on 2020-06-16 14:49.
 */
public class AioNetTcpUtils {

    public static AsynchronousSocketChannel getSocketChannel() throws IOException {
        return NioChannelUtils.getAsynchronousSocketChannel();
    }

    public static AsynchronousSocketChannel getSocketChannel(AsynchronousChannelGroup channelGroup) throws IOException {
        return NioChannelUtils.getAsynchronousSocketChannel(channelGroup);
    }


    public static AsynchronousServerSocketChannel getServerSocketChannel() throws IOException {
        return NioChannelUtils.getAsynchronousServerSocketChannel();
    }

    public static AsynchronousServerSocketChannel getServerSocketChannel(AsynchronousChannelGroup channelGroup) throws IOException {
        return NioChannelUtils.getAsynchronousServerSocketChannel(channelGroup);
    }

}
