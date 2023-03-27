package com.coding.netty.example01.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;

// @formatter:off
/**
 * 服务启动后，Ctrl+R打开命令行： 进入telnet命令行：
 * --------------------------------------------------
 * telnet localhost 6666
 * 然后点击快捷键：Ctrl+]，进入telnet命令行。
 * --------------------------------------------------
 */
// @formatter:on
@Slf4j
public class BIOServer {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(6666)) {
            log.info("服务器启动了");
            while (true) {
                final Socket socket = serverSocket.accept();
                log.info("连接到一个客户端");

                executorService.execute(() -> handler(socket));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void handler(Socket socket) {
        byte[] bytes = new byte[1024];
        try (InputStream inputStream = socket.getInputStream()) {
            int read;
            while ((read = inputStream.read(bytes)) != -1) {
                String info = new String(bytes, 0, read, Charset.forName("GB18030"));
                log.info(info);
                if ("quit".equalsIgnoreCase(info)) {
                    log.info("客户端退出连接");
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
