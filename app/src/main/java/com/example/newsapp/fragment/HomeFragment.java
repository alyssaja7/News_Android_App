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
import com.example.newsapp.adapter.HomeAdapter;
import com.example.newsapp.item.HomeItem;
import com.example.newsapp.R;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.widget.TextView;

//weather apikey: 1160ec4fcc881e1312a5606b011a693e


public class HomeFragment extends Fragment{
    View view;

    private LinearLayout spinner_layout;
    private RecyclerView recyclerView;
    private ArrayList<HomeItem> homeList;    //used for store json data
    private HomeAdapter homeAdapter;
    private RequestQueue requestQueue;              //need for volley


    private CardView cardView;

    String state;
    String city;
    TextView cityView;
    TextView tempView;
    TextView stateView;
    TextView typeView;
    LinearLayout bgView;

    public HomeFragment() { }


    //创建该fragment对应的视图，你必须在这里创建自己的视图并返回给调用者
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home,container,false);    //加载一个布局文件

        //get view for weather card
        cardView = view.findViewById(R.id.weather_card);
        bgView = view.findViewById(R.id.weather_bg);
        cityView = view.findViewById(R.id.city);
        stateView = view.findViewById(R.id.state);
        tempView = view.findViewById(R.id.temp);
        typeView = view.findViewById(R.id.weather_type);


        recyclerView = view.findViewById(R.id.recycler_view_homeFg);   //从布局文件中查找一个控件

        spinner_layout = view.findViewById(R.id.spinner_layout);
        spinner_layout.setVisibility(view.VISIBLE);

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

        getPosition();
        parseJSON();

    }




    //getPosition
    private void getPosition() {
        String url = "http://ip-api.com/json/";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response in home", response.toString());   //log里面输出response check
                        try {
                            state = response.getString("regionName");
                            city = response.getString("city");

                            stateView.setText(state);
                            cityView.setText(city);
                            getWeather();


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


    //api.openweathermap.org/data/2.5/weather?q={city name},{state}&appid={your api key}
    //getWeather
    private void getWeather() {

        //https://api.openweathermap.org/data/2.5/weather?q=[CITY]&units=metric&appid=[YOU

        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=1160ec4fcc881e1312a5606b011a693e";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response in weather", response.toString());   //log里面输出response check
                        try {
                            String mtmp = response.getJSONObject("main").getString("temp");
                            double dTmp = Double.parseDouble(mtmp);
                            long tmp = Math.round(dTmp);



                            JSONArray summary = response.getJSONArray("weather");
                            String weather_type = summary.getJSONObject(0).getString("main");
                            Log.e("summary-----",  weather_type);

                            typeView.setText(weather_type);

                            tempView.setText(tmp + "\u2103");

                            if(weather_type.equals("Clouds")){
                                Log.e("hi","1");
                                bgView.setBackgroundResource(R.drawable.cloudy_weather);
                            }else if(weather_type.equals("Clear")){
                                Log.e("hi","2");
                                bgView.setBackgroundResource(R.drawable.clear_weather);
                            }else if(weather_type.equals("Snow")){
                                Log.e("hi","3");
                                bgView.setBackgroundResource(R.drawable.snowy_weather);
                            }else if(weather_type.equals("Rain") ||  weather_type.equals("Drizzle") ){
                                Log.e("hi","4");
                                bgView.setBackgroundResource(R.drawable.clear_weather);
                            }else if(weather_type.equals("Thunderstorm")){
                                Log.e("hi","5");
                                bgView.setBackgroundResource(R.drawable.thunder_weather);
                            }else{
                                Log.e("hi","6");
                                bgView.setBackgroundResource(R.drawable.sunny_weather);
                            }

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





    //获取json数据
    private void parseJSON() {
        //String url = "http://ec2-52-90-160-167.compute-1.amazonaws.com:4000/search?order-by=newest&show-fields=starRating,headline,thumbnail,short-url&api-key=c05384c5-6def-48bd-8d98-ad6b7c9e9caa";
        String url = "http://10.0.2.2:4000/search?order-by=newest&show-fields=starRating,headline,thumbnail,short-url&api-key=c05384c5-6def-48bd-8d98-ad6b7c9e9caa";
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
                            spinner_layout.setVisibility(view.GONE);

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


    /**
     * 解决detail article收藏不能和外面同步的问题
     * dialog可以同步更新是因为用了新的数据
     * 而这里你只是back to parent, 并不更新，所以要通知他
     */

    @Override
    public void onResume() {
        recyclerView.setAdapter(homeAdapter);
        super.onResume();
    }





}
