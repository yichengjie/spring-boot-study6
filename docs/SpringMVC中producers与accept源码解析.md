1. 由RequestResponseBodyMethodProcessor#handleReturnValue => writeWithMessageConverters(...)处理Controller返回值
    ```java
    public class RequestResponseBodyMethodProcessor extends AbstractMessageConverterMethodProcessor {
       @Override
       public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType,
              ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
              throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {
          mavContainer.setRequestHandled(true);
          ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
          ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);
          // Try even with null return value. ResponseBodyAdvice could get involved.
          writeWithMessageConverters(returnValue, returnType, inputMessage, outputMessage);
       }  
    }
    ```
2. 从请求头accept中获取可接收响应MediaTypes,放入集合requestedMediaTypes中。eg: application/xml
    ````java
     public abstract class AbstractMessageConverterMethodProcessor extends AbstractMessageConverterMethodArgumentResolver
            implements HandlerMethodReturnValueHandler {
        private List<MediaType> getAcceptableMediaTypes(HttpServletRequest request)
     			throws HttpMediaTypeNotAcceptableException {
     	   return this.contentNegotiationManager.resolveMediaTypes(new ServletWebRequest(request));
        }
     }
     public class ContentNegotiationManager implements ContentNegotiationStrategy, MediaTypeFileExtensionResolver {
        @Override
        public List<MediaType> resolveMediaTypes(NativeWebRequest request) throws HttpMediaTypeNotAcceptableException {
            for (ContentNegotiationStrategy strategy : this.strategies) {
                List<MediaType> mediaTypes = strategy.resolveMediaTypes(request);
                if (mediaTypes.equals(MEDIA_TYPE_ALL_LIST)) {
                    continue;
                }
                return mediaTypes;
            }
            return MEDIA_TYPE_ALL_LIST;
        }
     }
    ````
3. 获取后台能提供支持的MediaTypes，放入producibleMediaTypes集合中。
    ````java
    //3.1 获取Controller方法上produces的值,如果不为空直接返回
    //3.2 遍历messageConverters#canWrite(返回值), 获取messageConverter支持的MediaTypes
    public abstract class AbstractMessageConverterMethodProcessor extends AbstractMessageConverterMethodArgumentResolver
            implements HandlerMethodReturnValueHandler {
       protected List<MediaType> getProducibleMediaTypes(HttpServletRequest request, Class<?> valueClass,@Nullable Type declaredType) {
           Set<MediaType> mediaTypes = (Set<MediaType>) request.getAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE);
           if (!CollectionUtils.isEmpty(mediaTypes)) {
               return new ArrayList<>(mediaTypes);
           }else if (!this.allSupportedMediaTypes.isEmpty()) {
               List<MediaType> result = new ArrayList<>();
               for (HttpMessageConverter<?> converter : this.messageConverters) {
                   if (converter instanceof GenericHttpMessageConverter && declaredType != null) {
                       if (((GenericHttpMessageConverter<?>) converter).canWrite(declaredType, valueClass, null)) {
                           result.addAll(converter.getSupportedMediaTypes());
                       }
                   }
                   else if (converter.canWrite(valueClass, null)) {
                       result.addAll(converter.getSupportedMediaTypes());
                   }
               }
               return result;
           }else {
               return Collections.singletonList(MediaType.ALL);
           }
       }
    }
    ````
4. 遍历requestedMediaTypes集合与producibleMediaTypes逐个对比匹配，兼容则放入集合mediaTypesToUse中，选取一条最优的MediaType
    ````java
    public abstract class AbstractMessageConverterMethodProcessor extends AbstractMessageConverterMethodArgumentResolver
            implements HandlerMethodReturnValueHandler {
        protected <T> void writeWithMessageConverters(@Nullable T value, MethodParameter returnType,
    			ServletServerHttpRequest inputMessage, ServletServerHttpResponse outputMessage)
    			throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {
               List<MediaType> requestedMediaTypes = getAcceptableMediaTypes(request);
               List<MediaType> producibleMediaTypes = getProducibleMediaTypes(request, valueType, declaredType);
               //1. 从请求头accept中获取可接收响应MediaTypes,放入集合requestedMediaTypes中。eg: application/xml
               //2. 获取后台能提供支持的MediaTypes，放入producibleMediaTypes集合中
               //3. 判断兼容性，并排序，获取最后的MediaType
               mediaTypesToUse = new ArrayList<>();
               for (MediaType requestedType : requestedMediaTypes) {
                    for (MediaType producibleType : producibleMediaTypes) {
                        if (requestedType.isCompatibleWith(producibleType)) {
                            mediaTypesToUse.add(getMostSpecificMediaType(requestedType, producibleType));
                        }
                    }
                }
                MediaType.sortBySpecificityAndQuality(mediaTypesToUse);
                MediaType selectedMediaType = null;
                for (MediaType mediaType : mediaTypesToUse) {
                    if (mediaType.isConcrete()) {
                        selectedMediaType = mediaType;
                        break;
                    }
                    else if (mediaType.equals(MediaType.ALL) || mediaType.equals(MEDIA_TYPE_APPLICATION)) {
                        selectedMediaType = MediaType.APPLICATION_OCTET_STREAM;
                        break;
                    }
                }
             // 4. 遍历messageConverters并调用canWrite方法，找到合适的messageConverter，并将Controller返回值输出到前端
             // .....
        }
    }
    ````
5. 遍历messageConverters并调用canWrite方法，找到合适的messageConverter，并将Controller返回值输出到前端
    ```java
     public abstract class AbstractMessageConverterMethodProcessor extends AbstractMessageConverterMethodArgumentResolver
                implements HandlerMethodReturnValueHandler {
        protected <T> void writeWithMessageConverters(@Nullable T value, MethodParameter returnType,
                        ServletServerHttpRequest inputMessage, ServletServerHttpResponse outputMessage)
                        throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {
             //1. 从请求头accept中获取可接收响应MediaTypes,放入集合requestedMediaTypes中。eg: application/xml
            //2. 获取后台能提供支持的MediaTypes，放入producibleMediaTypes集合中
            //3. 判断兼容性，并排序，获取最后的MediaType
            //4. 遍历messageConverters并调用canWrite方法，找到合适的messageConverter，并将Controller返回值输出到前端
            for (HttpMessageConverter<?> converter : this.messageConverters) {
                GenericHttpMessageConverter genericConverter =
                        (converter instanceof GenericHttpMessageConverter ? (GenericHttpMessageConverter<?>) converter : null);
                if (genericConverter != null ?
                        ((GenericHttpMessageConverter) converter).canWrite(declaredType, valueType, selectedMediaType) :
                        converter.canWrite(valueType, selectedMediaType)) {
                    outputValue = (T) getAdvice().beforeBodyWrite(outputValue, returnType, selectedMediaType,
                            (Class<? extends HttpMessageConverter<?>>) converter.getClass(),inputMessage, outputMessage);
                    if (outputValue != null) {
                        addContentDispositionHeader(inputMessage, outputMessage);
                        if (genericConverter != null) {
                            genericConverter.write(outputValue, declaredType, selectedMediaType, outputMessage);
                        }else {
                            ((HttpMessageConverter) converter).write(outputValue, selectedMediaType, outputMessage);
                        }
                    }
                    return;
                }
            }
        }
    }
    ```
6. messageConverters列表
    ```text
    0 = {ByteArrayHttpMessageConverter@5987} 
    1 = {StringHttpMessageConverter@5988} 
    2 = {StringHttpMessageConverter@5989} 
    3 = {ResourceHttpMessageConverter@5990} 
    4 = {ResourceRegionHttpMessageConverter@5991} 
    5 = {SourceHttpMessageConverter@5992} 
    6 = {AllEncompassingFormHttpMessageConverter@5993} 
    7 = {MappingJackson2HttpMessageConverter@5994} 
    8 = {MappingJackson2HttpMessageConverter@5995} 
    9 = {Jaxb2RootElementHttpMessageConverter@5996} 
    ```

