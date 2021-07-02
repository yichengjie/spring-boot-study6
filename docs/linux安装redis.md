1. 下载redis
    ```text
    https://redis.io/download
    ```
2. 将解压后目录移动到/usr/local 中
    ```text
    mv redis-6.2.4/ /usr/local/
    ```
3. 安装gcc
    ```text
    yum install gcc-c++
    ```
4. 如果未安装gcc执行make命令报错后，再安装gcc需要清理后再make
    ```text
    make distclean  && make
    ```
5. 进入```src```目录执行make install命令
    ```text
    make PREFIX=/usr/local/redis-6.2.4 install
    ```
6. 在redis根目录下创建conf和bin目录
7. 将redis根目录下```redis.conf```移动到conf中
9. 编辑redis.conf
   ```text
   8.1 将daemonize属性改为yes
   8.2 bind 127.0.0.1 这一行给注释掉
   8.3 protected-mode 设置成no（默认是设置成yes的， 防止了远程访问，在redis3.2.3版本后）
   ```
10. 修改Redis默认密码
    ```text
    9.1 去掉# requirepass foobared前的# 注释
    9.2 将foobared修改为自己的密码
    ```
11. 设置Redis开机启动，修改/etc/rc.d/rc.local文件，末尾添加内容
    ```text
    /usr/local/redis-6.2.4/bin/redis-server  /usr/local/redis-6.2.4/conf/redis.conf
    ```
12 启动服务
   ```text
   ./redis-server ../conf/redis.conf
   ```



