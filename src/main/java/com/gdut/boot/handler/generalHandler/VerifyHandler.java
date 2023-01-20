package com.gdut.boot.handler.generalHandler;

import com.gdut.boot.bean.Msg;
import com.gdut.boot.bean.RequestMessage;
import com.gdut.boot.constance.common.RequestStatus;
import com.gdut.boot.exception.BusinessException;
import com.gdut.boot.exception.ValidException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2022/1/270:36
 */

public class VerifyHandler extends Handler{
    @Override
    public Msg deal(RequestMessage requestMessage) {
        if(requestMessage.getReqType().equals(RequestStatus.POST)){
            //获取校验器
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();

            //进行校验
            Set<ConstraintViolation<Object>> validate = null;
            if(requestMessage.getVo() != null){
                //Get请求不需要
                validate = validator.validate(requestMessage.getVo());
            }
            if(validate == null || validate.size() == 0){
                //没有异常
                return this.next.deal(requestMessage);
            }
            throw new ValidException("参数发生异常", requestMessage, validate);
        }
        return this.next.deal(requestMessage);
    }
}
