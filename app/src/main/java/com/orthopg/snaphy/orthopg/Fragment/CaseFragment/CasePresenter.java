package com.orthopg.snaphy.orthopg.Fragment.CaseFragment;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.DataListCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.ObjectCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Comment;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.models.LikePost;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Post;
import com.androidsdk.snaphy.snaphyandroidsdk.models.PostDetail;
import com.androidsdk.snaphy.snaphyandroidsdk.models.SavePost;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CommentRepository;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CustomerRepository;
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
    TextView noCasePresentText;
    String localOrderBy = "datetime(added) DESC";


    public CasePresenter(RestAdapter restAdapter, CircleProgressBar progressBar, MainActivity mainActivity, TextView noCasePresentText){
        this.restAdapter = restAdapter;
        circleProgressBar = progressBar;
        this.mainActivity = mainActivity;
        this.noCasePresentText = noCasePresentText;
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
        likePostRepository.addStorage(mainActivity);
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
        savePostRepository.addStorage(mainActivity);
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
        likePostRepository.addStorage(mainActivity);
        likePostRepository.create(data, callback);
    }

    public void removeLike(LikePost likePost, final ObjectCallback<JSONObject> callback){
        if(likePost != null){
            if(likePost.getId() != null){
                LikePostRepository likePostRepository = restAdapter.createRepository(LikePostRepository.class);
                likePostRepository.addStorage(mainActivity);
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
        savePostRepository.addStorage(mainActivity);
        savePostRepository.create(data, callback);
    }

    public void removeSave(SavePost savePost, final ObjectCallback<JSONObject> callback){
        if(savePost != null){
            if(savePost.getId() != null){
                SavePostRepository savePostRepository = restAdapter.createRepository(SavePostRepository.class);
                savePostRepository.addStorage(mainActivity);
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
        postRepository.addStorage(mainActivity);
        HashMap<String, Object> hashMap = new HashMap<>();
        Post post = postRepository.createObject(hashMap);
        InitNewCaseObject(post);
    }


    public void InitNewCaseObject(Post post){
        NewCase newCase = new NewCase(mainActivity, post);
        //Now add to Presenter interface..
        Presenter.getInstance().addModel(Constants.ADD_NEW_CASE, newCase);
    }

    public void setOldFlag(String listType){
        PostRepository postRepository = restAdapter.createRepository(PostRepository.class);
        postRepository.addStorage(mainActivity);
        HashMap<String, Object> localFlagQuery = new HashMap<String, Object>();
        localFlagQuery.put(listType, listType);
        postRepository.getDb().checkOldData__db(localFlagQuery);
    }


    //Save post data. modify flag..
    public void savePostData(Post post, String listType){
        //post.save__db();
        CommentRepository commentRepository = restAdapter.createRepository(CommentRepository.class);
        commentRepository.addStorage(mainActivity);
        if (post.getPostDetails() != null) {
            post.getPostDetails().addRelation(post);
            if(post.getPostDetails().getComment() != null){
                //Save accepted answer..
                commentRepository.getDb().upsert__db(post.getPostDetails().getComment().getId().toString(), post.getPostDetails().getComment());
            }

            if(post.getCustomer() != null){
                CustomerRepository customerRepository = restAdapter.createRepository(CustomerRepository.class);
                customerRepository.addStorage(mainActivity);
                customerRepository.getDb().upsert__db(post.getCustomer().getId().toString(), post.getCustomer());
            }
             if(post.getComments()!=null){
                 if(post.getComments().size()!=0) {

                     for(Comment comment : post.getComments()){
                         commentRepository.getDb().upsert__db(comment.getId().toString(), comment);
                     }
                 }
             }

        }
        setFlag(listType, post, listType);
        PostRepository postRepository = restAdapter.createRepository(PostRepository.class);
        postRepository.addStorage(mainActivity);
        postRepository.getDb().upsert__db(post.getId().toString(), post);
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

                final PostRepository postRepository = restAdapter.createRepository(PostRepository.class);
                postRepository.addStorage(mainActivity);

                postRepository.getPostedCases(list.getSkip(), list.getLimit(), (String) customer.getId(), new DataListCallback<Post>() {
                    @Override
                    public void onBefore() {
                        if (mainActivity != null) {
                            //Start loading bar..
                            mainActivity.startProgressBar(circleProgressBar);
                            if(noCasePresentText != null){
                                noCasePresentText.setVisibility(View.GONE);
                            }
                        }
                        //Set old flag..
                        setOldFlag(listType);
                    }

                    @Override
                    public void onSuccess(DataList<Post> objects) {
                        if(objects != null){
                            for (Post post : objects) {
                                if (post != null) {
                                    savePostData(post, listType);
                                }
                            }
                            list.getPostDataList().addAll(objects);
                            //Now increment skip....
                            list.incrementSkip(objects.size());
                            //Now remove old data..
                            removeTagFromOldData(listType);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        //SHOW ERROR MESSAGE..
                        Log.e(Constants.TAG, t.toString() + "---CasePresenter.java");
                        //TODO: Check no internet..
                        loadDataOffline(listType);
                       /* if(list.getPostDataList().size() != 0){
                            HashMap<String, Object> localFlagQuery = new HashMap<String, Object>();
                            localFlagQuery.put(listType, listType);
                            //Display offline data ..here..
                            if(postRepository.getDb().count__db(localFlagQuery, localOrderBy, 50) > 0){
                                list.getPostDataList().addAll(postRepository.getDb().getAll__db(localFlagQuery, localOrderBy, 50));
                            }
                        }*/

                    }

                    @Override
                    public void onFinally() {
                        if (mainActivity != null) {
                            //Stop loading bar..
                            mainActivity.stopProgressBar(circleProgressBar);
                            if(list.getPostDataList().size() == 0){
                                if(noCasePresentText != null){
                                    noCasePresentText.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                });
            }//list != null
        }else{
            Log.e(Constants.TAG, "User not logged! Cannot display SavedCases List");
        }
    }

    //Set value of a flag..
    private void setFlag(String listType, Post post, String flag){
        if(listType.equals(Constants.TRENDING)){
            if(flag != null){
                post.setTrending(flag);
            }else{
                post.setTrending("");
            }
        }

        if(listType.equals(Constants.UNSOLVED)){
            if(flag != null){
                post.setUnsolved(flag);
            }else{
                post.setUnsolved("");
            }

        }
        if(listType.equals(Constants.POSTED)){
            if(flag != null){
                post.setPosted(flag);
            }else{
                post.setPosted("");
            }
        }
        if(listType.equals(Constants.LATEST)){
            if(flag != null){
                post.setLatest(flag);
            }else{
                post.setLatest("");
            }

        }

        if(listType.equals(Constants.SAVED)){
            if(flag != null){
                post.setSaved(flag);
            }else {
                post.setSaved("");
            }
        }
    }



    /**
     * Remove old data flag from post model.
     * @param listType
     */
    private void removeTagFromOldData(String listType){
        PostRepository postRepository = restAdapter.createRepository(PostRepository.class);
        postRepository.addStorage(mainActivity);

        //Now remove tags from old data....
        HashMap<String, Object> localFlagQuery = new HashMap<String, Object>();
        localFlagQuery.put(listType, listType);
        localFlagQuery.put(Constants.OLD_DB_FIELD_FLAG, 0);

        HashMap<String, Object> postMap = new HashMap<String, Object>();
        Post post = postRepository.createObject(postMap);
        setFlag(listType, post, "");
        postRepository.getDb().updateAll__db(localFlagQuery, post);
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

                final PostRepository postRepository = restAdapter.createRepository(PostRepository.class);
                postRepository.addStorage(mainActivity);
                postRepository.fetchSavedCases(list.getSkip(), list.getLimit(), (String) customer.getId(), new DataListCallback<Post>() {
                    @Override
                    public void onBefore() {
                        if (mainActivity != null) {
                            //Start loading bar..
                            mainActivity.startProgressBar(circleProgressBar);
                            if(noCasePresentText != null){
                                noCasePresentText.setVisibility(View.GONE);
                            }
                        }

                        //Set old flag..
                        setOldFlag(listType);
                    }

                    @Override
                    public void onSuccess(DataList<Post> objects) {
                        if(objects != null){
                            for (Post post : objects) {
                                if (post != null) {
                                    savePostData(post, listType);
                                }
                            }
                            list.getPostDataList().addAll(objects);
                            //Now increment skip..
                            list.incrementSkip(objects.size());
                            //Now remove old data..
                            removeTagFromOldData(listType);
                        }

                    }


                    @Override
                    public void onError(Throwable t) {
                        //SHOW ERROR MESSAGE..
                        Log.e(Constants.TAG, t.toString() + "---CasePresenter.java");
                        loadDataOffline(listType);
                    }

                    @Override
                    public void onFinally() {
                        if (mainActivity != null) {
                            //Stop loading bar..
                            mainActivity.stopProgressBar(circleProgressBar);
                        }
                        if(list.getPostDataList().size() == 0){
                            if(noCasePresentText != null){
                                noCasePresentText.setVisibility(View.VISIBLE);
                            }
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
            postDetailRepository.addStorage(mainActivity);
            postDetailRepository.getPostDetail(list.getSkip(), list.getLimit(), list.getListType(), new DataListCallback<PostDetail>() {
                @Override
                public void onBefore() {
                    //Start loading bar..
                    mainActivity.startProgressBar(circleProgressBar);
                    if(noCasePresentText != null){
                        noCasePresentText.setVisibility(View.GONE);
                    }
                    setOldFlag(listType);
                }

                @Override
                public void onSuccess(DataList<PostDetail> objects) {
                    if(objects != null){
                        //Add back reference...
                        for(PostDetail postDetail : objects){
                            if(postDetail != null){
                                if(postDetail.getPost() != null){
                                    postDetail.getPost().addRelation(postDetail);
                                    savePostData(postDetail.getPost(), listType);
                                }
                            }

                        }

                        list.getPostDetails().addAll(objects);
                        //Now increment skip..
                        list.incrementSkip(objects.size());
                        //Now remove old data..
                       removeTagFromOldData(listType);
                    }
                }

                @Override
                public void onError(Throwable t) {
                    //SHOW ERROR MESSAGE..
                    Log.e(Constants.TAG, t.toString() + "---CasePresenter.java");
                    loadDataOffline(listType);
                }

                @Override
                public void onFinally() {
                    //Stop loading bar..
                    mainActivity.stopProgressBar(circleProgressBar);
                    if(list.getPostDetails().size() == 0){
                        if(noCasePresentText != null){
                            noCasePresentText.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
        }else{
            Log.e(Constants.TAG, "Unknown list type"+ "---CasePresenter.java");
        }

    }

    public void loadDataOffline(String listType){
        //Get the list ...
        final TrackList list = trackList.get(listType);
        if(list != null) {
            PostRepository postRepository = restAdapter.createRepository(PostRepository.class);
            postRepository.addStorage(mainActivity);
            if(listType.equals(Constants.TRENDING)
                    || listType.equals(Constants.LATEST)
                    || listType.equals(Constants.UNSOLVED)){
                if (list.getPostDetails().size() == 0) {
                    HashMap<String, Object> localFlagQuery = new HashMap<String, Object>();
                    localFlagQuery.put(listType, listType);
                    //Display offline data ..here..
                    if (postRepository.getDb().count__db(localFlagQuery, localOrderBy, 50) > 0) {
                    //if (postRepository.getDb().count__db() > 0) {
                        DataList<Post> posts = postRepository.getDb().getAll__db(localFlagQuery, localOrderBy, 50);
                        //DataList<Post> posts = postRepository.getDb().getAll__db();
                        for(Post post: posts){
                            if(post != null){
                                if(post.getPostDetails() != null){
                                    list.getPostDetails().add(post.getPostDetails());
                                }
                            }
                        }
                    }
                }
            }else{
                if (list.getPostDataList().size() == 0) {
                    HashMap<String, Object> localFlagQuery = new HashMap<String, Object>();
                    localFlagQuery.put(listType, listType);
                    //Display offline data ..here..
                    if (postRepository.getDb().count__db(localFlagQuery, localOrderBy, 50) > 0) {
                    //if(postRepository.getDb().count__db()>0){
                        list.getPostDataList().addAll(postRepository.getDb().getAll__db(localFlagQuery, localOrderBy, 50));
                    }
                }
            }
        }
    }

}
