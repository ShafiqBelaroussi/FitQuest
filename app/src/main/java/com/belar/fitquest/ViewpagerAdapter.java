package com.belar.fitquest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewpagerAdapter extends FragmentPagerAdapter
{

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentListTitles = new ArrayList<>();

    public ViewpagerAdapter(@NonNull FragmentManager fm)
    {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        return fragmentList.get(position);
    }

    @Override
    public int getCount()
    {
        return fragmentListTitles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        return fragmentListTitles.get(position);
    }

    public void addFragment (Fragment _fragment, String _fragmentTitle)
    {
        fragmentList.add(_fragment);
        fragmentListTitles.add(_fragmentTitle);
    }
}
