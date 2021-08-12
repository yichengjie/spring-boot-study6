#### ImportAware的使用
1. 编写注解类
    ```java
    @Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
    @Target(value = { java.lang.annotation.ElementType.TYPE })
    @Documented
    // 这里使用@Import导入ImportAware的实现类WebSecurityConfiguration
    @Import({ WebSecurityConfiguration.class,SpringWebMvcImportSelector.class })
    @EnableGlobalAuthentication
    @Configuration
    public @interface EnableWebSecurity {
        boolean debug() default false;
    }
    ```
2. 编写ImportAware实现类
    ```java
    @Configuration
    public class WebSecurityConfiguration implements ImportAware, BeanClassLoaderAware {
        public void setImportMetadata(AnnotationMetadata importMetadata) {
            Map<String, Object> enableWebSecurityAttrMap = importMetadata
                    .getAnnotationAttributes(EnableWebSecurity.class.getName());
            AnnotationAttributes enableWebSecurityAttrs = AnnotationAttributes
                    .fromMap(enableWebSecurityAttrMap);
            debugEnabled = enableWebSecurityAttrs.getBoolean("debug");
            if (webSecurity != null) {
                webSecurity.debug(debugEnabled);
            }
        }
    }
    ```
#### 确保对象只被创建一次
1. 编写公用父类
    ```java
    public abstract class AbstractSecurityBuilder<O> implements SecurityBuilder<O> {
        private AtomicBoolean building = new AtomicBoolean();
        private O object;
        public AbstractSecurityBuilder() {
        }
        public final O build() throws Exception {
            if (this.building.compareAndSet(false, true)) {
                this.object = this.doBuild();
                return this.object;
            } else {
                throw new AlreadyBuiltException("This object has already been built");
            }
        }
        public final O getObject() {
            if (!this.building.get()) {
                throw new IllegalStateException("This object has not been built");
            } else {
                return this.object;
            }
        }
        protected abstract O doBuild() throws Exception;
    }
    ```
#### DelegatingFilterProxy与Shiro的Filter整合
1. DelegatingFilterProxy源码
    ```java
    public class DelegatingFilterProxy extends GenericFilterBean {
        @Nullable
        private String contextAttribute;
        @Nullable
        private WebApplicationContext webApplicationContext;
        @Nullable
        private String targetBeanName;
        private boolean targetFilterLifecycle;
        @Nullable
        private volatile Filter delegate;
        private final Object delegateMonitor;
        public DelegatingFilterProxy() {
            this.targetFilterLifecycle = false;
            this.delegateMonitor = new Object();
        }
        public DelegatingFilterProxy(Filter delegate) {
            this.targetFilterLifecycle = false;
            this.delegateMonitor = new Object();
            Assert.notNull(delegate, "Delegate Filter must not be null");
            this.delegate = delegate;
        }
        public DelegatingFilterProxy(String targetBeanName) {
            this(targetBeanName, (WebApplicationContext)null);
        }
        public DelegatingFilterProxy(String targetBeanName, @Nullable WebApplicationContext wac) {
            this.targetFilterLifecycle = false;
            this.delegateMonitor = new Object();
            Assert.hasText(targetBeanName, "Target Filter bean name must not be null or empty");
            this.setTargetBeanName(targetBeanName);
            this.webApplicationContext = wac;
            if (wac != null) {
                this.setEnvironment(wac.getEnvironment());
            }
        }
        protected void initFilterBean() throws ServletException {
            synchronized(this.delegateMonitor) {
                if (this.delegate == null) {
                    if (this.targetBeanName == null) {
                        this.targetBeanName = this.getFilterName();
                    }
                    WebApplicationContext wac = this.findWebApplicationContext();
                    if (wac != null) {
                        this.delegate = this.initDelegate(wac);
                    }
                }
            }
        }
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            Filter delegateToUse = this.delegate;
            if (delegateToUse == null) {
                synchronized(this.delegateMonitor) {
                    delegateToUse = this.delegate;
                    if (delegateToUse == null) {
                        WebApplicationContext wac = this.findWebApplicationContext();
                        if (wac == null) {
                            throw new IllegalStateException("No WebApplicationContext found: no ContextLoaderListener or DispatcherServlet registered?");
                        }
                        delegateToUse = this.initDelegate(wac);
                    }
                    this.delegate = delegateToUse;
                }
            }
            this.invokeDelegate(delegateToUse, request, response, filterChain);
        }
        public void destroy() {
            Filter delegateToUse = this.delegate;
            if (delegateToUse != null) {
                this.destroyDelegate(delegateToUse);
            }
        }
        @Nullable
        protected WebApplicationContext findWebApplicationContext() {
            if (this.webApplicationContext != null) {
                if (this.webApplicationContext instanceof ConfigurableApplicationContext) {
                    ConfigurableApplicationContext cac = (ConfigurableApplicationContext)this.webApplicationContext;
                    if (!cac.isActive()) {
                        cac.refresh();
                    }
                }
                return this.webApplicationContext;
            } else {
                String attrName = this.getContextAttribute();
                return attrName != null ? WebApplicationContextUtils.getWebApplicationContext(this.getServletContext(), attrName) : WebApplicationContextUtils.findWebApplicationContext(this.getServletContext());
            }
        }
        protected Filter initDelegate(WebApplicationContext wac) throws ServletException {
            String targetBeanName = this.getTargetBeanName();
            Assert.state(targetBeanName != null, "No target bean name set");
            Filter delegate = (Filter)wac.getBean(targetBeanName, Filter.class);
            if (this.isTargetFilterLifecycle()) {
                delegate.init(this.getFilterConfig());
            }
            return delegate;
        }
        protected void invokeDelegate(Filter delegate, ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            delegate.doFilter(request, response, filterChain);
        }
        protected void destroyDelegate(Filter delegate) {
            if (this.isTargetFilterLifecycle()) {
                delegate.destroy();
            }
        }
         public void setContextAttribute(@Nullable String contextAttribute) {
            this.contextAttribute = contextAttribute;
        }
    
        @Nullable
        public String getContextAttribute() {
            return this.contextAttribute;
        }
        public void setTargetBeanName(@Nullable String targetBeanName) {
            this.targetBeanName = targetBeanName;
        }
        @Nullable
        protected String getTargetBeanName() {
            return this.targetBeanName;
        }
        public void setTargetFilterLifecycle(boolean targetFilterLifecycle) {
            this.targetFilterLifecycle = targetFilterLifecycle;
        }
        protected boolean isTargetFilterLifecycle() {
            return this.targetFilterLifecycle;
        }
    }
    ```
