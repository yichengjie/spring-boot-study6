package com.yicj.enchancer.servlet.mvc.component;

import com.yicj.enchancer.exception.AppException;
import com.yicj.enchancer.properties.MvcCommonHandlerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class UnifiedExceptionHandler {

	private final MvcCommonHandlerProperties properties ;

	public UnifiedExceptionHandler(MvcCommonHandlerProperties properties) {
		this.properties = properties;
	}

	@ResponseBody
    @ExceptionHandler(value = AppException.class)
	public Map<String,Object> exceptionHandler(AppException e){
		log.error("business check tip : {}",e.getMessage());
		Map<String,Object> res = new HashMap<>() ;
		res.put("code", e.getCode());
		res.put("msg", e.getMessage());
       	return res;
    }

    // 其他异常
	@ResponseBody
	@ExceptionHandler(value =Exception.class)
	public Map<String, Object> exceptionHandler(Exception e){
		log.error("system error ",e);
		Map<String,Object> res = new HashMap<>() ;
		res.put("code",500);
		res.put("msg", "未知异常,请联系系统管理员！");
		return res;
	}
}