package ai.chainbase.wechat_msg.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource({"classpath:config/serverSetting.properties"})
@ConfigurationProperties()
public class ServerSetting {

   private String getAnswerUrl;

}
