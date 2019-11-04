package com.jt.thro;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jt.vo.SysResult;

import lombok.extern.slf4j.Slf4j;

//@ControllerAdvice //定义全局异常处理机制
@RestControllerAdvice //该注解对controller层生效
@Slf4j //引入日志API
public class SysResultController {
	
	@ExceptionHandler(RuntimeException.class)
	public SysResult sysResultException(Exception exception) {
		exception.printStackTrace();
		log.error("服务器异常"+exception.getMessage());
		return SysResult.fail();
	}
	
}
