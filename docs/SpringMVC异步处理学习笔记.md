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