/*
 *Copyright © 2018 anji-plus
 *安吉加加信息技术有限公司
 *http://www.anji-plus.com
 *All rights reserved.
 */
package com.damai.captcha.model.common;

import com.damai.captcha.util.StringUtils;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 返回数据
 * @author: 阿星不是程序员
 **/
@Getter
public class ResponseModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 8445617032523881407L;

    private String            repCode;

    private String            repMsg;

    private Object            repData;

    public ResponseModel() {
        this.repCode = RepCodeEnum.SUCCESS.getCode();
    }

    public ResponseModel(RepCodeEnum repCodeEnum) {
       this.setRepCodeEnum(repCodeEnum);
    }

    /**
     * 成功
     * */
    public static ResponseModel success(){
        return ResponseModel.successMsg("成功");
    }
    public static ResponseModel successMsg(String message){
        ResponseModel responseModel = new ResponseModel();
        responseModel.setRepMsg(message);
        return responseModel;
    }
    public static ResponseModel successData(Object data){
        ResponseModel responseModel = new ResponseModel();
        responseModel.setRepCode(RepCodeEnum.SUCCESS.getCode());
        responseModel.setRepData(data);
        return responseModel;
    }

    /**
     * 失败
     * */
    public static ResponseModel errorMsg(RepCodeEnum message){
        ResponseModel responseModel = new ResponseModel();
        responseModel.setRepCodeEnum(message);
        return responseModel;
    }
    public static ResponseModel errorMsg(String message){
        ResponseModel responseModel = new ResponseModel();
        responseModel.setRepCode(RepCodeEnum.ERROR.getCode());
        responseModel.setRepMsg(message);
        return responseModel;
    }
    public static ResponseModel errorMsg(RepCodeEnum repCodeEnum, String message){
        ResponseModel responseModel = new ResponseModel();
        responseModel.setRepCode(repCodeEnum.getCode());
        responseModel.setRepMsg(message);
        return responseModel;
    }
    public static ResponseModel exceptionMsg(String message){
        ResponseModel responseModel = new ResponseModel();
        responseModel.setRepCode(RepCodeEnum.EXCEPTION.getCode());
        responseModel.setRepMsg(RepCodeEnum.EXCEPTION.getDesc() + ": " + message);
        return responseModel;
    }

	@Override
	public String toString() {
		return "ResponseModel{" + "repCode='" + repCode + '\'' + ", repMsg='"
				+ repMsg + '\'' + ", repData=" + repData + '}';
	}

	public boolean isSuccess(){
        return StringUtils.equals(repCode, RepCodeEnum.SUCCESS.getCode());
    }
    
    public void setRepCode(String repCode) {
        this.repCode = repCode;
    }
    public void setRepCodeEnum(RepCodeEnum repCodeEnum) {
        this.repCode=repCodeEnum.getCode();
        this.repMsg=repCodeEnum.getDesc();
    }
    
    public void setRepMsg(String repMsg) {
        this.repMsg = repMsg;
    }
    
    public void setRepData(Object repData) {
        this.repData = repData;
    }


}
