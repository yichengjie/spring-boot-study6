package com.yicj.study.mvc.servlet.mvc.component;

import com.yicj.study.mvc.model.JsonResult;
import com.yicj.study.mvc.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class UnifiedExceptionHandler {

	@ResponseBody
    @ExceptionHandler(value = AppException.class)
	public JsonResult exceptionHandler(AppException e){
		log.error("business check tip : {}",e.getMessage());
		JsonResult res = new JsonResult() ;
		res.setCode(e.getCode());
		res.setMsg(e.getMessage());
       	return res;
    }

    // 其他异常
	@ResponseBody
	@ExceptionHandler(value =Exception.class)
	public JsonResult exceptionHandler(Exception e){
		log.error("system error ",e);
		JsonResult res = new JsonResult() ;
		res.setCode(500);
		res.setMsg("未知异常,请联系系统管理员！");
		return res;
	}
}