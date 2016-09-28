package com.orthopg.snaphy.orthopg.Fragment.NewsFragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ravi-Gupta on 9/27/2016.
 */
public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

    MainActivity mainActivity;
    List<NewsModel> newsModelList;

    public NewsListAdapter(MainActivity mainActivity, List<NewsModel> newsModelList) {
        this.mainActivity = mainActivity;
        this.newsModelList = newsModelList;
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

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NewsModel newsModel = newsModelList.get(position);
        ImageView image = holder.imageView;
        TextView heading = holder.heading;
        TextView description = holder.description;
        TextView tag = holder.tag;

        image.setImageDrawable(newsModel.getNewsImage());
        heading.setText(newsModel.getNewsHeading());
        description.setText(newsModel.getNewsDescription());
        tag.setText(newsModel.getType());

    }

    @Override
    public int getItemCount() {
        return newsModelList.size();
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
