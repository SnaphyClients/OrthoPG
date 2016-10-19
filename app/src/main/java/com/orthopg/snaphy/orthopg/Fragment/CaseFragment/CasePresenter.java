package com.orthopg.snaphy.orthopg.Fragment.CaseFragment;

import android.util.Log;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.DataListCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.ObjectCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
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


    public CasePresenter(RestAdapter restAdapter, CircleProgressBar progressBar, MainActivity mainActivity){
        this.restAdapter = restAdapter;
        circleProgressBar = progressBar;
        this.mainActivity = mainActivity;
        //Only add if not initialized already..
        trackList = new HashMap<String, TrackList>();
        Presenter.getInstance().addModel(Constants.LIST_CASE_FRAGMENT, trackList);
        HashMap<Object, TrackLike> trackLike = new HashMap<>();
        Presenter.getInstance().addModel(Constants.TRACK_LIKE, trackLike);
        HashMap<Object, TrackSave> trackSave = new HashMap<>();
        Presenter.getInstance().addModel(Constants.TRACK_SAVE, trackSave);
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
        if(customerId.isEmpty() || postId.isEmpty()){
            callback.onBefore();
            Throwable throwable = new Throwable("CustomerId or PostId is empty");
            callback.onError(throwable);
            callback.onFinally();
            return;
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("customerId", customerId);
        data.put("postId", postId);
        LikePostRepository likePostRepository = restAdapter.createRepository(LikePostRepository.class);
        likePostRepository.create(data, callback);
    }

    public void removeLike(LikePost likePost, final ObjectCallback<JSONObject> callback){
        if(likePost != null){
            if(likePost.getId() != null){
                LikePostRepository likePostRepository = restAdapter.createRepository(LikePostRepository.class);
                likePostRepository.deleteById((String) likePost.getId(), callback);
            }else{
                callback.onBefore();
                Throwable throwable = new Throwable("Like Post id is null");
                callback.onError(throwable);
                callback.onFinally();
            }
        }else{
            callback.onBefore();
            Throwable throwable = new Throwable("Like Post is null");
            callback.onError(throwable);
            callback.onFinally();
        }
    }

    public void addSave(String customerId, String postId, final ObjectCallback<SavePost> callback){
        if(customerId.isEmpty() || postId.isEmpty()){
            callback.onBefore();
            Throwable throwable = new Throwable("CustomerId or PostId is empty");
            callback.onError(throwable);
            callback.onFinally();
            return;
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("customerId", customerId);
        data.put("postId", postId);
        SavePostRepository savePostRepository = restAdapter.createRepository(SavePostRepository.class);
        savePostRepository.create(data, callback);
    }

    public void removeSave(SavePost savePost, final ObjectCallback<JSONObject> callback){
        if(savePost != null){
            if(savePost.getId() != null){
                SavePostRepository savePostRepository = restAdapter.createRepository(SavePostRepository.class);
                savePostRepository.deleteById((String) savePost.getId(), callback);
            }else{
                callback.onBefore();
                Throwable throwable = new Throwable("Save Post id is null");
                callback.onError(throwable);
                callback.onFinally();
            }
        }else{
            callback.onBefore();
            Throwable throwable = new Throwable("Save Post is null");
            callback.onError(throwable);
            callback.onFinally();
        }

    }


    /**
     * Reset or Create a new object if not present..
     */
    public void InitNewCaseObject(){
        PostRepository postRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(PostRepository.class);
        HashMap<String, Object> hashMap = new HashMap<>();
        Post post = postRepository.createObject(hashMap);
        InitNewCaseObject(post);
    }


    public void InitNewCaseObject(Post post){
        NewCase newCase = new NewCase(mainActivity, post);
        //Now add to Presenter interface..
        Presenter.getInstance().addModel(Constants.ADD_NEW_CASE, newCase);
    }




    /**
     *
     * @param reset String saved|posted
     */
    public void fetchPostedPost(final String listType, boolean reset){
        Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
        if(customer != null){
            //Get the list ...
            final TrackList list = trackList.get(listType);
            if(list != null) {
                if (reset) {
                    list.reset();
                }

                PostRepository postRepository = restAdapter.createRepository(PostRepository.class);
                postRepository.getPostedCases(list.getSkip(), list.getLimit(), (String) customer.getId(), new DataListCallback<Post>() {
                    @Override
                    public void onBefore() {
                        if (mainActivity != null) {
                            //Start loading bar..
                            mainActivity.startProgressBar(circleProgressBar);
                        }
                    }

                    @Override
                    public void onSuccess(DataList<Post> objects) {
                        if(objects != null){
                            for (Post post : objects) {
                                if (post != null) {
                                    if (post.getPostDetails() != null) {
                                        post.getPostDetails().addRelation(post);
                                    }
                                }
                            }
                            list.getPostDataList().addAll(objects);
                            //Now increment skip..
                            list.incrementSkip(objects.size());
                        }
                    }


                    @Override
                    public void onError(Throwable t) {
                        //SHOW ERROR MESSAGE..
                        Log.e(Constants.TAG, t.toString() + "---CasePresenter.java");
                    }

                    @Override
                    public void onFinally() {
                        if (mainActivity != null) {
                            //Stop loading bar..
                            mainActivity.stopProgressBar(circleProgressBar);
                        }
                    }
                });
            }//list != null
        }else{
            Log.e(Constants.TAG, "User not logged! Cannot display SavedCases List");
        }
    }


    /**
     *
     * @param reset String saved|posted
     */
    public void fetchSavedPost(final String listType, boolean reset){
        Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
        if(customer != null){
            //Get the list ...
            final TrackList list = trackList.get(listType);
            if(list != null) {
                if (reset) {
                    list.reset();
                }

                PostRepository postRepository = restAdapter.createRepository(PostRepository.class);
                postRepository.fetchSavedCases(list.getSkip(), list.getLimit(), (String) customer.getId(), new DataListCallback<Post>() {
                    @Override
                    public void onBefore() {
                        if (mainActivity != null) {
                            //Start loading bar..
                            mainActivity.startProgressBar(circleProgressBar);
                        }
                    }

                    @Override
                    public void onSuccess(DataList<Post> objects) {
                        if(objects != null){
                            for (Post post : objects) {
                                if (post != null) {
                                    if (post.getPostDetails() != null) {
                                        post.getPostDetails().addRelation(post);
                                    }
                                }
                            }
                            list.getPostDataList().addAll(objects);
                            //Now increment skip..
                            list.incrementSkip(objects.size());
                        }

                    }


                    @Override
                    public void onError(Throwable t) {
                        //SHOW ERROR MESSAGE..
                        Log.e(Constants.TAG, t.toString() + "---CasePresenter.java");
                    }

                    @Override
                    public void onFinally() {
                        if (mainActivity != null) {
                            //Stop loading bar..
                            mainActivity.stopProgressBar(circleProgressBar);
                        }
                    }
                });
            }//list != null
        }else{
            Log.e(Constants.TAG, "User not logged! Cannot display SavedCases List");
        }
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
                        list.incrementSkip(objects.size());
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
