package com.sjj.taskmanagement.common.errorHandler;


import com.sjj.taskmanagement.common.entities.ResultBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	//

	// 实体校验异常捕获
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResultBody handler(MethodArgumentNotValidException e) {

		BindingResult result = e.getBindingResult();
		ObjectError objectError = result.getAllErrors().stream().findFirst().get();

		log.error("实体校验异常：----------------{}", objectError.getDefaultMessage());
		return ResultBody.error(objectError.getDefaultMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = IllegalArgumentException.class)
	public ResultBody handler(IllegalArgumentException e) {
		log.error("Assert异常：----------------{}", e.getMessage());
		return ResultBody.error(e.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = RuntimeException.class)
	public ResultBody handler(RuntimeException e) {
		log.error("运行时异常：----------------{}", e.getMessage());
		return ResultBody.error(e.getMessage());
	}
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = AccessDeniedException.class)
	public ResultBody handler(AccessDeniedException e) {
		log.error("无权限：----------------{}", e.getMessage());
		return ResultBody.error("403", e.getMessage());
	}
}
