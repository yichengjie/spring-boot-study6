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
    a. InternalResourceViewResolver
    b. ThymeleafViewResolver
    ..
    ```