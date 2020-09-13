package com.example.newsapp.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsapp.R;
import com.example.newsapp.adapter.PoliticsAdapter;
import com.example.newsapp.item.PoliticsItem;

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

public class politics extends Fragment {
    View view;

    private LinearLayout spinner_layout;
    private RecyclerView recyclerView;
    private ArrayList<PoliticsItem> politicsList;
    private PoliticsAdapter politicsAdapter;
    private RequestQueue requestQueue;              //need for volley


    public politics() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_politics,container,false);
        recyclerView = view.findViewById(R.id.recycler_view_politicsFg);   //从布局文件中查找一个控件


        spinner_layout = view.findViewById(R.id.spinner_layout);
        spinner_layout.setVisibility(View.VISIBLE);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));   //add a horizontal divider line between news cards
        //parseJSON();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        politicsList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getContext());   // Instantiate the RequestQueue.
        parseJSON();
    }


    //获取json数据
    private void parseJSON() {
        String url = "http://10.0.2.2:4000/politics?api-key=c05384c5-6def-48bd-8d98-ad6b7c9e9caa&show-blocks=all";
        //String url = "http://ec2-52-90-160-167.compute-1.amazonaws.com:4000/politics";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Log.e("Response", response.toString());   //log里面输出response check

                            JSONArray latestTen = response.getJSONArray("latestTen");
                            for(int i = 0; i < latestTen.length(); ++i){
                                JSONObject article = latestTen.getJSONObject(i);
                                //Log.e("article", article.toString());
                                //String id = article.getString("id");
                                //Log.e("id", id);
                                String title = article.getString("title");
                                Log.e("politics title", title);

                                String imgUrl = article.getString("img");
                                //Log.e("img", imgUrl);

                                String time = article.getString("time");


                                String section = article.getString("section");


                                String webUrl = article.getString("webUrl");


                                String id = article.getString("id");

                                politicsList.add(new PoliticsItem( imgUrl,title, section, time, webUrl, id));
                            }
                            //Log.d("end",homeList.toString());
                            //Log.d("getContext", getContext().toString());
                            politicsAdapter = new PoliticsAdapter(getContext(),politicsList);        //adapter: UI和数据的接口
                            recyclerView.setAdapter(politicsAdapter);                       //set adapter on recycler view

                            spinner_layout.setVisibility(view.GONE);

                        } catch ( JSONException e) {
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

    /**
     * 解决detail article收藏不能和外面同步的问题
     * dialog可以同步更新是因为用了新的数据
     * 而这里你只是back to parent, 并不更新，所以要通知他
     */

    @Override
    public void onResume() {
        recyclerView.setAdapter(politicsAdapter);
        super.onResume();
    }

}


