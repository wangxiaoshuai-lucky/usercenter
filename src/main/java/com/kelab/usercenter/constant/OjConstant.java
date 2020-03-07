package com.kelab.usercenter.constant;

import com.kelab.info.base.constant.BaseRetCodeConstant;

public class OjConstant extends BaseRetCodeConstant {
    /**
     * 自定义的返回码从100开始
     */
    // 标签重复添加
    public static String REPEAT_ADD_ERROR = "REPEAT_ADD_ERROR";

    public static String  SUBMIT_FREQUENTLY_ERROR = "SUBMIT_FREQUENTLY_ERROR";

    public static String EMAIL_SEND_ERROR="EMAIL_SEND_ERROR";

    public static String  FILE_NOT_EXIST = "FILE_NOT_EXIST";

    public static String VERIFY_CODE_ERROR="VERIFY_CODE_ERROR";

    public static String PWD_FORMAT_ERROR="PWD_FORMAT_ERROR";

    public static String STUDENT_ID_EXIST_ERROR = "STUDENT_ID_EXISTED_ERROR";
}