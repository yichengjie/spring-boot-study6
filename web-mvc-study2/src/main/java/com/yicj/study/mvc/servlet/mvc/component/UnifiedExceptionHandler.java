package com.yicj.study.mvc.servlet.mvc.component;

import com.yicj.study.mvc.model.JsonResult;
import com.yicj.study.mvc.servlet.mvc.exception.AppException;
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
		log.error("global catch error : {}",e.getMessage());
		JsonResult res = new JsonResult() ;
		res.setCode(500);
		res.setMsg(e.getMessage());
       	return res;
    }

    // 其他异常
	@ResponseBody
	@ExceptionHandler(value =Exception.class)
	public JsonResult exceptionHandler(Exception e){
		log.error("global catch error ",e);
		JsonResult res = new JsonResult() ;
		res.setCode(500);
		res.setMsg("系统异常，请通知管理员！");
		return res;
	}
}