package com.example.newsapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsapp.R;
import com.example.newsapp.adapter.WorldsAdapter;
import com.example.newsapp.item.WorldsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class worlds extends Fragment {
    View view;

    private RecyclerView recyclerView;
    private ArrayList<WorldsItem> worldsList;
    private WorldsAdapter worldsAdapter;
    private RequestQueue requestQueue;              //need for volley


    public worlds() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_worlds,container,false);
        recyclerView = view.findViewById(R.id.recycler_view_worldsFg);                      //从布局文件中查找一个控件

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));   //add a horizontal divider line between news cards
        parseJSON();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        worldsList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getContext());   // Instantiate the RequestQueue.
        parseJSON();
    }




    //获取json数据
    private void parseJSON() {
        //String url = "http://10.0.2.2:3000/world?api-key=c05384c5-6def-48bd-8d98-ad6b7c9e9caa&show-blocks=all";
        String url = "http://10.0.2.2:3000/world";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("Response", response.toString());   //log里面输出response check

                            JSONArray latestTen = response.getJSONArray("latestTen");
                            for(int i = 0; i < latestTen.length(); ++i){
                                JSONObject article = latestTen.getJSONObject(i);
                                //Log.e("article", article.toString());
                                //String id = article.getString("id");
                                //Log.e("id", id);
                                String title = article.getString("title");
                                //Log.e("title", title);

                                String imgUrl = article.getString("img");
                                //Log.e("img", imgUrl);

                                String time = article.getString("time");
                                //Log.e("time", time);

                                String section = article.getString("section");
                                Log.e("world section", section);

                                String webUrl = article.getString("webUrl");
                                Log.e("world webUrl", webUrl);

                                String id = article.getString("id");

                                worldsList.add(new WorldsItem( imgUrl,title, section, time, webUrl, id));
                            }
                            //Log.d("end",homeList.toString());
                            //Log.d("getContext", getContext().toString());
                            worldsAdapter = new WorldsAdapter(getContext(),worldsList);        //adapter: UI和数据的接口
                            recyclerView.setAdapter(worldsAdapter);                       //set adapter on recycler view

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

}
