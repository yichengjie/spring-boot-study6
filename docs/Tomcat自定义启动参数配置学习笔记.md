1. win系统下catalina.bat中添加
   ```text
   set JAVA_OPTS=-Xms128m -Xmx1024m -Dnfs_path=D:\opt\app\xxx -Dnfs_path0=D:\opt\app\xx
   注意不要在参数前后加双引号，否则报错
   如果报新生代内存溢出可以适当调整参数
   set JAVA_OPTS=-XX:PermSize=128M -XX:MaxPermSize=256m -Xms1024m -Xmx2048m -Dnfs_path=D:\opt\app\config
   ```
2. linux系统下catalina.sh中添加
   ```text
   JAVA_OPTS="-Xms2048m -Xmx2048m -XX:MaxPermSize=512m -Djava.net.preferIPv4Stack=true -Dnfs_path=/opt/app/config"
   ```
3. tomcat设置东8时区,修改tomcat/bin/setenv.sh文件，将user.timezone修改为GMT+08
    ```text
    -Duser.timezone=GMT+08
    ```
