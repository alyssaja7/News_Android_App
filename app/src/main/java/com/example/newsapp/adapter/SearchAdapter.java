package com.example.newsapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsapp.R;
import com.example.newsapp.activity.DetailActivity;
import com.example.newsapp.activity.SearchActivity;
import com.example.newsapp.item.HomeItem;
import com.example.newsapp.item.SearchItem;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {



    ImageView dialog_bookmark;
    ImageView news_bookmark;
    String id;
    String gottenID;

    private Context context;
    private ArrayList<SearchItem> searchList;


    public static final String SHARED_PREFS = "sharedPrefs";
    Dialog newsDialog;


    public SearchAdapter(Context context, ArrayList<SearchItem> searchList) {
        this.context = context;
        this.searchList = searchList;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.search_item, parent, false);
        final SearchViewHolder viewHolder = new SearchViewHolder(view);


        /**
         * single click to open detail article
         */
        viewHolder.item_news.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, DetailActivity.class);
                id = searchList.get(viewHolder.getAdapterPosition()).getID();
                myIntent.putExtra("ID", id); //Optional parameters
                context.startActivity(myIntent);
            }
        });


        /**
         * long click to open news dialog
         */
        viewHolder.item_news.setOnLongClickListener(new View.OnLongClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onLongClick(View v) {
                initDialog(viewHolder);
//                newsDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

                newsDialog.show();              //这里有问题！！！！！！！！！！！！1
                return true;
            }
        });


        return viewHolder;
    }


    /**
     * Init news dialog
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initDialog(final SearchViewHolder viewHolder) {

        news_bookmark = viewHolder.imageView_Bookmarks;
        newsDialog = new Dialog(context);


        //get view
        newsDialog.setContentView(R.layout.dialog_news);
        TextView dialog_home_title = newsDialog.findViewById(R.id.dialog_home_title);
        ImageView dialog_home_img = newsDialog.findViewById(R.id.dialog_home_img);
        ImageView dialog_twitter = newsDialog.findViewById(R.id.dialog_twitter);
        dialog_bookmark = newsDialog.findViewById(R.id.dialog_bookmark);


        //get id
        id = searchList.get(viewHolder.getAdapterPosition()).getID();

        //set text or img
        final String title = searchList.get(viewHolder.getAdapterPosition()).getTitle();
        dialog_home_title.setText(title);

        String imgUrl = searchList.get(viewHolder.getAdapterPosition()).getImgUrl();
        Picasso.with(context).load(imgUrl).fit().centerInside().into(dialog_home_img);        //给这些imgview,textview设置内容或文本

        String time = searchList.get(viewHolder.getAdapterPosition()).getTime();
//        String diffTime = diffTime(time);



        /**
         *  set twitter click event
         */
        final String webUrl = searchList.get(viewHolder.getAdapterPosition()).getWebUrl();
        dialog_twitter.setOnClickListener(new View.OnClickListener(){
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
        //title = homeList.get(viewHolder.getAdapterPosition()).getTitle();
        dialog_bookmark.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                bookmark_filled = !bookmark_filled;

                SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(SHARED_PREFS, 0);
                gottenID = sharedPreferences.getString(id,null);
//
//                if(gottenID == null && bookmark_filled){
                if(gottenID == null){
                    Toast.makeText(context,  "\"" + title + "\"" +  " was added to bookmarks",Toast.LENGTH_SHORT).show();
                    dialog_bookmark.setImageResource(R.drawable.filled_bookmark);


                    //set news card bookmark
                    news_bookmark.setImageResource(R.drawable.filled_bookmark);


//                }else if(gottenID != null && bookmark_filled == false){
                }else if(gottenID != null){

                    Toast.makeText(context,  "\"" + title + "\"" +  " was removed from bookmarks",Toast.LENGTH_SHORT).show();
                    dialog_bookmark.setImageResource(R.drawable.bookmark_in_card);

                    //set news card bookmark
                    news_bookmark.setImageResource(R.drawable.bookmark_in_card);
                }



                /**
                 * continue to set the storage to bookmark fragment
                 * and sync the action with bookmark in news card view
                 * may need id in the future
                 */


                saveData(id);

            }
        });

        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(SHARED_PREFS, 0);
        gottenID = sharedPreferences.getString(id,null);

        if(gottenID != null){
            dialog_bookmark.setImageResource(R.drawable.filled_bookmark);
        }else{
            dialog_bookmark.setImageResource(R.drawable.bookmark_in_card);
        }
        Log.e("--","therre");



    }

    /**
     * save data by sharedPreferences
     */

    private void saveData(String id) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(SHARED_PREFS, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        gottenID = sharedPreferences.getString(id,null);

//        if(bookmark_filled){
        if(gottenID == null){
            editor.putString(id, id);
            editor.apply();


            //check if get successfully
            String getID = sharedPreferences.getString(id,null);
            //Toast.makeText(context, getID+ "----------putted-----------", Toast.LENGTH_SHORT).show();

        }else if(gottenID != null){
            editor.remove(id);
            editor.apply();


            //check if get successfully
            String getID = sharedPreferences.getString(id,null);
            //Toast.makeText(context, getID + "----------removed-----------", Toast.LENGTH_SHORT).show();
        }

    }





    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final SearchViewHolder holder, final int position) {
        SearchItem cur_item = searchList.get(position);

        //String id = cur_item.getId();
        final String imgUrl = cur_item.getImgUrl();     //调用HomeItem.java里的getter method
        final String title = cur_item.getTitle();
        final String time = cur_item.getTime();
        final String section = cur_item.getSection();
        final String webUrl = cur_item.getWebUrl();
        final String id = cur_item.getID();


        /**transfer time to required format: " xxxm/s/h ago"**/
        String diffTime = diffTime(time);


        //bind data to related view
        Picasso.with(context).load(imgUrl).fit().centerInside().into(holder.imageView);        //给这些imgview,textview设置内容或文本
        holder.textViewTitle.setText(title);
        holder.textViewTime.setText(diffTime);
        holder.textViewSection.setText(section);


        /**
         *
         *  news card bookmark click event
         *
         */
        holder.imageView_Bookmarks.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(SHARED_PREFS, 0);
                gottenID = sharedPreferences.getString(id,null);


                //add to bookmark when clicking


                if(gottenID == null){
                    Toast.makeText(context,  "\"" + title + "\"" +  " was added to bookmarks",Toast.LENGTH_SHORT).show();

                    holder.imageView_Bookmarks.setImageResource(R.drawable.filled_bookmark);

                }else if(gottenID != null){
                    Toast.makeText(context,  "\"" + title + "\"" +  " was removed from bookmarks",Toast.LENGTH_SHORT).show();

                    holder.imageView_Bookmarks.setImageResource(R.drawable.bookmark_in_card);

                }
//                Log.e("filled changed inside", String.valueOf(bookmark_filled));



                /**
                 * continue to set the storage to bookmark fragment
                 * and sync the action with bookmark in news card view
                 * may need id in the future
                 */


                //HomeItem cur_item = homeList.get(position);
//                final String id = cur_item.getID();

                saveData(id);

            }
        });

//        Log.e("filled changed outside", String.valueOf(bookmark_filled));
//
//        Log.e("filled in bind view", String.valueOf(bookmark_filled));

        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(SHARED_PREFS, 0);
        gottenID = sharedPreferences.getString(id,null);



        if(gottenID != null){
            Log.e("gottenID in news Card", gottenID);
            holder.imageView_Bookmarks.setImageResource(R.drawable.filled_bookmark);
        }else{
            Log.e("gottenID in news Card", "empty oooooo------");
            holder.imageView_Bookmarks.setImageResource(R.drawable.bookmark_in_card);
        }
    }

    /**
     *
     * Get the required format time to display in home cardview
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String diffTime(String time) {
        //local time
        LocalDateTime today = LocalDateTime.now();  //Create date
        ZoneId id = ZoneId.of("America/Los_Angeles");  //Create timezone
        ZonedDateTime localTime = ZonedDateTime.of(today, id);  //Add timezone to date

        //publish time
        Instant timestamp = Instant.parse(time);
        ZonedDateTime publishTime = timestamp.atZone(id);

        //get hours, min, sec
        int localSec = localTime.getSecond();
        int pSec = publishTime.getSecond();
        int localMin = localTime.getMinute();
        int pMin = publishTime.getMinute();
        int localHour = localTime.getHour();
        int pHour = publishTime.getHour();



        String diff = "";    //initialize

        int hDiff = localHour - pHour;
        int mDiff = localMin - pMin;
        int sDiff = localSec - pSec;


        //different situation to calculate the time diff
        if(hDiff < 0){
            diff = 24 - pHour + localHour + "h ago";
        }
        if(hDiff >= 1){
            diff = hDiff + "h ago";
        }
        if(hDiff == 1 && mDiff > 0){
            diff =  60 + mDiff + "m ago";
        }
        if(localHour == 0 && pHour != 0){
            hDiff = 24 - pHour;
            if(mDiff < 0){
                diff = 60 - pMin + localMin + "m ago";
            }
        }
        if(hDiff >= 1 && mDiff >= 0){
            diff = hDiff + "h ago";
        }
        if(hDiff > 1 && mDiff < 0){
            diff = hDiff - 1 + "h ago";
        }
        if(hDiff == 0 && mDiff >= 0){
            diff = mDiff + "m ago";
        }
        if(hDiff == 0 && mDiff == 0){
            diff = sDiff + "s ago";
        }

        Log.e("Search localT", localTime.toString());
        Log.e("Search publishT", publishTime.toString());
        Log.e("Search diff", diff);
        return diff;
    }



    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {


        private CardView item_news;
        public ImageView imageView;
        public ImageView imageView_Bookmarks;
        //public TextView id;
        public TextView textViewTitle;
        public TextView textViewSection;
        public TextView textViewTime;


        //完成初始化，通过findViewById
        public SearchViewHolder(@NonNull View itemView) {

            super(itemView);
            item_news = itemView.findViewById(R.id.search_news_item);

            imageView = itemView.findViewById(R.id.img);
            imageView_Bookmarks = itemView.findViewById(R.id.bookmarks);
            //id = itemView.findViewById(R.id.bookmarks)
            textViewTitle = itemView.findViewById(R.id.title);
            textViewSection = itemView.findViewById(R.id.section);
            textViewTime = itemView.findViewById(R.id.time);

            //Log.e("hi","4");
        }
    }



}
