package com.kaixin001;

/**
 * Say something?
 * <pre>
 * User: zhangzhi.cao
 * Date: 12-3-8
 * Time: 上午10:28
 * </pre>
 */
public class KaixinBuilder {
    private String apiKey;
    private String apiSecret;
    /**
     * 需与注册信息中网站地址的域名一致，可修改域名映射在本地进行测试
     */
    private String callback;

    public KaixinBuilder apiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public KaixinBuilder apiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
        return this;
    }

    public KaixinBuilder callback(String callback) {
        this.callback = callback;
        return this;
    }

    public Kaixin build() {
        Kaixin.consumerKey = apiKey;
        Kaixin.consumerSecret = apiSecret;
        Kaixin.redirectUri = callback;
        return new Kaixin();
    }
}
