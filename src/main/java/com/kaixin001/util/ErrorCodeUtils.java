package com.kaixin001.util;

import com.kaixin001.http.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Say something?
 * <pre>
 * User: zhangzhi.cao
 * Date: 12-4-5
 * Time: 下午4:42
 * </pre>
 */
public class ErrorCodeUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ErrorCodeUtils.class);
    private static final boolean debug = LOG.isDebugEnabled();
    private static final Pattern PATTERN = Pattern.compile("\"error\":\\s*\"(\\d+):Error:\\s*(.*)\"");

    public static ErrorCode parse(String rawErrorResponse) {
        ErrorCode ec = new ErrorCode();
        if (null == rawErrorResponse || "".equals(rawErrorResponse.trim())) {
            return ec;
        }
        Matcher matcher = PATTERN.matcher(rawErrorResponse);
        if (matcher.find() && 2 == matcher.groupCount()) {
            String errorCode = matcher.group(1);
            String detailMsg = matcher.group(2);
            ec.setCode(Integer.valueOf(errorCode));
            ec.setMessage(detailMsg);
        }
        return ec;
    }
}
