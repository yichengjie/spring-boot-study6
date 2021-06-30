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
   # 设置集群名称，集群内所有节点的名称必须一致。
   cluster.name: cluster-prod
   # 设置节点名称，集群内节点名称必须唯一。
   node.name: node-prod-1
   # 表示该节点会不会作为主节点，true表示会；false表示不会
   node.master: true
   # 当前节点是否用于存储数据，是：true、否：false
   node.data: true
   # 索引数据存放的位置
   path.data: /data/elasticsearch/data
   # 日志文件存放的位置
   path.logs: /data/elasticsearch/logs
   # 需求锁住物理内存，是：true、否：false
   bootstrap.memory_lock: true
   # 监听地址，用于访问该es
   network.host: 192.168.1.1
   # es对外提供的http端口，默认 9200
   http.port: 9200
   # TCP的默认监听端口，默认 9300
   transport.tcp.port: 9300
   # 设置这个参数来保证集群中的节点可以知道其它N个有master资格的节点。默认为1，对于大的集群来说，可以设置大一点的值（2-4）
   discovery.zen.minimum_master_nodes: 2
   # es7.x 之后新增的配置，写入候选主节点的设备地址，在开启服务后可以被选为主节点
   discovery.seed_hosts: ["192.168.1.1:9300", "192.168.1.2:9300", "192.168.1.3:9300"]
   discovery.zen.fd.ping_timeout: 1m
   discovery.zen.fd.ping_retries: 5
   # es7.x 之后新增的配置，初始化一个新的集群时需要此配置来选举master
   cluster.initial_master_nodes: ["node-prod-1", "node-prod-2", "node-prod-3"]
   # 是否支持跨域，是：true，在使用head插件时需要此配置
   http.cors.enabled: true
   # “*” 表示支持所有域名
   http.cors.allow-origin: "*"
   action.destructive_requires_name: true
   action.auto_create_index: .security,.monitoring*,.watches,.triggered_watches,.watcher-history*
   xpack.security.enabled: false
   xpack.monitoring.enabled: true
   xpack.graph.enabled: false
   xpack.watcher.enabled: false
   xpack.ml.enabled: false
   ```
2. 三台机器不一样的地方
    ```text
    node.name: node-prod-1      ===》192.168.100.1
    node.name: node-prod-2      ===》192.168.100.2
    node.name: node-prod-3      ===》192.168.100.3
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
7. discovery.seed_hosts: ["192.168.1.1:9300", "192.168.1.2:9300", "192.168.1.3:9300"]
    ```text
    设置集群的初始节点列表，集群互通端口为9300
    ```
### ES运行及验证
1. 启动es
    ```text
    ./elasticsearch -d
    ```
2. 查看启动
    ```text
    netstat -tlnp | egrep '9200|9300'
    service elasticsearch status
    ```
3. 查看节点健康状态
   ```text
   curl http://localhost:9200/_cat/health?v
   curl http://localhost:9200/_cluster/health?pretty=true
   curl http://localhost:9200/_cluster/state
   curl -XGET 'http://localhost:9200/_cat/shards?v'
   curl http://localhost:9200/_cat/indices
   ```
4. 查看unsigned 的原因
   ```text
   _cluster/allocation/explain
   ```
5. 查看集群中不同节点、不同索引的状态
   ```text
   _cat/shards?h=index,shard,prirep,state,unassigned.reason
   ```
6. 关闭防火墙
   ```text
   systemctl stop firewalld.service
   ```
7. 查看防火墙状态
   ```text
   systemctl status  firewalld
   ```
