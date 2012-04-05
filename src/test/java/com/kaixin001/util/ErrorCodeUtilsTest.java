package com.kaixin001.util;

import com.kaixin001.http.ErrorCode;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Say something?
 * <pre>
 * User: zhangzhi.cao
 * Date: 12-4-5
 * Time: 下午5:06
 * </pre>
 */
public class ErrorCodeUtilsTest {
    private static final Logger LOG = LoggerFactory.getLogger(ErrorCodeUtilsTest.class);
    private static final boolean debug = LOG.isDebugEnabled();

    @Test
    public void testParse() throws Exception {
        String rawResponse = "{\n" +
                "    \"error_code\": \"401\",\n" +
                "    \"request\": \"\\/app\\/status.\",\n" +
                "    \"error\": \"40115:Error: Oauth consumer_key\\u4e0d\\u5b58\\u5728\"\n" +
                "}";
        ErrorCode ec = ErrorCodeUtils.parse(rawResponse);
        assertNotNull(ec);
        assertEquals(40115, ec.getCode());
    }
}
