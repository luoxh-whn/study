


package com.bzchao.forward;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SimpleForward implements Runnable {
    // 服务器
    private ServerSocket server;
    // 监听本地端口
    private int localPort = 1080;
    // 目标主机地址
    private String remoteHostAddr = "0.0.0.0";
    // 目标主机端口
    private int remoteHostPort = 10001;
    // 设置超时时间 30s
    private static int TIMEOUT = 30;
    // 客户端列表 用于删除失效连接和超时连接
    private static HashMap<Socket, Date> clientList = new HashMap<>();

    public static void main(String[] args) throws IOException {
        //  new SimpleForward();
        SimpleForward simpleForward = new SimpleForward(1080, "127.0.0.1", 10001);
        new Thread(simpleForward).start();
    }

    public SimpleForward() throws IOException {
        run();
    }

    public SimpleForward(int localPort, String remoteHostAddr, int remoteHostPort) throws IOException {
        this.localPort = localPort;
        this.remoteHostAddr = remoteHostAddr;
        this.remoteHostPort = remoteHostPort;
    }

    @Override
    public void run() {
        try {
            this.server = new ServerSocket(this.localPort);
            System.out.println("服务器开启成功");
            System.out.println("监听端口 : " + this.localPort);
        } catch (IOException e) {
            System.out.println("服务器开启失败");
            System.out.println(e.getMessage());
            System.out.println("退出运行");
            return;
        }
        // 自动清除失效连接和超时连接
        new Thread(new Terminal()).start();
        new Thread(new AutoDestroy()).start();

        while (true) {
            Socket socket = null;
            Socket remoteHost = null;
            try {
                socket = server.accept();
                // 接收到请求就把socket扔进map,value为刷新时间
                clientList.put(socket, new Date());
                String address = socket.getRemoteSocketAddress().toString();
                System.out.println("新连接 ： " + address);
                // 建立与目标主机的连接
                remoteHost = new Socket(this.remoteHostAddr, this.remoteHostPort);
                System.out.println("连接地址 : " + this.remoteHostAddr + ":" + this.remoteHostPort);
                // 端口转发
                new Thread(new Switch(socket, remoteHost, remoteHost.getInputStream(), socket.getOutputStream())).start();
                new Thread(new Switch(socket, remoteHost, socket.getInputStream(), remoteHost.getOutputStream())).start();
            } catch (IOException e) {
                System.out.println("连接异常");
                System.out.println(e.getMessage());
                close(socket);
                close(remoteHost);
            }
        }
    }

    private void close(Socket socket) {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 用于端口转发
    private class Switch implements Runnable {
        private Socket host;
        private Socket remoteHost;
        private InputStream in;
        private OutputStream out;

        Switch(Socket host, Socket remoteHost, InputStream in, OutputStream out) {
            this.host = host;
            this.remoteHost = remoteHost;
            this.in = in;
            this.out = out;
        }

        @Override
        public void run() {
            int length = 0;
            byte[] buffer = new byte[1024];
            try {
                while (!host.isClosed() && (length = in.read(buffer)) > -1) {
                    clientList.put(host, new Date());
                    out.write(buffer, 0, length);
                }
            } catch (IOException e) {
                System.out.println("连接关闭");
            } finally {
                close(host);
                close(remoteHost);
            }
        }
    }

    // 用于清除失效连接和超时连接
    private class AutoDestroy implements Runnable {

        @Override
        public void run() {

            ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);

            Runnable cleanTask = () -> {
                List<Socket> list = new LinkedList<>();
                System.out.println("开始扫描失效与超时连接,共" + clientList.size());
                Date start = new Date();
                for (Socket socket : clientList.keySet()) {
                    Date lastTime = clientList.get(socket);
                    long grapTime = System.currentTimeMillis() - lastTime.getTime();
                    if (socket.isClosed() || grapTime / 1000 >= TIMEOUT) {
                        list.add(socket);
                    }
                }
                System.out.println("找到" + list.size() + "个,查询用时 : " + (System.currentTimeMillis() - start.getTime()) + "毫秒");
                System.out.println("开始清除失效与超时连接");
                for (Socket socket : list) {
                    close(socket);
                    clientList.remove(socket);
                }
                System.out.println("清理后连接数 : " + clientList.size());
            };
            pool.scheduleWithFixedDelay(cleanTask, 0, 30, TimeUnit.SECONDS);
        }
    }

    private class Terminal implements Runnable {
        private String format = "yyyy-MM-dd HH:mm:ss";
        private SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        @Override
        public void run() {
            while (!server.isClosed()) {
                System.out.print("请输入命令 : ");
                Scanner scanner = new Scanner(System.in);
                String cmd = scanner.nextLine();
                handler(cmd);
            }
        }

        private void handler(String cmd) {
            switch (cmd) {
                case "status":
                    System.out.println("当前时间 : " + dateFormat.format(new Date()));
                    System.out.println("总连接数 : " + clientList.size());
                    for (Socket socket : clientList.keySet()) {
                        long time = System.currentTimeMillis() - clientList.get(socket).getTime();
                        System.out.println("<" + socket.getRemoteSocketAddress().toString() + "> " + time / 1000);
                    }
                    break;
                case "clean":

            }
        }
    }
}
