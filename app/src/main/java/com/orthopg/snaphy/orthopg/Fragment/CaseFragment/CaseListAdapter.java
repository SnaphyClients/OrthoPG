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
import com.orthopg.snaphy.orthopg.CustomModel.TrackList;
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
    HashMap<String, TrackList> trackList;
    String TAG;
    CasePresenter casePresenter;

    public CaseListAdapter(MainActivity mainActivity, HashMap<String, TrackList> trackList, String TAG, CasePresenter casePresenter) {
        this.mainActivity = mainActivity;
        this.trackList = trackList;
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

    //Storing data
    private class Data{
        public PostDetail postDetail;
        public Post post;
        public  TrackList trackListItem;

        //Constructor.
        public Data(TrackList trackListItem, int position ){
            this.trackListItem = trackListItem;

            if(Constants.SELECTED_TAB.equals(Constants.TRENDING)
                    || Constants.SELECTED_TAB.equals(Constants.LATEST)
                    || Constants.SELECTED_TAB.equals(Constants.UNSOLVED)){

                    postDetail  = trackListItem.getPostDetails().get(position);
                    if(postDetail != null){
                        post = postDetail.getPost();
                    }
            }else{
                if(trackListItem.getPostDataList() != null){
                    if(trackListItem.getPostDataList().size() != 0){
                        post  = trackListItem.getPostDataList().get(position);

                        postDetail = post.getPostDetails();

                    }
                }
            }
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //get the list..
        TrackList trackListItem = trackList.get(Constants.SELECTED_TAB);



        if(trackListItem != null) {
            final Data data = new Data(trackListItem, position);

            if (data.postDetail == null || data.post == null) {
                Log.e(Constants.TAG, "CaseListAdapter returning postDetail or post obj is null");
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
            TextView delete = holder.deleteButton;
            TextView edit = holder.editButton;
            ImageView isAnswerSelected = holder.isAnswerSelected;
            TextView selectedAnswerUserName = holder.selectedAnswerUserName;
            TextView selectedAnswer = holder.selectedAnswer;
            final TextView numberOfLike = holder.numberOfLikes;
            final TextView numberOfSave = holder.numberOfSave;
            LinearLayout linearLayout = holder.linearLayout;
            LinearLayout likeLinearLayout = holder.likeLinearLayout;
            LinearLayout saveLinearLayout = holder.saveLinearLayout;

            caseImages.setLayoutManager(new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false));


            if (!data.post.getHeading().isEmpty()) {
                caseHeading.setText(data.post.getHeading());
            }


            //Now load case image list..
            if (data.post.getPostImages() != null) {
                if (data.post.getPostImages().size() == 0) {
                    caseImages.setVisibility(View.GONE);
                } else {
                    caseImages.setVisibility(View.VISIBLE);
                    caseImageAdapter = new CaseImageAdapter(mainActivity, data.post.getPostImages());
                    caseImages.setAdapter(caseImageAdapter);
                }
            } else {
                caseImages.setVisibility(View.GONE);
            }

            if (data.post.getCustomer() != null) {
                if (data.post.getAnonymous()) {
                    imageView.setImageResource(R.mipmap.anonymous);
                    userName.setText(Constants.ANONYMOUS);
                } else {
                    String name = mainActivity.snaphyHelper.getName(data.post.getCustomer().getFirstName(), data.post.getCustomer().getLastName());
                    if (!name.isEmpty()) {
                        name = Constants.Doctor + name;
                    }
                    userName.setText(name);

                    if (data.post.getCustomer().getProfilePic() != null) {
                        mainActivity.snaphyHelper.loadUnSignedThumbnailImage(data.post.getCustomer().getProfilePic(), imageView, R.mipmap.anonymous);
                    } else {
                        //Set deault image..
                        //TODO CHANGE BACKGROUND COLOR TO BLUE..
                        imageView.setImageResource(R.mipmap.anonymous);
                    }
                }

            }


            setTime(casePostedTime, data.postDetail.getAdded());

            if (data.post.getDescription() != null) {
                if (!data.post.getDescription().isEmpty()) {
                    caseDescription.setVisibility(View.VISIBLE);
                    caseDescription.setText(data.post.getDescription());
                } else {
                    caseDescription.setVisibility(View.GONE);
                }
            } else {
                caseDescription.setVisibility(View.GONE);
            }

            if (data.postDetail != null) {
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if (!data.postDetail.getType().isEmpty()) {
                    tag.setText(data.postDetail.getType());
                    if (data.postDetail.getType().equals(Constants.CASE)) {
                        tag.setTextColor(Color.parseColor(Constants.PRIMARY));
                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            tag.setBackgroundDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.curved_rectangle));
                        } else {
                            tag.setBackground(ContextCompat.getDrawable(mainActivity, R.drawable.curved_rectangle));
                        }

                    } else if (data.postDetail.getType().equals(Constants.BOOK_REVIEW)) {
                        tag.setTextColor(Color.parseColor(Constants.WARNING));
                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            tag.setBackgroundDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.curved_rectangle_warning));
                        } else {
                            tag.setBackground(ContextCompat.getDrawable(mainActivity, R.drawable.curved_rectangle_warning));
                        }
                    } else if (data.postDetail.getType().equals(Constants.INTERVIEW)) {
                        tag.setTextColor(Color.parseColor(Constants.SUCCESS));
                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            tag.setBackgroundDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.curved_rectangle_success));
                        } else {
                            tag.setBackground(ContextCompat.getDrawable(mainActivity, R.drawable.curved_rectangle_success));
                        }
                    }
                }
            }


            if (data.postDetail.getHasAcceptedAnswer()) {
                //Show accepted answer..
                if (data.postDetail.getComment() != null) {
                    showSelectedAnswer(selectedAnswer, isAnswerSelected, selectedAnswerUserName);
                    Comment acceptedAnswer = data.postDetail.getComment();
                    if (acceptedAnswer.getCustomer() != null) {
                        String name = mainActivity.snaphyHelper.getName(acceptedAnswer.getCustomer().getFirstName(), acceptedAnswer.getCustomer().getLastName());
                        if (!name.isEmpty()) {
                            name = Constants.Doctor + name.replace("^[Dd][Rr]", "");
                        }
                        selectedAnswerUserName.setText(name);
                    }

                    if (data.postDetail.getComment().getAnswer() != null) {
                        selectedAnswer.setText(data.postDetail.getComment().getAnswer());
                    }

                } else {
                    ///hide accepted answer..
                    hideSelectedAnswer(selectedAnswer, isAnswerSelected, selectedAnswerUserName);
                }
            } else {
                ///hide accepted answer..
                hideSelectedAnswer(selectedAnswer, isAnswerSelected, selectedAnswerUserName);
            }


            //TOTAL LIKE
            numberOfLike.setText(String.valueOf((int) data.postDetail.getTotalLike()));
            //TOTAL SAVE..
            numberOfSave.setText(String.valueOf((int) data.postDetail.getTotalSave()));


            if (Constants.SELECTED_TAB.equals(Constants.POSTED)) {
                delete.setVisibility(View.VISIBLE);
                edit.setVisibility(View.VISIBLE);
            } else {
                delete.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
            }

            final Customer loginCustomer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
            //Customer logged in
            if (loginCustomer != null && data.post.getId() != null) {
                casePresenter.fetchTotalLike((String) loginCustomer.getId(), (String) data.post.getId(), new ObjectCallback<LikePost>() {
                    @Override
                    public void onSuccess(LikePost object) {
                        if (object != null) {
                            TrackLike trackLike = new TrackLike();
                            trackLike.likePost = object;
                            trackLike.state = true;
                            Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_LIKE).put(data.post.getId(), trackLike);
                            like.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.like_selected));
                        } else {
                            TrackLike trackLike = new TrackLike();
                            trackLike.state = false;
                            Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_LIKE).put(data.post.getId(), trackLike);
                            like.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.like_unselected));
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        Log.e(Constants.TAG, t.toString());
                        TrackLike trackLike = new TrackLike();
                        trackLike.state = false;
                        Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_LIKE).put(data.post.getId(), trackLike);
                        like.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.like_unselected));
                    }
                });


                casePresenter.fetchTotalSave((String) loginCustomer.getId(), (String) data.post.getId(), new ObjectCallback<SavePost>() {
                    @Override
                    public void onSuccess(SavePost object) {
                        if (object != null) {
                            TrackSave trackSave = new TrackSave();
                            trackSave.savePost = object;
                            trackSave.state = true;
                            Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_SAVE).put(data.post.getId(), trackSave);
                            saveCase.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.save_selected));
                        } else {
                            TrackSave trackSave = new TrackSave();
                            trackSave.state = false;
                            Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_SAVE).put(data.post.getId(), trackSave);
                            saveCase.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.save_unselected));
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(Constants.TAG, t.toString());
                        TrackSave trackSave = new TrackSave();
                        trackSave.state = false;
                        Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_SAVE).put(data.post.getId(), trackSave);
                        saveCase.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.save_unselected));
                    }
                });
            }


            likeLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_LIKE).get(data.post.getId()) == null) {
                        //Add like
                        casePresenter.addLike((String) loginCustomer.getId(), (String) data.post.getId(), new ObjectCallback<LikePost>() {
                            @Override
                            public void onBefore() {
                                TrackLike trackLike = new TrackLike();
                                trackLike.state = true;
                                showLike(data.post, like, trackLike, numberOfLike, data.postDetail);
                            }

                            @Override
                            public void onSuccess(LikePost object) {
                                TrackLike trackLike = new TrackLike();
                                trackLike.state = true;
                                trackLike.likePost = object;
                                //showLike(data.post, like, trackLike, numberOfLike, data.postDetail);
                            }

                            @Override
                            public void onError(Throwable t) {
                                TrackLike trackLike = new TrackLike();
                                trackLike.state = false;
                                showLike(data.post, like, trackLike, numberOfLike, data.postDetail);
                                //TODO add toast..
                            }
                        });
                    } else {
                        final TrackLike trackLike = (TrackLike) Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_LIKE).get(data.post.getId());
                        if (trackLike.state) {
                            trackLike.state = false;
                            //showLike(data.post, like, trackLike, numberOfLike, data.postDetail);
                            //delete like
                            casePresenter.removeLike(trackLike.likePost, new ObjectCallback<JSONObject>() {
                                @Override
                                public void onBefore() {
                                    trackLike.state = false;
                                    showLike(data.post, like, trackLike, numberOfLike, data.postDetail);
                                }

                                @Override
                                public void onSuccess(JSONObject object) {
                                    trackLike.state = false;
                                    //showLike(data.post, like, trackLike, numberOfLike, data.postDetail);
                                }

                                @Override
                                public void onError(Throwable t) {
                                    trackLike.state = true;
                                    showLike(data.post, like, trackLike, numberOfLike, data.postDetail);
                                }

                                @Override
                                public void onFinally() {
                                    super.onFinally();
                                }
                            });

                        } else {
                            //add like..
                            casePresenter.addLike((String) loginCustomer.getId(), (String) data.post.getId(), new ObjectCallback<LikePost>() {
                                @Override
                                public void onBefore() {
                                    trackLike.state = true;
                                    showLike(data.post, like, trackLike, numberOfLike, data.postDetail);
                                }

                                @Override
                                public void onSuccess(LikePost object) {
                                    trackLike.state = true;
                                    trackLike.likePost = object;
                                    //showLike(data.post, like, trackLike, numberOfLike, data.postDetail);
                                }

                                @Override
                                public void onError(Throwable t) {
                                    trackLike.state = false;
                                    showLike(data.post, like, trackLike, numberOfLike, data.postDetail);
                                    //TODO add toast..
                                }
                            });
                        }
                    }
                }
            });


            saveLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_SAVE).get(data.post.getId()) == null) {
                        //Add like
                        casePresenter.addSave((String) loginCustomer.getId(), (String) data.post.getId(), new ObjectCallback<SavePost>() {
                            @Override
                            public void onBefore() {
                                TrackSave trackSave = new TrackSave();
                                trackSave.state = true;
                                showSave(data.post, saveCase, trackSave, numberOfSave, data.postDetail);
                            }

                            @Override
                            public void onSuccess(SavePost object) {
                                TrackSave trackSave = new TrackSave();
                                trackSave.state = true;
                                trackSave.savePost = object;
                                //showSave(data.post, saveCase, trackSave, numberOfSave, data.postDetail);
                            }

                            @Override
                            public void onError(Throwable t) {
                                TrackSave trackSave = new TrackSave();
                                trackSave.state = false;
                                showSave(data.post, saveCase, trackSave, numberOfSave, data.postDetail);
                                //TODO add toast..
                            }
                        });
                    } else {
                        final TrackSave trackSave = (TrackSave) Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_SAVE).get(data.post.getId());
                        if (trackSave.state) {
                            trackSave.state = false;
                            //showSave(data.post, saveCase, trackSave, numberOfSave, data.postDetail);
                            //delete like
                            casePresenter.removeSave(trackSave.savePost, new ObjectCallback<JSONObject>() {
                                @Override
                                public void onBefore() {
                                    trackSave.state = false;
                                    showSave(data.post, saveCase, trackSave, numberOfSave, data.postDetail);
                                }

                                @Override
                                public void onSuccess(JSONObject object) {
                                    trackSave.state = false;
                                    //showSave(data.post, saveCase, trackSave, numberOfSave, data.postDetail);
                                }

                                @Override
                                public void onError(Throwable t) {
                                    trackSave.state = true;
                                    showSave(data.post, saveCase, trackSave, numberOfSave, data.postDetail);
                                }

                                @Override
                                public void onFinally() {
                                    super.onFinally();
                                }
                            });

                        } else {
                            //add like..
                            casePresenter.addSave((String) loginCustomer.getId(), (String) data.post.getId(), new ObjectCallback<SavePost>() {
                                @Override
                                public void onBefore() {
                                    trackSave.state = true;
                                    showSave(data.post, saveCase, trackSave, numberOfSave, data.postDetail);
                                }

                                @Override
                                public void onSuccess(SavePost object) {
                                    trackSave.state = true;
                                    trackSave.savePost = object;
                                    //showSave(data.post, saveCase, trackSave, numberOfSave, data.postDetail);
                                }

                                @Override
                                public void onError(Throwable t) {
                                    trackSave.state = false;
                                    showSave(data.post, saveCase, trackSave, numberOfSave, data.postDetail);
                                    //TODO add toast..
                                }
                            });
                        }
                    }
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
        }//if data != null

    }

    public String parseLikeAndSave(int number) {
        if(number >= 1000) {
            if(number == 1000) {
                String convertNumber = 1+"k";
                return convertNumber;
            } else {
                double convertNumber = number/1000;
                return convertNumber+"k";
            }
        } else {
            return number+"";
        }
    }



    public void showLike(Post post, ImageView like, TrackLike trackLike, TextView numberOfLike, PostDetail postDetail){
        if(trackLike != null){
            if(trackLike.state){
                Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_LIKE).put(post.getId(), trackLike);
                like.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.like_selected));
                postDetail.setTotalLike(postDetail.getTotalLike() + 1);
                String parsedLike = parseLikeAndSave((int)postDetail.getTotalLike());
                numberOfLike.setText(parsedLike);
            }else{
                Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_LIKE).put(post.getId(), trackLike);
                like.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.like_unselected));
                if(postDetail.getTotalLike() == 0){

                } else {
                    postDetail.setTotalLike(postDetail.getTotalLike() - 1);
                }
                String parsedLike = parseLikeAndSave((int)postDetail.getTotalLike());
                numberOfLike.setText(parsedLike);
            }
        }
    }

    public void showSave(Post post, ImageView saveCase, TrackSave trackSave, TextView numberOfSave, PostDetail postDetail){
        if(trackSave != null){
            if(trackSave.state){
                Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_SAVE).put(post.getId(), trackSave);
                saveCase.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.save_selected));
                postDetail.setTotalSave(postDetail.getTotalSave() + 1);
                String parsedSave = parseLikeAndSave((int)postDetail.getTotalSave());
                numberOfSave.setText(parsedSave);
            }else{
                Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_SAVE).put(post.getId(), trackSave);
                saveCase.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.save_unselected));
                if(postDetail.getTotalSave() == 0) {

                } else {
                    postDetail.setTotalSave(postDetail.getTotalSave() - 1);
                }
                String parsedSave = parseLikeAndSave((int)postDetail.getTotalSave());
                numberOfSave.setText(parsedSave);
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
        String parseDate = mainActivity.parseDate(date);
        casePostedTime.setText(parseDate + " ago");
        //PARSE The javascript format date first..
        //casePostedTime.setText(caseModel.getPostTime());
    }




    @Override
    public int getItemCount() {
        TrackList trackListItem = trackList.get(Constants.SELECTED_TAB);
        if(Constants.SELECTED_TAB.equals(Constants.TRENDING)
                || Constants.SELECTED_TAB.equals(Constants.LATEST)
                || Constants.SELECTED_TAB.equals(Constants.UNSOLVED)){
            return trackListItem.getPostDetails().size();
        }else{
            return trackListItem.getPostDataList().size();
        }
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
        @Bind(R.id.layout_case_list_button2) TextView deleteButton;
        @Bind(R.id.layout_case_list_button1) TextView editButton;
        @Bind(R.id.layout_case_list_textview8) TextView numberOfLikes;
        @Bind(R.id.layout_case_list_textview9) TextView numberOfSave;
        @Bind(R.id.layout_case_list_linear_layout) LinearLayout linearLayout;
        @Bind(R.id.layout_case_list_linear_layout_like) LinearLayout likeLinearLayout;
        @Bind(R.id.layout_case_list_linear_layout_save) LinearLayout saveLinearLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
