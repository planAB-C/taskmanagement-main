package com.sjj.taskmanagement.controller;




import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import com.google.code.kaptcha.Producer;
import com.sjj.taskmanagement.common.entities.Constraint;
import com.sjj.taskmanagement.common.entities.ResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



import org.apache.commons.codec.binary.Base64;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;



@Api(tags = "验证码接口")
@RestController
public class AuthController extends BaseController{

	@Autowired
	Producer producer;
	/*
	 * @Author sjj
	 * @Description //TODO
	 * @Date 2021/10/17 2021/10/17
	 * @Param
	 * @return 生成一个验证码
	 **/

	@ApiOperation(value = "获取一个验证码")
	@GetMapping("/captcha")
	public ResultBody captcha() throws IOException {
		//获取key
		String key = UUID.randomUUID().toString();
		//获取验证码
		String code = producer.createText();
		//测试用的 只会生成11111验证码
	//	key="aaaaaa";
	//	code="11111";
		BufferedImage image = producer.createImage(code);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", outputStream);
		String str = "data:image/jpeg;base64,";
		String base64Img = str + Base64.encodeBase64String(outputStream.toByteArray());
		//将验证码存到redis之中设定过期时间为120s
		redisUtil.hset(Constraint.CAPTCHA_KEY, key, code, 120);
		return ResultBody.success(
				MapUtil.builder()
						.put("token", key)
						.put("captchaImg", base64Img)
						.build()

		);
	}

}
