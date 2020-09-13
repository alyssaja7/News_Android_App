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
import com.example.newsapp.item.BookmarkItem;
import com.squareup.picasso.Picasso;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import androidx.recyclerview.widget.RecyclerView;

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.BookmarksViewHolder> {

    private Context context;
    private ArrayList<BookmarkItem> bookmarksList;
    //String id;


    View view;
    ImageView news_bookmark;
    ImageView dialog_bookmark;
    RecyclerView recyclerView;
    TextView emptyView;


    //String gottenID;


    Dialog newsDialog;
    public static final String SHARED_PREFS = "sharedPrefs";

    public BookmarksAdapter(Context context, ArrayList<BookmarkItem> bookmarksList) {
        this.context = context;
        this.bookmarksList = bookmarksList;

    }


    public BookmarksAdapter(Context context, ArrayList<BookmarkItem> bookmarksList, RecyclerView recyclerView, TextView emptyView) {
        this.context = context;
        this.bookmarksList = bookmarksList;
        this.recyclerView = recyclerView;
        this.emptyView = emptyView;
    }




    @NonNull
    @Override
    public BookmarksAdapter.BookmarksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.bookmarks_item, parent, false);
        final BookmarksViewHolder viewHolder = new BookmarksViewHolder(view);



        /**
         * single click to open detail article
         */
       /* viewHolder.item_news.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, DetailActivity.class);
                id = bookmarksList.get(viewHolder.getAdapterPosition()).getId();
//                Log.e("id test", id);
                myIntent.putExtra("ID", id); //Optional parameters
                context.startActivity(myIntent);
            }
        });*/


        /**
         * long click to open news dialog
         */
/*        viewHolder.item_news.setOnLongClickListener(new View.OnLongClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onLongClick(View v) {
                initDialog(viewHolder);
                newsDialog.show();
                return true;
            }
        });*/




        return viewHolder;
    }


    /**
     * Init news dialog
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initDialog(final BookmarksViewHolder viewHolder) {

        news_bookmark = viewHolder.imageView_Bookmarks;
        newsDialog = new Dialog(context);


        //get view
        newsDialog.setContentView(R.layout.dialog_news);
        TextView dialog_home_title = newsDialog.findViewById(R.id.dialog_home_title);
        ImageView dialog_home_img = newsDialog.findViewById(R.id.dialog_home_img);
        ImageView dialog_twitter = newsDialog.findViewById(R.id.dialog_twitter);
        dialog_bookmark = newsDialog.findViewById(R.id.dialog_bookmark);




        //set text or img
        final String title = bookmarksList.get(viewHolder.getAdapterPosition()).getTitle();

        final String date = bookmarksList.get(viewHolder.getAdapterPosition()).getTime();
        final String section = bookmarksList.get(viewHolder.getAdapterPosition()).getSection();
        final String id = bookmarksList.get(viewHolder.getAdapterPosition()).getID();


        dialog_home_title.setText(title);

        final String imgUrl = bookmarksList.get(viewHolder.getAdapterPosition()).getImgUrl();
        Picasso.with(context).load(imgUrl).fit().centerInside().into(dialog_home_img);        //给这些imgview,textview设置内容或文本

//        String time = bookmarksList.get(viewHolder.getAdapterPosition()).getTime();
//        String diffTime = diffTime(time);

        dialog_bookmark.setImageResource(R.drawable.filled_bookmark);



        /**
         *  set twitter click event
         */
        final String webUrl = bookmarksList.get(viewHolder.getAdapterPosition()).getWebUrl();
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
                SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(SHARED_PREFS, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(id);

                Log.e("editor check", String.valueOf(sharedPreferences.getAll().size()));

                editor.commit();
                bookmarksList.remove(new BookmarkItem(imgUrl,  title, section, date,  webUrl,  id));


                if(bookmarksList.size() == 0) {
                    //recyclerView = view.findViewById(R.id.recycler_view_bookmarksFg);
                    //emptyView = view.findViewById(R.id.empty_view);
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }

                newsDialog.dismiss();    //让他消失（自动的 >:< ）

                Toast.makeText(context,  "\"" + title + "\"" +  " was removed from bookmarks",Toast.LENGTH_SHORT).show();


                //mBookmarksAdapter.notifyDataSetChanged();
                notifyDataSetChanged();
                //notifyItemRemoved(position);


                Log.e("dialog clicked","hi");
            }
        });


    }







    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final BookmarksAdapter.BookmarksViewHolder holder, final int position) {

        BookmarkItem cur_item = bookmarksList.get(position);
        //Log.wtf("position and another position", "" + position + " and " + holder.getAdapterPosition());

        final String myid = cur_item.getID();
        final String imgUrl = cur_item.getImgUrl();     //调用HomeItem.java里的getter method
        final String title = cur_item.getTitle();
        String time = cur_item.getTime();
        final String section = cur_item.getSection();
        //final String date = cur_item.getTime();
        //final String webUrl = cur_item.getWebUrl();

//        String diffTime = diffTime(time);


        //change date format to dd MMMM yyyy by ofPattern funtion
        final ZoneId timeId = ZoneId.of("America/Los_Angeles");  //Create timezone
        Instant timestamp = Instant.parse(time);
        ZonedDateTime reformat_date= timestamp.atZone(timeId);
        String newTime = DateTimeFormatter.ofPattern("dd MMM").format(reformat_date);



        Picasso.with(context).load(imgUrl).fit().centerInside().into(holder.imageView);        //给这些imgview,textview设置内容或文本
        holder.textViewTitle.setText(title);
        holder.textViewTime.setText(newTime);
        holder.textViewSection.setText(section);

        holder.imageView_Bookmarks.setImageResource(R.drawable.filled_bookmark);



        /**
         *
         *  news card bookmark click event
         *  remove the card
         *
         */
        holder.imageView_Bookmarks.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(SHARED_PREFS, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();

//                String getID = sharedPreferences.getString(id,null);
//                Toast.makeText(context, getID+ "gotten by getString ---------", Toast.LENGTH_SHORT).show();


                /**
                 * remove data if u click the bookmark button in bookmark page
                 */

                //Log.e("HHHHHHHHHHHHHHHHHH id----------------------------------", myid);

                editor.remove(myid);

                //Log.e("list-------------------------", bookmarksList.toString());
                //Log.e("editor------", String.valueOf(sharedPreferences.getAll().size()));
                editor.commit();

                bookmarksList.remove(position);

                if(bookmarksList.size() == 0) {
                    //recyclerView = view.findViewById(R.id.recycler_view_bookmarksFg);
                    //emptyView = view.findViewById(R.id.empty_view);
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }

                Toast.makeText(context,  "\"" + title + "\"" +  " was removed from bookmarks",Toast.LENGTH_SHORT).show();


                //mBookmarksAdapter.notifyDataSetChanged();
                notifyDataSetChanged();
                //notifyItemRemoved(position);


            }
        });

        /**
         * single click to open detail article
         */
        holder.item_news.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, DetailActivity.class);
                //id = bookmarksList.get(holder.getAdapterPosition()).getId();
//                Log.e("id test", id);
                myIntent.putExtra("ID", myid); //Optional parameters
                context.startActivity(myIntent);
            }
        });


        /**
         * long click to open news dialog
         */
        holder.item_news.setOnLongClickListener(new View.OnLongClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onLongClick(View v) {
                initDialog(holder);
                newsDialog.show();
                return true;
            }
        });


    }


    @Override
    public int getItemCount() {
        return bookmarksList.size();
    }

    public class BookmarksViewHolder extends RecyclerView.ViewHolder{

        private CardView item_news;
        public ImageView imageView;
        public ImageView imageView_Bookmarks;
        //public TextView id;
        public TextView textViewTitle;
        public TextView textViewSection;
        public TextView textViewTime;


        public BookmarksViewHolder(@NonNull View itemView) {


            super(itemView);

            item_news = itemView.findViewById(R.id.bookmarks_news_item);


            imageView = itemView.findViewById(R.id.img);
            imageView_Bookmarks = itemView.findViewById(R.id.bookmarks);
            //id = itemView.findViewById(R.id.bookmarks)
            textViewTitle = itemView.findViewById(R.id.title);
            textViewSection = itemView.findViewById(R.id.section);
            textViewTime = itemView.findViewById(R.id.time);
        }
    }



    private BookmarksAdapter mBookmarksAdapter;
    public void setmBookmarksAdapter(BookmarksAdapter mBookmarksAdapter) {
        this.mBookmarksAdapter = mBookmarksAdapter;
    }




}
