package com.shanhy.tools.tcpforward;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 读写流的Runnable
 *
 */
public class ReadWriteRunnable implements Runnable {

    /**
     * 读入流的数据的套接字。
     */
    private Socket readSocket; 

    /**
     * 输出数据的套接字。
     */
    private Socket writeSocket;

    /**
     * 两个套接字参数分别用来读数据和写数据。这个方法仅仅保存套接字的引用，
     * 在运行线程的时候会用到。
     * @param readSocket 读取数据的套接字。
     * @param writeSocket 输出数据的套接字。
     */
    public ReadWriteRunnable(Socket readSocket, Socket writeSocket) {
        this.readSocket = readSocket;
        this.writeSocket = writeSocket;
    }

    public void run() {
        byte[] b = new byte[1024];   
        InputStream is = null;
        OutputStream os = null;
        try {
            is = readSocket.getInputStream();
            os = writeSocket.getOutputStream();
            while(!readSocket.isClosed() && !writeSocket.isClosed()){
                int size = is.read(b); 
                if (size > -1) {
                    os.write(b, 0, size);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (null != os) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                    e.printStackTrace();
            }   
        }

    }

}
