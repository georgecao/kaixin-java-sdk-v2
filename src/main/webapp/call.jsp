<%@ page contentType="text/html;charset=utf-8" %>
<%@ page language="java" import="com.kaixin001.Kaixin" %>
<%@ page import="com.kaixin001.http.AccessToken" %>
<%@ page language="java" %>
<%
    Kaixin.consumerKey = "4981162124136a074769f1078676fdb2";
    Kaixin.consumerSecret = "71d46998b5518f6c3e04958118daccde";
    Kaixin.redirectUri = "http://www.dajie.com/callback.jsp";
%>

<jsp:useBean id="connection" scope="session" class="com.kaixin001.Kaixin"/>
<%
    String scope = "create_records create_album user_photo friends_photo upload_photo";
    if ("1".equals(request.getParameter("opt"))) {
        response.sendRedirect(connection.getAuthorizeURLForCode(scope, "", ""));
    } else if ("2".equals(request.getParameter("opt"))) {
        response.sendRedirect(connection.getAuthorizeURLForToken(scope, "", ""));
    } else if ("3".equals(request.getParameter("opt"))) {
        String username = "";
        String password = "";
        AccessToken accessToken = connection.getOAuthAccessTokenFromPassword(username, password, scope);
        if (accessToken != null)//
        {
            String access_token = accessToken.getToken();
            out.println("accessToken: " + accessToken);
            response.sendRedirect("callback.php?access_token=" + access_token);
        } else {
            out.println("access token request error");
        }
    } else if ("4".equals(request.getParameter("opt"))) {

        String refresh_token = "";
        AccessToken accessToken = connection.getOAuthAccessTokenFromRefreshToken(refresh_token, scope);
        if (accessToken != null)//
        {
            String access_token = accessToken.getToken();
            out.println("accessToken: " + accessToken);
            response.sendRedirect("callback.jsp?access_token=" + access_token);
        } else {
            out.println("access token request error");
        }
    } else {
%>
可在此文件中修改scope
String scope = "create_records create_album user_photo friends_photo upload_photo";
<p><a href="call.jsp?opt=1">a. Authorization Code：Web Server Flow，适用于所有有Server端配合的应用。</a></p>

<p><a href="call.jsp?opt=2">b. Implicit Grant：User-Agent Flow，适用于所有无Server端配合的应用。</a></p>

<p><a href="call.jsp?opt=3">c. Resource Owner Password Credentials：采用用户名、密码获取Access Token，适用于任何类型应用。</a></p>
c.使用前在此文件中添加用户名和密码：
String username = "";
String password = "";
该方法签署保密协议后才能使用

<p><a href="call.jsp?opt=4">d. Refresh Token：令牌刷新方式，适用于所有有Server端配合的应用。</a></p>
d.使用前在此文件中设置refresh_token：
String refresh_token = "";
<%}%>