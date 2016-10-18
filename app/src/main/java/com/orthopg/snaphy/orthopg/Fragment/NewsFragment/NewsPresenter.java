package com.orthopg.snaphy.orthopg.Fragment.NewsFragment;

import android.util.Log;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.DataListCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.News;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.NewsRepository;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.strongloop.android.loopback.RestAdapter;

import java.util.HashMap;

/**
 * Created by Ravi-Gupta on 10/6/2016.
 */

public class NewsPresenter {

    RestAdapter restAdapter;
    DataList<News> newsDataList;
    public double limit = 5;
    public double skip = 0;
    CircleProgressBar circleProgressBar;
    MainActivity mainActivity;

    public NewsPresenter(RestAdapter restAdapter, CircleProgressBar progressBar, MainActivity mainActivity){
        this.restAdapter = restAdapter;
        circleProgressBar = progressBar;
        this.mainActivity = mainActivity;
        //Only add if not initialized already..
        if(Presenter.getInstance().getList(News.class, Constants.NEWS_LIST_NEWS_FRAGMENT) == null){
            newsDataList = new DataList<>();
            Presenter.getInstance().addList(Constants.NEWS_LIST_NEWS_FRAGMENT, newsDataList);
        }else{
            newsDataList = Presenter.getInstance().getList(News.class, Constants.NEWS_LIST_NEWS_FRAGMENT);
        }
    }

    public void fetchNews(boolean reset) {
        if(reset){
            skip = 0;
            //Clear the list..
            newsDataList.clear();
        }

        HashMap<String, Object> filter = new HashMap<>();
        //filter.order = "added DESC";
        filter.put("order", "added DESC");
        filter.put("skip", skip);
        filter.put("limit", limit);
        HashMap<String, Object> where = new HashMap<>();
        where.put("status", Constants.PUBLISH);
        filter.put("where", where);
        NewsRepository newsRepository = restAdapter.createRepository(NewsRepository.class);
        newsRepository.find(filter, new DataListCallback<News>() {
            @Override
            public void onBefore() {
                super.onBefore();
                mainActivity.startProgressBar(circleProgressBar);
            }

            @Override
            public void onSuccess(DataList<News> objects) {
                if(objects != null){
                    newsDataList.addAll(objects);
                    //Now add skip..
                    skip = skip + objects.size();
                }

            }

            @Override
            public void onError(Throwable t) {
                Log.e(Constants.TAG, t.toString());
            }

            @Override
            public void onFinally() {
                mainActivity.stopProgressBar(circleProgressBar);
            }
        });
    }
}
