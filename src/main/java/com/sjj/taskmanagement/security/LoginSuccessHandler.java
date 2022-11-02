package com.sjj.taskmanagement.security;

import cn.hutool.json.JSONUtil;
import com.sjj.taskmanagement.common.entities.ResultBody;
import com.sjj.taskmanagement.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/*
 * @Author sjj
 * @Description 登陆成功处理器//TODO
 * @Date 2021/10/17 2021/10/17
 **/

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
  	private JwtUtils jwtUtils;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		response.setContentType("application/json;charset=UTF-8");
		ServletOutputStream outputStream = response.getOutputStream();

		 //生成jwt，并放置到请求头中
		String jwt = jwtUtils.generateToken(authentication.getName());
		response.setHeader(jwtUtils.getHeader(), jwt);

		ResultBody result = ResultBody.success("");

		outputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));

		outputStream.flush();
		outputStream.close();
	}

}
