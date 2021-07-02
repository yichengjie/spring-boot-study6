1. github地址下载较新的release版本的tomcat-cluster-redis-session-manager.zip文件
    ```text
   https://github.com/ran-jit/tomcat-cluster-redis-session-manager/releases
    ```
2. 解压文件参照readMe.txt操作即可
#### 注意事项
1. readme中第二步添加tomcat 系统属性catalina.base可以不操作
2. 浏览器访问时一定要保证地址栏端口之前的部分相同，否则sessionId会不同
    ```text
    1.1 访问http://localhost:9091/test.jsp与http://localhost:9092/test.jsp得到session一样
    1.2 访问http://localhost:9091/test.jsp与http://127.0.0.1:9092/test.jsp得到session不一样
    ```