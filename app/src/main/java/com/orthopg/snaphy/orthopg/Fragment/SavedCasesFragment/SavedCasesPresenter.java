package com.orthopg.snaphy.orthopg.Fragment.SavedCasesFragment;

import android.util.Log;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.DataListCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Post;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.PostRepository;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.Fragment.CaseFragment.TrackLike;
import com.orthopg.snaphy.orthopg.Fragment.CaseFragment.TrackSave;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.strongloop.android.loopback.RestAdapter;

import java.util.HashMap;

/**
 * Created by snaphy on 14/10/16.
 */

public class SavedCasesPresenter {
    RestAdapter restAdapter;
    DataList<Post> postDataList;
    public double limit = 5;
    public double skip = 0;
    CircleProgressBar circleProgressBar;
    MainActivity mainActivity;
    //Track the like of logged customer based on each post.
    HashMap<Object, TrackLike> trackLike;
    HashMap<Object, TrackSave> trackSave;

    public SavedCasesPresenter(RestAdapter restAdapter, CircleProgressBar progressBar, MainActivity mainActivity){
        this.restAdapter = restAdapter;
        circleProgressBar = progressBar;
        this.mainActivity = mainActivity;
        //Only add if not initialized already..
        if(Presenter.getInstance().getList(Post.class, Constants.SAVED_CASE_LIST) == null){
            postDataList = new DataList<>();
            Presenter.getInstance().addList(Constants.SAVED_CASE_LIST, postDataList);
        }else{
            postDataList = Presenter.getInstance().getList(Post.class, Constants.SAVED_CASE_LIST);
        }

        if(Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_LIKE) == null){
            HashMap<Object, TrackLike> trackLike = new HashMap<>();
            Presenter.getInstance().addModel(Constants.TRACK_LIKE, trackLike);
        }else{
            trackLike = Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_LIKE);
        }

        if(Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_SAVE) == null){
            HashMap<Object, TrackSave> trackSave = new HashMap<>();
            Presenter.getInstance().addModel(Constants.TRACK_SAVE, trackSave);
        }else{
            trackSave = Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_SAVE);
        }
    }


    /**
     *
     * @param reset String trending|unsolved|new
     */
    public void fetchSavedPost(boolean reset){
        Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
        if(customer != null){
            if(reset){
                skip = 0;
                //Clear the list..
                postDataList.clear();
            }

            if(skip > 0){
                skip = skip + limit;
            }

            PostRepository postRepository = restAdapter.createRepository(PostRepository.class);
            postRepository.fetchSavedCases(skip, limit, (String)customer.getId(), new DataListCallback<Post>() {
                @Override
                public void onBefore() {
                    if(mainActivity != null){
                        //Start loading bar..
                        mainActivity.startProgressBar(circleProgressBar);
                    }
                }

                @Override
                public void onSuccess(DataList<Post> objects) {
                    for(Post post: objects){
                        if(post != null){
                            if(post.getPostDetails() != null){
                                post.getPostDetails().addRelation(post);
                            }
                        }
                    }
                    postDataList.addAll(objects);
                }


                @Override
                public void onError(Throwable t) {
                    //SHOW ERROR MESSAGE..
                    Log.e(Constants.TAG, t.toString() + "---SavedCasePresenter.java");
                }

                @Override
                public void onFinally() {
                    if(mainActivity != null){
                        //Stop loading bar..
                        mainActivity.stopProgressBar(circleProgressBar);
                    }
                }
            });
        }else{
            Log.e(Constants.TAG, "User not logged! Cannot display SavedCases List");
        }
    }


}
