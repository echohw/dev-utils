package com.example.devutils.utils.net;

import com.example.devutils.utils.io.NioBufferUtils;
import com.example.devutils.utils.io.NioChannelUtils;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.function.Consumer;

/**
 * Created by AMe on 2020-06-12 19:13.
 */
public class NioNetUdpUtils extends NetUtils {

    public static DatagramChannel getDatagramChannel() throws IOException {
        return NioChannelUtils.getDatagramChannel();
    }

    public static int send(String host, int port, byte[] data) throws IOException {
        try (
            DatagramChannel datagramChannel = NioChannelUtils.getDatagramChannel();
        ) {
            ByteBuffer byteBuffer = NioBufferUtils.getByteBuffer(data);
            InetSocketAddress address = getInetSocketAddress(host, port);
            return send(datagramChannel, byteBuffer, address);
        } catch (IOException ex) {
            throw ex;
        }
    }

    public static int send(DatagramChannel datagramChannel, ByteBuffer byteBuffer, SocketAddress address) throws IOException {
        return datagramChannel.send(byteBuffer, address);
    }

    public static void receive(int port, int bufferSize, Consumer<ByteBuffer> consumer) throws IOException {
        try (
            DatagramChannel datagramChannel = NioChannelUtils.getDatagramChannel();
        ) {
            InetSocketAddress address = NetUtils.getInetSocketAddress(port);
            datagramChannel.bind(address);
            ByteBuffer byteBuffer = NioBufferUtils.getByteBuffer(bufferSize, false);
            receive(datagramChannel, byteBuffer, consumer);
        } catch (IOException ex) {
            throw ex;
        }
    }

    public static void receive(DatagramChannel datagramChannel, ByteBuffer byteBuffer, Consumer<ByteBuffer> consumer) throws IOException {
        datagramChannel.receive(byteBuffer);
        consumer.accept((ByteBuffer) byteBuffer.flip());
    }

}
