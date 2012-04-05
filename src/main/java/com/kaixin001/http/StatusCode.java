package com.kaixin001.http;

/**
 * Say something?
 * <pre>
 * User: zhangzhi.cao
 * Date: 12-4-5
 * Time: 下午4:31
 * </pre>
 */
public interface StatusCode {
    static final int OK = 200;// OK: Success!
    static final int NOT_MODIFIED = 304;// Not Modified: There was no new data to return.
    static final int BAD_REQUEST = 400;// Bad Request: The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.
    static final int NOT_AUTHORIZED = 401;// Not Authorized: Authentication credentials were missing or incorrect.
    static final int FORBIDDEN = 403;// Forbidden: The request is understood, but it has been refused.  An accompanying error message will explain why.
    static final int NOT_FOUND = 404;// Not Found: The URI requested is invalid or the resource requested, such as a user, does not exists.
    static final int NOT_ACCEPTABLE = 406;// Not Acceptable: Returned by the Search API when an invalid format is specified in the request.
    static final int INTERNAL_SERVER_ERROR = 500;// Internal Server Error: Something is broken.  Please post to the group so the Kaixin team can investigate.
    static final int BAD_GATEWAY = 502;// Bad Gateway: Kaixin is down or being upgraded.
    static final int SERVICE_UNAVAILABLE = 503;// Service Unavailable: The Kaixin servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.
}
