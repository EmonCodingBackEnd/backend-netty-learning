package com.coding.netty.example01.nio.zerocopy;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class OldIOServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(7001);

        while (true) {
            Socket socket = serverSocket.accept();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            byte[] bytes = new byte[4096];

            try {
                // 从Socket读取数据
                while (true) {
                    int readCount = dataInputStream.read(bytes, 0, bytes.length);
                    if (-1 == readCount) {
                        break;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
