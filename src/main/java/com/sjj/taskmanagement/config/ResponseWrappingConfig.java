package com.sjj.taskmanagement.config;


import com.sjj.taskmanagement.common.entities.Response;
import com.sjj.taskmanagement.common.entities.ResultBody;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


//这个是给所有的返回值加
@ControllerAdvice(basePackages = "com.sjj")
public class ResponseWrappingConfig implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
//        判断是否要执行beforeBodyWrite方法，true为执行，false不执行

        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 不是json类型则不嵌套
//        public static final MediaType APPLICATION_JSON = new MediaType("application", "json");
        if (!(MediaType.APPLICATION_JSON.equals(selectedContentType))) {
            return body;
        }
//        如果返回的数据不是 专门的返回类的话 就封装起来
        if (!(body instanceof Response) && !(body instanceof ResultBody) ) {
//            System.out.println(body);
            Response<Object> r;
            r = new Response<>();
            r.setCode("200");
            r.setMessage("成功！");
            r.setResult(body);
            return r;
        }
        return body;
    }

}
