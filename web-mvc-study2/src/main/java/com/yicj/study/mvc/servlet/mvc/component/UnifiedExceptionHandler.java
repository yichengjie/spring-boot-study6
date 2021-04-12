package com.yicj.study.mvc.servlet.mvc.component;

import com.yicj.study.mvc.model.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class UnifiedExceptionHandler {

	@ResponseBody
    @ExceptionHandler(value =Exception.class)
	public JsonResult exceptionHandler(Exception e){
		log.error("global catch error : ",e);
		JsonResult res = new JsonResult() ;
		res.setCode(500);
		res.setMsg(e.getMessage());
       	return res;
    }
}