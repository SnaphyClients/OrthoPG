package com.orthopg.snaphy.orthopg.Fragment.CaseFragment;

import android.util.Log;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.DataListCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.ObjectCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.LikePost;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Post;
import com.androidsdk.snaphy.snaphyandroidsdk.models.PostDetail;
import com.androidsdk.snaphy.snaphyandroidsdk.models.SavePost;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.LikePostRepository;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.PostDetailRepository;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.PostRepository;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.SavePostRepository;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.CustomModel.NewCase;
import com.orthopg.snaphy.orthopg.CustomModel.TrackList;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.strongloop.android.loopback.RestAdapter;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Ravi-Gupta on 10/4/2016.
 */
public class CasePresenter {

    RestAdapter restAdapter;
    HashMap<String, TrackList> trackList;
    //DataList<PostDetail> postDetails;
    public double limit = 5;
    CircleProgressBar circleProgressBar;
    MainActivity mainActivity;
    //HashMap<String, Double> track = new HashMap<>();
    //Track the like of logged customer based on each post.
    HashMap<Object, TrackLike> trackLike = new HashMap<>();
    HashMap<Object, TrackSave> trackSave = new HashMap<>();





    public CasePresenter(RestAdapter restAdapter, CircleProgressBar progressBar, MainActivity mainActivity){
        this.restAdapter = restAdapter;
        circleProgressBar = progressBar;
        this.mainActivity = mainActivity;
        //Only add if not initialized already..
        if(Presenter.getInstance().getModel(HashMap.class, Constants.LIST_CASE_FRAGMENT) == null){
            trackList = new HashMap<String, TrackList>();
            Presenter.getInstance().addModel(Constants.LIST_CASE_FRAGMENT, trackList);
        }else{
            trackList = Presenter.getInstance().getModel(HashMap.class, Constants.LIST_CASE_FRAGMENT);
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


    public void fetchTotalLike(String customerId, String postId, final ObjectCallback<LikePost> callback){
        LikePostRepository likePostRepository = restAdapter.createRepository(LikePostRepository.class);
        HashMap<String, Object> filter = new HashMap<>();
        HashMap<String, Object> where = new HashMap<>();
        where.put("customerId", customerId);
        where.put("postId", postId);
        filter.put("where", where);
        likePostRepository.findOne(filter, new ObjectCallback<LikePost>() {
            @Override
            public void onBefore() {
                callback.onBefore();
            }

            @Override
            public void onSuccess(LikePost object) {
                callback.onSuccess(object);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }

            @Override
            public void onFinally() {
                callback.onFinally();
            }
        });
    }



    public void fetchTotalSave(String customerId, String postId, final ObjectCallback<SavePost> callback){
        SavePostRepository savePostRepository = restAdapter.createRepository(SavePostRepository.class);
        HashMap<String, Object> filter = new HashMap<>();
        HashMap<String, Object> where = new HashMap<>();
        where.put("customerId", customerId);
        where.put("postId", postId);
        filter.put("where", where);
        savePostRepository.findOne(filter, new ObjectCallback<SavePost>() {
            @Override
            public void onBefore() {
                callback.onBefore();
            }

            @Override
            public void onSuccess(SavePost object) {
                callback.onSuccess(object);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }

            @Override
            public void onFinally() {
                callback.onFinally();
            }
        });
    }




    public void addLike(String customerId, String postId, final ObjectCallback<LikePost> callback){
        HashMap<String, Object> data = new HashMap<>();
        data.put("customerId", customerId);
        data.put("postId", postId);
        LikePostRepository likePostRepository = restAdapter.createRepository(LikePostRepository.class);
        likePostRepository.create(data, callback);
    }

    public void removeLike(LikePost likePost, final ObjectCallback<JSONObject> callback){
        if(likePost != null){
            LikePostRepository likePostRepository = restAdapter.createRepository(LikePostRepository.class);
            likePostRepository.deleteById((String) likePost.getId(), callback);
        }

    }

    public void addSave(String customerId, String postId, final ObjectCallback<SavePost> callback){
        HashMap<String, Object> data = new HashMap<>();
        data.put("customerId", customerId);
        data.put("postId", postId);
        SavePostRepository savePostRepository = restAdapter.createRepository(SavePostRepository.class);
        savePostRepository.create(data, callback);
    }

    public void removeSave(SavePost savePost, final ObjectCallback<JSONObject> callback){
        if(savePost != null){
            SavePostRepository savePostRepository = restAdapter.createRepository(SavePostRepository.class);
            savePostRepository.deleteById((String) savePost.getId(), callback);
        }

    }


    /**
     * Reset or Create a new object if not present..
     */
    public void InitNewCaseObject(){
        PostRepository postRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(PostRepository.class);
        HashMap<String, Object> hashMap = new HashMap<>();
        Post post = postRepository.createObject(hashMap);
        NewCase newCase = new NewCase(mainActivity, post);
        //Now add to Presenter interface..
        Presenter.getInstance().addModel(Constants.ADD_NEW_CASE, newCase);
    }






    /**
     *
     * @param listType String trending|unsolved|new
     */
    public void fetchPost(final String listType, boolean reset){
        //Get the list ...
        final TrackList list = trackList.get(listType);
        if(list != null){
            if(reset){
               list.reset();
            }


            PostDetailRepository postDetailRepository =  restAdapter.createRepository(PostDetailRepository.class);
            postDetailRepository.getPostDetail(list.getSkip(), list.getLimit(), list.getListType(), new DataListCallback<PostDetail>() {
                @Override
                public void onBefore() {
                    //Start loading bar..
                    mainActivity.startProgressBar(circleProgressBar);
                }

                @Override
                public void onSuccess(DataList<PostDetail> objects) {
                    if(objects != null){
                        //Add back reference...
                        for(PostDetail postDetail : objects){
                            if(postDetail != null){
                                if(postDetail.getPost() != null){
                                    postDetail.getPost().addRelation(postDetail);
                                }
                            }
                        }

                        list.getPostDetails().addAll(objects);
                        //Now increment skip..
                        list.incrementSkip();
                    }
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
        }else{
            Log.e(Constants.TAG, "Unknown list type"+ "---CasePresenter.java");
        }

    }
}
