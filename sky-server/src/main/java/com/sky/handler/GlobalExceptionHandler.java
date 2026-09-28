package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }


    @ExceptionHandler
    public Result exceptionHandler(DuplicateKeyException ex){
        String msg = ex.getMessage();
        // 找到 "Duplicate entry" 的位置
        int index = msg.indexOf("Duplicate entry");
        if (index != -1) {
            // 截取从 "Duplicate entry" 开始的后面一段
            String cleanMsg = msg.substring(index);
            String username = cleanMsg.split(" ")[2].replace("'", "");
            return Result.error(username + MessageConstant.ALREADY_EXIST);
        }
        else {
            return Result.error("未知错误");
        }
    }
}
