package com.darren.mydemo.utils;

import com.orhanobut.logger.Logger;

/**
 * Created by jayli on 2017/5/2 0002.
 * 日志打印工具类
 */

public class LogUtils {

    public static void d(String message) {
        Logger.d(message);
    }

    public static void e(String message) {
        Logger.e(message);
    }

    public static void w(String message) {
        Logger.w(message);
    }

    public static void i(String message) {
        Logger.i(message);
    }

    public static void v(String message) {
        Logger.v(message);
    }

    public static void wtf(String message) {
        Logger.wtf(message);
    }
}
