package com.bzchao.common;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketUtils {
    public static void close(Socket socket) {
        try {
            if (null != socket) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close(ServerSocket socket) {
        try {
            if (null != socket) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
