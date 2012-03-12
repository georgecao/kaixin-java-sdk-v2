<%@ page contentType="text/html;charset=utf-8" %>
<%@ page language="java" %>
<%@ page language="java" %>
<%@ page import="com.kaixin001.*" %>
<%@ page import="com.kaixin001.http.AccessToken" %>
<%@ page import="com.kaixin001.http.Response" %>
<%@ page import="java.util.List" %>

<script type="text/javascript">
    var access_token = window.location.hash;
    if (access_token) {
        access_token = access_token.substring(1);
        document.write(access_token);
        window.location.href = window.location.pathname + "?" + access_token;
    }
</script>
<%
    Kaixin.consumerKey = "4981162124136a074769f1078676fdb2";
    Kaixin.consumerSecret = "71d46998b5518f6c3e04958118daccde";
    Kaixin.redirectUri = "http://www.dajie.com/callback.jsp";
%>
<jsp:useBean id="connection" scope="session" class="com.kaixin001.Kaixin"/>
<%
    String code = request.getParameter("code");
    String access_token = request.getParameter("access_token");
    String method = request.getParameter("method");

    if (code != null)//code方式
    {
        out.println("code:" + code);
        //获取accesstoken
        AccessToken accessToken = connection.getOAuthAccessTokenFromCode(code);
        if (accessToken != null)//
        {
            access_token = accessToken.getToken();
            out.println("accessToken: " + accessToken);
        } else {
            out.println("access token request error");
        }
    } else if (method != null && access_token != null) {
        connection.setOAuthAccessToken(new AccessToken(access_token, ""));
        if ("me".equals(method)) {
            String fields = "";//"uid,name,gender,logo50";
            User user = connection.getMyInfo(fields);
            out.println(user.toString());
        } else if ("show".equals(method)) {
            String uids = "";
            String fields = "";//"uid,name,gender,logo50";
            List<User> userList = connection.getUsers(uids, fields, 0, 20);
            for (User user : userList) {
                out.println(user.toString());
            }
        } else if ("friends".equals(method)) {
            String fields = "";//"uid,name,gender,logo50";
            List<User> userList = connection.getFriends(fields, 0, 20);

            for (User user : userList) {
                out.println(user.toString());
            }
        } else if ("relation".equals(method)) {
            long uid1 = 13926;
            long uid2 = 13895;
            int relationship = connection.getRelationShip(uid1, uid2);

            out.println(relationship);
        } else if ("status".equals(method)) {
            String uids = "";
            List<AppStatus> appStatuses = connection.getAppStatus(uids, 0, 20);

            for (AppStatus appStatus : appStatuses) {
                out.println(appStatus.toString());
            }
        } else if ("friendstatus".equals(method)) {
            UIDs idsList = connection.getAppFriendUids(0, 20);

            out.println(idsList.toString());
        } else if ("invited".equals(method)) {
            long uid = 0;
            InvitedUIDs invitedUids = connection.getAppInvitedUids(uid, 0, 20);

            if (invitedUids == null) {
                out.println("no invited anyone!");
            } else {
                out.println(invitedUids.toString());
            }
        } else if ("records".equals(method)) {
            String content = new String(request.getParameter("content").getBytes("ISO8859-1"), "utf-8");//
            String picurl = request.getParameter("picurl");
            String pic = request.getParameter("pic");
            long rid = connection.postRecord(content, null, null, null, null, null, null, pic, picurl);

            if (rid == 0) {
                out.println("publish error!");
            } else {
                out.println("rid : " + rid);
            }
        } else if ("album_show".equals(method)) {
            long uid = 0;
            long start = 0;
            long num = 2;
            Response res = connection.showAlbum(uid, start, num);
            out.print(res.asJSONObject().toString());
        } else if ("album_create".equals(method)) {
            String title = "标题";
            Integer privacy = 0;
            String password = "";
            Integer category = 0;
            Integer allow_repaste = 0;
            String location = "";
            String description = "";
            long res = connection.createAlbum(title, privacy, password, category, allow_repaste, location, description);

            if (res == 0) {
                out.println("publish error!");
            } else {
                out.println("albumid : " + res);
            }
        } else if ("photo_show".equals(method)) {
            long uid = 0;
            long albumid = 0;
            long pid = 0;
            String password = "";
            long start = 0;
            long num = 2;
            Response res = connection.showPhoto(uid, albumid, pid, password, start, num);
            out.print(res.asJSONObject().toString());
        } else if ("photo_upload".equals(method)) {
            long albumid = 0;
            String title = "图片标题";
            String size = "mid";
            Integer send_news = 1;
            String pic = request.getParameter("pic");//可以是文件路径,如"D:\\tup.jpg"
            Response res = connection.uploadPhoto(albumid, title, size, send_news, pic);
            out.print(res.asJSONObject().toString());
        }
    } else if (access_token != null) {
        out.println("access_token:" + access_token);
        out.println("授权成功");
        out.println("<br/>");
    }

%>

<div>
    <h2>接口列表：</h2>
    <ul>
        <li><a href="callback.jsp?access_token=<%= access_token %>&method=me">/users/me　获取当前登录用户的资料</a></li>
        <li><a href="callback.jsp?access_token=<%= access_token %>&method=show">/users/show　根据UID获取多个用户的资料</a></li>
        <li><a href="callback.jsp?access_token=<%= access_token %>&method=friends">/friends/me　获取当前登录用户的好友资料 </a></li>
        <li>
            <a href="callback.jsp?access_token=<%= access_token %>&method=relation">/friends/relationship　获取两个用户间的好友关系 </a>
        </li>
        <li><a href="callback.jsp?access_token=<%= access_token %>&method=status">/app/status　 获取用户安装组件的状态</a></li>
        <li>
            <a href="callback.jsp?access_token=<%= access_token %>&method=friendstatus">/app/friends　获取当前用户安装组件的好友uid列表 </a>
        </li>
        <li><a href="callback.jsp?access_token=<%= access_token %>&method=invited">/app/invited　获取某用户邀请成功的好友uid列表</a>
        </li>
        <li><a href="callback.jsp?access_token=<%= access_token %>&method=album_show">/album/show　获取某照片专辑列表</a></li>
        <li><a href="callback.jsp?access_token=<%= access_token %>&method=album_create">/album/create　创建一个照片专辑列表</a>
        </li>
        <li><a href="callback.jsp?access_token=<%= access_token %>&method=photo_show">/photo/show　获取照片</a></li>
        <li>
            <h3>上传照片</h3>
            <ul>
                <li>
                    <form action="callback.jsp" method="get">
                        <input name="method" type="hidden" value="photo_upload"/>
                        <input name="access_token" type="hidden" value="<%=access_token%>"/>

                        <p><label>pic:</label><input type="file" name="pic"/></p>

                        <p><input class="submit" type="submit" value="上传"/></p>
                    </form>
                </li>
            </ul>
        </li>
        <li>
            <h3>发记录（可带一张图片）</h3>
            <ul>
                <li>
                    <form action="callback.jsp" method="post">
                        <input name="method" type="hidden" value="records"/>
                        <input name="access_token" type="hidden" value="<%=access_token%>"/>

                        <p><label>content: </label><input class="text" name="content" type="text" value=""/></p>

                        <p><label>save_to_album: </label><input class="text" name="save_to_album" type="text"
                                                                value="1"/></p>

                        <p><label>location: </label><input class="text" name="location" type="text" value=""/></p>

                        <p><label>lat: </label><input class="text" name="lat" type="text" value=""/></p>

                        <p><label>lon: </label><input class="text" name="lon" type="text" value=""/></p>

                        <p><label>spri: </label><input class="text" name="spri" type="text" value="1"/></p>

                        <p><label>rotate: </label><input class="text" name="rotate" type="text" value="0"/></p>

                        <p><label>sync_status: </label><input class="text" name="sync_status" type="text" value="1"/>
                        </p>

                        <p><label>pic:</label><input type="file" name="pic"/></p>

                        <p><label>picurl:</label><input type="text" name="picurl" value=""/></p>

                        <p><input class="submit" type="submit" value="发表"/></p>
                    </form>
                </li>
            </ul>
        </li>
    </ul>
</div>
<div><a href="call.jsp">重新执行授权过程</a></div>
