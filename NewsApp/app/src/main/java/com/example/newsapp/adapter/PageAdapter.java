package com.example.newsapp.adapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {

    private List<String> tabsList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();
//    private int numOftabs;


    //constructor
    public PageAdapter(@NonNull FragmentManager fm, int position) {
        super(fm, position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
//        numOftabs = tabsList.size();
//        return numOftabs;
        return tabsList.size();
    }



    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabsList.get(position);
    }

    public void addFragment(String title, Fragment fragment){
        tabsList.add(title);
        fragmentList.add(fragment);
    }
}
