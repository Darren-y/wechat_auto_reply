package ai.chainbase.wechat_msg.controller;

import ai.chainbase.wechat_msg.common.config.ServerSetting;
import ai.chainbase.wechat_msg.mapper.MsgRecordMapper;
import ai.chainbase.wechat_msg.model.entity.MsgRecord;
import ai.chainbase.wechat_msg.model.request_msg.BaseWxMsg;
import ai.chainbase.wechat_msg.utils.CheckUtil;
import ai.chainbase.wechat_msg.utils.HandleReqMsg;
import ai.chainbase.wechat_msg.utils.HandleRespMsg;
import ai.chainbase.wechat_msg.utils.MyCacheUtil;
import com.qq.weixin.mp.aes.AesException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;

/**
 * @Author: Admin
 * @Date: 2019/3/26
 * @Version: 1.0
 * @Description:
 */
@RestController()
@RequestMapping("/wechat")
@Log4j2
public class Controller {


    @Autowired
    private MsgRecordMapper mapper;

    @Autowired
    private ServerSetting serverSetting;

    /**
     * 自动回复用户信息
     *
     * @param request
     * @param baseWxMsg
     * @return
     */
    @PostMapping(value = "/")
    public String autoReply(HttpServletRequest request, BaseWxMsg baseWxMsg) throws AesException {
        Map map = HandleReqMsg.getDecryptWxMsg(request, baseWxMsg);
        if (map.size() > 0) {
            HandleRespMsg handleRespMsg = new HandleRespMsg();
            Map resultMap = handleRespMsg.getRespMsg(map, serverSetting.getGetAnswerUrl());
            String respMsg = (String) resultMap.get("respMsg");
            Object msgRecord = resultMap.get("msgRecord");
            if (msgRecord != null)
                mapper.insert((MsgRecord) msgRecord);
            if (!respMsg.equals(""))
                return respMsg;
        }
        return "success";
    }


    /**
     * 开启接口/get_reply，微信会发送GET请求校验接口，校验成功后以POST请求访问接口/get_reply
     *
     * @param req
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/")
    public String check(HttpServletRequest req) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String signature = req.getParameter("signature");
        String timestamp = req.getParameter("timestamp");
        String echostr = req.getParameter("echostr");
        String nonce = req.getParameter("nonce");
        log.info("开始微信接口验证");
        if (signature != null && !signature.equals("") &&
                timestamp != null && !timestamp.equals("") &&
                nonce != null && !nonce.equals("")) {
            if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
                log.info("微信接口校验成功");
                //如果校验成功，将得到的随机字符串原路返回
                return echostr;
            } else {
                log.error("微信接口校验失败");
            }
        } else {
            log.error("微信接口校验信息缺失");
        }
        return null;
    }


}
