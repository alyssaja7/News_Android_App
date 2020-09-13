package com.example.newsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.newsapp.R;
import com.example.newsapp.fragment.BookmarksFragment;
import com.example.newsapp.fragment.HeadlinesFragment;
import com.example.newsapp.fragment.HomeFragment;
import com.example.newsapp.fragment.TrendingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ActionBar bottom_bar;   //declare action bar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Make sure this is before calling super.onCreate
        // setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTab();

    }

    //初始化顶部和底部标签
    private void initTab() {
        /*set top toolbar*/
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("NewsApp");



        /*set bottom toolbar*/
        bottom_bar = getSupportActionBar();     //initialize
        BottomNavigationView navigation = findViewById(R.id.bottom_bar);   //initialize the bottom toolbar by id
        navigation.setOnNavigationItemSelectedListener(menuItemSelectedListener);   //set a listener
        loadFragment(new HomeFragment());    //when app open for first time, load this fragment

    }
    //设置topbar icon
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_topbar,menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        String msg = "";
//        if(item.getItemId() == R.id.topBar_search){
//            msg = "Search";
//            Toast.makeText(this, msg + "Clicked", Toast.LENGTH_LONG).show();
//        }
//        return super.onOptionsItemSelected(item);
//    }

    //let the user navigate on clicking the tabs
    private BottomNavigationView.OnNavigationItemSelectedListener menuItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener(){

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.Home:
                    loadFragment(new HomeFragment());
                    return true;
                case R.id.Headlines:
                    loadFragment(new HeadlinesFragment());
                    return true;
                case R.id.Trending:
                    loadFragment(new TrendingFragment());
                    return true;
                case R.id.Bookmarks:
                    loadFragment(new BookmarksFragment());
                    return true;
            }

            return false;
        }
    };


    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}









