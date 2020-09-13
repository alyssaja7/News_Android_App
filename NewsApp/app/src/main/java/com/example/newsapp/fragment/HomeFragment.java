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
import com.example.newsapp.adapter.HomeAdapter;
import com.example.newsapp.item.HomeItem;
import com.example.newsapp.R;

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

//weather apikey: 1160ec4fcc881e1312a5606b011a693e


public class HomeFragment extends Fragment{
    View view;
    private RecyclerView recyclerView;
    private ArrayList<HomeItem> homeList;    //used for store json data
    private HomeAdapter homeAdapter;
    private RequestQueue requestQueue;              //need for volley

    public HomeFragment() { }

//    //test
//        @Override
//        public boolean onBackPressed() {
//            if (true) {
//                //action not popBackStack
//                Log.e("HI", "true");
//                return true;
//            } else {
//                Log.e("HI", "false");
//                return false;
//
//            }
//        }





    //创建该fragment对应的视图，你必须在这里创建自己的视图并返回给调用者
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home,container,false);    //加载一个布局文件
        recyclerView = view.findViewById(R.id.recycler_view_homeFg);   //从布局文件中查找一个控件
        // Log.d("onCreateView",homeList.toString());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));    //set items layed out in linear way
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));   //add a horizontal divider line between news cards

        return view;
    }






    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getContext());   // Instantiate the RequestQueue.
        parseJSON();



        //Log.d("onCreate",homeList.toString());
    }









    //获取json数据
    private void parseJSON() {
        String url = "http://10.0.2.2:3000/search?order-by=newest&show-fields=starRating,headline,thumbnail,short-url&api-key=c05384c5-6def-48bd-8d98-ad6b7c9e9caa";
        //String url = "https://content.guardianapis.com/search?order-by=newest&show-fields=starRating,headline,thumbnail,short-url&api-key=c05384c5-6def-48bd-8d98-ad6b7c9e9caa";
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
                                //Log.e("section", section);

                                String webUrl = article.getString("webUrl");
                                Log.e("webUrl", webUrl);

                                String id = article.getString("id");

                                homeList.add(new HomeItem( imgUrl,title, section, time, webUrl, id));
                            }
                            //Log.d("end",homeList.toString());
                            //Log.d("getContext", getContext().toString());
                            homeAdapter = new HomeAdapter(getContext(),homeList);        //adapter: UI和数据的接口 ---> pass json data to adapter
                            recyclerView.setAdapter(homeAdapter);                       //set adapter on recycler view


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
    public void onDetach(){
        super.onDetach();
        Log.e("HomeFragment", "ondetach");
        if (homeAdapter != null){
            homeAdapter.notifyDataSetChanged();
        }

    }

//    @Override
//    public void onBackPressed(){
//        super.onBackPressed();
//

 //   }


}
