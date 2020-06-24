package com.example.devutils.utils.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TCP操作工具类
 * Created by AMe on 2020-06-16 03:12.
 */
public class NetTcpUtils {

    public static Socket getSocket(String host, int port) throws IOException {
        return new Socket(host, port);
    }

    public static ServerSocket getServerSocket(int port) throws IOException {
        return new ServerSocket(port);
    }

}
