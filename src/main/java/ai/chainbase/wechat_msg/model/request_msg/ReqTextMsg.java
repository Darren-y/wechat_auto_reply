package ai.chainbase.wechat_msg.model.request_msg;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

/**
 * @Author: Admin
 * @Date: 2019/3/26
 * @Version: 1.0
 * @Description: 接收到的微信文本消息
 */
@Data
@NoArgsConstructor
public class ReqTextMsg extends ReqMsg {
    //文本内容
    private String content;

    public static ReqTextMsg adapt(ReqMsg reqMsg){
        ReqTextMsg reqTextMsg = new ReqTextMsg();
        BeanUtils.copyProperties(reqMsg, reqTextMsg);
        return reqTextMsg;
    }

}
