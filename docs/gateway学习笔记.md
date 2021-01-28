### spring cloud gateway自定义filter获取request
1. 修改body内容
    ```text
    Mono<String> bodyToMono = serverRequest.bodyToMono(String.class);
    return bodyToMono.flatMap(body -> {
        log.info(body);
        ServerHttpRequest newRequest = new ServerHttpRequestDecorator(request) {
            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(super.getHeaders());
                httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                return httpHeaders;
            }
    
            @Override
            public Flux<DataBuffer> getBody() {
                NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
                DataBuffer bodyDataBuffer = nettyDataBufferFactory.wrap(body.getBytes());
                return Flux.just(bodyDataBuffer);
            }
        };
        return chain.filter(exchange.mutate().request(newRequest).build());
    });
    ```
2. 修改body内容
    ```text
    Flux<String> flux = serverRequest.bodyToFlux(String.class);
    ServerHttpRequest newRequest = new ServerHttpRequestDecorator(request) {
        private StringBuilder respBody=new StringBuilder();
        @Override
        public HttpHeaders getHeaders() {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.putAll(super.getHeaders());
            httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
            return httpHeaders;
        }
        @Override
        public Flux<DataBuffer> getBody() {
            return flux.map(body -> {
                respBody.append(body).append("\n");
                NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
                return nettyDataBufferFactory.wrap(body.getBytes());
            }).doOnComplete(()-> log.info("\n请求url={},请求参数={}\n,headers={}",super.getPath().value(),respBody.toString(),super.getHeaders()));
        }
    };
    return chain.filter(exchange.mutate().request(newRequest).build());
    ```
3. 待看文章：https://www.cnblogs.com/hyf-huangyongfei/p/12849406.html