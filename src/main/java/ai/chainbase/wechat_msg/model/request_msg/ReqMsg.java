package ai.chainbase.wechat_msg.model.request_msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Admin
 * @Date: 2019/3/26
 * @Version: 1.0
 * @Description: 接收到的微信消息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReqMsg {
    // 开发者微信号
    private String toUserName;
    // 发送方帐号（一个OpenID）
    private String fromUserName;
    // 消息创建时间 （整型）
    private long createTime;
    // 消息类型（text/image/location/link）
    private String msgType;
    // 消息id，64位整型
    private long msgId;
}
