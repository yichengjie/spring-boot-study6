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
