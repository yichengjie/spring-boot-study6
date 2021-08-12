1. ServletContextInitializerBeans构造方法中调用addAdaptableBeans注册Filter以及Servlet
    ```java
    public class ServletContextInitializerBeans extends AbstractCollection<ServletContextInitializer> {
        private static final String DISPATCHER_SERVLET_NAME = "dispatcherServlet";
        public ServletContextInitializerBeans(ListableBeanFactory beanFactory,
                Class<? extends ServletContextInitializer>... initializerTypes) {
            this.initializers = new LinkedMultiValueMap<>();
            this.initializerTypes = (initializerTypes.length != 0) ? Arrays.asList(initializerTypes)
                    : Collections.singletonList(ServletContextInitializer.class);
            addServletContextInitializerBeans(beanFactory);
            // 注册Filter与Servlet
            addAdaptableBeans(beanFactory);
            List<ServletContextInitializer> sortedInitializers = this.initializers.values().stream()
                    .flatMap((value) -> value.stream().sorted(AnnotationAwareOrderComparator.INSTANCE))
                    .collect(Collectors.toList());
            this.sortedList = Collections.unmodifiableList(sortedInitializers);
            logMappings(this.initializers);
        }
        // 搜集Spring容器中所有的Servlet、Filter对象，转化为RegistrationBean对象，注册到Spring容器中
        // ★★★注意：RegistrationBean实现了ServletContextInitializer接口，被容器自动调用，是Filter能被注册成web组件的关键
        @SuppressWarnings("unchecked")
        protected void addAdaptableBeans(ListableBeanFactory beanFactory) {
            MultipartConfigElement multipartConfig = getMultipartConfig(beanFactory);
            addAsRegistrationBean(beanFactory, Servlet.class, new ServletRegistrationBeanAdapter(multipartConfig));
            // 注册Filter
            addAsRegistrationBean(beanFactory, Filter.class, new FilterRegistrationBeanAdapter());
            for (Class<?> listenerType : ServletListenerRegistrationBean.getSupportedTypes()) {
                addAsRegistrationBean(beanFactory, EventListener.class, (Class<EventListener>) listenerType,
                        new ServletListenerRegistrationBeanAdapter());
            }
        }
        // 注册Bean
        protected <T> void addAsRegistrationBean(ListableBeanFactory beanFactory, Class<T> type,
                RegistrationBeanAdapter<T> adapter) {
            addAsRegistrationBean(beanFactory, type, type, adapter);
        }
        // 通过RegistrationBean将Filter、Servlet添加到Tomcat容器中
        private <T, B extends T> void addAsRegistrationBean(ListableBeanFactory beanFactory, Class<T> type,
                    Class<B> beanType, RegistrationBeanAdapter<T> adapter) {
                List<Map.Entry<String, B>> entries = getOrderedBeansOfType(beanFactory, beanType, this.seen);
                for (Entry<String, B> entry : entries) {
                    String beanName = entry.getKey();
                    B bean = entry.getValue();
                    if (this.seen.add(bean)) {
                        RegistrationBean registration = adapter.createRegistrationBean(beanName, bean, entries.size());
                        int order = getOrder(bean);
                        registration.setOrder(order);
                        this.initializers.add(type, registration);
                        if (logger.isTraceEnabled()) {
                            logger.trace("Created " + type.getSimpleName() + " initializer for bean '" + beanName + "'; order="
                                    + order + ", resource=" + getResourceDescription(beanName, beanFactory));
                        }
                    }
                }
            }
    }   
    private static class FilterRegistrationBeanAdapter implements RegistrationBeanAdapter<Filter> {
        @Override
        public RegistrationBean createRegistrationBean(String name, Filter source, int totalNumberOfSourceBeans) {
            FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>(source);
            bean.setName(name);
            return bean;
        }
    }
    ``` 
2. 查看FilterRegistrationBean的继承结构
    ```java
    public class FilterRegistrationBean<T extends Filter> extends AbstractFilterRegistrationBean<T> {
        private T filter;
    	public FilterRegistrationBean(T filter, ServletRegistrationBean<?>... servletRegistrationBeans) {
    		super(servletRegistrationBeans);
    		Assert.notNull(filter, "Filter must not be null");
    		this.filter = filter;
    	}
    }
    public abstract class AbstractFilterRegistrationBean<T extends Filter> extends DynamicRegistrationBean<Dynamic> {
        // 将Filter注册到ServletContext对象中去
        @Override
        protected Dynamic addRegistration(String description, ServletContext servletContext) {
            Filter filter = getFilter();
            return servletContext.addFilter(getOrDeduceName(filter), filter);
        }
        @Override
        protected void configure(FilterRegistration.Dynamic registration) {
            super.configure(registration);
            EnumSet<DispatcherType> dispatcherTypes = this.dispatcherTypes;
            if (dispatcherTypes == null) {
                T filter = getFilter();
                if (ClassUtils.isPresent("org.springframework.web.filter.OncePerRequestFilter",
                        filter.getClass().getClassLoader()) && filter instanceof OncePerRequestFilter) {
                    dispatcherTypes = EnumSet.allOf(DispatcherType.class);
                }
                else {
                    dispatcherTypes = EnumSet.of(DispatcherType.REQUEST);
                }
            }
            Set<String> servletNames = new LinkedHashSet<>();
            for (ServletRegistrationBean<?> servletRegistrationBean : this.servletRegistrationBeans) {
                servletNames.add(servletRegistrationBean.getServletName());
            }
            servletNames.addAll(this.servletNames);
            if (servletNames.isEmpty() && this.urlPatterns.isEmpty()) {
                registration.addMappingForUrlPatterns(dispatcherTypes, this.matchAfter, DEFAULT_URL_MAPPINGS);
            }
            else {
                if (!servletNames.isEmpty()) {
                    registration.addMappingForServletNames(dispatcherTypes, this.matchAfter,
                            StringUtils.toStringArray(servletNames));
                }
                if (!this.urlPatterns.isEmpty()) {
                    registration.addMappingForUrlPatterns(dispatcherTypes, this.matchAfter,
                            StringUtils.toStringArray(this.urlPatterns));
                }
            }
        }    
    }
    public abstract class DynamicRegistrationBean<D extends Registration.Dynamic> extends RegistrationBean {
        @Override
            protected final void register(String description, ServletContext servletContext) {
                D registration = addRegistration(description, servletContext);
                if (registration == null) {
                    logger.info(StringUtils.capitalize(description) + " was not registered (possibly already registered?)");
                    return;
                }
                configure(registration);
            }
            protected void configure(D registration) {
                registration.setAsyncSupported(this.asyncSupported);
                if (!this.initParameters.isEmpty()) {
                    registration.setInitParameters(this.initParameters);
                }
            }
            // 等待子类实现
            protected abstract D addRegistration(String description, ServletContext servletContext);
    }
    public abstract class RegistrationBean implements ServletContextInitializer, Ordered {
        @Override
        public final void onStartup(ServletContext servletContext) throws ServletException {
            String description = getDescription();
            if (!isEnabled()) {
                logger.info(StringUtils.capitalize(description) + " was not registered (disabled)");
                return;
            }
            register(description, servletContext);
        }
    }
    @FunctionalInterface
    public interface ServletContextInitializer {
       // 容器启动的时候会被自动调用
    	void onStartup(ServletContext servletContext) throws ServletException;
    }
    ```
3. 执行流程是从ServletContextInitializer开始执行
    ```text
    3.1 容器收集所有的ServletContextInitializer并调用onStartup方法
    3.2 RegistrationBean#onStartup(ServletContext) 被调用
    3.3 DynamicRegistrationBean的register(description, servletContext)将被调用
    3.4 AbstractFilterRegistrationBean的addRegistration(description, servletContext)被调用，将Filter注册到ServletContext实例中
    ```
4. SpringBoot项目中ShiroFilterFactoryBean不需要DelegatingFilterProxy的原因就在于容器的主动注册
    ```xml
    <!--springboot项目中不需要这个配置-->
    <filter>
        <filter-name>shiroFilter</filter-name>
        <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
        <init-param>
            <param-name>targetFilterLifecycle</param-name>
            <param-value>true</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>shiroFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
    ```
6. Servlet的注册与上面基本一样

