package com.orthopg.snaphy.orthopg.Fragment.CaseDetailFragment;

import android.util.Log;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.DataListCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.ObjectCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Comment;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Post;
import com.androidsdk.snaphy.snaphyandroidsdk.models.PostDetail;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CommentDetailRepository;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CommentRepository;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.PostDetailRepository;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.CustomModel.CommentState;
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
    public double skip = 0;
    private String postId;
    CircleProgressBar circleProgressBar;
    private int position;
    MainActivity mainActivity;


    public CaseDetailPresenter(RestAdapter restAdapter, CircleProgressBar progressBar, MainActivity mainActivity, Post post, int position){
        this.restAdapter = restAdapter;
        circleProgressBar = progressBar;
        this.mainActivity = mainActivity;
        this.position = position;
        if(post != null){
            if(post.getComments() != null){
                skip = post.getComments().size();
            }
            postId = (String)post.getId();
        }
    }


    public void fetchMoreComment(final Post post){
        DataList<String> exceptCommentListId = new DataList<>();
        if(post != null){
            if(post.getPostDetails() != null){
                if(post.getPostDetails().getHasAcceptedAnswer()){
                    if(post.getPostDetails().getComment() != null){
                        exceptCommentListId.add((String)post.getPostDetails().getComment().getId());
                    }
                }
            }
        }
        CommentRepository commentRepository = restAdapter.createRepository(CommentRepository.class);
        commentRepository.fetchPostCommments(postId, skip, limit, exceptCommentListId, new DataListCallback<Comment>() {
            @Override
            public void onBefore() {
                if(circleProgressBar != null){
                    mainActivity.startProgressBar(circleProgressBar);
                }
            }

            @Override
            public void onSuccess(DataList<Comment> objects) {
                if(objects != null){
                    for(Comment comment: objects){
                        if(comment != null){
                            comment.addRelation(post);

                        }
                    }

                    //Add skip..
                    skip = skip + objects.size();
                    //Add to commentList..
                    if(post != null){
                        if(post.getComments() != null){
                            post.getComments().addAll(objects);
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                Log.e(Constants.TAG, toString());
            }

            @Override
            public void onFinally() {
                if(circleProgressBar != null){
                    mainActivity.stopProgressBar(circleProgressBar);
                }
            }
        });
    }


    public void acceptAnswer(String postId, String commentId, boolean add){
        PostDetailRepository postDetailRepository = restAdapter.createRepository(PostDetailRepository.class);
        postDetailRepository.addRemoveAcceptedAnswer(postId, commentId, add, new ObjectCallback<Post>() {
            @Override
            public void onBefore() {
                if(circleProgressBar != null){
                    mainActivity.startProgressBar(circleProgressBar);
                }
            }

            @Override
            public void onSuccess(Post post) {
                if(post != null && post.getPostDetails() != null){
                    post.getPostDetails().addRelation(post);
                }else{
                    return;
                }
                Log.d(Constants.TAG, "Successfully added or removed answer");
                //Now update the post..
                if(Presenter.getInstance().getModel(HashMap.class, Constants.LIST_CASE_FRAGMENT) != null){
                    HashMap<String, TrackList> list = Presenter.getInstance().getModel(HashMap.class, Constants.LIST_CASE_FRAGMENT);
                    if(list != null){
                        TrackList trackListItem = list.get(Constants.SELECTED_TAB);
                        if(Constants.SELECTED_TAB.equals(Constants.TRENDING)
                                || Constants.SELECTED_TAB.equals(Constants.LATEST)
                                || Constants.SELECTED_TAB.equals(Constants.UNSOLVED)){
                            DataList<PostDetail> getPostDataList = trackListItem.getPostDetails();
                            if(getPostDataList != null){
                                if(getPostDataList.size() != 0){
                                    //Now replace the post with new post..
                                    getPostDataList.remove(position);
                                    getPostDataList.add(position, post.getPostDetails());
                                }

                            }
                        }else{
                            if(trackListItem.getPostDataList() != null){
                                if(trackListItem.getPostDataList().size() != 0){
                                    //Now replace the post with new post..
                                    trackListItem.getPostDataList().remove(position);
                                    trackListItem.getPostDataList().add(position, post);
                                }
                            }
                        }
                    }

                }
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
