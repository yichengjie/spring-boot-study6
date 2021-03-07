1. 为了对请求进行拦截，Spring Security提供了过滤器DelegatingFilterProxy类给与开发者配置。
2. 项目中只需要添加@EnableWebSecurity注解就可以驱动Spring Security了
3. WebSecurityConfiguration中使用@Bean定义springSecurityFilterChina,类型为FilterChainProxy
    ```text
    a. FilterChainProxy的名称为springSecurityFilterChain
    b. SecurityFilterAutoConfiguration中初始化DelegatingFilterProxyRegistrationBean(targetBeanName)
    c. DelegatingFilterProxy根据targetBeanName获取FilterChainProxy，作为delegate对象保存
    ```
4. 在Spring Security操作的过程中它会提供Servlet过滤器DelegatingFilterProxy

