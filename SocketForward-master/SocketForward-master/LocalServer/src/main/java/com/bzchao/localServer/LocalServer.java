package com.bzchao.localServer;

import com.bzchao.common.CheckRunnable;
import com.bzchao.common.ReadWriteRunnable;
import com.bzchao.common.SocketUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;


/**
 * 主类。
 *
 * @author 张超
 */
@Slf4j
public class LocalServer {
    /**
     * 当前服务器ServerSocket的最大连接数
     */

    public static void main(String[] args) {
        log.info("转发服务器数据：{}", Arrays.toString(args));

        // 启动一个新线程。检查是否要种植程序。
        new Thread(new CheckRunnable()).start();

        // 公网服务器的IP地址和端口。
        String serverIP = args[0];
        int serverPort = Integer.parseInt(args[1]);

        // 转出去的目标服务器IP地址和端口号。
        String outIp = args[2];
        int outPort = Integer.parseInt(args[3]);

        Socket serverSocket = null;
        try {
            //和服务器建立连接
            serverSocket = new Socket(serverIP, serverPort);

            log.info("连接到服务器成功, {}:{}", serverIP, serverPort);
            // 转发到目的地服务器的socket。
            Socket targetClient = null;
            while (true) {
                Thread.sleep(100);
                if (targetClient == null || targetClient.isClosed()) {
                    try {
                        targetClient = new Socket();
                        targetClient.connect(new InetSocketAddress(outIp, outPort), 1000);
                        log.info("重新建立连接，{}:{}", outIp, outPort);

                        // 读取用户发来的流，然后转发到目的地服务器。
                        new Thread(new ReadWriteRunnable(serverSocket, false, targetClient, true)).start();

                        // 读取目的地服务器的发过来的流，然后转发给用户。
                        new Thread(new ReadWriteRunnable(targetClient, true, serverSocket, false)).start();
                    } catch (IOException e) {
                        targetClient = null;
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            SocketUtils.close(serverSocket);
        }
    }

}