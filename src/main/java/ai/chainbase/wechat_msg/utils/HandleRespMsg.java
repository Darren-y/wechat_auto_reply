package ai.chainbase.wechat_msg.utils;

import ai.chainbase.wechat_msg.common.enums.TokenEnum;
import ai.chainbase.wechat_msg.model.entity.MsgRecord;
import ai.chainbase.wechat_msg.model.response_msg.RespMsg;
import ai.chainbase.wechat_msg.model.response_msg.RespTextMsg;
import com.alibaba.fastjson.JSONObject;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import com.thoughtworks.xstream.XStream;
import lombok.extern.log4j.Log4j2;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Admin
 * @Date: 2019/3/27
 * @Version: 1.0
 * @Description:
 */
@Log4j2
public class HandleRespMsg {

    public static Map getRespMsg(Map map, String url) throws AesException {
        String respMsg;
        String fromUserName = (String) map.get("FromUserName");
        String toUserName = (String) map.get("ToUserName");
        String createTime = (String) map.get("CreateTime");
        String msgType = (String) map.get("MsgType");
        String reply;
        String content = null;
        if(msgType.equalsIgnoreCase("text")){
            content = (String) map.get("Content");
            reply = getReplyMsg(fromUserName,content,url);
        }else if(msgType.equalsIgnoreCase("image")){
            reply="暂不支持图片信息识别，请用文字描述";
        }else if(msgType.equalsIgnoreCase("voice")){
            reply="暂不支持语音信息识别，请用文字描述";
        }else if(msgType.equalsIgnoreCase("video")){
            reply="暂不支持视频信息识别，请用文字描述";
        }else if(msgType.equalsIgnoreCase("shortvideo")){
            reply="暂不支持短视频信息识别，请用文字描述";
        }else if(msgType.equalsIgnoreCase("location")){
            reply="暂不支持定位信息识别，请用文字描述";
        }else if(msgType.equalsIgnoreCase("link")){
            reply="暂不支持链接信息识别，请用文字描述";
        }else{
            reply="暂不支持该格式的信息识别，请用文字描述";
        }
        Long timestamp = new Date().getTime();
        RespTextMsg respTextMsg = new RespTextMsg();
        respTextMsg.setFromUserName(toUserName);
        respTextMsg.setToUserName(fromUserName);
        respTextMsg.setContent(reply);
        respTextMsg.setCreateTime(timestamp);
        respTextMsg.setMsgType("text");
        respTextMsg.setFuncFlag("0");

        String xmlMsg = parse2Xml(respTextMsg,RespTextMsg.class);
        log.info("公众号回复用户信息："+xmlMsg);

        //加密回复信息，返回给微信
        WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(
                TokenEnum.TOKEN.getValue(),
                TokenEnum.ENCODING_AES_KEY.getValue(),
                TokenEnum.APP_ID.getValue());
        respMsg = wxBizMsgCrypt.encryptMsg(xmlMsg,timestamp+"","lianji"+timestamp);

        //储存通讯记录
        MsgRecord msgRecord = MsgRecord.builder()
                .fromUserName(fromUserName)
                .msgType(msgType)
                .createTime(Long.parseLong(createTime))
                .content(content)
                .replyTime(timestamp)
                .reply(reply)
                .build();

        Map resultMap = new HashMap();
        resultMap.put("respMsg",respMsg);
        resultMap.put("msgRecord",msgRecord);
        return resultMap;
    }

    /**
     * 根据接收的信息，做出回复
     * @param receiveMsg
     * @return
     */
    public static String getReplyMsg(String fromUserName,String receiveMsg,String url){
        JSONObject param = new JSONObject();
        param.put("id",fromUserName);
        param.put("question",receiveMsg);
        String reply = HttpUtil.sendPostJson(url,param.toJSONString());
        log.info("根据用户消息得到的回复："+reply);
        return reply;
    }

    /**
     * 把公众号回复的消息转换成XML格式
     */
    public static String parse2Xml(RespMsg msg, Class child) {
        XStream xStream = new XStream();
        xStream.alias("xml", child);
        return xStream.toXML(msg);
    }
}
