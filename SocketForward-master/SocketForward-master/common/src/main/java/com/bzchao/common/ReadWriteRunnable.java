package com.bzchao.common;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 读写流的Runnable
 *
 * @author 张超
 */
@Slf4j
public class ReadWriteRunnable implements Runnable {

    /**
     * 读入流的数据的套接字。
     */
    private Socket readSocket;
    private boolean readClose;
    /**
     * 输出数据的套接字。
     */
    private Socket writeSocket;
    private boolean writeClose;

    /**
     * 两个套接字参数分别用来读数据和写数据。这个方法仅仅保存套接字的引用，
     * 在运行线程的时候会用到。
     *
     * @param readSocket  读取数据的套接字。
     * @param writeSocket 输出数据的套接字。
     */
    public ReadWriteRunnable(Socket readSocket, boolean readClose, Socket writeSocket, boolean writeClose) {
        log.info("新建连接2，{},{}", readSocket.getPort(), writeSocket.getPort());
        this.readSocket = readSocket;
        this.readClose = readClose;
        this.writeSocket = writeSocket;
        this.writeClose = writeClose;
    }

    public void setSocket(Socket readSocket, Socket writeSocket) {
        if (readSocket != null) {
            SocketUtils.close(this.readSocket);
            this.readSocket = readSocket;
        }
        if (writeSocket != null) {
            SocketUtils.close(this.writeSocket);
            this.writeSocket = writeSocket;
        }
    }

    @Override
    public void run() {
        byte[] b = new byte[1024];
        InputStream is = null;
        OutputStream os = null;
        try {
            is = readSocket.getInputStream();
            os = writeSocket.getOutputStream();
            while (!readSocket.isClosed() && !writeSocket.isClosed()) {
                int size = is.read(b);
                if (size > -1) {
                    String s = new String(b);
                    log.info("{}->{}:{}", readSocket.getPort(), writeSocket.getPort(), s);
                    os.write(b, 0, size);
                } else {
                    if (readClose) {
                        log.info("关闭读取连接：", readSocket.getPort());
                        SocketUtils.close(readSocket);
                        is.close();
                    }
                    if (writeClose) {
                        log.info("关闭输出连接：", writeSocket.getPort());
                        SocketUtils.close(writeSocket);
                        os.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            log.info("连接结束");
            try {
                if (is != null) {
                    if (readClose) {
                        log.info("关闭读取连接：", readSocket.getPort());
                        SocketUtils.close(readSocket);
                        is.close();
                    }
                } else {
                    log.info("读取连接为空");
                }
                if (null != os) {
                    os.flush();
                    if (writeClose) {
                        log.info("关闭输出连接：", writeSocket.getPort());
                        SocketUtils.close(writeSocket);
                        os.close();
                    }
                } else {
                    log.info("输出连接为空");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}