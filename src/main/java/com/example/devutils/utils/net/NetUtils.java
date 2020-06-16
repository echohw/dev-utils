package com.example.devutils.utils.net;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * Created by AMe on 2020-06-12 19:28.
 */
public class NetUtils {

    public static InetAddress getInetAddress(String host) throws UnknownHostException {
        return InetAddress.getByName(host);
    }

    public static InetSocketAddress getInetSocketAddress(String hostname, int port) {
        return new InetSocketAddress(hostname, port);
    }

    public static InetSocketAddress getInetSocketAddress(int port) {
        return new InetSocketAddress(port);
    }

}
