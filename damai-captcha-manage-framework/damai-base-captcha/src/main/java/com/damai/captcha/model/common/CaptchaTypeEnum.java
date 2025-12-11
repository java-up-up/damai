package com.damai.captcha.model.common;

import lombok.Getter;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 动作类型枚举
 * @author: 阿星不是程序员
 **/
@Getter
public enum CaptchaTypeEnum {
    /**
     * 旋转拼图.
     */
    ROTATEPUZZLE("rotatePuzzle","旋转拼图"),
    /**
     * 滑块拼图
     */
    BLOCKPUZZLE("blockPuzzle","滑块拼图"),
    /**
     * 文字点选.
     */
    CLICKWORD("clickWord","文字点选"),
    /**
     * 默认.
     */
    DEFAULT("default","默认");

    private String codeValue;
    private String codeDesc;

    private CaptchaTypeEnum(String  codeValue, String codeDesc) {
        this.codeValue = codeValue;
        this.codeDesc = codeDesc;
    }
    
    /**
     * 根据codeValue获取枚举
     */
    public static CaptchaTypeEnum parseFromCodeValue(String codeValue){
        for (CaptchaTypeEnum e : CaptchaTypeEnum.values()){
            if(e.codeValue.equals(codeValue)){ return e;}
        }
        return null;
    }

    /**
     * 根据codeValue获取描述
     * */
    public static String getCodeDescByCodeBalue(String codeValue){
        CaptchaTypeEnum enumItem = parseFromCodeValue(codeValue);
        return enumItem == null ? "" : enumItem.getCodeDesc();
    }

    /**
     * 验证codeValue是否有效
     * */
    public static boolean validateCodeValue(String codeValue){ return parseFromCodeValue(codeValue)!=null;}

    /**
     * 列出所有值字符串
     * */
    public static String getString(){
        StringBuffer buffer = new StringBuffer();
        for (CaptchaTypeEnum e : CaptchaTypeEnum.values()){
            buffer.append(e.codeValue).append("--").append(e.getCodeDesc()).append(", ");
        }
        buffer.deleteCharAt(buffer.lastIndexOf(","));
        return buffer.toString().trim();
    }

}
