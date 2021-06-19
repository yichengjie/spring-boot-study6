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
6. 查看es运行状态
    ```text
    curl http://10.122.83.137:9200/_cat/health?v
    curl --basic -u admin:dlxtstadmin  http://10.122.83.128:9200/_cat/health?v
    ```
