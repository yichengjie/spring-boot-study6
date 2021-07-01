1. server 80 添加server_name和rewrite
    ```text
    server {
        listen       80;
        server_name  www.yicj1.com;	
        rewrite ^(.*)$ https://$server_name$1 permanent;
        client_max_body_size 1024m;
        ...
    }
    ```
2. server 443 添加证书配置
    ```text
    server {
        listen 443 ssl;
        server_name www.yicj1.com;
        ssl_certificate D://install//nginx-1.14.2//ssl//shidian.crt;
        ssl_certificate_key D://install//nginx-1.14.2//ssl//shidian.key;
        ssl_session_cache    shared:SSL:1m;
        ssl_session_timeout  5m;
        ssl_ciphers  HIGH:!aNULL:!MD5;
        ssl_prefer_server_ciphers  on;	
        location / {
            add_header Content-Security-Policy upgrade-insecure-requests;
            proxy_set_header X-Forwarded-Proto $scheme;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header Host $host;
            proxy_pass http://127.0.0.1:8080;
            proxy_redirect http://127.0.0.1:8080/ https://$host/;
        }
    }
    ```