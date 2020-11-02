package cn.yesomething.improjectclient.manager;

//管理后台地址
public class UrlManager {
    //安卓10.0.2.2
    //本地127.0.1
    public static final String myServer = "http://10.0.2.2:8080/IMProject";
    //用户相关
    public static final String generateUserSigUrl = "/im_general_service/generate_user_sig";
    public static final String userRegisterUrl = "/im_user_service/user_register";
    public static final String userLoginUrl = "/im_user_service/user_login";
    public static final String userSelectUrl = "/im_user_service/user_select";
    public static final String userUpdateUrl = "/im_user_service/user_update";
    public static final String userPictureUpdateUrl = "/im_user_service/user_picture_upload";
    public static final String userWordCloudGenerateUrl = "/im_user_service/user_word_cloud_generate";
    //消息相关
    public static final String messageSelectUrl = "/im_message_service/message_select";
    public static final String messageInsertUrl = "/im_message_service/message_insert";
    //好友相关
    public static final String friendsAddUrl = "/im_friends_service/friends_add";
    public static final String friendsUpdateUrl = "/im_friends_service/friends_update";
    public static final String friendsSelectUrl = "/im_friends_service/friends_select";
    public static final String friendsListSelectUrl = "/im_friends_service/friends_list_select";
}
