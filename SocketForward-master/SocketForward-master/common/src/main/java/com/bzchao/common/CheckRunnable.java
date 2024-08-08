package com.bzchao.common;

import java.io.File;
import java.io.IOException;

/**
 * 新启动一个线程，每隔一段时间就检查一下是否存在 running.txt文件。如果存在，程序正常运行。
 * 如果不存在，系统退出。
 *
 * @author 张超
 */
public class CheckRunnable implements Runnable {

    /**
     * 取得Java程序当前目录下的running.txt硬盘地址。如果是编译后的jar包，那么
     * running.txt 就在jar包所在的文件夹。如果是开发阶段，就在 class 文件目录里面
     *
     * @return 取得 running.txt 路径的  File。
     */
    private File getFile() {
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        File runningFile = null;
        if (path.endsWith(".jar")) {
            File tmp = new File(path);
            tmp = tmp.getParentFile();
            runningFile = new File(tmp.getAbsolutePath() + File.separator + "running.txt");
        } else {
            runningFile = new File(path + "running.txt");
        }
        return runningFile;
    }

    /**
     * 构造方法
     */
    public CheckRunnable() {
        File file = this.getFile();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void run() {
        try {
            while (true) {

                Thread.sleep(30L * 1000L);
                // 没有 running.txt 就退出
                File file = this.getFile();
                if (!file.exists()) {
                    System.exit(0);
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}