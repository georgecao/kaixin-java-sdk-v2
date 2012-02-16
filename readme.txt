Java SDK For Kaixin001 OAuth2.0


1、请把您的APP Key、secret以及回调地址添加到src/kx4j/KxSDK.java中：
	public static String CONSUMER_KEY =  "";//api key
    	public static String CONSUMER_SECRET = "";//secret key
	public static String Redirect_uri = ".../callback.jsp";//需与注册信息中网站地址的域名一致，可修改域名映射在本地进行测试



2、运行test/web/call.jsp文件就可以体验oauth2.0授权流程了。使用oauth授权，组件类型必须是“连接开心网”，若是“站内组件”会返回40011错误。其中：
  c. Resource Owner Password Credentials：采用用户名、密码获取Access Token，适用于任何类型应用。
	
需签署保密协议之后才能使用。使用前需在authorize.php文件中添加用户名和密码：
	String username = "";
	String password = "";
  d. Refresh Token：令牌刷新方式，适用于所有有Server端配合的应用。
	
	需用其它三种方式中的一种获取到refresh_token后才能使用。



3、在本SDK的回调页面test/web/callback.jsp文件中提供了各接口调用实例。

目前实现的接口有：

Users: 用户信息接口
/users/me　获取当前登录用户的资料
/users/show　根据UID获取多个用户的资料

Friends: 好友信息接口
/friends/me　获取当前登录用户的好友资料
/friends/relationship　获取两个用户间的好友关系


App: APP信息接口
/app/status　 获取用户安装组件的状态
/app/friends　获取当前用户安装组件的好友uid列表
/app/invited　获取某用户邀请成功的好友uid列表

Records: 记录接口
/records/add　 发表一条记录(可带图)

Album: 照片专辑接口
/album/create　 创建一个照片专辑
/album/show　 获取用户照片专辑列表

Photo: 照片接口
/photo/upload　 上传一张照片到指定照片专辑
/photo/show　 获取用户照片专辑的照片