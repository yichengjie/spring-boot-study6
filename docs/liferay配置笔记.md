### 首次登录不强制修改密码
1. portal-ext.properties中添加配置
    ```text
    passwords.default.policy.change.required=false
    ```
2. 控制面板-安全性-密码策略, 点击【Default Password Policy】, 密码更改中，要求更改修改为【否】