package ai.chainbase.wechat_msg.utils;

import ai.chainbase.wechat_msg.common.enums.TokenEnum;
import ai.chainbase.wechat_msg.model.request_msg.BaseWxMsg;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import lombok.extern.log4j.Log4j2;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Admin
 * @Date: 2019/3/26
 * @Version: 1.0
 * @Description:
 */
@Log4j2
public class HandleReqMsg {

    /**
     * 解码微信密文消息
     * xml 2 map
     */
    public static Map getDecryptWxMsg(HttpServletRequest request, BaseWxMsg baseWxMsg) {
        Map map = new HashMap(6);
        InputStream in = null;
        try {
            //获取微信消息密文
            in = request.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            //获取微信消息明文wxMsgString
            WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(
                    TokenEnum.TOKEN.getValue(),
                    TokenEnum.ENCODING_AES_KEY.getValue(),
                    TokenEnum.APP_ID.getValue());
            String wxMsgString = wxBizMsgCrypt.decryptMsg(baseWxMsg.getMsg_signature(),
                    baseWxMsg.getTimestamp(),
                    baseWxMsg.getNonce(),
                    sb.toString());
            log.info("公众号接收到用户信息: "+wxMsgString);
            //将xml格式的微信消息转换成map
            Document doc = DocumentHelper.parseText(wxMsgString);
            Element root = doc.getRootElement();
            List<Element> list = root.elements();
            for (Element em : list) {
                map.put(em.getName(), em.getText());
            }
        } catch (IOException e) {
            log.error(request.getRequestURI() + ": " + e.getMessage());
            e.printStackTrace();
        } catch (DocumentException e) {
            log.error(request.getRequestURI() + ": " + e.getMessage());
            e.printStackTrace();
        } catch (AesException e) {
            log.error(request.getRequestURI() + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                log.error(request.getRequestURI() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
        return map;
    }



}
