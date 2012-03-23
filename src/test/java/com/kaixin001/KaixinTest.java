package com.kaixin001;

import com.kaixin001.http.AccessToken;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Say something?
 * <pre>
 * User: zhangzhi.cao
 * Date: 12-3-8
 * Time: 上午11:31
 * </pre>
 */
public class KaixinTest {
    private static final Logger LOG = LoggerFactory.getLogger(KaixinTest.class);
    private static final boolean DEBUG = LOG.isDebugEnabled();
    private Kaixin kaixin;
    private String scope = "create_records";

    @Before
    public void setUp() throws Exception {
        kaixin = new KaixinBuilder()
                .apiKey("4981162124136a074769f1078676fdb2")
                .apiSecret("71d46998b5518f6c3e04958118daccde")
                .callback("http://www.dajie.com/oauth/kaixin")
                .build();
    }

    @Test
    public void testGetAuthorizeURLForCode() throws Exception {
        String url = kaixin.getAuthorizeURLForCode(scope, "", "");
        LOG.info(url);
    }

    @Test
    public void testGetAuthorizeURLForToken() throws Exception {
        String url = kaixin.getAuthorizeURLForToken(scope, "", "");
        LOG.info(url);
    }

    @Test
    public void testGetOAuthAccessTokenFromCode() throws Exception {
        AccessToken token = kaixin.getOAuthAccessTokenFromCode("12122221212");
        LOG.info("Access token is: {}", token);
    }

    @Test
    public void testGetOAuthAccessTokenFromPassword() throws Exception {

    }

    @Test
    public void testGetOAuthAccessTokenFromRefreshToken() throws Exception {

    }

    @Test
    public void testSetOAuthAccessToken() throws Exception {

    }

    @Test
    public void testGetMyInfo() throws Exception {

    }

    @Test
    public void testGetUsers() throws Exception {

    }

    @Test
    public void testGetFriends() throws Exception {

    }

    @Test
    public void testGetRelationShip() throws Exception {

    }

    @Test
    public void testGetAppStatus() throws Exception {

    }

    @Test
    public void testGetAppFriendUids() throws Exception {

    }

    @Test
    public void testGetAppInvitedUids() throws Exception {

    }

    @Test
    public void testSendSysNews() throws Exception {

    }

    @Test
    public void testPostRecord() throws Exception {

    }

    @Test
    public void testCreateAlbum() throws Exception {

    }

    @Test
    public void testShowAlbum() throws Exception {

    }

    @Test
    public void testUploadPhoto() throws Exception {

    }

    @Test
    public void testShowPhoto() throws Exception {

    }
}
