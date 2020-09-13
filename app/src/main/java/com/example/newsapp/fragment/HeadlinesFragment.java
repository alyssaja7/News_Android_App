package com.example.newsapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newsapp.R;
import com.example.newsapp.adapter.PageAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.viewpager.widget.ViewPager;

public class HeadlinesFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;


    private PageAdapter pageAdapter;

    public HeadlinesFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_headlines,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewpager);

        pageAdapter = new PageAdapter(getChildFragmentManager(),tabLayout.getTabCount());

        //add fragment to pageAdapter
        pageAdapter.addFragment("WORLDS", new worlds());
        pageAdapter.addFragment("BUSINESS", new business());
        pageAdapter.addFragment("POLITICS", new politics());
        pageAdapter.addFragment("SPORTS", new sports());
        pageAdapter.addFragment("TECHNOLOGY", new technology());
        pageAdapter.addFragment("SCIENCE", new science());

        //set up adapter
        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);


    }
}

