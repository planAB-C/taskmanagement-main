package com.sjj.taskmanagement.security;

import cn.hutool.json.JSONUtil;
import com.sjj.taskmanagement.common.entities.ResultBody;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/*
 * @Author sjj
 * @Description //TODO 权限不足的处理器
 * @Date 2021/10/17 2021/10/17
 **/

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);

		ServletOutputStream outputStream = response.getOutputStream();

		ResultBody result = ResultBody.error(accessDeniedException.getMessage());

		outputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));

		outputStream.flush();
		outputStream.close();

	}
}
