#### ServletContainerInitializer加载原理及过程
1. Servlet3.x规范利用spi机制，自动加载实现ServletContainerInitializer接口的类,并调用onStartup方法。
2. SpringServletContainerInitializer根据@HandlesTypes(WebApplicationInitializer.class)进行class筛选。
3. 在onStartup中调用所有WebApplicationInitializer对象的onStart方法。
4. SpringServletContainerInitializer代码实现
	 ```java
	@HandlesTypes(WebApplicationInitializer.class)
	public class SpringServletContainerInitializer implements ServletContainerInitializer {
		@Override
		public void onStartup(@Nullable Set<Class<?>> webAppInitializerClasses, ServletContext servletContext)throws ServletException {
			List<WebApplicationInitializer> initializers = new LinkedList<>();
			if (webAppInitializerClasses != null) {
				for (Class<?> waiClass : webAppInitializerClasses) {
					if (!waiClass.isInterface() && !Modifier.isAbstract(waiClass.getModifiers()) &&
							WebApplicationInitializer.class.isAssignableFrom(waiClass)) {
						try {
							initializers.add((WebApplicationInitializer)
							ReflectionUtils.accessibleConstructor(waiClass).newInstance());
						}
						catch (Throwable ex) {
							throw new ServletException("Failed to instantiate WebApplicationInitializer class", ex);
						}
					}
				}
			}
			if (initializers.isEmpty()) {
				servletContext.log("No Spring WebApplicationInitializer types detected on classpath");
				return;
			}
			servletContext.log(initializers.size() + " Spring WebApplicationInitializers detected on classpath");
			AnnotationAwareOrderComparator.sort(initializers);
			for (WebApplicationInitializer initializer : initializers) {
				initializer.onStartup(servletContext);
			}
		}
	
	}
	 ```
#### ServletContextInitializer加载原理及过程
1. ServletWebServerApplicationContext容器刷新方法onRefresh() 调用 createWebServer() 创建Web服务
2. 创建容器时，通过getSelfInitializer() => getServletContextInitializerBeans() 获取所有的ServletContextInitializer
3. 遍历ServletContextInitializer实例并调用onStartup(servletContext)方法 
#### 补充说明
1. 在嵌入式容器中ServletContainerInitializer将不会被自动加载，替代方案可使用ServletContextInitializer解决
	```text
	@Bean
    public ServletContextInitializer servletContextInitializer(){
       return servletContext ->{
           CharacterEncodingFilter filter = new CharacterEncodingFilter() ;
           FilterRegistration.Dynamic dynamic = servletContext.addFilter("c-filter", filter);
           dynamic.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false,"/");
       } ;
    }
	```
2. 在嵌入式容器中@WebServlet不会被自动扫描，替代方案可使用@ServletRegistrationBean解决
	```text
	// ServletRegistrationBean 继承ServletContextInitializer，所以这里与2中效果等同
	@Bean
	public ServletRegistrationBean servletRegistrationBean(){
	     ServletRegistrationBean registrationBean =
	             new ServletRegistrationBean(new AsyncServlet(), "/async-servlet") ;
	     return registrationBean ;
	 }
	```
3. Springboot应用部署到传统servlet容器也是利用ServletContainerInitializer的SPI实现
    ```java
    public abstract class SpringBootServletInitializer implements WebApplicationInitializer {
        protected Log logger; // Don't initialize early
        private boolean registerErrorPageFilter = true;
        protected final void setRegisterErrorPageFilter(boolean registerErrorPageFilter) {
            this.registerErrorPageFilter = registerErrorPageFilter;
        }
        @Override
        public void onStartup(ServletContext servletContext) throws ServletException {
            this.logger = LogFactory.getLog(getClass());
            WebApplicationContext rootAppContext = createRootApplicationContext(
                    servletContext);
            if (rootAppContext != null) {
                servletContext.addListener(new ContextLoaderListener(rootAppContext) {
                    @Override
                    public void contextInitialized(ServletContextEvent event) {
                        // no-op because the application context is already initialized
                    }
                });
            }
            else {
                this.logger.debug("No ContextLoaderListener registered, as "
                        + "createRootApplicationContext() did not "
                        + "return an application context");
            }
        }
        protected WebApplicationContext createRootApplicationContext(
                ServletContext servletContext) {
            SpringApplicationBuilder builder = createSpringApplicationBuilder();
            StandardServletEnvironment environment = new StandardServletEnvironment();
            environment.initPropertySources(servletContext, null);
            builder.environment(environment);
            builder.main(getClass());
            ApplicationContext parent = getExistingRootWebApplicationContext(servletContext);
            if (parent != null) {
                this.logger.info("Root context already created (using as parent).");
                servletContext.setAttribute(
                        WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, null);
                builder.initializers(new ParentContextApplicationContextInitializer(parent));
            }
            builder.initializers(
                    new ServletContextApplicationContextInitializer(servletContext));
            builder.contextClass(AnnotationConfigServletWebServerApplicationContext.class);
            builder = configure(builder);
            SpringApplication application = builder.build();
            if (application.getAllSources().isEmpty() && AnnotationUtils
                    .findAnnotation(getClass(), Configuration.class) != null) {
                application.addPrimarySources(Collections.singleton(getClass()));
            }
            Assert.state(!application.getAllSources().isEmpty(),
                    "No SpringApplication sources have been defined. Either override the "
                            + "configure method or add an @Configuration annotation");
            // Ensure error pages are registered
            if (this.registerErrorPageFilter) {
                application.addPrimarySources(
                        Collections.singleton(ErrorPageFilterConfiguration.class));
            }
            return run(application);
        }
        protected SpringApplicationBuilder createSpringApplicationBuilder() {
            return new SpringApplicationBuilder();
        }
        protected WebApplicationContext run(SpringApplication application) {
            return (WebApplicationContext) application.run();
        }
        private ApplicationContext getExistingRootWebApplicationContext(
                ServletContext servletContext) {
            Object context = servletContext.getAttribute(
                    WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
            if (context instanceof ApplicationContext) {
                return (ApplicationContext) context;
            }
            return null;
        }
        protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
            return builder;
        }
    }
    ```
