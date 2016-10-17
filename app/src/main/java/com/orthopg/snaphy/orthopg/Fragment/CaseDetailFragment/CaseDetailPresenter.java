package com.orthopg.snaphy.orthopg.Fragment.CaseDetailFragment;

import android.util.Log;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.ObjectCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Comment;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CommentDetailRepository;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CommentRepository;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.PostDetailRepository;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.CustomModel.TrackList;
import com.orthopg.snaphy.orthopg.Fragment.CaseFragment.TrackLike;
import com.orthopg.snaphy.orthopg.Fragment.CaseFragment.TrackSave;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.sdsmdg.tastytoast.TastyToast;
import com.strongloop.android.loopback.RestAdapter;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Ravi-Gupta on 10/6/2016.
 */

public class CaseDetailPresenter {

    RestAdapter restAdapter;
    public double limit = 5;
    CircleProgressBar circleProgressBar;
    MainActivity mainActivity;


    public CaseDetailPresenter(RestAdapter restAdapter, CircleProgressBar progressBar, MainActivity mainActivity){
        this.restAdapter = restAdapter;
        circleProgressBar = progressBar;
        this.mainActivity = mainActivity;

    }


    public void acceptAnswer(String postId, String commentId, boolean add){
        PostDetailRepository postDetailRepository = restAdapter.createRepository(PostDetailRepository.class);
        postDetailRepository.addRemoveAcceptedAnswer(postId, commentId, add, new ObjectCallback<JSONObject>() {
            @Override
            public void onBefore() {
                if(circleProgressBar != null){
                    mainActivity.startProgressBar(circleProgressBar);
                }
            }

            @Override
            public void onSuccess(JSONObject object) {
                super.onSuccess(object);
                Log.d(Constants.TAG, "Successfully added or removed answer");
            }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
                Log.e(Constants.TAG, toString());
                if(mainActivity !=null){
                    TastyToast.makeText(mainActivity.getApplicationContext(), Constants.ACCEPT_ANSWER_ERROR_COMMENT, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
            }

            @Override
            public void onFinally() {
                if(circleProgressBar != null){
                    mainActivity.stopProgressBar(circleProgressBar);
                }
            }
        });
    }
}
