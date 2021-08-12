1. 入口从FilterChainProxy的doFilter开始
2. FilterChainProxy的doFilter中通过DefaultSecurityFilterChain中获取所有的filters通过VirtualFilterChain将filter组装起来
    ```text
    0 = {WebAsyncManagerIntegrationFilter@6012} 
    1 = {SecurityContextPersistenceFilter@6011} 
    2 = {HeaderWriterFilter@6010} 
    3 = {CsrfFilter@6009} 
    4 = {LogoutFilter@6005} 
    5 = {UsernamePasswordAuthenticationFilter@6003} 
    6 = {DefaultLoginPageGeneratingFilter@6138} 
    7 = {ConcurrentSessionFilter@6142} 
    8 = {BasicAuthenticationFilter@6143} 
    9 = {RequestCacheAwareFilter@6144} 
    10 = {SecurityContextHolderAwareRequestFilter@6145} 
    11 = {AnonymousAuthenticationFilter@6268} 
    12 = {SessionManagementFilter@6269} 
    13 = {ExceptionTranslationFilter@6270} 
    14 = {FilterSecurityInterceptor@6271} 
    ```
3. 其他
    ```text
    AutowireBeanFactoryObjectPostProcessor
    ```