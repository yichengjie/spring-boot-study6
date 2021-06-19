1. 下载插件（填写es版本及自己邮箱）
    ```text
    https://readonlyrest.com/download/
    ```
2. 进入到es的安装目录下bin文件夹
    ```text
    cd elasticsearch/bin
    ```
2. 执行命令安装wins插件
    ```text
    elasticsearch-plugin install file:///D:/tools/liferay/linux/readonlyrest-1.30.1_es7.6.2.zip
    ```
3. 执行命令安装linux插件
    ```text
    ./elasticsearch-plugin file://~/liferay/tools/readonlyrest-1.30.1_es7.6.2.zip
    ```
4. 卸载插件
    ```text
    elasticsearch-plugin remove readonlyrest
    ```
5. 查看已经安装的所有插件
    ```text
    elasticsearch-plugin list
    ```
6. 在elasticsearch/config目录中新建readonlyrest.yml （与elasticsearch.yml同级目录中）
   ```text
   # readonlyrest.yml 内容如下
   readonlyrest:
     enable: true
     response_if_req_forbidden: Sorry, your request is forbidden.
     access_control_rules:
     - name: "Accept all request from cluster"
       type: allow
       hosts: 
           - 192.168.1.1
           - 192.168.1.2
           - 192.168.1.3
     - name: "Accept all request for logstash and kibana grafana"
       type: allow
       auth_key: admin:123456
     - name: "Require HTTP Basic Auth"
       type: allow
       auth_key: connector:connectorpwd
     - name: "Accept read only user"
       auth_key: reader:readerpwd
       actions: ["indices:data/read/*"]
   ```  
7. 查看es运行状态
    ```text
    curl --basic -u admin:123456  http://localhost:9200/_cat/health?v
    ```
