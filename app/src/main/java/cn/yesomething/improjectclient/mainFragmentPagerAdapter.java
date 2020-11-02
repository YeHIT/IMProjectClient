package cn.yesomething.improjectclient;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

import cn.yesomething.improjectclient.PageContact.ContactFragment;
import cn.yesomething.improjectclient.PageConversation.ConversationFragment;

public class mainFragmentPagerAdapter extends FragmentPagerAdapter {
    private ContactFragment Contact_FGM=null;//联系人界面
    private ConversationFragment Conversation_FGM=null;//会话列表界面
    private Context mContext;
    //界面编号
    private final int Pager_Count =2;//一共2个一级子界面
    private static final int ContactPAGE=1;
    private static final int ConversationPAGE=0;
    private static final int MinePAGE=2;

    public mainFragmentPagerAdapter(Context context, FragmentManager fm, List<Fragment> fragList) {
        super(fm);
        mContext = context;
        Contact_FGM = new ContactFragment();
        Conversation_FGM = new ConversationFragment();
    }
    @Override
    public Fragment getItem(int position)
    {
        Fragment fragment = null;
        switch(position){
            case ContactPAGE:
                fragment = Contact_FGM;
                Toast.makeText(mContext, "in page contact", Toast.LENGTH_SHORT).show();
                break;
            case ConversationPAGE:
                fragment = Conversation_FGM;
                Toast.makeText(mContext, "in page conversation", Toast.LENGTH_SHORT).show();
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
