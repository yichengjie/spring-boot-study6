1. @ControllerAdvice: 定义一个控制器的通知类，允许定义一些关于增强控制器的各类通知和限定增强那些控制器功能
2. @InitBinder: 定义控制器参数绑定规则，如转换规则，格式化等，在参数转换前执行
3. @ExceptionHandler： 定义控制器发生异常后的操作
4. 实例核心代码
    ```java
    @ControllerAdvice(
        //指定拦截的包
        basePackages = {"com.yicj.mvc.controller.*"},
        // 限定被标注@Controller的类才被拦截
        annotations = Controller.class
    )
    public class MyControllerAdvice {
        // 绑定格式化、参数转换规则和增加验证器等
        @InitBinder
        public void initDataBinder(WebDataBinder binder){
            // 自定义日期编辑器，限定格式为 yyyy-MM-dd，且参数不容许为空
            CustomDateEditor dateEditor =
                    new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"),false) ;
            // 注册自定义日期编辑器
            binder.registerCustomEditor(Date.class, dateEditor);
        }
        // 在执行控制器前执行，可以初始化数据模型
        @ModelAttribute
        public void projectMode(Model model){
            model.addAttribute("project_name","web-mvc") ;
        }
        // 异常处理,当controller发生异常时，都能用相同的视图响应
        @ExceptionHandler(Exception.class)
        public String exception(Model model, Exception ex){
            log.info("==> 统一异常处理 : ", ex);
            model.addAttribute("exception_message", ex.getMessage()) ;
            //返回异常视图
            return "exception" ;
        }
    }
    ```
5. 实例业务代码
    ```java
    @Controller
    @RequestMapping("/advice")
    public class AdviceController {
        @GetMapping("/hello")
        public String hello(Date date, ModelMap modelMap){
            log.info("======> project name : {}", modelMap.get("project_name"));
            log.info("======> format date : {}",date == null ? null: new SimpleDateFormat("yyyy-MM-dd").format(date));
            throw new RuntimeException("异常了，跳转到ControllerAdvice的异常处理") ;
        }
    }
    ```