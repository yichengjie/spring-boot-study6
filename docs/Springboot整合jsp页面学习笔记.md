1. 将pom中packaging修改为war
    ```xml
    <packaging>jar</packaging>
    ```
2. 添加springboot的plugin
    ```xml
    <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
    </plugin>
    ```
3. 添加jsp页面处理的JspServlet依赖
    ```xml
    <dependency>
        <groupId>org.apache.tomcat.embed</groupId>
        <artifactId>tomcat-embed-jasper</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-tomcat</artifactId>
    </dependency>
    ```
4. 新建jsp页面src/main/webapp/WEB-INF/pages/hello.jsp
    ```jspx
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <html>
    <head>
        <title>Title</title>
    </head>
    <body>
        <h3>hello world</h3>
    </body>
    </html>
    ```
5. application.properties中添加配置
    ```properties
    spring.mvc.view.prefix=/WEB-INF/pages/
    spring.mvc.view.suffix=.jsp
    ```
6. 打包运行
    ```
    6.1 mvn -Dmaven.test.skip -U clean package
    6.2 java -jar spring-boot-jsp-1.0-SNAPSHOT.war
    ```