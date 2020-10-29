package cn.yesomething.improjectclient;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.yesomething.improjectclient.PageContact.ContactFragment;
import cn.yesomething.improjectclient.PageConversation.ConversationFragment;

public class mainFragmentPagerAdapter extends FragmentPagerAdapter {
    private ContactFragment Contact_FGM=null;//联系人界面
    private ContactFragment Conversation_FGM=null;//会话列表人界面
    //private ConversationFragment Conversation_FGM=null;//会话列表人界面
    private ContactFragment Mine_FGM=null;//联系人界面
    //界面编号
    private final int Pager_Count =3;//一共3个一级子界面
    private static final int ContactPAGE=0;
    private static final int ConversationPAGE=1;
    private static final int MinePAGE=2;

    public mainFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragList) {
        super(fm);
        //Conversation_FGM = new ConversationFragment();
        Conversation_FGM = new ContactFragment();
        Contact_FGM = new ContactFragment();
        Mine_FGM = new ContactFragment();

    }
    @Override
    public Fragment getItem(int position)
    {
        Fragment fragment = null;
        switch(position){
            case ContactPAGE:
                fragment = Contact_FGM;
                break;
            case ConversationPAGE:
                fragment = Conversation_FGM;
                break;
            case MinePAGE:
                fragment = Mine_FGM;
                break;


            default:
                throw new IllegalStateException("Unexpected page: " + position);
        }
        return fragment;

    }
    @Override
    public int getCount()
    {
        return Pager_Count;
    }


    public static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }


}
