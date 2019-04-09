package ai.chainbase.wechat_msg.common.handler;

import com.qq.weixin.mp.aes.AesException;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 * @Author: Admin
 * @Date: 2019/3/28
 * @Version: 1.0
 * @Description:异常处理类
 */
@Log4j2
@RestControllerAdvice
public class ExpHandler {

    @ExceptionHandler(value = AesException.class)
    Object handleAesException(AesException e, HttpServletRequest request){
        log.error(request.getRequestURI()+": "+e);
        //直接回复微信success
        return "success";
    }

    @ExceptionHandler(value = UnsupportedEncodingException.class)
    Object handleUnsupportedEncodingException(UnsupportedEncodingException e, HttpServletRequest request){
        log.error(request.getRequestURI()+": "+e);
        //直接回复微信success
        return "success";
    }

    @ExceptionHandler(value = NoSuchAlgorithmException.class)
    Object handleNoSuchAlgorithmException(NoSuchAlgorithmException e, HttpServletRequest request){
        log.error(request.getRequestURI()+": "+e);
        //直接回复微信success
        return "success";
    }

    /**
     * 数据库格式和表格字段格式需要设置utf8mb4，否则储存emoji表情会出错
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(value = SQLException.class)
    Object handleSQLException(SQLException e, HttpServletRequest request){
        log.error(request.getRequestURI()+": "+e);
        //直接回复微信success
        return "success";
    }


    @ExceptionHandler(value = NullPointerException.class)
    Object handleNullPointerException(NullPointerException e,HttpServletRequest request){
        log.error(request.getRequestURI()+": "+e);
        //直接回复微信success
        return "success";
    }
}
