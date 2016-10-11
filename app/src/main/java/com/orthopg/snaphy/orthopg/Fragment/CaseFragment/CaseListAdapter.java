package com.orthopg.snaphy.orthopg.Fragment.CaseFragment;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.ObjectCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Comment;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.models.LikePost;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Post;
import com.androidsdk.snaphy.snaphyandroidsdk.models.PostDetail;
import com.androidsdk.snaphy.snaphyandroidsdk.models.SavePost;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.Fragment.PostedCasesFragment.PostedCasesFragment;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.R.attr.name;
import static com.orthopg.snaphy.orthopg.R.id.imageView;
import static com.orthopg.snaphy.orthopg.R.mipmap.like;

/**
 * Created by Ravi-Gupta on 9/21/2016.
 */
public class CaseListAdapter extends RecyclerView.Adapter<CaseListAdapter.ViewHolder> {

    MainActivity mainActivity;
    DataList<PostDetail> postDetailDataList;
    String TAG;
    CasePresenter casePresenter;

    public CaseListAdapter(MainActivity mainActivity,  DataList<PostDetail> postDetailDataList, String TAG, CasePresenter casePresenter) {
        this.mainActivity = mainActivity;
        this.postDetailDataList = postDetailDataList;
        this.TAG = TAG;
        this.casePresenter = casePresenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View caseView = inflater.inflate(R.layout.layout_case_list, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(caseView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final PostDetail postDetail = postDetailDataList.get(position);
        final Post post;
        if(postDetail != null){
            if(postDetail.getPost() != null){
                post = postDetail.getPost();
            }else{
                return;
            }
        }else{
            return;
        }
        CaseImageAdapter caseImageAdapter;

        ImageView imageView = holder.userImage;
        TextView caseHeading = holder.caseHeading;
        TextView userName = holder.userName;
        TextView casePostedTime = holder.casePostedTime;
        final ImageView like = holder.like;
        final ImageView saveCase = holder.saveCase;
        RecyclerView caseImages = holder.caseImages;
        TextView caseDescription = holder.caseDescription;
        TextView tag = holder.tag;
        ImageButton delete = holder.deleteButton;
        ImageButton edit = holder.editButton;
        ImageView isAnswerSelected = holder.isAnswerSelected;
        TextView selectedAnswerUserName = holder.selectedAnswerUserName;
        TextView selectedAnswer = holder.selectedAnswer;
        final TextView numberOfLike = holder.numberOfLikes;
        final TextView numberOfSave = holder.numberOfSave;
        LinearLayout linearLayout = holder.linearLayout;

        caseImages.setLayoutManager(new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false));


        if(!post.getHeading().isEmpty()){
            caseHeading.setText(post.getHeading());
        }


        //Now load case image list..
        if(post.getPostImages() != null ){
            if(post.getPostImages().size() == 0){
                caseImages.setVisibility(View.GONE);
            }else{
                caseImages.setVisibility(View.VISIBLE);
                caseImageAdapter = new CaseImageAdapter(mainActivity, post.getPostImages());
                caseImages.setAdapter(caseImageAdapter);
            }
        }else{
            caseImages.setVisibility(View.GONE);
        }

        if(post.getCustomer() != null) {
            if(post.getAnonymous()){
                imageView.setImageResource(R.mipmap.anonymous);
                userName.setText(Constants.ANONYMOUS);
            }else{
                String name = mainActivity.snaphyHelper.getName(post.getCustomer().getFirstName(), post.getCustomer().getLastName());
                if(!name.isEmpty()){
                    name = Constants.Doctor + name;
                }
                userName.setText(name);

                if(post.getCustomer().getProfilePic() != null){
                    mainActivity.snaphyHelper.loadUnSignedThumbnailImage(post.getCustomer().getProfilePic(), imageView, R.mipmap.anonymous);
                }else{
                    //Set deault image..
                    //TODO CHANGE BACKGROUND COLOR TO BLUE..
                    imageView.setImageResource(R.mipmap.anonymous);
                }
            }

        }


        setTime(casePostedTime, postDetail.getAdded());

        if(post.getDescription() != null) {
            if (!post.getDescription().isEmpty()) {
                caseDescription.setVisibility(View.VISIBLE);
                caseDescription.setText(post.getDescription());
            } else {
                caseDescription.setVisibility(View.GONE);
            }
        } else {
            caseDescription.setVisibility(View.GONE);
        }

        if(postDetail != null) {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(!postDetail.getType().isEmpty()) {
                tag.setText(postDetail.getType());
                if(postDetail.getType().equals(Constants.CASE)){
                    tag.setTextColor(Color.parseColor(Constants.PRIMARY));
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        tag.setBackgroundDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.curved_rectangle));
                    } else {
                        tag.setBackground(ContextCompat.getDrawable(mainActivity, R.drawable.curved_rectangle));
                    }

                } else if(postDetail.getType().equals(Constants.BOOK_REVIEW)) {
                    tag.setTextColor(Color.parseColor(Constants.WARNING));
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        tag.setBackgroundDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.curved_rectangle_warning));
                    } else {
                        tag.setBackground(ContextCompat.getDrawable(mainActivity, R.drawable.curved_rectangle_warning));
                    }
                } else if(postDetail.getType().equals(Constants.INTERVIEW)) {
                    tag.setTextColor(Color.parseColor(Constants.SUCCESS));
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        tag.setBackgroundDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.curved_rectangle_success));
                    } else {
                        tag.setBackground(ContextCompat.getDrawable(mainActivity, R.drawable.curved_rectangle_success));
                    }
                }
            }
        }


        if(postDetail.getHasAcceptedAnswer()){
            //Show accepted answer..
            if(postDetail.getComment() != null){
                showSelectedAnswer(selectedAnswer, isAnswerSelected,selectedAnswerUserName);
                Comment acceptedAnswer = postDetail.getComment();
                if(acceptedAnswer.getCustomer() != null){
                    String name = mainActivity.snaphyHelper.getName(acceptedAnswer.getCustomer().getFirstName(), acceptedAnswer.getCustomer().getLastName());
                    if(!name.isEmpty()){
                        name = Constants.Doctor + name.replace("^[Dd][Rr]", "");
                    }
                    selectedAnswerUserName.setText(name);
                }

                if(postDetail.getComment().getAnswer() != null){
                    selectedAnswer.setText(postDetail.getComment().getAnswer());
                }

            }else{
                ///hide accepted answer..
                hideSelectedAnswer(selectedAnswer, isAnswerSelected,selectedAnswerUserName);
            }
        }else{
            ///hide accepted answer..
            hideSelectedAnswer(selectedAnswer, isAnswerSelected,selectedAnswerUserName);
        }


        //TOTAL LIKE
        numberOfLike.setText(String.valueOf((int)postDetail.getTotalLike()));
        //TOTAL SAVE..
        numberOfSave.setText(String.valueOf((int)postDetail.getTotalSave()));


        if(TAG.equals(PostedCasesFragment.TAG)) {
            delete.setVisibility(View.VISIBLE);
            edit.setVisibility(View.VISIBLE);
        } else {
            delete.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
        }

        final Customer loginCustomer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
        //Customer logged in
        if(loginCustomer != null && post.getId() != null){
            casePresenter.fetchTotalLike((String)loginCustomer.getId(), (String) post.getId(), new ObjectCallback<LikePost>() {
                @Override
                public void onSuccess(LikePost object) {
                    if(object != null){
                        TrackLike trackLike = new TrackLike();
                        trackLike.likePost = object;
                        trackLike.state = true;
                        Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_LIKE).put(post.getId(), trackLike);
                        like.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.like_selected));
                    }else{
                        TrackLike trackLike = new TrackLike();
                        trackLike.state = false;
                        Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_LIKE).put(post.getId(), trackLike);
                        like.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.like_unselected));
                    }
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    Log.e(Constants.TAG, t.toString());
                    TrackLike trackLike = new TrackLike();
                    trackLike.state = false;
                    Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_LIKE).put(post.getId(), trackLike);
                    like.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.like_unselected));
                }
            });


            casePresenter.fetchTotalSave((String) loginCustomer.getId(), (String) post.getId(), new ObjectCallback<SavePost>() {
                @Override
                public void onSuccess(SavePost object) {
                    if(object != null){
                        TrackSave trackSave = new TrackSave();
                        trackSave.savePost = object;
                        trackSave.state = true;
                        Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_SAVE).put(post.getId(), trackSave);
                        saveCase.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.save_selected));
                    }else{
                        TrackSave trackSave = new TrackSave();
                        trackSave.state = false;
                        Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_SAVE).put(post.getId(), trackSave);
                        saveCase.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.save_unselected));
                    }
                }

                @Override
                public void onError(Throwable t) {
                    Log.e(Constants.TAG, t.toString());
                    TrackSave trackSave = new TrackSave();
                    trackSave.state = false;
                    Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_SAVE).put(post.getId(), trackSave);
                    saveCase.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.save_unselected));
                }
            });
        }



        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_LIKE).get(post.getId()) == null){
                    //Add like
                    casePresenter.addLike((String) loginCustomer.getId(), (String) post.getId(), new ObjectCallback<LikePost>() {
                        @Override
                        public void onBefore() {
                            TrackLike trackLike = new TrackLike();
                            trackLike.state = true;
                            showLike(post, like, trackLike,numberOfLike, postDetail);
                        }

                        @Override
                        public void onSuccess(LikePost object) {
                            TrackLike trackLike = new TrackLike();
                            trackLike.state = true;
                            trackLike.likePost = object;
                            showLike(post, like, trackLike, numberOfLike, postDetail);
                        }

                        @Override
                        public void onError(Throwable t) {
                            TrackLike trackLike = new TrackLike();
                            trackLike.state = false;
                            showLike(post, like, trackLike, numberOfLike, postDetail);
                            //TODO add toast..
                        }
                    });
                }else{
                    final TrackLike trackLike = (TrackLike)Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_LIKE).get(post.getId());
                    if(trackLike.state){
                        trackLike.state = false;
                        showLike(post, like, trackLike, numberOfLike, postDetail);
                        //delete like
                        casePresenter.removeLike(trackLike.likePost, new ObjectCallback<JSONObject>() {
                            @Override
                            public void onBefore() {
                                trackLike.state = false;
                                showLike(post, like, trackLike, numberOfLike, postDetail);
                            }

                            @Override
                            public void onSuccess(JSONObject object) {
                                trackLike.state = false;
                                showLike(post, like, trackLike, numberOfLike, postDetail);
                            }

                            @Override
                            public void onError(Throwable t) {
                                trackLike.state = true;
                                showLike(post, like, trackLike, numberOfLike, postDetail);
                            }

                            @Override
                            public void onFinally() {
                                super.onFinally();
                            }
                        });

                    }else{
                        //add like..
                        casePresenter.addLike((String) loginCustomer.getId(), (String) post.getId(), new ObjectCallback<LikePost>() {
                            @Override
                            public void onBefore() {
                                trackLike.state = true;
                                showLike(post, like, trackLike, numberOfLike, postDetail);
                            }

                            @Override
                            public void onSuccess(LikePost object) {
                                trackLike.state = true;
                                trackLike.likePost = object;
                                showLike(post, like, trackLike, numberOfLike, postDetail);
                            }

                            @Override
                            public void onError(Throwable t) {
                                trackLike.state = false;
                                showLike(post, like, trackLike, numberOfLike, postDetail);
                                //TODO add toast..
                            }
                        });
                    }
                }
            }
        });




        saveCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_SAVE).get(post.getId()) == null){
                    //Add like
                    casePresenter.addSave((String) loginCustomer.getId(), (String) post.getId(), new ObjectCallback<SavePost>() {
                        @Override
                        public void onBefore() {
                            TrackSave trackSave = new TrackSave();
                            trackSave.state = true;
                            showSave(post, saveCase, trackSave, numberOfSave, postDetail);
                        }

                        @Override
                        public void onSuccess(SavePost object) {
                            TrackSave trackSave = new TrackSave();
                            trackSave.state = true;
                            trackSave.savePost = object;
                            showSave(post, saveCase, trackSave, numberOfSave, postDetail);
                        }

                        @Override
                        public void onError(Throwable t) {
                            TrackSave trackSave = new TrackSave();
                            trackSave.state = false;
                            showSave(post, saveCase, trackSave, numberOfSave, postDetail);
                            //TODO add toast..
                        }
                    });
                }else{
                    final TrackSave trackSave = (TrackSave)Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_SAVE).get(post.getId());
                    if(trackSave.state){
                        trackSave.state = false;
                        showSave(post, saveCase, trackSave, numberOfSave, postDetail);
                        //delete like
                        casePresenter.removeSave(trackSave.savePost, new ObjectCallback<JSONObject>() {
                            @Override
                            public void onBefore() {
                                trackSave.state = false;
                                showSave(post, saveCase, trackSave, numberOfSave, postDetail);
                            }

                            @Override
                            public void onSuccess(JSONObject object) {
                                trackSave.state = false;
                                showSave(post, saveCase, trackSave, numberOfSave, postDetail);
                            }

                            @Override
                            public void onError(Throwable t) {
                                trackSave.state = true;
                                showSave(post, saveCase, trackSave, numberOfSave, postDetail);
                            }

                            @Override
                            public void onFinally() {
                                super.onFinally();
                            }
                        });

                    }else{
                        //add like..
                        casePresenter.addSave((String) loginCustomer.getId(), (String) post.getId(), new ObjectCallback<SavePost>() {
                            @Override
                            public void onBefore() {
                                trackSave.state = true;
                                showSave(post, saveCase, trackSave, numberOfSave, postDetail);
                            }

                            @Override
                            public void onSuccess(SavePost object) {
                                trackSave.state = true;
                                trackSave.savePost = object;
                                showSave(post, saveCase, trackSave, numberOfSave, postDetail);
                            }

                            @Override
                            public void onError(Throwable t) {
                                trackSave.state = false;
                                showSave(post, saveCase, trackSave, numberOfSave, postDetail);
                                //TODO add toast..
                            }
                        });
                    }
                }
            }
        });


        final Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
        final String MCINumber = customer.getMciNumber() != null ? customer.getMciNumber() : "";
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TAG.equals(PostedCasesFragment.TAG)) {
                    if (!MCINumber.isEmpty()) {
                        //mainActivity.replaceFragment(R.id.fragment_case_button4, null);
                        mainActivity.replaceFragment(R.id.layout_case_list_textview4, position);
                    } else {
                        TastyToast.makeText(mainActivity.getApplicationContext(), "Verification is under process", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
                    }
                } else {
                    //Show Toast
                }
            }
        });

    }


    public void showLike(Post post, ImageView like, TrackLike trackLike, TextView numberOfLike, PostDetail postDetail){
        if(trackLike != null){
            if(trackLike.state){
                Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_LIKE).put(post.getId(), trackLike);
                like.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.like_selected));
                postDetail.setTotalLike(postDetail.getTotalLike() + 1);
                numberOfLike.setText(String.valueOf((int)postDetail.getTotalLike()));
            }else{
                Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_LIKE).put(post.getId(), trackLike);
                like.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.like_unselected));
                if(postDetail.getTotalLike() == 0){

                } else {
                    postDetail.setTotalLike(postDetail.getTotalLike() - 1);
                }
                numberOfLike.setText(String.valueOf((int)postDetail.getTotalLike()));
            }
        }
    }

    public void showSave(Post post, ImageView saveCase, TrackSave trackSave, TextView numberOfSave, PostDetail postDetail){
        if(trackSave != null){
            if(trackSave.state){
                Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_SAVE).put(post.getId(), trackSave);
                saveCase.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.save_selected));
                postDetail.setTotalSave(postDetail.getTotalSave() + 1);
                numberOfSave.setText(String.valueOf((int)postDetail.getTotalSave()));
            }else{
                Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_SAVE).put(post.getId(), trackSave);
                saveCase.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.save_unselected));
                if(postDetail.getTotalSave() == 0) {

                } else {
                    postDetail.setTotalSave(postDetail.getTotalSave() - 1);
                }
                numberOfSave.setText(String.valueOf((int)postDetail.getTotalSave()));
            }
        }
    }


    public void hideSelectedAnswer(TextView selectedAnswer, ImageView grenTick, TextView userName){
        selectedAnswer.setVisibility(View.GONE);
        grenTick.setVisibility(View.GONE);
        userName.setVisibility(View.GONE);
    }
    public void showSelectedAnswer(TextView selectedAnswer, ImageView grenTick, TextView userName){
        selectedAnswer.setVisibility(View.VISIBLE);
        grenTick.setVisibility(View.VISIBLE);
        userName.setVisibility(View.VISIBLE);
    }



    public void setTime(TextView casePostedTime, String date){
        //TODO DEFINE TIME HERE..
        //PARSE The javascript format date first..
        //casePostedTime.setText(caseModel.getPostTime());
    }




    @Override
    public int getItemCount() {
        return postDetailDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.layout_case_list_image) ImageView userImage;
        @Bind(R.id.layout_case_list_textview1) TextView caseHeading;
        @Bind(R.id.layout_case_list_textview2) TextView userName;
        @Bind(R.id.layout_case_list_textview3) TextView casePostedTime;
        @Bind(R.id.layout_case_list_imagebutton2) ImageView like;
        @Bind(R.id.layout_case_list_imagebutton1) ImageView saveCase;
        @Bind(R.id.layout_case_list_recycler_view) RecyclerView caseImages;
        @Bind(R.id.layout_case_list_textview4) TextView caseDescription;
        @Bind(R.id.layout_case_list_textview5) TextView tag;
        @Bind(R.id.layout_case_list_imageview1) ImageView isAnswerSelected;
        @Bind(R.id.layout_case_list_textview6) TextView selectedAnswerUserName;
        @Bind(R.id.layout_case_list_textview7) TextView selectedAnswer;
        @Bind(R.id.layout_case_list_button1) ImageButton deleteButton;
        @Bind(R.id.layout_case_list_button2) ImageButton editButton;
        @Bind(R.id.layout_case_list_textview8) TextView numberOfLikes;
        @Bind(R.id.layout_case_list_textview9) TextView numberOfSave;
        @Bind(R.id.layout_case_list_linear_layout)
        LinearLayout linearLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
