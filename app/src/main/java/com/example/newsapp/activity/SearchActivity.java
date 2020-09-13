package com.example.newsapp.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsapp.R;
import com.example.newsapp.adapter.SearchAdapter;
import com.example.newsapp.item.SearchItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//如果不extends AppCompatActivity那么getSupportActionBar是会报错的
public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayout spinner_layout;
    private SearchAdapter searchAdapter;
    private ArrayList<SearchItem> searchList;


    private com.android.volley.RequestQueue requestQueue;

    private Toolbar toolbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);





        recyclerView = findViewById(R.id.recycler_view_search);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        spinner_layout = findViewById(R.id.spinner_layout);
        spinner_layout.setVisibility(View.VISIBLE);

        searchList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);


        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        String query = intent.getStringExtra(SearchManager.QUERY); //if it's a string you stored.
        Log.e("query in searchActivity", query);

        initToolbar(query);
        doSearch(query);


    }



    private void initToolbar(String query) {

        toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        //set back navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24px);
        getSupportActionBar().setTitle("Search Results for " + query);
        /**
         * set the back button, back to 上一层 by finish() in onClickListener
         */
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
                                                 @Override
                                                 public void onClick(View v) {
                                                     finish();

                                                 }
                                             }

        );

    }


    private void doSearch(String query) {
        //http://localhost:3000/search/result?q=debates&api-key=c05384c5-6def-48bd-8d98-ad6b7c9e9caa&show-blocks=all
        String url = "http://ec2-52-90-160-167.compute-1.amazonaws.com:4000/search/result?q=" + query + "&api-key=c05384c5-6def-48bd-8d98-ad6b7c9e9caa&show-blocks=all";
        Log.e("url",url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Log.e("Response", response.toString());   //log里面输出response check

                            JSONArray latestTen = response.getJSONArray("latestTen");
                            for(int i = 0; i < latestTen.length(); ++i){
                                JSONObject article = latestTen.getJSONObject(i);

                                String title = article.getString("title");
                                Log.e("search title", title);

                                String imgUrl = article.getString("img");

                                String time = article.getString("time");

                                String section = article.getString("section");

                                String webUrl = article.getString("webUrl");

                                String id = article.getString("id");

                                searchList.add(new SearchItem( imgUrl,title, section, time, webUrl, id));
                            }
                            //Log.d("end",homeList.toString());
                            //Log.d("getContext", getContext().toString());
                            searchAdapter = new SearchAdapter(SearchActivity.this,searchList);        //adapter: UI和数据的接口
                            recyclerView.setAdapter(searchAdapter);                       //set adapter on recycler view


                            spinner_layout.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        requestQueue.add(request);

    }


    @Override
    public void onResume() {
        recyclerView.setAdapter(searchAdapter);
        super.onResume();
    }



}
