1. 查找指定文件名
    ```text
    # -name 后加文件名称
    find ~/ -name 326006.png
    ```
2. 查看端口对应的进程ID
   ```text
   netstat -tunlp|grep 5601
   ```
3. 查看linux内存使用情况
   ```text
   cat /proc/meminfo
   ```