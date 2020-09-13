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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsapp.R;
import com.example.newsapp.activity.DetailActivity;
import com.example.newsapp.item.HomeItem;
import com.squareup.picasso.Picasso;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
    private Context context;
    private ArrayList<HomeItem> homeList;

    String id;
    String gottenID;
    ImageView dialog_bookmark;
    ImageView news_bookmark;

    Dialog newsDialog;
    public static final String SHARED_PREFS = "sharedPrefs";

    public HomeAdapter(Context context, ArrayList<HomeItem> homeList){
        this.context = context;
        this.homeList = homeList;
    }

    /**
     *
     * 下面是三个adapter class 要求的三个方法
     *
     * onCreateViewHolder: 负责承载每个子项的布局
     * onBindViewHolder()：负责将每个子项holder绑定数据（定义一个抽象方法，完成ViewHolder与Data数据集的绑定）
     *
     **/
    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.home_item, parent, false);
        final HomeViewHolder viewHolder = new HomeViewHolder(view);

        //single click to open detail article
        viewHolder.item_news.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, DetailActivity.class);
                id = homeList.get(viewHolder.getAdapterPosition()).getID();
                myIntent.putExtra("ID", id);        //Optional parameters
                context.startActivity(myIntent);
            }
        });

         //long click to open news dialog
        viewHolder.item_news.setOnLongClickListener(new View.OnLongClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onLongClick(View v) {
                initDialog(viewHolder);
                newsDialog.show();
                return true;
            }
        });
        return viewHolder;
    }



    /**
     * Init news dialog
     * news dialog will be opened when long click news item
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initDialog(final HomeViewHolder viewHolder) {
        news_bookmark = viewHolder.imageView_Bookmarks;
        newsDialog = new Dialog(context);

        newsDialog.setContentView(R.layout.dialog_news);
        TextView dialog_home_title = newsDialog.findViewById(R.id.dialog_home_title);
        ImageView dialog_home_img = newsDialog.findViewById(R.id.dialog_home_img);
        ImageView dialog_twitter = newsDialog.findViewById(R.id.dialog_twitter);
        dialog_bookmark = newsDialog.findViewById(R.id.dialog_bookmark);

        id = homeList.get(viewHolder.getAdapterPosition()).getID();

        final String title = homeList.get(viewHolder.getAdapterPosition()).getTitle();
        dialog_home_title.setText(title);

        String imgUrl = homeList.get(viewHolder.getAdapterPosition()).getImgUrl();
        Picasso.with(context).load(imgUrl).fit().centerInside().into(dialog_home_img);



        //set twitter click event
        final String webUrl = homeList.get(viewHolder.getAdapterPosition()).getWebUrl();
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




        //set bookmark click event
        dialog_bookmark.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(SHARED_PREFS, 0);
                gottenID = sharedPreferences.getString(id,null);

                if(gottenID == null){
                    Toast.makeText(context,  "\"" + title + "\"" +  " was added to bookmarks",Toast.LENGTH_SHORT).show();
                    dialog_bookmark.setImageResource(R.drawable.filled_bookmark);

                    //set news card bookmark: 这里做的是根据dialog bookmark来进行news_bookmark的状态同步
                    news_bookmark.setImageResource(R.drawable.filled_bookmark);

                }else if(gottenID != null){

                    Toast.makeText(context,  "\"" + title + "\"" +  " was removed from bookmarks",Toast.LENGTH_SHORT).show();
                    dialog_bookmark.setImageResource(R.drawable.bookmark_in_card);

                    //set news card bookmark
                    news_bookmark.setImageResource(R.drawable.bookmark_in_card);
                }

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

    }




    /**
     * save data by sharedPreferences
     */

    private void saveData(String id) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(SHARED_PREFS, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        gottenID = sharedPreferences.getString(id,null);


        if(gottenID == null){
            editor.putString(id, id);
            editor.apply();

            //check if get successfully
            //String getID = sharedPreferences.getString(id,null);
            //Toast.makeText(context, getID+ "----------putted-----------", Toast.LENGTH_SHORT).show();

        }else if(gottenID != null){
            editor.remove(id);
            editor.apply();


            //check if get successfully
            //String getID = sharedPreferences.getString(id,null);
            //Toast.makeText(context, getID + "----------removed-----------", Toast.LENGTH_SHORT).show();
        }


    }



    /**
     * onBindViewHolder:
     * Called by RecyclerView to display the data at the specified position.
     * This method should update the contents of the itemView to reflect the item at the given position.
     *
     * use potion to get current item of our homeList
     *
     */

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final HomeViewHolder holder, final int position) {

        HomeItem cur_item = homeList.get(position);

        final String imgUrl = cur_item.getImgUrl();     //调用HomeItem.java里的getter method
        final String title = cur_item.getTitle();
        final String time = cur_item.getTime();
        Log.e("time------", time);
        final String section = cur_item.getSection();
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


                if(gottenID == null){
                    Toast.makeText(context,  "\"" + title + "\"" +  " was added to bookmarks",Toast.LENGTH_SHORT).show();
                    holder.imageView_Bookmarks.setImageResource(R.drawable.filled_bookmark);

                }else if(gottenID != null){
                    Toast.makeText(context,  "\"" + title + "\"" +  " was removed from bookmarks",Toast.LENGTH_SHORT).show();
                    holder.imageView_Bookmarks.setImageResource(R.drawable.bookmark_in_card);

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

        Log.e("localTime",localTime.toString());

        //publish time
        Instant timestamp = Instant.parse(time);
        ZonedDateTime publishTime = timestamp.atZone(id);
        Log.e("publishTime", publishTime.toString());

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



//        Log.e("localT", localTime.toString());
//        Log.e("publishT", publishTime.toString());
//        Log.e("diff", diff);
        return diff;
    }


    @Override
    public int getItemCount() {
        return homeList.size();
    }


    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     * recycleView 需要 a viewHolder class
     * get view reference from home_item.xml
     *
     */
    public class HomeViewHolder extends RecyclerView.ViewHolder{

        private CardView item_news;
        public ImageView imageView;
        public ImageView imageView_Bookmarks;
        public TextView textViewTitle;
        public TextView textViewSection;
        public TextView textViewTime;


        //完成初始化，通过findViewById
        public HomeViewHolder(@NonNull View itemView) {

            super(itemView);
            item_news = itemView.findViewById(R.id.home_news_item);

            imageView = itemView.findViewById(R.id.img);
            imageView_Bookmarks = itemView.findViewById(R.id.bookmarks);

            textViewTitle = itemView.findViewById(R.id.title);
            textViewSection = itemView.findViewById(R.id.section);
            textViewTime = itemView.findViewById(R.id.time);

        }
    }

}