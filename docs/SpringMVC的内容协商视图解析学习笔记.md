1. 视图解析其：ContentNegotiatingViewResolver
    ```text
    a. 关联ViewResolver Bean列表
    b. 关联ContentNegotiationManager Bean
    c. 解析最佳匹配View
    ```
2. 内容协商管理器：ContentNegotiationManager
    ```text
    a. 有ContentNegotiationConfigurer配置
    b. 由ContentNegotiationManagerFactoryBean 创建
    c. 关联ContentNegotiationStrategy集合
    ```
3. 内容协商策略：ContentNegotiationStrategy
    ```text
    a. PathExtensionContentNegotiationStrategy
    b. ParameterContentNegotiationStrategy
    c. FixedContentNegotiationStrategy
    d. HeaderContentNegotiationStrategy
    e. ServletPathExtensionContentNegotiationStrategy
    ```
4. ViewResolver Bean列表
    ```text
   0 = {ContentNegotiatingViewResolver@5836} 
   1 = {InternalResourceViewResolver@5842} 
   2 = {BeanNameViewResolver@5843} 
   3 = {ViewResolverComposite@5844} 
   4 = {InternalResourceViewResolver@5845} 
    ```
5. DispatcherServlet#doDispatch => processDispatchResult => render => resolveViewName然后调用
   ContentNegotiatingViewResolver#resolveViewName(viewName, locale)获取View对象
#### ContentNegotiatingViewResolver#resolveViewName获取View对象流程
1. ContentNegotiatingViewResolver#getMediaTypes(HttpServletRequest request)获取MediaType集合
    ```text
    1.1  ContentNegotiationManager#resolveMediaTypes从request中获取支持的MediaType集合，
         当有一个ContentNegotiationStrategy返回不为空，则直接return，存为acceptableMediaTypes集合，
         eg：application/xml
    1.2  ContentNegotiationManager#getProducibleMediaTypes从request中获取支持的MediaType集合，通常返回*/* 
    1.3  遍历1.1中的结果集与1.2中结果集如果匹配则保存并返回集合，eg：application/xml
    ```
2. ContentNegotiatingViewResolver#getCandidateViews根据viewName和requestedMediaTypes获取备选View
    ```text
    1.1 this.contentNegotiationManager.resolveFileExtensions(requestedMediaType)获取extension。eg：xml
    1.2 遍历viewResolvers通过viewResolver#resolveViewName((viewName)获取view对象，并添加到备选view集合
    1.3 遍历viewResolvers通过viewResolver#resolveViewName(viewName + '.' + extension)并添加到备选view集合
    ```
3. ContentNegotiatingViewResolver#getBestView根据requestedMediaTypes筛选candidateViews
    ```text
    1.1 遍历candidateViews通过candidateView#getContentType并转换为MediaType对象，判断是否与requestedMediaTypes匹配
    1.2 如果1.1 匹配则返回candidateView
    ```
