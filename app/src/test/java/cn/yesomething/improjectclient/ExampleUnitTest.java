package cn.yesomething.improjectclient;

import org.junit.Test;

import cn.yesomething.improjectclient.manager.UrlManager;
import cn.yesomething.improjectclient.utils.MyConnectionToServer;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void getSig() {
        String userSig = MyConnectionToServer.sendPost(UrlManager.myServer+UrlManager.generateUsersigUrl,
                "{\"userName\":\"111\"}");
        System.out.println(userSig);
    }

    @Test
    public void userLogin() {
        String userSig = MyConnectionToServer.sendPost(UrlManager.myServer+UrlManager.userLoginUrl,
                "{\"userName\":\"111\",\"userPassword\":\"111\"}");
        System.out.println(userSig);
    }

    @Test
    public void userUpdate() {
        String userSig = MyConnectionToServer.sendPost(UrlManager.myServer+UrlManager.userUpdateUrl,
                "{\"userName\":\"xy\",\"userPassword\":\"112\"}");
        System.out.println(userSig);
    }
}