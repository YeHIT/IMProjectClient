package cn.yesomething.improjectclient;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

import cn.yesomething.improjectclient.PageContact.ContactFragment;
import cn.yesomething.improjectclient.PageConversation.ConversationFragment;

public class mainFragmentPagerAdapter extends FragmentPagerAdapter {
    private ContactFragment Contact_FGM=null;//联系人界面
    private ConversationFragment Conversation_FGM=null;//会话列表界面

    //界面编号
    private final int Pager_Count =2;//一共2个一级子界面
    private static final int ContactPAGE=1;
    private static final int ConversationPAGE=0;
    private static final int MinePAGE=2;

    public mainFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragList) {
        super(fm);
        Contact_FGM = new ContactFragment();
        Conversation_FGM = new ConversationFragment();
    }
    @Override
    public Fragment getItem(int position)
    {
        Fragment fragment = null;
        switch(position){
            case ContactPAGE:
                fragment = Conversation_FGM;
                break;
            case ConversationPAGE:
                fragment = Contact_FGM;
                break;
//            case MinePAGE:
//                fragment = Conversation_FGM;
//                break;

            default:
                throw new IllegalStateException("Unexpected value: " + position);
        }
        return fragment;

    }
    @Override
    public int getCount()
    {
        return Pager_Count;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }


}
