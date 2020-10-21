package cn.yesomething.improjectclient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class NewFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragList;
    private List<String> tabList;
    public NewFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragList) {
        super(fm);
        this.fragList = fragList;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return tabList.get(position);
    }
    @Override
    public Fragment getItem(int position)
    {
        return fragList.get(position);
    }
    @Override
    public int getCount()
    {
        return fragList.size();
    }
}
