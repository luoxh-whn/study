package com.bzchao.socketServer;

import com.bzchao.common.CheckRunnable;
import com.bzchao.common.ReadWriteRunnable;
import com.bzchao.common.SocketUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;


/**
 * 主类。
 *
 * @author 张超
 */
@Slf4j
public class SocketServer {
    /**
     * 当前服务器ServerSocket的最大连接数
     */
    private static final int MAX_CONNECTION_NUM = 50;

    public static void main(String[] args) {
        log.info("端口转发2：{}", Arrays.toString(args));

        // 启动一个新线程。检查是否要种植程序。
        new Thread(new CheckRunnable()).start();

        // 当前服务器的IP地址和端口号。
        String thisIp = args[0];
        int thisPort = Integer.parseInt(args[1]);

        // 转出去的目标服务器IP地址和端口号。
        String outIp = args[2];
        int outPort = Integer.parseInt(args[3]);

        ServerSocket reServer = null;
        ServerSocket targetServer = null;
        try {
            reServer = new ServerSocket(thisPort, MAX_CONNECTION_NUM, InetAddress.getByName(thisIp));
            targetServer = new ServerSocket(outPort, MAX_CONNECTION_NUM, InetAddress.getByName(outIp));
            //            // 当前服务器连接到目的地服务器的socket。
            Socket targetClient = targetServer.accept();
            log.info("已连接输出端口：{}", outPort);
            while (true) {
                // 用户连接到当前服务器的socket
                Socket reClient = reServer.accept();

                log.info("新的请求..");

                // 读取用户发来的流，然后转发到目的地服务器。
                new Thread(new ReadWriteRunnable(reClient, true, targetClient, false)).start();

                // 读取目的地服务器的发过来的流，然后转发给用户。
                new Thread(new ReadWriteRunnable(targetClient, false, reClient, true)).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            SocketUtils.close(reServer);
        }
    }

}