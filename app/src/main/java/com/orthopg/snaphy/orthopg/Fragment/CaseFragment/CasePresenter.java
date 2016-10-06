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
import com.strongloop.android.loopback.RestAdapter;

/**
 * Created by Ravi-Gupta on 10/4/2016.
 */
public class CasePresenter {

    RestAdapter restAdapter;
    DataList<PostDetail> postDetails;
    public double limit = 5;
    public double skip = 0;
    CircleProgressBar circleProgressBar;
    MainActivity mainActivity;

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
        if(reset){
            skip = 0;
            //Clear the list..
            postDetails.clear();
        }

        if(skip > 0){
            skip = skip + limit;
        }

        PostDetailRepository postDetailRepository =  restAdapter.createRepository(PostDetailRepository.class);
        postDetailRepository.getPostDetail(skip, limit, listType, new DataListCallback<PostDetail>() {
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
                //SHOW ERROR MESSAGE..
                Log.e(Constants.TAG, t.toString() + "---CasePresenter.java");
            }

            @Override
            public void onFinally() {
                //Stop loading bar..
                mainActivity.stopProgressBar(circleProgressBar);
            }
        });
    }
}
