1. DeferredResult异步执行
    ```java
    @RestController
    @EnableScheduling
    public class DeferredResultController {
        private final BlockingQueue<DeferredResult<String>> queue = new ArrayBlockingQueue<>(5) ;
        private final Random random = new Random(59) ;
        @Scheduled(fixedRate = 50)
        public void process() throws InterruptedException{
            log.info("=======================");
            DeferredResult<String> result = queue.take() ;
            // 随机超时时间
            int time= random.nextInt(100) % 2 == 0 ? 1000 : 300 ;
            Thread.sleep(time);
            // 模拟等待时间，rpc或db查询
            String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
            result.setResult("Hello world " + format) ;
        }
        @GetMapping("/deferredHello")
        public DeferredResult<String> deferredHello(){
            DeferredResult<String> result = new DeferredResult<>(500L,"timeout") ;
            log.info("deferredHello start... ");
            queue.offer(result) ;
            // 完成回调函数
            result.onCompletion(()-> log.info("HelloAsyncController 执行结束.."));
            // 超时回调函数
            result.onTimeout(()-> log.info("HelloAsyncController 执行超时.."));
            return result ;
        }
    }
    ```
2. Callable异步执行
    ```java
    @RestController
    public class CallableController {
        private final Random random = new Random(59) ;
        @GetMapping("/callableHello")
        public Callable<String> callableHello(){
            // 随机超时时间
            return ()-> {
                int time= random.nextInt(100) % 2 == 0 ? 1000 : 300 ;
                Thread.sleep(time);
                // 模拟等待时间，rpc或db查询
                String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
                return "Callable Hello world ["+time+"] " + format;
            };
        }
    }
    ```
3. CompletionStage异步执行
    ```java
    @RestController
    public class CompletionStageController {
        private final Random random = new Random(59) ;
    
        @GetMapping("/completionStageHello")
        public CompletionStage<String> completionStage(){
            return CompletableFuture.supplyAsync(()->{
                int time= random.nextInt(100) % 2 == 0 ? 1000 : 300 ;
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 模拟等待时间，rpc或db查询
                String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
                return "CompletionStage Hello world ["+time+"] " + format;
            }) ;
        }
    }
    ```
#### SpringMVC异步Servlet实现原理
1. HandlerMethodReturnValueHandler
2. Servlet 3.0 AsyncContext
    ```java
    @WebServlet(asyncSupported = true, //激活异步特性
            name = "asyncServlet", // Servlet名称
            urlPatterns = "/asyncServlet")
    public class AsyncServlet extends HttpServlet {
        private final Random random = new Random(59) ;
        @Override
        protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            req.setCharacterEncoding("UTF-8");
            resp.setContentType(getServletContext().getInitParameter("content"));
            //进入异步模式,调用业务处理线程进行业务处理
            //Servlet不会被阻塞,而是直接往下执行
            //业务处理完成后的回应由AsyncContext管理
            AsyncContext asyncContext = req.startAsync();
            asyncContext.setTimeout(900000000);
            asyncContext.addListener(new AsyncListener() {
                @Override
                public void onComplete(AsyncEvent event) throws IOException {
                    log.info("执行完成...");
                }
                @Override
                public void onTimeout(AsyncEvent event) throws IOException {
                    log.info("执行超时...");
                   /*HttpServletResponse suppliedResponse = (HttpServletResponse)event.getSuppliedResponse();
                   suppliedResponse.setStatus(503);
                   suppliedResponse.getWriter().write("503 error ");
                   suppliedResponse.getWriter().flush();*/
                }
                @Override
                public void onError(AsyncEvent event) throws IOException {
                    log.info("执行出错...");
                }
                @Override
                public void onStartAsync(AsyncEvent event) throws IOException {
                    log.info("开始执行异步...");
                }
            });
            int time= random.nextInt(100) % 2 == 0 ? 1000 : 300 ;
            CommonUtils.sleep(time, ()->{
                // 模拟等待时间，rpc或db查询
                String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
                ServletResponse response = asyncContext.getResponse();
                response.setContentType("text/plain;charset=UTF-8");
                //获取字符流输出流
                try {
                    PrintWriter writer = response.getWriter();
                    writer.println("Hello, world ["+time+"] " + format);
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                asyncContext.complete();
            });
        }
    }
    ```
3. DispatcherServlet整合