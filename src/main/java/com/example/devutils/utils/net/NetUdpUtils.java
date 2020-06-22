package com.example.devutils.utils.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.function.Consumer;

/**
 * Created by AMe on 2020-06-12 18:32.
 */
public class NetUdpUtils extends NetUtils {

    public static DatagramSocket getDatagramSocket() throws SocketException {
        return new DatagramSocket();
    }

    public static DatagramSocket getDatagramSocket(int port) throws SocketException {
        return new DatagramSocket(port);
    }

    public static DatagramPacket getDatagramPacket(byte[] buf) {
        return getDatagramPacket(buf, buf.length);
    }

    public static DatagramPacket getDatagramPacket(byte[] buf, int length) {
        return new DatagramPacket(buf, length);
    }

    public static DatagramPacket getDatagramPacket(byte[] buf, InetAddress address, int port) {
        return new DatagramPacket(buf, buf.length, address, port);
    }

    public static DatagramPacket getDatagramPacket(byte[] buf, int length, InetAddress address, int port) {
        return new DatagramPacket(buf, length, address, port);
    }

    public static void send(String host, int port, byte[] data) throws IOException {
        try (
            DatagramSocket socket = getDatagramSocket();
        ) {
            InetAddress address = getInetAddress(host);
            DatagramPacket packet = getDatagramPacket(data, data.length, address, port);
            send(socket, packet);
        }
    }

    public static void send(DatagramSocket socket, DatagramPacket packet) throws IOException {
        socket.send(packet);
    }

    public static void receive(int port, int bufferSize, Consumer<DatagramPacket> consumer) throws IOException {
        try (
            DatagramSocket socket = getDatagramSocket(port);
        ) {
            byte[] bytes = new byte[bufferSize];
            DatagramPacket packet = getDatagramPacket(bytes, bytes.length);
            receive(socket, packet, consumer);
        }
    }

    public static void receive(DatagramSocket socket, DatagramPacket packet, Consumer<DatagramPacket> consumer) throws IOException {
        socket.receive(packet);
        consumer.accept(packet);
    }

}
