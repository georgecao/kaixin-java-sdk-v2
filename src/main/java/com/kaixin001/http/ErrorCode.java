package com.kaixin001.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Say something?
 * <pre>
 * User: zhangzhi.cao
 * Date: 12-4-5
 * Time: 下午4:39
 * </pre>
 */
public class ErrorCode {
    private static final Logger LOG = LoggerFactory.getLogger(ErrorCode.class);
    private static final boolean debug = LOG.isDebugEnabled();
    private int code = 0;
    private String message = "";

    public ErrorCode() {
    }

    public ErrorCode(int code) {
        this(code, "");
    }

    public ErrorCode(int code, String msg) {
        this.code = code;
        this.message = msg;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ErrorCode");
        sb.append("{code=").append(code);
        sb.append(", message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
