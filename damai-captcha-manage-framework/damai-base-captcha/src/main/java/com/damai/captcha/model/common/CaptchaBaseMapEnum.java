package com.damai.captcha.model.common;

import lombok.Getter;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 底图类型枚举
 * @author: 阿星不是程序员
 **/
@Getter
public enum CaptchaBaseMapEnum {
    /**
     * 枚举
     * */
    ROTATE("ROTATE", "旋转拼图底图"),
    ROTATE_BLOCK("ROTATE_BLOCK", "旋转拼图旋转块底图"),
    ORIGINAL("ORIGINAL", "滑动拼图底图"),
    SLIDING_BLOCK("SLIDING_BLOCK", "滑动拼图滑块底图"),
    PIC_CLICK("PIC_CLICK", "文字点选底图");

    private final String codeValue;
    private final String codeDesc;

    private CaptchaBaseMapEnum(String codeValue, String codeDesc) {
        this.codeValue = codeValue;
        this.codeDesc = codeDesc;
    }
    
    /**
     * 根据codeValue获取枚举
     * */
    public static CaptchaBaseMapEnum parseFromCodeValue(String codeValue) {
        for (CaptchaBaseMapEnum e : CaptchaBaseMapEnum.values()) {
            if (e.codeValue.equals(codeValue)) {
                return e;
            }
        }
        return null;
    }
    
    
    /**
     * 列出所有值字符串
     */
    public static String getString() {
        StringBuilder buffer = new StringBuilder();
        for (CaptchaBaseMapEnum e : CaptchaBaseMapEnum.values()) {
            buffer.append(e.codeValue).append("--").append(e.getCodeDesc()).append(", ");
        }
        buffer.deleteCharAt(buffer.lastIndexOf(","));
        return buffer.toString().trim();
    }

}
