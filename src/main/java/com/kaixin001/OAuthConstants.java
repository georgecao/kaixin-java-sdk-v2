package com.kaixin001;

/**
 * Say something?
 * <pre>
 * User: zhangzhi.cao
 * Date: 12-3-8
 * Time: 上午10:38
 * </pre>
 */
public interface OAuthConstants {
    static final String TIMESTAMP = "oauth_timestamp";
    static final String SIGN_METHOD = "oauth_signature_method";
    static final String SIGNATURE = "oauth_signature";
    static final String CONSUMER_SECRET = "oauth_consumer_secret";
    static final String CONSUMER_KEY = "oauth_consumer_key";
    static final String CALLBACK = "oauth_callback";
    static final String VERSION = "oauth_version";
    static final String NONCE = "oauth_nonce";
    static final String PARAM_PREFIX = "oauth_";
    static final String TOKEN = "oauth_token";
    static final String TOKEN_SECRET = "oauth_token_secret";
    static final String OUT_OF_BAND = "oob";
    static final String VERIFIER = "oauth_verifier";
    static final String HEADER = "Authorization";
    static final String SCOPE = "scope";

    //OAuth 2.0
    static final String ACCESS_TOKEN = "access_token";
    static final String CLIENT_ID = "client_id";
    static final String CLIENT_SECRET = "client_secret";
    static final String REDIRECT_URI = "redirect_uri";
    static final String CODE = "code";
    static final String AUTHORIZATION_CODE = "authorization_code";
    static final String RESPONSE_TYPE = "response_type";
    static final String GRANT_TYPE = "grant_type";
    static final String REFRESH_TOKEN = "refresh_token";

    static final String USERNAME = "username";
    static final String PASSWORD = "password";

    static final String EXPIRES_IN = "expires_in";

    static final String TOKEN_TYPE = "token_type";

    static final String STATE = "state";

}
