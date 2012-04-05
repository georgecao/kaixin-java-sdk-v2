package com.kaixin001.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

/**
 * Say something?
 * <pre>
 * User: zhangzhi.cao
 * Date: 12-4-5
 * Time: 下午4:24
 * </pre>
 */
public class CloseUtils {
    private static final Logger LOG = LoggerFactory.getLogger(CloseUtils.class);
    private static final boolean debug = LOG.isDebugEnabled();

    public static void close(Closeable resource) {
        if (null != resource) {
            try {
                resource.close();
            } catch (IOException e) {
                LOG.warn("Error occurred:", e);
            }
        }
    }
}
