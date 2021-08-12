1. 为了对请求进行拦截，Spring Security提供了过滤器DelegatingFilterProxy类给与开发者配置。
2. 项目中只需要添加@EnableWebSecurity注解就可以驱动Spring Security了
3. WebSecurityConfiguration#springSecurityFilterChain()中初始化FilterChainProxy,
    ```text
    a.名称为springSecurityFilterChain
    b.构造传入List为DefaultSecurityFilterChain
    ```
4. SecurityFilterAutoConfiguration#securityFilterChainRegistration()注册DelegatingFilterProxy,
   并关联FilterChainProxy
   ```text
   a. DelegatingFilterProxyRegistrationBean(targetBeanName)
   c. DelegatingFilterProxy根据targetBeanName获取FilterChainProxy，作为delegate对象保存
   ```
5. 在Spring Security执行过程中，会调用DelegatingFilterProxy#doFilter
6. 自定义WebSecurityConfigurerAdapter实现
    ```java
    @Configuration
    public class MyWebSecurityConfigurer extends WebSecurityConfigurerAdapter {
        @Bean
        public PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder() ;
        }
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> userConfig =
                    auth.inMemoryAuthentication().passwordEncoder(passwordEncoder());
            userConfig.withUser("admin")
                    .password(passwordEncoder().encode("123"))
                    .roles("USER","ADMIN");
            userConfig.withUser("user")
                    .password(passwordEncoder().encode("123"))
                    //使用authorities需要加‘ROLE_’前缀
                    .authorities("ROLE_USER");
        }
    }
    ```
#### 部分流程介绍
1. 通过webSecurity.build()构建springSecurityFilterChain时，初始化自定义WebSecurityConfigurerAdapter
    ```text
    a. 获取所有的SecurityConfigurer
    c. 调用configure的init(WebSecurity web)方法
    ```
2. 自定义WebSecurityConfigurerAdapter执行流程
    ```text
    a. 执行init(final WebSecurity web) => getHttp() => authenticationManager() => configure(auth)
    ```
3. getHttp执行流程
    ```text
    a. 获取authenticationManager并配置
    b. http = new HttpSecurity(objectPostProcessor, authenticationBuilder, sharedObjects)
    c. http中加入默认配置eg：CsrfConfigurer,ExceptionHandlingConfigurer,AnonymousConfigurer等
    d. 调用configure(HttpSecurity http)配置http
    ```
5. DefaultSecurityFilterChain关联的filters集合
    ```
    0 = {WebAsyncManagerIntegrationFilter@5388} 
    1 = {SecurityContextPersistenceFilter@5389} 
    2 = {HeaderWriterFilter@5390} 
    3 = {CsrfFilter@5391} 
    4 = {LogoutFilter@5392} 
    5 = {UsernamePasswordAuthenticationFilter@5393} 
    6 = {DefaultLoginPageGeneratingFilter@5394} 
    7 = {BasicAuthenticationFilter@5395} 
    8 = {RequestCacheAwareFilter@5396} 
    9 = {SecurityContextHolderAwareRequestFilter@5397} 
    10 = {AnonymousAuthenticationFilter@5398} 
    11 = {SessionManagementFilter@5399} 
    12 = {ExceptionTranslationFilter@5400} 
    13 = {FilterSecurityInterceptor@5401} 
    ```
