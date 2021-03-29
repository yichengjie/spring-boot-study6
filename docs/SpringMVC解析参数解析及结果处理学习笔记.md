#### Controller处理概述
1. RequestMappingHandlerAdapter.invokeHandlerMethod调用ServletInvocableHandlerMethod.invokeAndHandle
2. 获取Controller参数并执行
    ```text
     InvocableHandlerMethod.invokeForRequest{
          Object[] args = HandlerMethodArgumentResolverComposite.resolveArgument# 这里从 
          Object returnValue = doInvoke(args);
          return returnValue ;
     }
    ```
3. 处理Controller返回结果
    ```text
    HandlerMethodReturnValueHandlerComposite.handleReturnValue{
       HandlerMethodReturnValueHandler handler = selectHandler(returnValue, returnType);
       handler.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
     }
    ```
#### 获取Controller参数并执行
1. resolveArgument解析controller方法的每一个参数类型放入MethodParameter[]数组中
2. 遍历MethodParameter对象，使用HandlerMethodArgumentResolverComposite.supportsParameter(MethodParameter parameter)
3. HandlerMethodArgumentResolverComposite.getArgumentResolver获取能处理MethodParameter的XXXResolver，
   以RequestResponseBodyMethodProcessor为例，继续以下逻辑
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
4. 根据3中获取到的RequestResponseBodyMethodProcessor调用resolveArgument=>readWithMessageConverters
5. 遍历RequestResponseBodyMethodProcessor中的HttpMessageConverter集合，调用canRead判断Converter是否能支持解析参数
    ```text
    0 = {ByteArrayHttpMessageConverter@6458} 
    1 = {StringHttpMessageConverter@6459} 
    2 = {StringHttpMessageConverter@6460} 
    3 = {ResourceHttpMessageConverter@6461} 
    4 = {ResourceRegionHttpMessageConverter@6462} 
    5 = {SourceHttpMessageConverter@6463} 
    6 = {AllEncompassingFormHttpMessageConverter@6464} 
    7 = {MappingJackson2HttpMessageConverter@6465} 
    8 = {MappingJackson2HttpMessageConverter@6466} 
    9 = {Jaxb2RootElementHttpMessageConverter@6467} 
    ``` 
6. 通过MappingJackson2HttpMessageConverter读取数据
#### 处理Controller返回结果
1. selectHandler(returnValue, returnType)遍历ReturnValueHandler根据supportsReturnType选取,
   以RequestResponseBodyMethodProcessor为例,因被@ResponseBody注解所以被选中
    ```text
    0 = {ModelAndViewMethodReturnValueHandler@6200} 
    1 = {ModelMethodProcessor@6201} 
    2 = {ViewMethodReturnValueHandler@6202} 
    3 = {ResponseBodyEmitterReturnValueHandler@6203} 
    4 = {StreamingResponseBodyReturnValueHandler@6204} 
    5 = {HttpEntityMethodProcessor@6205} 
    6 = {HttpHeadersReturnValueHandler@6206} 
    7 = {CallableMethodReturnValueHandler@6207} 
    8 = {DeferredResultMethodReturnValueHandler@6208} 
    9 = {AsyncTaskMethodReturnValueHandler@6209} 
    10 = {ModelAttributeMethodProcessor@6210} 
    11 = {RequestResponseBodyMethodProcessor@6211} 
    12 = {ViewNameMethodReturnValueHandler@6212} 
    13 = {MapMethodProcessor@6213} 
    14 = {ModelAttributeMethodProcessor@6214} 
    ```
2. handler.handleReturnValue处理返回结果 => RequestResponseBodyMethodProcessor.writeWithMessageConverters
3. 根据request获取accept头对应的MediaType集合，
4. 获取controller对应的MediaType集合，如果controller未标注produces，则遍历messageConvert，
   根据canWrite(返回结果类型)，将messageConvert支持的MediaType集合
    ```text
    0 = {PropertiesHttpMessageConverter@6332} 
    1 = {ByteArrayHttpMessageConverter@6333} 
    2 = {StringHttpMessageConverter@6334} 
    3 = {StringHttpMessageConverter@6335} 
    4 = {ResourceHttpMessageConverter@6336} 
    5 = {ResourceRegionHttpMessageConverter@6337} 
    6 = {SourceHttpMessageConverter@6338} 
    7 = {AllEncompassingFormHttpMessageConverter@6339} 
    8 = {MappingJackson2HttpMessageConverter@6340} 
    9 = {MappingJackson2HttpMessageConverter@6341} 
    ```
5. 遍历3集合与4集合逐个匹配，并将结果集排序，获取最优的MediaType
6. 遍历所有的messageConverters根据canWrite(返回值类型,MediaType)选出messageConverter
   并调用write将结果写回客户端






