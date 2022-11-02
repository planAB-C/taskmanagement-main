package com.sjj.taskmanagement.common.errorHandler;

import org.springframework.security.core.AuthenticationException;
/*
 * @Author sjj
 * @Description //TODO 自定义验证码错误
 * @Date 2021/10/17 2021/10/17
 **/

public class CaptchaException extends AuthenticationException {

	public CaptchaException(String msg) {
		super(msg);
	}
}
