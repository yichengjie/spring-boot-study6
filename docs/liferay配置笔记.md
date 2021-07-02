1. 首次登录不强制修改密码
   ```text
   1.1 portal-ext.properties中添加配置 passwords.default.policy.change.required=false
   1.2 控制面板-安全性-密码策略, 点击【Default Password Policy】, 密码更改中，要求更改修改为【否】
   ```
2. 页面在导航栏不显示（例如404页面）
   ```text
   点击配置页面-常规的-从导航菜单微件隐藏 选中
   ```
3. nginx启用https反向代理，部分css和js加载报错(请求地址为http)
   ```text
   //portal-ext.properties 中添加配置
   web.server.protocol=https
   ```
4. 关闭用户条款
   ```text
   //portal-ext.properties 中添加配置
   terms.of.use.required=false
   ```
#### liferay站点配置默认404页面
1. 使用管理员账号登录liferay后台，在对应站点下创建404页面
   ```text
   5.1 登录liferay后台，在对应站点的公开页面下建立404页面（类型：嵌入式）
   5.2 配置友好url为/nofindpage
   5.3 URL配置为真实的404地址即可
   ```
2. 在portal-ext.properties中添加配置（注意这里的nofindpage与前面的友好url对应）
   ```text
   layout.friendly.url.page.not.found=/web/hello/nofindpage
   sites.friendly.url.page.not.found=/web/hello/nofindpage
   ```