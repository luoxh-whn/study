# JavaTcpForward

#### 项目介绍
Java实现的TCP端口转发

#### 软件架构
Java Socket、Maven


#### 安装和使用说明

1. 打包：mvn clean package
2. 运行[语法]：java -jar JavaTcpForward.jar [参数1]当前服务器IP地址 [参数2]当前服务器端口 [参数3]目的地服务器IP地址 [参数4]目的地服务器端口
3. 运行[示例]：java -jar JavaTcpForward.jar 127.0.0.1 8081 192.168.20.11 22
4. 运行后，会生成 running.flag 文件。删除该文件，5秒钟内程序会停止。

