package com.example.newsapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.github.mikephil.charting.components.IMarker;
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
    //对于adapter，两个重要的对象：一个context,一个data
    private Context context;
    private ArrayList<HomeItem> homeList;
    boolean bookmark_filled = false;
    //ImageView cardBookMark;

    Dialog newsDialog;
    public HomeAdapter(Context context, ArrayList<HomeItem> homeList){
        this.context = context;
        this.homeList = homeList;
    }





    /**
     * 下面是三个adapter class 要求的三个方法
     *
     * onCreateViewHolder: 负责承载每个子项的布局
     * onBindViewHolder()：负责将每个子项holder绑定数据（定义一个抽象方法，完成ViewHolder与Data数据集的绑定）
     *
     **/
    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e("hi","1");
        //Log.d("aaa","onCreateViewHolder————"+viewType);
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.home_item, parent, false);
        final HomeViewHolder viewHolder = new HomeViewHolder(view);


        /**
         * single click to open detail article
         */

        viewHolder.item_news.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, DetailActivity.class);
                String id = homeList.get(viewHolder.getAdapterPosition()).getID();
                myIntent.putExtra("ID", id); //Optional parameters
//                myIntent.putExtra("filledState", bookmark_filled); //Optional parameters

                context.startActivity(myIntent);

                //Toast.makeText(context, "Single click: " + String.valueOf(viewHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }
        });


        /**
         * long click to open news dialog
         */
        viewHolder.item_news.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                initDialog(viewHolder);
                //Toast.makeText(context, "Long click: " + String.valueOf(viewHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
                newsDialog.show();
                return true;
            }
        });

        return viewHolder;
    }


    /**
     * Init news dialog
     */
    private void initDialog(final HomeViewHolder viewHolder) {
        Log.e("hi","2");
        newsDialog = new Dialog(context);
        //get view
        newsDialog.setContentView(R.layout.dialog_news);
        TextView dialog_home_title = newsDialog.findViewById(R.id.dialog_home_title);
        ImageView dialog_home_img = newsDialog.findViewById(R.id.dialog_home_img);
        ImageView dialog_twitter = newsDialog.findViewById(R.id.dialog_twitter);
        final ImageView dialog_bookmark = newsDialog.findViewById(R.id.dialog_bookmark);


        dialog_home_title.setText(homeList.get(viewHolder.getAdapterPosition()).getTitle());
        String imgUrl = homeList.get(viewHolder.getAdapterPosition()).getImgUrl();
        //Log.e("imgUrl in initDialog", imgUrl);
        Picasso.with(context).load(imgUrl).fit().centerInside().into(dialog_home_img);        //给这些imgview,textview设置内容或文本


        /**
         *  set twitter click event
         */
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







        /**
         * set bookmark click event
         */
        final String title = homeList.get(viewHolder.getAdapterPosition()).getTitle();
        dialog_bookmark.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                bookmark_filled = !bookmark_filled;
                //add to bookmark when clicking

                if(bookmark_filled){
                    Toast.makeText(context,  "\"" + title + "\"" +  " was added to bookmarks",Toast.LENGTH_SHORT).show();
                    dialog_bookmark.setImageResource(R.drawable.filled_bookmark);
                   // cardBookMark.setImageResource(R.drawable.filled_bookmark);

                }else{
                    Toast.makeText(context,  "\"" + title + "\"" +  " was removed from favorites",Toast.LENGTH_SHORT).show();
                    dialog_bookmark.setImageResource(R.drawable.bookmark_in_card);
                   // cardBookMark.setImageResource(R.drawable.bookmark_in_card);
                }
                Log.e("filled in dialog inside", String.valueOf(bookmark_filled));





                /**
                 * continue to set the storage to bookmark fragment
                 * and sync the action with bookmark in news card view
                 * may need id in the future
                 */
            }
        });



        Log.e("filld in dialog outside", String.valueOf(bookmark_filled));
        if(bookmark_filled){
            dialog_bookmark.setImageResource(R.drawable.filled_bookmark);
        }else{
            dialog_bookmark.setImageResource(R.drawable.bookmark_in_card);
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
    public void onBindViewHolder(@NonNull final HomeViewHolder holder, int position) {
        Log.e("hi","3");
       // Log.d("aaa","Position————"+position);
        HomeItem cur_item = homeList.get(position);

        //String id = cur_item.getId();
        String imgUrl = cur_item.getImgUrl();     //调用HomeItem.java里的getter method
        final String title = cur_item.getTitle();
        String time = cur_item.getTime();
        String section = cur_item.getSection();

        //Log.d("aaa","Section————"+section);
        //Log.d("aaa","time————"+time);
        //Log.d("aaa","img————"+imgUrl);
        //Log.d("aaa","title————"+title);
        //Log.e("time", time);
        /**transfer time to required format: " xxxm/s/h ago"**/
        String diffTime = diffTime(time);
        //Log.e("diffTime", diffTime);

        //bind data to related view
        Picasso.with(context).load(imgUrl).fit().centerInside().into(holder.imageView);        //给这些imgview,textview设置内容或文本
        holder.textViewTitle.setText(title);
        holder.textViewTime.setText(diffTime);
        holder.textViewSection.setText(section);

        //cardBookMark = holder.imageView_Bookmarks;



        //holder.imageView_Bookmarks.setOnClickListener();
        holder.imageView_Bookmarks.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //add to bookmarks
                //Toast.makeText(context,  "\"" + title + "\"" +  " was added to bookmarks",Toast.LENGTH_SHORT).show();
                //holder.imageView_Bookmarks.setImageResource(R.drawable.filled_bookmark);


                bookmark_filled = !bookmark_filled;
                //add to bookmark when clicking

                if(bookmark_filled){
                    Toast.makeText(context,  "\"" + title + "\"" +  " was added to bookmarks",Toast.LENGTH_SHORT).show();
                    holder.imageView_Bookmarks.setImageResource(R.drawable.filled_bookmark);
                }else{
                    Toast.makeText(context,  "\"" + title + "\"" +  " was removed from favorites",Toast.LENGTH_SHORT).show();
                    holder.imageView_Bookmarks.setImageResource(R.drawable.bookmark_in_card);
                }
                Log.e("filled changed inside", String.valueOf(bookmark_filled));



                /**
                 * continue to set the storage to bookmark fragment
                 * and sync the action with bookmark in news card view
                 * may need id in the future
                 */
            }
        });

        Log.e("filled changed outside", String.valueOf(bookmark_filled));



        Log.e("filled in bind view", String.valueOf(bookmark_filled));
        if(bookmark_filled){
            holder.imageView_Bookmarks.setImageResource(R.drawable.filled_bookmark);
        }else{
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
        //public TextView id;
        public TextView textViewTitle;
        public TextView textViewSection;
        public TextView textViewTime;


        //完成初始化，通过findViewById
        public HomeViewHolder(@NonNull View itemView) {

            super(itemView);
            item_news = itemView.findViewById(R.id.home_news_item);


            imageView = itemView.findViewById(R.id.img);
            imageView_Bookmarks = itemView.findViewById(R.id.bookmarks);
            //id = itemView.findViewById(R.id.bookmarks)
            textViewTitle = itemView.findViewById(R.id.title);
            textViewSection = itemView.findViewById(R.id.section);
            textViewTime = itemView.findViewById(R.id.time);

            Log.e("hi","4");
        }
    }







}
