1. Springboot的web项目添加shiro依赖
    ```xml
    <dependency>
         <groupId>org.apache.shiro</groupId>
         <artifactId>shiro-spring</artifactId>
    </dependency>
    ```
2. 编写Shiro的主配置文件
    ```java
    @Configuration
    public class ShiroConfig {
        @Bean
        public MyRealm myRealm(){
            MyRealm myRealm = new MyRealm() ;
            // 注意这里使用了密码加密策略，所以自定义Realm中也需要使用相同策略加密
            HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher("MD5") ;
            credentialsMatcher.setHashIterations(1024);
            myRealm.setCredentialsMatcher(credentialsMatcher);
            return myRealm ;
        }
        @Bean
        public SecurityManager securityManager(){
            DefaultWebSecurityManager webSecurityManager = new DefaultWebSecurityManager() ;
            webSecurityManager.setRealm(myRealm());
            return webSecurityManager ;
        }
        @Bean
        public ShiroFilterFactoryBean shiroFilterFactoryBean(){
            ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean() ;
            shiroFilter.setSecurityManager(securityManager());
            // 这个这个地址POST方式提交作为登录处理页面
            shiroFilter.setLoginUrl("/doLogin");
            shiroFilter.setSuccessUrl("/index.html");
            shiroFilter.setUnauthorizedUrl("/unauthorized.html");
            Map<String, String> map = new HashMap<String, String>();
            map.put("/logout","logout") ;
            map.put("/admin","authc,perms[admin]") ;
            map.put("/hello","authc,perms[user]") ;
            map.put("/**", "authc");
            shiroFilter.setFilterChainDefinitionMap(map);
            return shiroFilter ;
        }
    }
    ```
3. 编写自定义Realm实现
    ```java
    public class MyRealm extends AuthorizingRealm {
        @Override
        protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            simpleAuthorizationInfo.addStringPermission("user");
            return simpleAuthorizationInfo;
        }
        @Override
        protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
            String username = token.getPrincipal().toString();
            if ("yicj".equalsIgnoreCase(username)){
                //1. 根据用户名查询数据库，获取用户信息
                //2. 根据数据库中查询到得用户信息，构建AuthenticationInfo信息并返回
                return new SimpleAuthenticationInfo("yicj", "9c9e21dfa4b664cfebc32093cb3555bb",ByteSource.Util.bytes("yicj"), getName());
            }
            return null ;
        }
    }
    ```
4. 编写Controller业务方法
    ```java
    @Slf4j
    @Controller
    public class LoginController {
        // 这里必须用RequestMapping而不能使用GetMapping
        //1. 未登录直接访问其他页面，此时会以GET方式跳转到这里
        //2. 登录用户名密码错误，此时会以POST方式跳转到这里
        @RequestMapping("/doLogin")
        public ModelAndView login(ModelAndView model, @RequestAttribute(required = false) String shiroLoginFailure) {
            //FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME
            model.setViewName("login");
            if ("org.apache.shiro.authc.UnknownAccountException".equalsIgnoreCase(shiroLoginFailure)){
                model.addObject("shiroLoginFailure", "用户名不存在！") ;
            }else if ("org.apache.shiro.authc.IncorrectCredentialsException".equalsIgnoreCase(shiroLoginFailure)){
                model.addObject("shiroLoginFailure", "密码错误！") ;
            }else if (!StringUtils.isEmpty(shiroLoginFailure)){
                model.addObject("shiroLoginFailure","登录失败!") ;
            }
            log.info("model : {}", model);
            return model;
        }
        // 业务方法1
        @ResponseBody
        @GetMapping("/hello")
        public String hello(){
            return "hello world" ;
        }
        // 业务方法2
        @ResponseBody
        @GetMapping("/admin")
        public String admin(){
            return "hello admin" ;
        }
    }
    ```
5. 编写登录页面(/templates/login.html)
    ```html
    <form action="/doLogin" method="post">
        <table>
            <tr><td>用户名：</td><td><input type="text" name="username" /></td></tr>
            <tr><td>密码：</td><td><input type="password" name="password" /></td></tr>
            <tr><td colspan="2"><input type="submit" value="登录" /></td></tr>
            <tr><td colspan="2" th:text="${shiroLoginFailure}" class="red"></td></tr>
        </table>
    </form>
    ```
6. 其他页面(/static/index.html,/static/unauthorized.html)
    ```html
    <!--index.html-->
    <body>
        <h2>index ...</h2>
    </body>
    <!--index.html-->
    <body>
        <h2>unauthorized !</h2>
    </body>
    ```
7. 补充知识点
    ```java
    //默认过滤器集合
    public enum DefaultFilter {
        anon(AnonymousFilter.class),
        authc(FormAuthenticationFilter.class),
        authcBasic(BasicHttpAuthenticationFilter.class),
        authcBearer(BearerHttpAuthenticationFilter.class),
        logout(LogoutFilter.class),
        noSessionCreation(NoSessionCreationFilter.class),
        perms(PermissionsAuthorizationFilter.class),
        port(PortFilter.class),
        rest(HttpMethodPermissionFilter.class),
        roles(RolesAuthorizationFilter.class),
        ssl(SslFilter.class),
        user(UserFilter.class),
        invalidRequest(InvalidRequestFilter.class);
    }
    ```
