package com.ecust.auth.advicer;

import com.ecust.base.ResponseResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NormalExceptionHandler {
    @ExceptionHandler(value = {NormalException.class})
    public ResponseResult NormalErrorResponse(NormalException normalException){
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(401);
        responseResult.setMsg(normalException.getMessage());
        return responseResult;
    }
    @ExceptionHandler(value = {UnauthorizationException.class})
    public ResponseResult UnauthorizationErrorResponse(UnauthorizationException unauthorizationException){
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(402);
        responseResult.setMsg(unauthorizationException.getMessage());
        return responseResult;
    }
    @ExceptionHandler(value = {LoginException.class})
    public ResponseResult LoginErrorResponse(LoginException loginException){
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(403);
        responseResult.setMsg(loginException.getMessage());
        return responseResult;
    }
}
