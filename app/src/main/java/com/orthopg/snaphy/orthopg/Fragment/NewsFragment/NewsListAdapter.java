package com.orthopg.snaphy.orthopg.Fragment.NewsFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.News;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ravi-Gupta on 9/27/2016.
 */
public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

    MainActivity mainActivity;
    DataList<News> newsDataList;

    public NewsListAdapter(MainActivity mainActivity, DataList<News> newsDataList) {
        this.mainActivity = mainActivity;
        this.newsDataList = newsDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View newsView = inflater.inflate(R.layout.layout_news, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(newsView);
        return viewHolder;
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        News news = newsDataList.get(position);
        ImageView image = holder.imageView;
        TextView heading = holder.heading;
        TextView description = holder.description;
        TextView tag = holder.tag;
        Typeface font = Typeface.createFromAsset(mainActivity.getAssets(), "fonts/OpenSans-Regular.ttf");

        if(news.getImage() != null){
            mainActivity.snaphyHelper.loadUnsignedUrl(news.getImage(), image);
        }

        if(news.getTitle() != null) {
            if(!news.getTitle().isEmpty()) {
                heading.setText(news.getTitle());
            }
        }

        if(news.getDescription() != null) {
            if(!news.getDescription().isEmpty()) {
                description.setTypeface(font);
                description.setText(news.getDescription());
            }
        }

        if(news.getType() != null) {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (!news.getType().isEmpty()) {
                tag.setText(news.getType());
                if (news.getType().equals(Constants.NEWS)) {
                    tag.setTextColor(Color.parseColor(Constants.PRIMARY));
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        tag.setBackgroundDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.curved_rectangle));
                    } else {
                        tag.setBackground(ContextCompat.getDrawable(mainActivity, R.drawable.curved_rectangle));
                    }

                } else if (news.getType().equals(Constants.ADV)) {
                    tag.setTextColor(Color.parseColor(Constants.SUCCESS));
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        tag.setBackgroundDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.curved_rectangle_success));
                    } else {
                        tag.setBackground(ContextCompat.getDrawable(mainActivity, R.drawable.curved_rectangle_success));
                    }
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return newsDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.layout_news_imageview1) ImageView imageView;
        @Bind(R.id.layout_news_textview1) TextView tag;
        @Bind(R.id.layout_news_textview2) TextView heading;
        @Bind(R.id.layout_news_textview3) TextView description;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
