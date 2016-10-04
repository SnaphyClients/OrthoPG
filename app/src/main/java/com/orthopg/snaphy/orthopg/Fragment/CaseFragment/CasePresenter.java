package com.orthopg.snaphy.orthopg.Fragment.CaseFragment;

import android.util.Log;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.DataListCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.PostDetail;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.PostDetailRepository;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.sdsmdg.tastytoast.TastyToast;
import com.strongloop.android.loopback.RestAdapter;

import java.util.HashMap;

/**
 * Created by Ravi-Gupta on 10/4/2016.
 */
public class CasePresenter {
    RestAdapter restAdapter;
    DataList<PostDetail> postDetails;
    public int limit = 5;
    CircleProgressBar circleProgressBar;
    MainActivity mainActivity;

    //TODO add loading bar as argument.
    public CasePresenter(RestAdapter restAdapter, CircleProgressBar progressBar, MainActivity mainActivity){
        this.restAdapter = restAdapter;
        circleProgressBar = progressBar;
        this.mainActivity = mainActivity;
        //Only add if not initialized already..
        if(Presenter.getInstance().getList(PostDetail.class, Constants.POST_DETAIL_LIST_CASE_FRAGMENT) == null){
            postDetails = new DataList<>();
            Presenter.getInstance().addList(Constants.POST_DETAIL_LIST_CASE_FRAGMENT, postDetails);
        }else{
            postDetails = Presenter.getInstance().getList(PostDetail.class, Constants.POST_DETAIL_LIST_CASE_FRAGMENT);
        }
    }

    /**
     *
     * @param listType String trending|unsolved|new
     */
    public void fetchPost(String listType, boolean reset){
        HashMap<String, Object> filter = new HashMap<>();
        HashMap<String, String> include = new HashMap<>();
        filter.put("include", "post");
        if(filter.get("skip") == null || reset){
            filter.put("skip", 0);
        }else{
            filter.put("skip", (int)filter.get("skip") + limit);
        }

        if(reset){
            //Clear the list..
            postDetails.clear();
        }

        filter.put("limit", limit);
        HashMap<Object,Object> where = new HashMap<>();
        filter.put("where", where);
        addFilter(listType, filter);

        PostDetailRepository postDetailRepository =  restAdapter.createRepository(PostDetailRepository.class);
        postDetailRepository.find(filter, new DataListCallback<PostDetail>() {
            @Override
            public void onBefore() {
                //Start loading bar..
                mainActivity.startProgressBar(circleProgressBar);
            }

            @Override
            public void onSuccess(DataList<PostDetail> objects) {
                //Add back refrence...
                for(PostDetail postDetail : objects){
                    if(postDetail != null){
                        if(postDetail.getPost() != null){
                            postDetail.getPost().addRelation(postDetail);
                        }
                    }
                }
                postDetails.addAll(objects);
            }

            @Override
            public void onError(Throwable t) {
                //TODO SHOW ERROR MESSAGE..
                Log.e(Constants.TAG, t.toString() + "---CasePresenter.java");
                TastyToast.makeText(mainActivity.getApplicationContext(), "Something went wrong! Try again", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }

            @Override
            public void onFinally() {
                //Stop loading bar..
                mainActivity.stopProgressBar(circleProgressBar);
            }
        });
    }


    private void addFilter(String listType, HashMap<String, Object> filter){
        if(listType.equals(Constants.LATEST)){
            filter.put("order", "added DESC");
        }
        else if(listType.equals(Constants.TRENDING)){
            filter.put("order", "totalLike DESC");
        }
        else if(listType.equals(Constants.UNSOLVED)){
            //TODO ADD THIS FILTER LATER..
        }

        //Add publish status..
        HashMap<String, Object> where = (HashMap<String, Object>)filter.get("where");
        where.put("status", Constants.PUBLISH);
    }
}
