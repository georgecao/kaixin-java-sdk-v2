/*
Copyright (c) 2007-2009, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.
 * Neither the name of the Yusuke Yamamoto nor the
names of its contributors may be used to endorse or promote products
derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package kx2_4j;


import kx2_4j.http.AccessToken;
import kx2_4j.http.HttpClient;
import kx2_4j.http.PostParameter;
import kx2_4j.http.Response;
import kx2_4j.org.json.JSONException;
import kx2_4j.org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * A java reporesentation of the <a href="http://wiki.open.kaixin001.com/">KxSDK API</a>
 */
public class KxSDK {
    public static String CONSUMER_KEY = "";//api key
    public static String CONSUMER_SECRET = "";//secret key
    public static String Redirect_uri = ".../callback.jsp";//需与注册信息中网站地址的域名一致，可修改域名映射在本地进行测试
    public static String baseURL = "https://api.kaixin001.com/";
    public static String authorizationURL = "http://api.kaixin001.com/oauth2/authorize";
    public static String accessTokenURL = "http://api.kaixin001.com/oauth2/access_token";
    public static String accessTokenURLssl = "https://api.kaixin001.com/oauth2/access_token";
    public static String format = "json";

    protected HttpClient http = new HttpClient();
    protected AccessToken accessToken = null;

    public String getAuthorizeURLforCode(String scope, String state, String display) {
        return getAuthorizeURL("code", scope, state, display);
    }

    public String getAuthorizeURLforToken(String scope, String state, String display) {
        return getAuthorizeURL("token", scope, state, display);
    }

    protected String getAuthorizeURL(String type, String scope, String state, String display) {
        PostParameter[] params = new PostParameter[6];
        params[0] = new PostParameter("response_type", type);
        params[1] = new PostParameter("client_id", CONSUMER_KEY);
        params[2] = new PostParameter("redirect_uri", Redirect_uri);
        params[3] = new PostParameter("scope", scope);
        params[4] = new PostParameter("state", state);
        params[5] = new PostParameter("display", display);
        String query = HttpClient.encodeParameters(params);
        return authorizationURL + "?" + query;
    }

    public AccessToken getOAuthAccessTokenFromCode(String code) throws KxException {
        AccessToken oauthToken = null;
        try {
            PostParameter[] params = new PostParameter[5];
            params[0] = new PostParameter("grant_type", "authorization_code");
            params[1] = new PostParameter("client_id", CONSUMER_KEY);
            params[2] = new PostParameter("client_secret", CONSUMER_SECRET);
            params[3] = new PostParameter("code", code);
            params[4] = new PostParameter("redirect_uri", Redirect_uri);

            oauthToken = new AccessToken(http.get(accessTokenURL, params));
        } catch (KxException te) {
            throw new KxException("The user has not given access to the account.", te, te.getStatusCode());
        }
        return oauthToken;
    }

    public AccessToken getOAuthAccessTokenFromPassword(String username, String password, String scope) throws KxException {
        AccessToken oauthToken = null;
        try {
            PostParameter[] params = new PostParameter[6];
            params[0] = new PostParameter("grant_type", "password");
            params[1] = new PostParameter("client_id", CONSUMER_KEY);
            params[2] = new PostParameter("client_secret", CONSUMER_SECRET);
            params[3] = new PostParameter("username", username);
            params[4] = new PostParameter("password", password);
            params[5] = new PostParameter("scope", scope);
            oauthToken = new AccessToken(http.get(accessTokenURLssl, params));
        } catch (KxException te) {
            throw new KxException("The user has not given access to the account.", te, te.getStatusCode());
        }
        return oauthToken;
    }

    public AccessToken getOAuthAccessTokenFromRefreshtoken(String refresh_token, String scope) throws KxException {
        AccessToken oauthToken = null;
        try {
            PostParameter[] params = new PostParameter[5];
            params[0] = new PostParameter("grant_type", "refresh_token");
            params[1] = new PostParameter("client_id", CONSUMER_KEY);
            params[2] = new PostParameter("client_secret", CONSUMER_SECRET);
            params[3] = new PostParameter("refresh_token", refresh_token);
            params[4] = new PostParameter("scope", scope);
            oauthToken = new AccessToken(http.get(accessTokenURL, params));
        } catch (KxException te) {
            throw new KxException("The user has not given access to the account.", te, te.getStatusCode());
        }
        return oauthToken;
    }

    public void setOAuthAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        KxSDK kxSDK = (KxSDK) o;
        if (!http.equals(kxSDK.http)) {
            return false;
        }
        if (!accessToken.equals(kxSDK.accessToken)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = http.hashCode();
        result = 31 * result + accessToken.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "KxSDK{"
                + "http=" + http
                + ", accessToken='" + accessToken + '\''
                + '}';
    }


    public User getMyInfo(String fields) throws KxException {
        PostParameter[] params = new PostParameter[2];
        params[0] = new PostParameter("access_token", this.accessToken.getToken());
        if (null == fields) fields = "";
        params[1] = new PostParameter("fields", fields);
        return new User(get("users/me", params).asJSONObject());
    }


    public List<User> getUsers(String uids, String fields, int start, int num) throws KxException {
        if (uids.length() == 0) throw new KxException("app_param_lost");
        String[] uidArr = uids.split(",");
        int uidNum = uidArr.length;
        if (uidNum > 50) throw new KxException("app_uids_wrong");

        PostParameter[] params = new PostParameter[5];
        params[0] = new PostParameter("access_token", this.accessToken.getToken());
        if (null == fields) fields = "";
        if (start < 0) start = 0;
        if (num < 0 || num > 50) num = 20;
        params[1] = new PostParameter("uids", uids);
        params[2] = new PostParameter("fields", fields);
        params[3] = new PostParameter("start", start);
        params[4] = new PostParameter("num", num);
        return User.constructUser(get("users/show", params));
    }

    public List<User> getFriends(String fields, int start, int num) throws KxException {
        PostParameter[] params = new PostParameter[4];
        params[0] = new PostParameter("access_token", this.accessToken.getToken());
        if (null == fields) fields = "";
        if (start < 0) start = 0;
        if (num < 0 || num > 50) num = 20;
        ;
        params[1] = new PostParameter("fields", fields);
        params[2] = new PostParameter("start", start);
        params[3] = new PostParameter("num", num);
        return User.constructUser(get("friends/me", params));
    }

    public int getRelationShip(long uid1, long uid2) throws KxException {
        if (uid1 <= 0 || uid2 <= 0) {
            throw new KxException("app_param_lost");
        }
        PostParameter[] params = new PostParameter[3];
        params[0] = new PostParameter("access_token", this.accessToken.getToken());
        params[1] = new PostParameter("uid1", uid1);
        params[2] = new PostParameter("uid2", uid2);
        JSONObject json = get("friends/relationship", params).asJSONObject();

        int relationship = -1;
        try {
            relationship = json.getInt("relationship");
        } catch (JSONException e) {
            relationship = -1;
        }
        return relationship;
    }


    public List<AppStatus> getAppStatus(String uids, int start, int num) throws KxException {
        if (uids.length() == 0) throw new KxException("app_param_lost");
        String[] uidArr = uids.split(",");
        int uidNum = uidArr.length;
        if (uidNum > 50) throw new KxException("app_uids_wrong");

        PostParameter[] params = new PostParameter[4];
        params[0] = new PostParameter("access_token", this.accessToken.getToken());
        if (start < 0) start = 0;
        if (num < 0 || num > 50) num = 20;
        ;
        params[1] = new PostParameter("uids", uids);
        params[2] = new PostParameter("start", start);
        params[3] = new PostParameter("num", num);

        return AppStatus.constructStatus(get("app/status", params));
    }

    public UIDs getAppFriendUids(int start, int num) throws KxException {
        PostParameter[] params = new PostParameter[3];
        params[0] = new PostParameter("access_token", this.accessToken.getToken());
        if (start < 0) start = 0;
        if (num < 0 || num > 50) num = 20;
        ;
        params[1] = new PostParameter("start", start);
        params[2] = new PostParameter("num", num);
        return new UIDs(get("app/friends", params), this);
    }

    public InvitedUIDs getAppInvitedUids(long uid, int start, int num) throws KxException {
        if (uid <= 0) {
            throw new KxException("app_param_lost");
        }

        PostParameter[] params = new PostParameter[4];
        params[0] = new PostParameter("access_token", this.accessToken.getToken());
        if (start < 0) start = 0;
        if (num < 0 || num > 50) num = 20;
        ;
        params[1] = new PostParameter("uid", uid);
        params[2] = new PostParameter("start", start);
        params[3] = new PostParameter("num", num);

        return new InvitedUIDs(get("app/invited", params), this);
    }

    public long postRecord(String content, Integer save_to_album, String location, String lat, String lon, Integer sync_status, Integer spri, String pic, String picurl) throws KxException {
        if (content == null || content.length() <= 0) {
            throw new KxException("records content can't be null!");
        }
        if (save_to_album == null) save_to_album = 0;
        if (location == null) location = "";
        if (sync_status == null) sync_status = 0;
        if (lat == null) lat = "";
        if (lon == null) lon = "";
        if (spri == null) spri = 0;

        Response res = null;
        if (pic != null && pic.length() > 0) {
            PostParameter[] params = new PostParameter[8];
            params[0] = new PostParameter("access_token", this.accessToken.getToken());
            params[1] = new PostParameter("content", content);
            params[2] = new PostParameter("save_to_album", save_to_album);
            params[3] = new PostParameter("location", location);
            params[4] = new PostParameter("sync_status", sync_status);
            params[5] = new PostParameter("spri", spri);
            params[6] = new PostParameter("lat", lat);
            params[7] = new PostParameter("lon", lon);
            File picFile = new File(pic);
            if (!picFile.exists()) {
                System.out.println("文件\"" + pic + "\"不存在");
                return 0;
            }
            res = http.multPartURL("pic", baseURL + "records/add" + "." + format, params, picFile);
        } else {
            PostParameter[] params = new PostParameter[9];
            params[0] = new PostParameter("content", content);
            params[1] = new PostParameter("save_to_album", save_to_album);
            params[2] = new PostParameter("location", location);
            params[3] = new PostParameter("sync_status", sync_status);
            params[4] = new PostParameter("spri", spri);
            params[5] = new PostParameter("lat", lat);
            params[6] = new PostParameter("lon", lon);
            params[7] = new PostParameter("picurl", picurl);
            params[8] = new PostParameter("access_token", this.accessToken.getToken());
            res = post("records/add", params);
        }

        JSONObject json = res.asJSONObject();
        long rid = 0;
        try {
            rid = json.getLong(("rid"));
        } catch (JSONException ex) {
            Logger.getLogger(KxSDK.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rid;
    }

    public long album_create(String title, Integer privacy, String password, Integer category, Integer allow_repaste, String location, String description) throws KxException {
        PostParameter[] params = new PostParameter[8];
        params[0] = new PostParameter("title", title);
        params[1] = new PostParameter("privacy", privacy);
        params[2] = new PostParameter("password", password);
        params[3] = new PostParameter("category", category);
        params[4] = new PostParameter("allow_repaste", allow_repaste);
        params[5] = new PostParameter("location", location);
        params[6] = new PostParameter("description", description);
        params[7] = new PostParameter("access_token", this.accessToken.getToken());
        Response res = post("album/create", params);

        JSONObject json = res.asJSONObject();
        long albumid = 0;
        try {
            albumid = json.getLong(("albumid"));
        } catch (JSONException ex) {
            Logger.getLogger(KxSDK.class.getName()).log(Level.SEVERE, null, ex);
        }
        return albumid;
    }

    public Response album_show(long uid, long start, long num) throws KxException {
        PostParameter[] params = new PostParameter[4];
        params[0] = new PostParameter("uid", uid);
        params[1] = new PostParameter("start", start);
        params[2] = new PostParameter("num", num);
        params[3] = new PostParameter("access_token", this.accessToken.getToken());
        return get("album/show", params);
    }

    public Response photo_upload(long albumid, String title, String size, Integer send_news, String pic) throws KxException {
        PostParameter[] params = new PostParameter[5];
        params[0] = new PostParameter("albumid", albumid);
        params[1] = new PostParameter("title", title);
        params[2] = new PostParameter("size", size);
        params[3] = new PostParameter("send_news", send_news);
        params[4] = new PostParameter("access_token", this.accessToken.getToken());

        File picFile = new File(pic);
        if (!picFile.exists()) {
            System.out.println("文件\"" + pic + "\"不存在");
            return null;
        }
        return http.multPartURL("pic", baseURL + "photo/upload" + "." + format, params, picFile);
    }

    public Response photo_show(long uid, long albumid, long pid, String password, long start, long num) throws KxException {
        PostParameter[] params = new PostParameter[7];
        params[0] = new PostParameter("uid", uid);
        params[1] = new PostParameter("albumid", albumid);
        params[2] = new PostParameter("pid", pid);
        params[3] = new PostParameter("password", password);
        params[4] = new PostParameter("start", start);
        params[5] = new PostParameter("num", num);
        params[6] = new PostParameter("access_token", this.accessToken.getToken());
        return post("photo/show", params);
    }

    //--------------base method----------

    protected Response get(String api, PostParameter[] params) throws KxException {
        api = baseURL + api + "." + format;
        return http.get(api, params);
    }

    protected Response post(String api, PostParameter[] params) throws KxException {
        api = baseURL + api + "." + format;
        return http.post(api, params);
    }
}
