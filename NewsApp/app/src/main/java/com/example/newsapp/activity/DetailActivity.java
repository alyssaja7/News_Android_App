package com.example.newsapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsapp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;


import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DetailActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private Context context;

    boolean bookmark_filled = false;
//    Boolean bookmark_filled;

    String title, date, description, imgUrl, section, webUrl;
    String newDate;
    private Toolbar toolbar;
    private TextView topTitle_detail, title_detail, section_detail,date_detail, content_detail,expand_link_detail;
    private ImageView image_detail,backButton_detail,bookmark_detail_title,twitter_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_article);

        //get passed ID
        Intent intent = getIntent();
        String id = intent.getStringExtra("ID"); //if it's a string you stored.
//        bookmark_filled = intent.getBooleanExtra("bookmark_filled", false);


        requestQueue = Volley.newRequestQueue(this);   // Instantiate the RequestQueue.
        context = this;
        initView();
        parseJSON(id);  //get data and bind data to view

    }


    /**
     * init View
     */
    public void initView() {

        initToolbar();

        //find textView
        topTitle_detail = findViewById(R.id.topTitle_detail);
        title_detail = findViewById(R.id.title_detail);
        section_detail = findViewById(R.id.section_detail);
        date_detail = findViewById(R.id.date_detail);
        content_detail =findViewById(R.id.content_detail);

        //find imageView
        image_detail = findViewById(R.id.img_detail);
        //backButton_detail = findViewById(R.id.backButton_detail);
        bookmark_detail_title = findViewById(R.id.bookmark_detail_title);
//        if(bookmark_filled){
//            bookmark_detail_title.setImageResource(R.drawable.filled_bookmark);
//        }else{
//            bookmark_detail_title.setImageResource(R.drawable.bookmark_in_card);
//        }


        twitter_detail = findViewById(R.id.twitter_detail);
        expand_link_detail = findViewById(R.id.expand_link_detail);

    }

    private void initToolbar() {

        toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        //set back navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24px);
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






    /**
     * 获取JSON数据
     * @param id
     */
    private void parseJSON(String id) {
        //http://localhost:3000/article?id=sport/2020/apr/03/englands-cricketers-to-hand-back-500000-and-donate-to-charities?api-key=c05384c5-6def-48bd-8d98-ad6b7c9e9caa&show-blocks=all
        String url = "http://10.0.2.2:3000/article?id=" + id + "?api-key=c05384c5-6def-48bd-8d98-ad6b7c9e9caa&show-blocks=all";
        //String url = "http://10.0.2.2:3000/article?id=" + id;
        Log.e("url",url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e("response", response.toString());
                    JSONObject article = response.getJSONObject("article");

                    //get data
                     title = article.getString("Title");
                     date = article.getString("Date");
                     imgUrl = article.getString("Image");
                     description = article.getString("Description");
                     section = article.getString("Section");
                     webUrl = article.getString("URL");

                     //change date format to dd MMMM yyyy by ofPattern funtion
                    ZoneId id = ZoneId.of("America/Los_Angeles");  //Create timezone
                    Instant timestamp = Instant.parse(date);
                    ZonedDateTime reformat_date= timestamp.atZone(id);
                    newDate = DateTimeFormatter.ofPattern("dd MMM yyyy").format(reformat_date);

                     //bind data to view
                     topTitle_detail.setText(title);
                     title_detail.setText(title);
                     date_detail.setText(newDate);
                     section_detail.setText(section);
                     content_detail.setText(description);
                     Picasso.with(context).load(imgUrl).fit().centerInside().into(image_detail);        //给这些imgview,textview设置内容或文本


                    /**
                     * Set "View Full Article functionality"
                     * add 下划线
                     */
                    expand_link_detail.setText("View Full Article");
                    expand_link_detail.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //给view full content set 下划线

                    expand_link_detail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            String url = webUrl;
                            intent.setData(Uri.parse(url));
                            context.startActivity(intent);

                        }
                    });



                    /**
                     *  set twitter functionality
                     */

                    twitter_detail.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            String url = "https://twitter.com/intent/tweet?text=Check%20out%20this%20Link:&url=" + webUrl + "&hashtags=CSCI571NewsSearch";
                            intent.setData(Uri.parse(url));
                            context.startActivity(intent);
                        }
                    });


                    /**
                     * set bookmark click event
                     */

                    bookmark_detail_title.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(context, "Long click: " + String.valueOf(viewHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
//                            Toast.makeText(context,  "\"" + title + "\"" +  " was added to bookmarks",Toast.LENGTH_LONG).show();
//                            bookmark_detail_title.setImageResource(R.drawable.filled_bookmark);


                            bookmark_filled = !bookmark_filled;
                            //add to bookmark when clicking

                            if(bookmark_filled){
                                Toast.makeText(context,  "\"" + title + "\"" +  " was added to bookmarks",Toast.LENGTH_SHORT).show();
                                bookmark_detail_title.setImageResource(R.drawable.filled_bookmark);
                            }else{
                                Toast.makeText(context,  "\"" + title + "\"" +  " was removed from favorites",Toast.LENGTH_SHORT).show();
                                bookmark_detail_title.setImageResource(R.drawable.bookmark_in_card);
                            }


                            /**
                             * continue to set the storage to bookmark fragment
                             * and sync the action with bookmark in news card view
                             * may need id in the future
                             */


                        }
                    });




                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }


}
