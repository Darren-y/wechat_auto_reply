package ai.chainbase.wechat_msg.model.response_msg;


import lombok.Data;


/**
 * @Author: Admin
 * @Date: 2019/3/27
 * @Version: 1.0
 * @Description: 回复微信信息实体类，注意属性首字母必须大写
 */
@Data
public class RespTextMsg extends RespMsg{
    //文本内容
    private String Content;

    private String FuncFlag;
}
