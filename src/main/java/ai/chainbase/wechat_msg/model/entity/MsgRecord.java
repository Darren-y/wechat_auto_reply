package ai.chainbase.wechat_msg.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yjm
 * @since 2019-03-28
 */
@Data
@Accessors(chain = true)
@TableName("msg_record")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MsgRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 微信发送方账号OpenID
     */
    private String fromUserName;


    /**
     * 消息发送时间
     */
    private Long createTime;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 回复时间
     */
    private Long replyTime;

    /**
     * 回复内容
     */
    private String reply;


    public static final String ID = "id";

    public static final String FROM_USER_NAME = "from_user_name";

    public static final String MSG_TYPE = "msg_type";

    public static final String CREATE_TIME = "create_time";

    public static final String CONTENT = "content";

    public static final String REPLY_TIME = "reply_time";

    public static final String REPLY = "reply";

}
