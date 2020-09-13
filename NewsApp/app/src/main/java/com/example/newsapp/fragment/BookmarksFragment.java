package com.example.newsapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newsapp.R;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BookmarksFragment extends Fragment {

    View view;
    private RecyclerView recyclerView;

    public BookmarksFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home,container,false);    //加载一个布局文件
        recyclerView = view.findViewById(R.id.recycler_view_homeFg);   //从布局文件中查找一个控件

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));    //set items layed out in linear way

        return view;

    }
}
