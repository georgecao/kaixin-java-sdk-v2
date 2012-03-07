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
package com.kaixin001;


import com.kaixin001.http.AccessToken;
import com.kaixin001.http.HttpClient;
import com.kaixin001.http.PostParameter;
import com.kaixin001.http.Response;
import com.kaixin001.org.json.JSONException;
import com.kaixin001.org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * A java representation of the <a href="http://wiki.open.kaixin001.com/">Kaixin API</a>
 */
public class Kaixin {
    private static final Logger log = Logger.getLogger(Kaixin.class.getName());
    public static String consumerKey = "";//api key
    public static String consumerSecret = "";//secret key
    public static String redirectUri = "";//需与注册信息中网站地址的域名一致，可修改域名映射在本地进行测试
    public static String baseURL = "https://api.kaixin001.com/";
    public static String authorizationURL = "http://api.kaixin001.com/oauth2/authorize";
    public static String accessTokenURL = "http://api.kaixin001.com/oauth2/access_token";
    public static String accessTokenURLssl = "https://api.kaixin001.com/oauth2/access_token";
    public static String format = "json";

    protected HttpClient http = new HttpClient();
    protected AccessToken accessToken = null;

    public String getAuthorizeURLForCode(String scope, String state, String display) {
        return getAuthorizeURL("code", scope, state, display);
    }

    public String getAuthorizeURLForToken(String scope, String state, String display) {
        return getAuthorizeURL("token", scope, state, display);
    }

    protected String getAuthorizeURL(String type, String scope, String state, String display) {
        PostParameter[] params = new PostParameter[6];
        params[0] = new PostParameter("response_type", type);
        params[1] = new PostParameter("client_id", consumerKey);
        params[2] = new PostParameter("redirect_uri", redirectUri);
        params[3] = new PostParameter("scope", scope);
        params[4] = new PostParameter("state", state);
        params[5] = new PostParameter("display", display);
        String query = HttpClient.encodeParameters(params);
        return authorizationURL + "?" + query;
    }

    public AccessToken getOAuthAccessTokenFromCode(String code) throws KaixinException {
        AccessToken oauthToken = null;
        try {
            PostParameter[] params = new PostParameter[5];
            params[0] = new PostParameter("grant_type", "authorization_code");
            params[1] = new PostParameter("client_id", consumerKey);
            params[2] = new PostParameter("client_secret", consumerSecret);
            params[3] = new PostParameter("code", code);
            params[4] = new PostParameter("redirect_uri", redirectUri);

            oauthToken = new AccessToken(http.get(accessTokenURL, params));
        } catch (KaixinException te) {
            throw new KaixinException("The user has not given access to the account.", te, te.getStatusCode());
        }
        return oauthToken;
    }

    public AccessToken getOAuthAccessTokenFromPassword(String username, String password, String scope) throws KaixinException {
        AccessToken oauthToken = null;
        try {
            PostParameter[] params = new PostParameter[6];
            params[0] = new PostParameter("grant_type", "password");
            params[1] = new PostParameter("client_id", consumerKey);
            params[2] = new PostParameter("client_secret", consumerSecret);
            params[3] = new PostParameter("username", username);
            params[4] = new PostParameter("password", password);
            params[5] = new PostParameter("scope", scope);
            oauthToken = new AccessToken(http.get(accessTokenURLssl, params));
        } catch (KaixinException te) {
            throw new KaixinException("The user has not given access to the account.", te, te.getStatusCode());
        }
        return oauthToken;
    }

    public AccessToken getOAuthAccessTokenFromRefreshToken(String refreshToken, String scope) throws KaixinException {
        AccessToken oauthToken = null;
        try {
            PostParameter[] params = new PostParameter[5];
            params[0] = new PostParameter("grant_type", "refresh_token");
            params[1] = new PostParameter("client_id", consumerKey);
            params[2] = new PostParameter("client_secret", consumerSecret);
            params[3] = new PostParameter("refresh_token", refreshToken);
            params[4] = new PostParameter("scope", scope);
            oauthToken = new AccessToken(http.get(accessTokenURL, params));
        } catch (KaixinException te) {
            throw new KaixinException("The user has not given access to the account.", te, te.getStatusCode());
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

        Kaixin kaixin = (Kaixin) o;
        if (!http.equals(kaixin.http)) {
            return false;
        }
        if (!accessToken.equals(kaixin.accessToken)) {
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
        return "Kaixin{"
                + "http=" + http
                + ", accessToken='" + accessToken + '\''
                + '}';
    }


    public User getMyInfo(String fields) throws KaixinException {
        PostParameter[] params = new PostParameter[2];
        params[0] = new PostParameter("access_token", this.accessToken.getToken());
        if (null == fields) fields = "";
        params[1] = new PostParameter("fields", fields);
        return new User(get("users/me", params).asJSONObject());
    }


    public List<User> getUsers(String uids, String fields, int start, int num) throws KaixinException {
        if (uids.length() == 0) throw new KaixinException("app_param_lost");
        String[] uidArr = uids.split(",");
        int uidNum = uidArr.length;
        if (uidNum > 50) throw new KaixinException("app_uids_wrong");

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

    public List<User> getFriends(String fields, int start, int num) throws KaixinException {
        PostParameter[] params = new PostParameter[4];
        params[0] = new PostParameter("access_token", this.accessToken.getToken());
        if (null == fields) fields = "";
        if (start < 0) start = 0;
        if (num < 0 || num > 50) num = 20;
        params[1] = new PostParameter("fields", fields);
        params[2] = new PostParameter("start", start);
        params[3] = new PostParameter("num", num);
        return User.constructUser(get("friends/me", params));
    }

    public int getRelationShip(long uid1, long uid2) throws KaixinException {
        if (uid1 <= 0 || uid2 <= 0) {
            throw new KaixinException("app_param_lost");
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


    public List<AppStatus> getAppStatus(String uids, int start, int num) throws KaixinException {
        if (uids.length() == 0) throw new KaixinException("app_param_lost");
        String[] uidArr = uids.split(",");
        int uidNum = uidArr.length;
        if (uidNum > 50) throw new KaixinException("app_uids_wrong");

        PostParameter[] params = new PostParameter[4];
        params[0] = new PostParameter("access_token", this.accessToken.getToken());
        if (start < 0) start = 0;
        if (num < 0 || num > 50) num = 20;

        params[1] = new PostParameter("uids", uids);
        params[2] = new PostParameter("start", start);
        params[3] = new PostParameter("num", num);

        return AppStatus.constructStatus(get("app/status", params));
    }

    public UIDs getAppFriendUids(int start, int num) throws KaixinException {
        PostParameter[] params = new PostParameter[3];
        params[0] = new PostParameter("access_token", this.accessToken.getToken());
        if (start < 0) start = 0;
        if (num < 0 || num > 50) num = 20;
        params[1] = new PostParameter("start", start);
        params[2] = new PostParameter("num", num);
        return new UIDs(get("app/friends", params), this);
    }

    public InvitedUIDs getAppInvitedUids(long uid, int start, int num) throws KaixinException {
        if (uid <= 0) {
            throw new KaixinException("app_param_lost");
        }

        PostParameter[] params = new PostParameter[4];
        params[0] = new PostParameter("access_token", this.accessToken.getToken());
        if (start < 0) start = 0;
        if (num < 0 || num > 50) num = 20;
        params[1] = new PostParameter("uid", uid);
        params[2] = new PostParameter("start", start);
        params[3] = new PostParameter("num", num);

        return new InvitedUIDs(get("app/invited", params), this);
    }

    /**
     * 向指定的好友发送系统消息，注意：只能给当前用户的好友发送系统消息
     *
     * @param fuids    Required.
     *                 系统消息接收用户的uid,多个用户用半角逗号隔开，最多30个
     * @param linkText Required.
     *                 动态里面的链接文字，不超过15个汉字。例如，链接文案：去xx帮忙。
     * @param link     Required.
     *                 动态里的链接地址。必须以http或https开头。
     * @param text     Required.
     *                 发送动态所使用的文案，不超过60个汉字，否则会被截断。 该文案可以有{_USER_} {_USER_TA_}变量，解析时会被替换为当前用户名字和他/她。 例如，动态文案：{_USER_} 在做XX任务时遇到了强大的XX，快去帮帮{_USER_TA_}！
     * @param word     Optional.
     *                 动态里用户的附言
     * @param picUrl   Optional.
     *                 发送动态所使用的图片地址，如果动态分享中需要发布图片，则此项必填。 单张图片时，大小为80×80。
     * @return an instance of {@link Response}
     * @throws KaixinException
     */
    public Response sendSysNews(String fuids, String linkText, String link, String text, String word, String picUrl) throws KaixinException {
        List<PostParameter> params = new ArrayList<PostParameter>(7);
        params.add(new PostParameter("fuids", fuids));
        params.add(new PostParameter("linktext", linkText));
        params.add(new PostParameter("link", link));
        params.add(new PostParameter("text", text));
        if (null != word && word.trim().length() != 0) {
            params.add(new PostParameter("word", word));
        }
        if (null != picUrl && picUrl.trim().length() != 0) {
            params.add(new PostParameter("picurl", picUrl));
        }
        params.add(new PostParameter("access_token", this.accessToken.getToken()));
        return post("/sysnews/send", params);
    }

    /**
     * 发布一条记录(可以带一张图片)
     *
     * @param content     Required.
     *                    发记录的内容(最多140个汉字或280个英文字母字符)
     * @param saveToAlbum Optional.
     *                    是否存到记录相册中，0/1-不保存/保存，默认为0不保存
     * @param location    Optional.
     *                    记录的地理位置(目前仅在“我的记录”列表中显示)
     * @param lat         Optional.
     *                    纬度 -90.0到+90.0，+表示北纬(目前暂不能显示)
     * @param lon         Optional.
     *                    经度 -180.0到+180.0，+表示东经(目前暂不能显示)
     * @param syncStatus  Optional.
     *                    是否同步签名 0/1/2-无任何操作/同步/不同步，默认为0无任何操作
     * @param privacy     Optional.
     *                    权限设置，0/1/2/3-任何人可见/好友可见/仅自己可见/好友及好友的好友可见 默认为0任何人可见
     * @param pic         Optional.
     *                    发记录上传的图片，图片在10M以内，格式支持jpg/jpeg/gif/png/bmp *pic和picurl只能选择其一，两个同时提交时，只取pic * oauth1.0，pic参数不需要参加签名
     * @param picUrl      Optional.
     *                    外部图片链接，图片在10M以内，格式支持jpg/jpeg/gif/png/bmp *pic和picurl只能选择其一，两个同时提交时，只取pic
     * @return an instance of {@link Response}
     * @throws KaixinException
     */
    public long postRecord(String content, Integer saveToAlbum, String location, String lat, String lon, Integer syncStatus, Integer privacy, String pic, String picUrl) throws KaixinException {
        if (content == null || content.length() <= 0) {
            throw new KaixinException("records content can't be null!");
        }
        if (saveToAlbum == null) saveToAlbum = 0;
        if (location == null) location = "";
        if (syncStatus == null) syncStatus = 0;
        if (lat == null) lat = "";
        if (lon == null) lon = "";
        if (privacy == null) privacy = 0;
        List<PostParameter> postParameters = new ArrayList<PostParameter>(9);
        postParameters.add(new PostParameter("access_token", this.accessToken.getToken()));
        postParameters.add(new PostParameter("content", content));
        postParameters.add(new PostParameter("save_to_album", saveToAlbum));
        postParameters.add(new PostParameter("location", location));
        postParameters.add(new PostParameter("sync_status", syncStatus));
        postParameters.add(new PostParameter("spri", privacy));
        postParameters.add(new PostParameter("lat", lat));
        postParameters.add(new PostParameter("lon", lon));
        Response res = null;
        if (pic != null && pic.length() > 0) {
            File picFile = new File(pic);
            if (!picFile.exists()) {
                System.out.println("文件\"" + pic + "\"不存在");
                return 0;
            }
            res = http.multPartURL("pic", baseURL + "records/add" + "." + format, postParameters.toArray(new PostParameter[postParameters.size()]), picFile);
        } else {
            postParameters.add(new PostParameter("picurl", picUrl));
            res = post("records/add", postParameters);
        }
        JSONObject json = res.asJSONObject();
        long rid = 0;
        try {
            rid = json.getLong(("rid"));
        } catch (JSONException ex) {
            log.log(Level.SEVERE, null, ex);
        }
        return rid;
    }

    public long createAlbum(String title, Integer privacy, String password, Integer category, Integer allowRepaste, String location, String description) throws KaixinException {
        PostParameter[] params = new PostParameter[8];
        params[0] = new PostParameter("title", title);
        params[1] = new PostParameter("privacy", privacy);
        params[2] = new PostParameter("password", password);
        params[3] = new PostParameter("category", category);
        params[4] = new PostParameter("allow_repaste", allowRepaste);
        params[5] = new PostParameter("location", location);
        params[6] = new PostParameter("description", description);
        params[7] = new PostParameter("access_token", this.accessToken.getToken());
        Response res = post("album/create", params);

        JSONObject json = res.asJSONObject();
        long albumId = 0;
        try {
            albumId = json.getLong(("albumid"));
        } catch (JSONException ex) {
            log.log(Level.SEVERE, null, ex);
        }
        return albumId;
    }

    public Response showAlbum(long uid, long start, long num) throws KaixinException {
        PostParameter[] params = new PostParameter[4];
        params[0] = new PostParameter("uid", uid);
        params[1] = new PostParameter("start", start);
        params[2] = new PostParameter("num", num);
        params[3] = new PostParameter("access_token", this.accessToken.getToken());
        return get("album/show", params);
    }

    public Response uploadPhoto(long albumId, String title, String size, Integer send_news, String pic) throws KaixinException {
        PostParameter[] params = new PostParameter[5];
        params[0] = new PostParameter("albumid", albumId);
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

    public Response showPhoto(long uid, long albumId, long pid, String password, long start, long num) throws KaixinException {
        PostParameter[] params = new PostParameter[7];
        params[0] = new PostParameter("uid", uid);
        params[1] = new PostParameter("albumid", albumId);
        params[2] = new PostParameter("pid", pid);
        params[3] = new PostParameter("password", password);
        params[4] = new PostParameter("start", start);
        params[5] = new PostParameter("num", num);
        params[6] = new PostParameter("access_token", this.accessToken.getToken());
        return post("photo/show", params);
    }

    //--------------base method----------
    protected Response get(String api, PostParameter[] params) throws KaixinException {
        api = baseURL + api + "." + format;
        return http.get(api, params);
    }

    protected Response post(String api, PostParameter[] params) throws KaixinException {
        api = baseURL + api + "." + format;
        return http.post(api, params);
    }

    protected Response get(String api, List<PostParameter> params) throws KaixinException {
        return get(api, params.toArray(new PostParameter[params.size()]));
    }

    protected Response post(String api, List<PostParameter> params) throws KaixinException {
        return post(api, params.toArray(new PostParameter[params.size()]));
    }
}
