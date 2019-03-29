package ai.chainbase.wechat_msg.model.request_msg;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Admin
 * @Date: 2019/3/27
 * @Version: 1.0
 * @Description:
 */
@Data
public class BaseWxMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    private String signature;
    private String timestamp;
    private String nonce;
    private String openid;
    private String msg_signature;
    private String encrypt_type;

}
