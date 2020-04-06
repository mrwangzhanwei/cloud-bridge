# cloud-brideg
**netty集群**
    
    为了解决物联网硬件TCP客户端只能连接一个server服务器有可能出现单节点故障的问题。
    客户端连接任意 server 服务器实现集群内转发消息。
    目前只是初步实现了基础功能，项目还不够完善，持续更新中。。。

**框架架构构架**
    
    1 common 工程
    2 server 工程
    3 client 工程
    
1.代码工具运行时注意修改一下启动类的运行参数
    VMoptions 加上 -Xbootclasspath/a:你的target目录下的config文件夹的绝对路径，记得最后的 \ (Linux环境下是 /)
    或者将 server 工程下的 build 的所有内容注释掉
    关于 -Xbootclasspath 参考：https://blog.csdn.net/sayyy/article/details/81120749
    
