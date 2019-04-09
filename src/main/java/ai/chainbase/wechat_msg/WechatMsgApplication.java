package ai.chainbase.wechat_msg;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("ai.chainbase.wechat_msg.mapper")
public class WechatMsgApplication{

	public static void main(String[] args) {
		SpringApplication.run(WechatMsgApplication.class, args);
	}

}
