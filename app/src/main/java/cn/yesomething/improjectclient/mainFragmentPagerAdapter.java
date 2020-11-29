package cn.yesomething.improjectclient;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

import cn.yesomething.improjectclient.PageContact.ContactFragment;
import cn.yesomething.improjectclient.PageConversation.ConversationFragment;
import cn.yesomething.improjectclient.PageMine.MineFragment;

public class mainFragmentPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "mainPagerAdapter";

    private ContactFragment contactFragment=null;//联系人界面
    private ConversationFragment conversationFragment=null;//会话列表界面
    private MineFragment mineFragment = null;       //资料界面
    private Context mContext;
    //界面编号
    private final int PAGER_COUNT = 3;//一共3个一级子界面
    private static final int CONVERSATION_PAGE = 0;
    private static final int CONTACT_PAGE = 1;
    private static final int MINE_PAGE = 2;
    private int mChildCount = 0;

    public mainFragmentPagerAdapter(Context context, FragmentManager fm, List<Fragment> fragList) {
        super(fm,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
        contactFragment = new ContactFragment();
        conversationFragment = new ConversationFragment();
        mineFragment = new MineFragment();
    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch(position){
            case CONVERSATION_PAGE:
                fragment = conversationFragment;
                break;
            case CONTACT_PAGE:
                fragment = contactFragment;
                break;
            case MINE_PAGE:
                fragment = mineFragment;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + position);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        Log.e(TAG, "instantiateItem: "+Integer.toString(position) );

        return super.instantiateItem(vg, position);
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.e(TAG, "destroyItem: "+Integer.toString(position)+object );
        super.destroyItem(container, position, object);
    }



}
