1. https://blog.csdn.net/baoyinwang/article/details/107099417
2. nginx部分片段（部分网站http需要重定向）
    ```text
    server {
        listen 443 ssl;
        server_name www.yicj1.com;
        ssl_certificate D://install//nginx-1.14.2//ssl//yicj1//yicj1.crt;
        ssl_certificate_key D://install//nginx-1.14.2//ssl//yicj1//yicj1.key;
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
            proxy_redirect http://$host/ https://$host/;
        }
    }
    ```
