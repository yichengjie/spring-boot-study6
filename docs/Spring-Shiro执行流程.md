1. Spring容器中注册ShiroFilterFactoryBean
    ```java
    public class ShiroFilterFactoryBean implements FactoryBean, BeanPostProcessor {
      public Object getObject() throws Exception {
         if (instance == null) {
             instance = createInstance();
         }
         return instance;
      }
      
      protected AbstractShiroFilter createInstance() throws Exception {
          log.debug("Creating Shiro Filter instance.");
          SecurityManager securityManager = getSecurityManager();
          if (securityManager == null) {
              String msg = "SecurityManager property must be set.";
              throw new BeanInitializationException(msg);
          }
          if (!(securityManager instanceof WebSecurityManager)) {
              String msg = "The security manager does not implement the WebSecurityManager interface.";
              throw new BeanInitializationException(msg);
          }
          FilterChainManager manager = createFilterChainManager();
          PathMatchingFilterChainResolver chainResolver = new PathMatchingFilterChainResolver();
          chainResolver.setFilterChainManager(manager);
          return new SpringShiroFilter((WebSecurityManager) securityManager, chainResolver);
      }
      protected FilterChainManager createFilterChainManager() {
          DefaultFilterChainManager manager = new DefaultFilterChainManager();
          Map<String, Filter> defaultFilters = manager.getFilters();
          //apply global settings if necessary:
          for (Filter filter : defaultFilters.values()) {
              applyGlobalPropertiesIfNecessary(filter);
          }
          Map<String, Filter> filters = getFilters();
          if (!CollectionUtils.isEmpty(filters)) {
              for (Map.Entry<String, Filter> entry : filters.entrySet()) {
                  String name = entry.getKey();
                  Filter filter = entry.getValue();
                  applyGlobalPropertiesIfNecessary(filter);
                  if (filter instanceof Nameable) {
                      ((Nameable) filter).setName(name);
                  }
                  manager.addFilter(name, filter, false);
              }
          }
          manager.setGlobalFilters(this.globalFilters);
          Map<String, String> chains = getFilterChainDefinitionMap();
          if (!CollectionUtils.isEmpty(chains)) {
              for (Map.Entry<String, String> entry : chains.entrySet()) {
                  String url = entry.getKey();
                  String chainDefinition = entry.getValue();
                  manager.createChain(url, chainDefinition);
              }
          }
          manager.createDefaultChain("/**"); // TODO this assumes ANT path matching, which might be OK here
          return manager;
      }
    }
    ```
2. 默认Filter集合
    ```text
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
    ```