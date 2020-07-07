package com.paas.web.conf.exception;

import com.alibaba.fastjson.JSON;
import com.paas.service.common.exception.BizException;
import com.paas.service.common.exception.CommonEnum;
import com.paas.service.common.exception.ResultBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * create by sumerian on 2020/6/18
 * <p>
 * desc:使用ExceptionHandler应用到所有@RequestMapping注解的方法，处理发生的异常。
 **/

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String logExceptionFormat = "Capture Exception By GlobalExceptionHandler: Code: %s Detail: %s";
        private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

        /**
         * 处理自定义的业务异常
         * @param req
         * @param e
         * @return
         */
        @ExceptionHandler(value = BizException.class)
        @ResponseBody
        public ResultBody bizExceptionHandler(HttpServletRequest req, BizException e){
            logger.error("发生业务异常！原因是：{}",e.getErrorMsg());
            return ResultBody.error(e.getErrorCode(),e.getErrorMsg());
        }

        /**
         * 处理空指针的异常
         * @param req
         * @param e
         * @return
         */
        @ExceptionHandler(value =NullPointerException.class)
        @ResponseBody
        public ResultBody exceptionHandler(HttpServletRequest req, NullPointerException e){
            logger.error("发生空指针异常！原因是:",e);
            return ResultBody.error(CommonEnum.BODY_NOT_MATCH);
        }


        /**
         * 处理其他异常
         * @param req
         * @param e
         * @return
         */
        @ExceptionHandler(value =Exception.class)
        @ResponseBody
        public ResultBody exceptionHandler(HttpServletRequest req, Exception e){
            logger.error("未知异常！原因是:",e);
            return ResultBody.error(CommonEnum.INTERNAL_SERVER_ERROR);
        }

    //运行时异常
    @ExceptionHandler(RuntimeException.class)
    public String runtimeExceptionHandler(RuntimeException ex) {
        return resultFormat(1, ex);
    }

    //空指针异常
    @ExceptionHandler(NullPointerException.class)
    public String nullPointerExceptionHandler(NullPointerException ex) {
        System.err.println("NullPointerException:");
        return resultFormat(2, ex);
    }

    //类型转换异常
    @ExceptionHandler(ClassCastException.class)
    public String classCastExceptionHandler(ClassCastException ex) {
        return resultFormat(3, ex);
    }

    //IO异常
    @ExceptionHandler(IOException.class)
    public String iOExceptionHandler(IOException ex) {
        return resultFormat(4, ex);
    }

    //未知方法异常
    @ExceptionHandler(NoSuchMethodException.class)
    public String noSuchMethodExceptionHandler(NoSuchMethodException ex) {
        return resultFormat(5, ex);
    }

    //数组越界异常
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public String indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException ex) {
        return resultFormat(6, ex);
    }

    //400错误
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public String requestNotReadable(HttpMessageNotReadableException ex) {
        System.out.println("400..requestNotReadable");
        return resultFormat(7, ex);
    }

    //400错误
    @ExceptionHandler({TypeMismatchException.class})
    public String requestTypeMismatch(TypeMismatchException ex) {
        System.out.println("400..TypeMismatchException");
        return resultFormat(8, ex);
    }

    //400错误
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public String requestMissingServletRequest(MissingServletRequestParameterException ex) {
        System.out.println("400..MissingServletRequest");
        return resultFormat(9, ex);
    }

    //405错误
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public String request405(HttpRequestMethodNotSupportedException ex) {
        return resultFormat(10, ex);
    }

    //406错误
    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    public String request406(HttpMediaTypeNotAcceptableException ex) {
        System.out.println("406...");
        return resultFormat(11, ex);
    }

    //500错误
    @ExceptionHandler({ConversionNotSupportedException.class, HttpMessageNotWritableException.class})
    public String server500(RuntimeException ex) {
        System.out.println("500...");
        return resultFormat(12, ex);
    }

    //栈溢出
    @ExceptionHandler({StackOverflowError.class})
    public String requestStackOverflow(StackOverflowError ex) {
        return resultFormat(13, ex);
    }

    //除数不能为0
    @ExceptionHandler({ArithmeticException.class})
    public String arithmeticException(ArithmeticException ex) {
        return resultFormat(13, ex);
    }


    //其他错误
    @ExceptionHandler({Exception.class})
    public String exception(Exception ex) {
        return resultFormat(14, ex);
    }

    private <T extends Throwable> String resultFormat(Integer code, T ex) {
        ex.printStackTrace();
        logger.error(String.format(logExceptionFormat, code, ex.getMessage()));
        return JSON.toJSONString(ResultBody.error(String.valueOf(code), ex.getMessage()));
    }

}

