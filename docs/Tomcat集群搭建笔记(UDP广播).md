#### 修改server.xml文件
1. 修改```<Server port="8005" shutdown="SHUTDOWN">```中端口 （同一台机器上端口不同即可）
2. 修改```<Connector port="8080" protocol="HTTP/1.1" redirectPort="8443" />```中端口（同一台机器上端口不同即可）
3. 修改```<Connector port="8009" protocol="AJP/1.3" redirectPort="8443" address="192.168.73.72" />```中端口（同一台机器上端口不同即可）
4. 修改```<Engine name="Catalina" defaultHost="localhost" jvmRoute="node1">``` 中jvmRoute的值，集群中名称不同
5. Engine下添加Cluster
    ```xml
    <!--1.修改Cluster-Channel-Receiver中address为本机IP地址 -->
    <!--2.修改Cluster-Channel-Receiver中port，端口同一台机器上不重复即可-->
    <Cluster className="org.apache.catalina.ha.tcp.SimpleTcpCluster" channelSendOptions="6">
          <Manager className="org.apache.catalina.ha.session.BackupManager"
                   expireSessionsOnShutdown="false" notifyListenersOnReplication="true" mapSendOptions="6"/>
          <Channel className="org.apache.catalina.tribes.group.GroupChannel">
              <Membership className="org.apache.catalina.tribes.membership.McastService"
                            address="228.0.0.4" port="45564" frequency="500" dropTime="3000"/>
              <Receiver className="org.apache.catalina.tribes.transport.nio.NioReceiver"
                          address="192.168.73.72" port="5000" selectorTimeout="100" maxThreads="6"/>
              <Sender className="org.apa`c`he.catalina.tribes.transport.ReplicationTransmitter">
                   <Transport className="org.apache.catalina.tribes.transport.nio.PooledParallelSender"/>
              </Sender>
              <Interceptor className="org.apache.catalina.tribes.group.interceptors.TcpFailureDetector"/>
              <Interceptor className="org.apache.catalina.tribes.group.interceptors.MessageDispatchInterceptor"/>
              <Interceptor className="org.apache.catalina.tribes.group.interceptors.ThroughputInterceptor"/>
          </Channel>
          <Valve className="org.apache.catalina.ha.tcp.ReplicationValve" filter=""/>
          <Deployer className="org.apache.catalina.ha.deploy.FarmWarDeployer"
                tempDir="/tmp/war-temp/" deployDir="/tmp/war-deploy/" 
                watchDir="/tmp/war-listen/" watchEnabled="false"/>
          <ClusterListener className="org.apache.catalina.ha.session.ClusterSessionListener"/>
    </Cluster>
    ```
#### 修改自己应用配置
1. 在web应用的web.xml中添加<distributable/> 标签
    ```xml
    <web-app>
      <distributable/>
      <display-name>Welcome to Tomcat</display-name>
      <description>
         Welcome to Tomcat
      </description>
    </web-app>
    ```
2. 编写jsp页面验证session
    ```text
    <%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <%@ page import="java.util.*"%>
    <html>
    <head><title>Cluster app test</title></head>
    <body>
        Server info:
        <%
           out.print(request.getLocalAddr() + ":" + request.getLocalPort() + "<br>");
           out.print("<br>ID " + session.getId() + " <br>");
        %>
    </body>
    </html>
    ```
#### 注意问题
1. 浏览器访问时一定要保证地址栏端口之前的部分相同，否则sessionId会不同
   ```text
   1.1 访问http://localhost:9091/test.jsp与http://localhost:9092/test.jsp得到session一样
   1.2 访问http://localhost:9091/test.jsp与http://127.0.0.1:9092/test.jsp得到session不一样
   ```