1. 需要有java环境且配置环境变量
2. cmd输入keytool命令生成证书
    ```cmd
    keytool -genkeypair -alias https -keyalg RSA -keystore D:\opt\app\keys\https\https.key
    ```
3. 输入各种提示信息
    ```txt
    3.1 密钥本身的口令
    3.2 姓名：随便填写
    3.3 组织: 随便填写
    3.4 组织名称：随便填写
    3.5 城市：随便填
    3.6 省份：随便填
    3.7 国家： china
    3.9 确认是否正确： y
    1.10 keystore的密码：可直接回车
    ```
4. 将生成的https.key放入项目的resources目录中
5. 如果使用的是idea，需在pom文件的build中添加resource配置，否则https.key不会编译到classpath目录中
    ```xml
    <resources>
        <resource>
            <directory>src/main/resources</directory>
        </resource>
    </resources>
    ```
6. 添加https的配置
    ```properties
    # 配置https
    server.ssl.key-store=classpath:https.key
    # key-store的密码
    server.ssl.key-store-password=yichengjie
    # 秘密本身的密码
    server.ssl.key-password=yichengjie
    ```
7. 启动应用可用https访问，注意http协议将不可用