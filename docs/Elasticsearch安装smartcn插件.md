1. 下载smartcn, 直接浏览器访问地址，根据当前es版本号替换地址中analysis-smartcn-{x.x.x}.zip
    ```text
    https://artifacts.elastic.co/downloads/elasticsearch-plugins/analysis-smartcn/analysis-smartcn-7.9.0.zip
    ```
2. 进入到es的安装目录下bin文件夹
    ```text
    cd elasticsearch/bin
    ```
2. 执行命令安装wins插件
    ```text
    elasticsearch-plugin install file:///D:/tools/linux/analysis-smartcn-7.9.0.zip
    ```
3. 执行命令安装linux插件
    ```text
    ./elasticsearch-plugin install file:///home/liferay/analysis-smartcn-7.9.0.zip
    ```
4. 卸载插件
    ```text
    elasticsearch-plugin remove analysis-smartcn
    ```
5. 查看已经安装的所有插件
    ```text
    elasticsearch-plugin list
    ```