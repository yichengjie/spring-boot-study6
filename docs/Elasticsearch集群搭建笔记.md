### 配置JDK环境变量
1. 配置jdk环境变量(编辑/etc/profile)，文件末尾添加
    ```text
    # user config java env
    export JAVA_HOME=/data/jdk
    export PATH=$PATH:$JAVA_HOME/bin
    export CLASSPATH=.:$JAVA_HOME/lib/tools.jar:$JAVA_HOME/lib/dt.jar:$CLASSPATH
    ```
2. 使配置生效
    ```text
    source /etc/profile
    ```
3. 检查安装
    ```text
    java -version
    ```
### 操作系统调优（内存优化）
1. 在/etc/sysctl.conf添加如下内容
    ```text
    # 系统最大打开文件描述符数(fs.file-max)
    # 限制一个进程拥有虚拟内存区域的大小(vm.max_map_count)
    fs.file-max=655360
    vm.max_map_count=655360
    ```
2. 使用配置生效
    ```text
    /sbin/sysctl -p
    ```
### 操作系统调优（修改资源限制）
1. 在/etc/security/limits.conf中添加如下内容
    ```text
    # 最大开打开文件描述符（nofile）
    # 最大用户进程数（nproc）
    # 最大锁定内存地址空间（memlock）
    * soft nofile 65536
    * hard nofile 65536
    * soft nproc 65536
    * hard nproc 65536
    * soft memlock unlimited
    * hard memlock unlimited
    ```
### 操作系统调优（修改资源限制-其他）
1. 修改/etc/security/limits.d/${20:各系统可能不一样}-nproc.conf，将* soft nproc的值从1024修改为65536
    ```text
    *   soft    nproc    65536
    ```
2. 查看修改结果
    ```text
    ulimit -a
    ```
### 安装elasticsearch并配置
1. 安装es,解压即可
2. jvm调优，修改elasticsearch/config/jvm.options，将-Xms1g、Xmx1g修改为-Xms2g
    ```text
    -Xms2g
    -Xmx2g
    ```
### 配置elasticsearch.yml文件
1. 模板yml文件
    ```text
    cluster.name: escluster
    node.name: es1
    node.master: true
    node.data: true
    path.data: /data/elasticsearch/data
    path.logs: /data/elasticsearch/logs
    bootstrap.memory_lock: true
    bootstrap.system_call_filter: false
    http.port: 9200
    network.host: 0.0.0.0
    discovery.zen.minimum_master_nodes: 2
    discovery.zen.ping_timeout: 3s
    discovery.zen.ping.unicast.hosts: ["192.168.100.1:9300","192.168.100.2:9300","192.168.100.3:9300"]
    ```
2. 三台机器不一样的地方
    ```text
    node.name: es1      ===》192.168.100.1
    node.name: es2      ===》192.168.100.2
    node.name: es3      ===》192.168.100.3
    ```
### elasticsearch.yml模板参数解释
1. cluster.name
    ```text
    集群名字，三台集群的集群名字都必须一致
    ```
2. node.name
    ```text
    节点名字，三台ES节点字都必须不一样
    ```
3. discovery.zen.minimum_master_nodes:2
    ```text
    表示集群最少的master数，如果集群的最少master数据少于指定的数，  
    将无法启动，官方推荐node master数设置为集群数/2+1，我这里三台ES服务器，
    配置最少需要两台master，整个集群才可正常运行，
    ```
4. node.master
    ```text
    该节点是否有资格选举为master，如果上面设了两个mater_node 2，也就是最少两个master节点，
    则集群中必须有两台es服务器的配置为node.master: true的配置，配置了2个节点的话，
    如果主服务器宕机，整个集群会不可用，所以三台服务器，需要配置3个node.masdter为true,
    这样三个master，宕了一个主节点的话，他又会选举新的master，还有两个节点可以用，
    只要配了node master为true的ES服务器数正在运行的数量不少于master_node的配置数，
    则整个集群继续可用，我这里则配置三台es node.master都为true，
    也就是三个master，master服务器主要管理集群状态，负责元数据处理，
    比如索引增加删除分片分配等，数据存储和查询都不会走主节点，压力较小，
    jvm内存可分配较低一点
    ```
5. node.data
    ```text
    存储索引数据，三台都设为true即可
    ```
6. bootstrap.memory_lock: true
    ```text
    锁住物理内存，不使用swap内存，有swap内存的可以开启此项
    ```
7. discovery.zen.ping_timeout: 3000s
    ```text
    自动发现拼其他节点超时时间
    ```
8. discovery.zen.ping.unicast.hosts: ["192.168.100.1:9300","192.168.100.2:9300","192.168.100.3:9300"]
    ```text
    设置集群的初始节点列表，集群互通端口为9300
    ```
### ES运行及验证
1. 启动es
    ```text
    nohup ./elasticsearch> nohup.out 2>&1 &
    ```
2. 查看启动
    ```text
    netstat -tlnp | egrep '9200|9300'
    service elasticsearch status
    ```
