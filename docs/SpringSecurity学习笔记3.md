1. @EnableWebSecurity注解导入WebSecurityConfiguration类，构建springSecurityFilterChain实例
    ```java
    @Configuration
    public class WebSecurityConfiguration implements ImportAware, BeanClassLoaderAware {
        private WebSecurity webSecurity;
        private Boolean debugEnabled;
        private List<SecurityConfigurer<Filter, WebSecurity>> webSecurityConfigurers;
        private ClassLoader beanClassLoader;
        @Autowired(required = false)
        private ObjectPostProcessor<Object> objectObjectPostProcessor;
        
        //1. 构建springSecurityFilterChain
        @Bean(name = AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME)
        public Filter springSecurityFilterChain() throws Exception {
            boolean hasConfigurers = webSecurityConfigurers != null
                    && !webSecurityConfigurers.isEmpty();
            if (!hasConfigurers) {
                WebSecurityConfigurerAdapter adapter = objectObjectPostProcessor
                        .postProcess(new WebSecurityConfigurerAdapter() {
                        });
                webSecurity.apply(adapter);
            }
           
            // 这里会通过层层调用，然后调用到SecurityConfigurer的init方法
            return webSecurity.build();
        }
       
        //2. 获取系统中用户自定义的SecurityConfigurer实例，并保存到webSecurity的SecurityConfigurer集合中
        @Autowired(required = false)
        public void setFilterChainProxySecurityConfigurer(
                ObjectPostProcessor<Object> objectPostProcessor,
                @Value("#{@autowiredWebSecurityConfigurersIgnoreParents.getWebSecurityConfigurers()}") List<SecurityConfigurer<Filter, WebSecurity>> webSecurityConfigurers)
                throws Exception {
            webSecurity = objectPostProcessor
                    .postProcess(new WebSecurity(objectPostProcessor));
            if (debugEnabled != null) {
                webSecurity.debug(debugEnabled);
            }
            Collections.sort(webSecurityConfigurers, AnnotationAwareOrderComparator.INSTANCE);
            Integer previousOrder = null;
            Object previousConfig = null;
            for (SecurityConfigurer<Filter, WebSecurity> config : webSecurityConfigurers) {
                Integer order = AnnotationAwareOrderComparator.lookupOrder(config);
                if (previousOrder != null && previousOrder.equals(order)) {
                    throw new IllegalStateException(
                            "@Order on WebSecurityConfigurers must be unique. Order of "
                                    + order + " was already used on " + previousConfig + ", so it cannot be used on "
                                    + config + " too.");
                }
                previousOrder = order;
                previousConfig = config;
            }
            for (SecurityConfigurer<Filter, WebSecurity> webSecurityConfigurer : webSecurityConfigurers) {
                webSecurity.apply(webSecurityConfigurer);
            }
            this.webSecurityConfigurers = webSecurityConfigurers;
        }
    }
    ```
2. webSecurity.build() => AbstractConfiguredSecurityBuilder#doBuild() 调用SecurityConfigurer的init与configure方法,
最后调用WebSecurity#performBuild方法构建了FilterChainProxy对象并返回
    ```java
    public abstract class AbstractConfiguredSecurityBuilder<O, B extends SecurityBuilder<O>>
            extends AbstractSecurityBuilder<O> {
        @Override
        protected final O doBuild() throws Exception {
            synchronized (configurers) {
                buildState = BuildState.INITIALIZING;
                beforeInit();
                init();   // 1. 调用SecurityConfigurer的init方法
                buildState = BuildState.CONFIGURING;
                beforeConfigure();
                configure(); // 2. 调用SecurityConfigurer的configure方法
                buildState = BuildState.BUILDING;
                O result = performBuild(); // 3. 构建springSecurityFilterChain对象
                buildState = BuildState.BUILT;
                return result;
            }
        }
    }
    ```
3. 构建springSecurityFilterChain对象
    ```java
    public final class WebSecurity extends
            AbstractConfiguredSecurityBuilder<Filter, WebSecurity> implements
            SecurityBuilder<Filter>, ApplicationContextAware {
        @Override
        protected Filter performBuild() throws Exception {
            Assert.state(
                    !securityFilterChainBuilders.isEmpty(),
                    () -> "At least one SecurityBuilder<? extends SecurityFilterChain> needs to be specified. "
                            + "Typically this done by adding a @Configuration that extends WebSecurityConfigurerAdapter. "
                            + "More advanced users can invoke "
                            + WebSecurity.class.getSimpleName()
                            + ".addSecurityFilterChainBuilder directly");
            int chainSize = ignoredRequests.size() + securityFilterChainBuilders.size();
            List<SecurityFilterChain> securityFilterChains = new ArrayList<>(
                    chainSize);
            for (RequestMatcher ignoredRequest : ignoredRequests) {
                securityFilterChains.add(new DefaultSecurityFilterChain(ignoredRequest));
            }
            for (SecurityBuilder<? extends SecurityFilterChain> securityFilterChainBuilder : securityFilterChainBuilders) {
                securityFilterChains.add(securityFilterChainBuilder.build());
            }
            FilterChainProxy filterChainProxy = new FilterChainProxy(securityFilterChains);
            if (httpFirewall != null) {
                filterChainProxy.setFirewall(httpFirewall);
            }
            filterChainProxy.afterPropertiesSet();
            Filter result = filterChainProxy;
            if (debugEnabled) {
                logger.warn("\n\n"
                        + "********************************************************************\n"
                        + "**********        Security debugging is enabled.       *************\n"
                        + "**********    This may include sensitive information.  *************\n"
                        + "**********      Do not use in a production system!     *************\n"
                        + "********************************************************************\n\n");
                result = new DebugFilter(filterChainProxy);
            }
            postBuildAction.run();
            return result;
        }	
    }
    ```
4. WebSecurityConfigurerAdapter#init => webSecurity#addSecurityFilterChainBuilder(httpSecurity)
5. FilterChainProxy的filter方法中通过构建VirtualFilterChain对象将SpringSecurity中所有的Filter对象串联起来
6. 在spring.factories中自动配置类UserDetailsServiceAutoConfiguration的的启动
    ```java
    @Configuration
    @ConditionalOnClass(AuthenticationManager.class)
    @ConditionalOnBean(ObjectPostProcessor.class)
    @ConditionalOnMissingBean({ AuthenticationManager.class, AuthenticationProvider.class,
            UserDetailsService.class })
    public class UserDetailsServiceAutoConfiguration {
        @Bean
        @ConditionalOnMissingBean(type = "org.springframework.security.oauth2.client.registration.ClientRegistrationRepository")
        @Lazy
        public InMemoryUserDetailsManager inMemoryUserDetailsManager(
                SecurityProperties properties,
                ObjectProvider<PasswordEncoder> passwordEncoder) {
            SecurityProperties.User user = properties.getUser();
            List<String> roles = user.getRoles();
            return new InMemoryUserDetailsManager(User.withUsername(user.getName())
                    .password(getOrDeducePassword(user, passwordEncoder.getIfAvailable()))
                    .roles(StringUtils.toStringArray(roles)).build());
        }
    }
    ```
4. 在AbstractAuthenticationFilterConfigurer的核心方法configure中配置filter的AuthenticationManager
    ```java
    // FormLoginConfigurer继承AbstractAuthenticationFilterConfigurer
    public abstract class AbstractAuthenticationFilterConfigurer<B extends HttpSecurityBuilder<B>, T extends AbstractAuthenticationFilterConfigurer<B, T, F>, F extends AbstractAuthenticationProcessingFilter>
            extends AbstractHttpConfigurer<T, B> {
        @Override
        public void configure(B http) throws Exception {
            PortMapper portMapper = http.getSharedObject(PortMapper.class);
            if (portMapper != null) {
                authenticationEntryPoint.setPortMapper(portMapper);
            }
            RequestCache requestCache = http.getSharedObject(RequestCache.class);
            if (requestCache != null) {
                this.defaultSuccessHandler.setRequestCache(requestCache);
            }
            authFilter.setAuthenticationManager(http
                    .getSharedObject(AuthenticationManager.class));
            authFilter.setAuthenticationSuccessHandler(successHandler);
            authFilter.setAuthenticationFailureHandler(failureHandler);
            if (authenticationDetailsSource != null) {
                authFilter.setAuthenticationDetailsSource(authenticationDetailsSource);
            }
            SessionAuthenticationStrategy sessionAuthenticationStrategy = http
                    .getSharedObject(SessionAuthenticationStrategy.class);
            if (sessionAuthenticationStrategy != null) {
                authFilter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);
            }
            RememberMeServices rememberMeServices = http
                    .getSharedObject(RememberMeServices.class);
            if (rememberMeServices != null) {
                authFilter.setRememberMeServices(rememberMeServices);
            }
            F filter = postProcess(authFilter);
            http.addFilter(filter);
        }
    }
    ```
