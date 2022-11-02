package com.sjj.taskmanagement.security;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.sjj.taskmanagement.common.entities.Constraint;
import com.sjj.taskmanagement.common.errorHandler.CaptchaException;
import com.sjj.taskmanagement.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/*
 * @Author sjj
 * @Description //TODO 自定义验证码错误拦截器
 * @Date 2021/10/17 2021/10/17
 **/

@Component
//继承一次性拦截器
public class CaptchaFilter extends OncePerRequestFilter {

	@Autowired
	RedisUtil redisUtil;

	@Autowired
	LoginFailureHandler loginFailureHandler;

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

		String url = httpServletRequest.getRequestURI();
		//判断是否为登陆POST请求
		if ("/login".equals(url) && httpServletRequest.getMethod().equals("POST")) {

			try{
				// 校验验证码
				validate(httpServletRequest);
			} catch (CaptchaException e) {

				// 交给认证失败处理器
				loginFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
			}
		}

		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

	// 校验验证码逻辑
	private void validate(HttpServletRequest httpServletRequest) {

		String code = httpServletRequest.getParameter("code");
		String key = httpServletRequest.getParameter("token");

		if (StringUtils.isBlank(code) || StringUtils.isBlank(key)) {
			throw new CaptchaException("验证码错误");
		}

		if (!code.equals(redisUtil.hget(Constraint.CAPTCHA_KEY, key))) {
			throw new CaptchaException("验证码错误");
		}

		// 一次性使用
		redisUtil.hdel(Constraint.CAPTCHA_KEY, key);
	}
}
