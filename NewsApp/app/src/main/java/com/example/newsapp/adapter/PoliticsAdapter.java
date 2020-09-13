package com.example.newsapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.newsapp.item.PoliticsItem;
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

public class PoliticsAdapter extends RecyclerView.Adapter<PoliticsAdapter.PoliticsViewHolder> {
    //对于adapter，两个重要的对象：一个context,一个data
    private Context context;
    private ArrayList<PoliticsItem> politicsList;

    boolean bookmark_filled = false;

    Dialog newsDialog;
    public PoliticsAdapter(Context context,ArrayList<PoliticsItem> politicsList) {
        this.context = context;
        this.politicsList = politicsList;
    }

    @NonNull
    @Override
    public PoliticsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.politics_item,parent, false);
        final PoliticsViewHolder viewHolder = new PoliticsViewHolder(view);


        /**
         * single click to open detail article
         */

        viewHolder.item_news.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, DetailActivity.class);
                String id = politicsList.get(viewHolder.getAdapterPosition()).getId();
                myIntent.putExtra("ID", id); //Optional parameters
                context.startActivity(myIntent);

                Toast.makeText(context, "Single click: " + String.valueOf(viewHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
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
    private void initDialog(final PoliticsViewHolder viewHolder) {
        newsDialog = new Dialog(context);
        newsDialog.setContentView(R.layout.dialog_news);
        TextView dialog_home_title = newsDialog.findViewById(R.id.dialog_home_title);
        ImageView dialog_home_img = newsDialog.findViewById(R.id.dialog_home_img);
        ImageView dialog_twitter = newsDialog.findViewById(R.id.dialog_twitter);
        final ImageView dialog_bookmark = newsDialog.findViewById(R.id.dialog_bookmark);


        dialog_home_title.setText(politicsList.get(viewHolder.getAdapterPosition()).getTitle());
        String imgUrl = politicsList.get(viewHolder.getAdapterPosition()).getImgUrl();
        //Log.e("imgUrl in initDialog", imgUrl);
        Picasso.with(context).load(imgUrl).fit().centerInside().into(dialog_home_img);        //给这些imgview,textview设置内容或文本


        /**
         *  set twitter click event
         */
        final String webUrl = politicsList.get(viewHolder.getAdapterPosition()).getWebUrl();
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
        final String title = politicsList.get(viewHolder.getAdapterPosition()).getTitle();
        dialog_bookmark.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Long click: " + String.valueOf(viewHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
//                Toast.makeText(context,  "\"" + title + "\"" +  " was added to bookmarks",Toast.LENGTH_LONG).show();
//                dialog_bookmark.setImageResource(R.drawable.filled_bookmark);
                // dialog_bookmark.setBackgroundColor(Color.rgb(255, 136, 0));

                bookmark_filled = !bookmark_filled;
                //add to bookmark when clicking

                if(bookmark_filled){
                    Toast.makeText(context,  "\"" + title + "\"" +  " was added to bookmarks",Toast.LENGTH_SHORT).show();
                    dialog_bookmark.setImageResource(R.drawable.filled_bookmark);
                }else{
                    Toast.makeText(context,  "\"" + title + "\"" +  " was removed from favorites",Toast.LENGTH_SHORT).show();
                    dialog_bookmark.setImageResource(R.drawable.bookmark_in_card);
                }



                /**
                 * continue to set the storage to bookmark fragment
                 * and sync the action with bookmark in news card view
                 * may need id in the future
                 */
            }
        });

        if(bookmark_filled){
            dialog_bookmark.setImageResource(R.drawable.filled_bookmark);
        }else{
            dialog_bookmark.setImageResource(R.drawable.bookmark_in_card);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final PoliticsViewHolder holder, int position) {
        // Log.d("aaa","Position————"+position);
        PoliticsItem cur_item = politicsList.get(position);

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

        Picasso.with(context).load(imgUrl).fit().centerInside().into(holder.imageView);        //给这些imgview,textview设置内容或文本
        holder.textViewTitle.setText(title);
        holder.textViewTime.setText(diffTime);
        holder.textViewSection.setText(section);

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



                /**
                 * continue to set the storage to bookmark fragment
                 * and sync the action with bookmark in news card view
                 * may need id in the future
                 */
            }
        });

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

        Log.e("Politics localT", localTime.toString());
        Log.e("Politics publishT", publishTime.toString());
        Log.e("Politics diff", diff);
        return diff;
    }




    @Override
    public int getItemCount() {
        return politicsList.size();
    }

    public class PoliticsViewHolder extends RecyclerView.ViewHolder {
        private CardView item_news;
        public ImageView imageView;
        public ImageView imageView_Bookmarks;
        //public TextView id;
        public TextView textViewTitle;
        public TextView textViewSection;
        public TextView textViewTime;

        public PoliticsViewHolder(@NonNull View itemView) {
            super(itemView);
            item_news = itemView.findViewById(R.id.politics_news_item);
            imageView_Bookmarks = itemView.findViewById(R.id.bookmarks);

            imageView = itemView.findViewById(R.id.img);
            //id = itemView.findViewById(R.id.bookmarks)
            textViewTitle = itemView.findViewById(R.id.title);
            textViewSection = itemView.findViewById(R.id.section);
            textViewTime = itemView.findViewById(R.id.time);
        }
    }
}
