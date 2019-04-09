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

    public Map getRespMsg(Map map, String url) throws AesException {
        String respMsg = "";
        String fromUserName = (String) map.get("FromUserName");
        String toUserName = (String) map.get("ToUserName");
        String createTime = (String) map.get("CreateTime");
        String msgType = (String) map.get("MsgType");
        String reply = null;
        String content = null;
        if (msgType.equalsIgnoreCase("text")) {
            content = (String) map.get("Content");
        }
        Object cacheFlag = MyCacheUtil.get(fromUserName);

        if (content!=null && content.equals("0")) {
            //退出问答
            setStartFlag("0", fromUserName);
            reply = "已退出服务，谢谢使用";
        } else {
            if (content != null && cacheFlag != null) {
                String[] cacheFlags = String.valueOf(cacheFlag).split(":");
                Long timeFlag = Long.parseLong(cacheFlags[1]);
                String questionType = cacheFlags[0];
                if (!isExpired(timeFlag)) {
                    reply = getReply(questionType, fromUserName, content, url);
                    reply = (reply == null || reply.equals("")) ? "请详细描述您的提问" : reply;
                    //重置有效时间
                    setStartFlag(questionType, fromUserName);
                } else {
                    reply = "由于您长时间未操作，已退出服务";
                    setStartFlag("0", fromUserName);
                }
            } else if (content == null && cacheFlag != null) {
                //在问答中发送非文本消息
                reply = setNotTextMsgReply(msgType);
            } else if (content != null && cacheFlag == null) {
                //没有进入问答，根据用户消息，判断是否进入问答
                if(content.equals("1")){
                    reply = "链基智能健康小助手为您服务，请输入您的提问：";
                    setStartFlag("1", fromUserName);
                }
            }else{
                log.info("《《《 待处理 》》》");
            }
        }

        Long timestamp = new Date().getTime();
        RespTextMsg respTextMsg = new RespTextMsg();
        respTextMsg.setFromUserName(toUserName);
        respTextMsg.setToUserName(fromUserName);
        respTextMsg.setContent(reply);
        respTextMsg.setCreateTime(timestamp);
        respTextMsg.setMsgType("text");
        respTextMsg.setFuncFlag("0");

        if (reply != null && !reply.equals("")) {
            String xmlMsg = parse2Xml(respTextMsg, RespTextMsg.class);
            log.info("公众号回复用户信息：" + xmlMsg);

            //加密回复信息，返回给微信
            WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(
                    TokenEnum.TOKEN.getValue(),
                    TokenEnum.ENCODING_AES_KEY.getValue(),
                    TokenEnum.APP_ID.getValue());
            respMsg = wxBizMsgCrypt.encryptMsg(xmlMsg, timestamp + "", "lianji" + timestamp);
        }
        //储存通讯记录
        MsgRecord msgRecord = MsgRecord.builder()
                .fromUserName(fromUserName)
                .createTime(Long.parseLong(createTime))
                .content(content)
                .replyTime(timestamp)
                .reply(reply)
                .build();

        Map resultMap = new HashMap();
        resultMap.put("respMsg", respMsg);
        resultMap.put("msgRecord", msgRecord);
        return resultMap;
    }


    /**
     * 把公众号回复的消息转换成XML格式
     */
    public String parse2Xml(RespMsg msg, Class child) {
        XStream xStream = new XStream();
        xStream.alias("xml", child);
        return xStream.toXML(msg);
    }

    /**
     * 设置问答有效时间+退出问答
     *
     * @param questionType
     * @param fromUserName
     */
    private void setStartFlag(String questionType, String fromUserName) {
        switch (questionType){
            case "0":
                MyCacheUtil.remove(fromUserName);
                break;
            case "1":
                //设置问答有效时间，两次请求时间间隔超过2min退出问答
                MyCacheUtil.put(fromUserName, "1:" + (new Date().getTime() + 120000));
                break;
        }
    }

    /**
     * 回复非文本提问
     *
     * @param msgType
     * @return
     */
    private String setNotTextMsgReply(String msgType) {
        switch (msgType) {
            case "image":
                return "暂不支持图片信息识别，请用文字描述";
            case "voice":
                return "暂不支持语音信息识别，请用文字描述";
            case "video":
                return "暂不支持视频信息识别，请用文字描述";
            case "shortvideo":
                return "暂不支持短视频信息识别，请用文字描述";
            case "location":
                return "暂不支持定位信息识别，请用文字描述";
            case "link":
                return "暂不支持链接信息识别，请用文字描述";
            default:
                return "暂不支持该格式的信息识别，请用文字描述";
        }
    }

    /**
     * 判断提问是否超时
     *
     * @param time
     * @return
     */
    private boolean isExpired(Long time) {
        if (time != null) {
            if (time >= (new Date().getTime())) {
                return false;
            }
        }
        return true;
    }


    /**
     * 根据提问类型调用不同的接口
     *
     * @param questionType
     * @param fromUserName
     * @param content
     * @param url
     * @return
     */
    private String getReply(String questionType, String fromUserName, String content, String url) {
        switch (questionType) {
            case "1":
                return getReplyMsg(fromUserName, content, url);
            default:
                return null;
        }
    }

    /**
     * 调用健康问答接口
     *
     * @param receiveMsg
     * @return
     */
    public String getReplyMsg(String fromUserName, String receiveMsg, String url) {
        JSONObject param = new JSONObject();
        param.put("id", fromUserName);
        param.put("question", receiveMsg);
        String reply = HttpUtil.sendPostJson(url, param.toJSONString());
        log.info("根据用户消息得到的回复：" + reply);
        return reply;
    }

}
