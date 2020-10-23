package cn.yesomething.improjectclient.manager;

import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import java.util.ArrayList;
import java.util.List;

//管理SDK用户相关
public class UserManager {

    /**
     * 根据用户名查看SDK对应的用户信息
     * @param userName 用户名
     * @param callback 回调
     */
    public static void getUserInfo(String userName,
                                   V2TIMValueCallback< List<V2TIMUserFullInfo>> callback) {
        ArrayList<String> userNameList = new ArrayList<>() ;
        userNameList.add(userName);
        getUserInfo(userNameList,callback);
    }

    /**
     * 根据用户名列表查看SDK对应的用户信息
     * @param userNameList 用户名列表
     * @param callback 回调
     */
    public static void getUserInfo(List< String > userNameList,
                                   V2TIMValueCallback< List<V2TIMUserFullInfo>> callback) {
        V2TIMManager.getInstance().getUsersInfo(userNameList,callback);
    }
}
