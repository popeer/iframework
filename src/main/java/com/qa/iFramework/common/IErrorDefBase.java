package com.qa.iFramework.common;

/**
 * 定义CIA所有错误枚举的共同特性
 * CIA的错误定义的枚举 需 继承
 * interface 中 定义的 OK_CODE，OK_MSG 是为所有错误定义共用的
 * Created by haijia on 11/29/16.
 */
public interface IErrorDefBase {
    /**
     * 统一的 表示调用成功 的错误码
     */
    public static final int OK_CODE = 0;

    /**
     * 统一的 表示调用成功 的消息
     */
    public static final String OK_MSG = "OK";

    /**
     * 取错误信息
     * @return
     */
    String getMsg();

    /**
     * 取错误码
     * @return
     */
    int getCode();
}
