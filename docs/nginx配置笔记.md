1. 简单配置
    ```text
    server {
        listen 9090;
        location / {
            proxy_pass http://127.0.0.1:8080;
        }
    }
    ```   
2. 有绝对地址的配置
    ```text
    server {
        listen 9090;
        location / {
            proxy_pass http://127.0.0.1:8080;
            proxy_set_header Host $host:$server_port;
            proxy_redirect http://127.0.0.1:8080/ http://$host:$server_port/;
        }
    }
    ```