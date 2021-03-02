1. RequestMappingHandlerAdapter.invokeHandlerMethod 
   => ServletInvocableHandlerMethod.invokeAndHandle 
   => InvocableHandlerMethod.invokeForRequest
   => HandlerMethodArgumentResolverComposite.resolveArgument
2. resolveArgument解析controller方法的每一个参数类型放入MethodParameter[]数组中
3. 遍历MethodParameter对象，使用HandlerMethodArgumentResolverComposite.supportsParameter(MethodParameter parameter)
4. HandlerMethodArgumentResolverComposite.getArgumentResolver获取能处理MethodParameter的XXXResolver，
   注意RequestResponseBodyMethodProcessor的实现
    ```text
    0 = {RequestParamMethodArgumentResolver@6275} 
    1 = {RequestParamMapMethodArgumentResolver@6276} 
    2 = {PathVariableMethodArgumentResolver@6277} 
    3 = {PathVariableMapMethodArgumentResolver@6278} 
    4 = {MatrixVariableMethodArgumentResolver@6279} 
    5 = {MatrixVariableMapMethodArgumentResolver@6280} 
    6 = {ServletModelAttributeMethodProcessor@6281} 
    7 = {RequestResponseBodyMethodProcessor@6282} 
    8 = {RequestPartMethodArgumentResolver@6283} 
    9 = {RequestHeaderMethodArgumentResolver@6284} 
    10 = {RequestHeaderMapMethodArgumentResolver@6285} 
    11 = {ServletCookieValueMethodArgumentResolver@6286} 
    12 = {ExpressionValueMethodArgumentResolver@6287} 
    13 = {SessionAttributeMethodArgumentResolver@6288} 
    14 = {RequestAttributeMethodArgumentResolver@6289} 
    15 = {ServletRequestMethodArgumentResolver@6290} 
    16 = {ServletResponseMethodArgumentResolver@6291} 
    17 = {HttpEntityMethodProcessor@6292} 
    18 = {RedirectAttributesMethodArgumentResolver@6293} 
    19 = {ModelMethodProcessor@6294} 
    20 = {MapMethodProcessor@6295} 
    21 = {ErrorsMethodArgumentResolver@6296} 
    22 = {SessionStatusMethodArgumentResolver@6297} 
    23 = {UriComponentsBuilderMethodArgumentResolver@6298} 
    24 = {PrincipalMethodArgumentResolver@6299} 
    25 = {RequestParamMethodArgumentResolver@6300} 
    26 = {ServletModelAttributeMethodProcessor@6301} 
    ```
5. 根据3中获取到的RequestResponseBodyMethodProcessor调用resolveArgument=>readWithMessageConverters
6. 遍历RequestResponseBodyMethodProcessor中的HttpMessageConverter集合，调用canRead判断Converter是否能支持解析参数
    ```text
    0 = {ByteArrayHttpMessageConverter@6254} 
    1 = {StringHttpMessageConverter@6255} 
    2 = {StringHttpMessageConverter@6256} 
    3 = {ResourceHttpMessageConverter@6257} 
    4 = {ResourceRegionHttpMessageConverter@6258} 
    5 = {SourceHttpMessageConverter@6259} 
    6 = {AllEncompassingFormHttpMessageConverter@6260} 
    7 = {MappingJackson2HttpMessageConverter@6261} 
    8 = {MappingJackson2HttpMessageConverter@6262} 
    ``` 
7. 通过MappingJackson2HttpMessageConverter读取数据

