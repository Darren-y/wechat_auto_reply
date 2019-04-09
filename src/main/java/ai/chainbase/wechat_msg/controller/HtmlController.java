package ai.chainbase.wechat_msg.controller;

import ai.chainbase.wechat_msg.common.config.ServerSetting;
import ai.chainbase.wechat_msg.mapper.MsgRecordMapper;
import ai.chainbase.wechat_msg.model.entity.MsgRecord;
import ai.chainbase.wechat_msg.utils.HandleRespMsg;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @Author: Admin
 * @Date: 2019/4/1
 * @Version: 1.0
 * @Description:
 */
//@CrossOrigin(value = "*")
//@Controller()
public class HtmlController {

    @Autowired
    private ServerSetting serverSetting;

    @Autowired
    private MsgRecordMapper mapper;

    @RequestMapping("/quiz")
    public Object goQuiz(){
        return "health_quiz";
    }

    @PostMapping(value = "/quiz/do")
    public void healthQuiz(@RequestParam("question")String question, HttpServletResponse response){
        Long timestamp = new Date().getTime();
        HandleRespMsg handleRespMsg = new HandleRespMsg();
        String answer = handleRespMsg.getReplyMsg(timestamp+"",question,serverSetting.getGetAnswerUrl());

        MsgRecord record = MsgRecord.builder().content(question).createTime(timestamp).reply(answer).replyTime(new Date().getTime()).build();
        mapper.insert(record);

        JSONObject json = new JSONObject();
        json.put("answer",answer);
        try {
            response.getWriter().write(json.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
