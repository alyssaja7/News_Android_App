package com.example.newsapp.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsapp.R;
import com.example.newsapp.adapter.BookmarksAdapter;
import com.example.newsapp.item.BookmarkItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BookmarksFragment extends Fragment {

    View view;
    private RecyclerView recyclerView;
    private TextView emptyView;

    private ArrayList<BookmarkItem> bookmarksList;

    //String title, date, imgUrl, section, webUrl;

    SharedPreferences sharedPreferences;
    private BookmarksAdapter bookmarksAdapter;
    private RequestQueue requestQueue;              //need for volley


    public static final String SHARED_PREFS = "sharedPrefs";



    //String id;
   // Boolean bookmark_filled;



    public BookmarksFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("Run","onCreateView 2");

        view = inflater.inflate(R.layout.fragment_bookmarks,container,false);    //加载一个布局文件
        recyclerView = view.findViewById(R.id.recycler_view_bookmarksFg);   //从布局文件中查找一个控件
        emptyView = view.findViewById(R.id.empty_view);


        Log.e("check recy", recyclerView.toString());





        Log.e("check sf---", sharedPreferences.toString());

        if (sharedPreferences.getAll().size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        } else{
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);

        }





        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));    //set items layed out in linear way
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));   //add a horizontal divider line between news cards

        parseJSON();


        Log.e("hi recy", recyclerView.toString());
        Log.e("editor hi check", String.valueOf(sharedPreferences.getAll().size()));
        return view;

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e("Run","onCreate 1");

        super.onCreate(savedInstanceState);
        bookmarksList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getContext());   // Instantiate the RequestQueue.
//        parseJSON();    这个为什么两个都会新建view  我 fuck!!!!!!!!!!

        sharedPreferences = getContext().getApplicationContext().getSharedPreferences(SHARED_PREFS, 0);

    }




    private void parseJSON() {
        Log.e("Run","parseJSON 3");

        bookmarksList = new ArrayList<>();
        final Map<String, ?> allEntries = sharedPreferences.getAll();


        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {

            final String id = entry.getKey();

            Log.e("--id in Fragment-----", id);

            //String url = "http://ec2-52-90-160-167.compute-1.amazonaws.com:4000/article?id=" + id + "?api-key=c05384c5-6def-48bd-8d98-ad6b7c9e9caa&show-blocks=all";
            String url = "http://10.0.2.2:4000/article?id=" + id + "?api-key=c05384c5-6def-48bd-8d98-ad6b7c9e9caa&show-blocks=all";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //Log.e("Response", response.toString());   //log里面输出response check

                                JSONObject article = response.getJSONObject("article");

                                //get data
                                 String date = article.getString("Date");
                                 Log.e("date in -----", date);
                                 String imgUrl = article.getString("Image");
                                 String title = article.getString("Title");
                                 String section = article.getString("Section");
                                 String webUrl = article.getString("URL");

                                 bookmarksList.add(new BookmarkItem(imgUrl, title, section, date, webUrl, id));


                                bookmarksAdapter = new BookmarksAdapter(getContext(), bookmarksList, recyclerView, emptyView);        //adapter: UI和数据的接口
                                bookmarksAdapter.setmBookmarksAdapter(bookmarksAdapter);



                                recyclerView.setAdapter(bookmarksAdapter);                       //set adapter on recycler view
//                              spinner_layout.setVisibility(view.GONE);
                                //bookmarksAdapter.notifyDataSetChanged();



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //Log.e("bookmarkList------", String.valueOf(bookmarksList.size()));

                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });

            requestQueue.add(request);
        }
    }


//    @Override
//    public void onResume() {
//
//        //recyclerView.setAdapter(bookmarksAdapter);
//        super.onResume();
//        //bookmarksAdapter.notifyDataSetChanged();
//
//        parseJSON();
//    }
//





}
