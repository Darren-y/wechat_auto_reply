package ai.chainbase.wechat_msg.common.enums;

/**
 * @Author: Admin
 * @Date: 2019/3/28
 * @Version: 1.0
 * @Description:
 */
public enum TokenEnum {
    TOKEN("lianji321"),
    ENCODING_AES_KEY("KW97cclI8ONWxqTxIMh1pjUjJDepmcqFL8HNSkR0IOX"),
    APP_ID("wxe90936470ccef57b");


    private String value;

    private TokenEnum(String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }

}
