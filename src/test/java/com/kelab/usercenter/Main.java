package com.kelab.usercenter;

import com.kelab.util.md5.Md5Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        System.out.println(Md5Util.StringInMd5("123456"));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sendTime = format.format(new Date());
        System.out.println(sendTime);
    }
}
