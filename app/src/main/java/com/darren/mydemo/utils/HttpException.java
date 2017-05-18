package com.darren.mydemo.utils;

/**
 * Created by lenovo on 2017/5/18.
 */

public class HttpException extends Exception {
    public HttpException() {
        super();
    }

    public HttpException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public HttpException(String detailMessage) {
        super(detailMessage);
    }

    public HttpException(Throwable throwable) {
        super(throwable);
    }
}
