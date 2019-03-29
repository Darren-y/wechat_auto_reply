package ai.chainbase.wechat_msg.model.response_msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Admin
 * @Date: 2019/3/27
 * @Version: 1.0
 * @Description:回复微信文本信息实体类，注意属性首字母必须大写
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RespMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    // 开发者微信号
    private String ToUserName;
    // 发送方帐号（一个OpenID）
    private String FromUserName;
    // 消息创建时间 （整型）
    private long CreateTime;
    // 消息类型（text/image/location/link）
    private String MsgType;
}
