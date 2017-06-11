package com.orthopg.snaphy.orthopg.Fragment.CaseFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CustomerRepository;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.PostRepository;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.CustomModel.TrackList;
import com.orthopg.snaphy.orthopg.Fragment.ProfileFragment.ProfileFragment;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;
import com.orthopg.snaphy.orthopg.RecyclerItemClickListener;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ravi-Gupta on 9/21/2016.
 */
public class CaseListAdapter extends RecyclerView.Adapter<CaseListAdapter.ViewHolder> {

    MainActivity mainActivity;
    HashMap<String, TrackList> trackList;
    String TAG;
    CasePresenter casePresenter;
    int casePosition;
    Post post;

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
        public Customer customer;

        //Constructor.
        public Data(TrackList trackListItem, int position ){
            this.trackListItem = trackListItem;

            if(Constants.SELECTED_TAB.equals(Constants.TRENDING)
                    || Constants.SELECTED_TAB.equals(Constants.LATEST)
                    || Constants.SELECTED_TAB.equals(Constants.UNSOLVED)){
                    postDetail  = trackListItem.getPostDetails().get(position);
                    if(postDetail != null){
                        post = postDetail.getPost();
                        customer = postDetail.getPost().getCustomer();
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

    //TODO: Remove final and handle flickering..
    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //get the list..
        final TrackList trackListItem = trackList.get(Constants.SELECTED_TAB);

        Typeface font = Typeface.createFromAsset(mainActivity.getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface font_bold = Typeface.createFromAsset(mainActivity.getAssets(), "fonts/OpenSans-Bold.ttf");

        if(trackListItem != null) {
            final Data data = new Data(trackListItem, position);

            if (data.postDetail == null || data.post == null) {
                Log.e(Constants.TAG, "CaseListAdapter returning postDetail or post obj is null");
                return;
            }

            CaseImageAdapter caseImageAdapter;

            ImageView imageView = holder.userImage;
            final TextView caseHeading = holder.caseHeading;
            TextView userName = holder.userName;
            TextView casePostedTime = holder.casePostedTime;
            final ImageView like = holder.like;
            final ImageView saveCase = holder.saveCase;
            RecyclerView caseImages = holder.caseImages;
            final TextView caseDescription = holder.caseDescription;
            TextView tag = holder.tag;
            final TextView delete = holder.deleteButton;
            final TextView edit = holder.editButton;
            RelativeLayout isAnswerSelected = holder.isAnswerSelected;
            TextView selectedAnswerUserName = holder.selectedAnswerUserName;
            TextView selectedAnswer = holder.selectedAnswer;
            final TextView numberOfLike = holder.numberOfLikes;
            final TextView numberOfSave = holder.numberOfSave;
            final TextView numberOfAnswers = holder.numberOfAnswers;
            final LinearLayout likeLinearLayout = holder.likeLinearLayout;
            final LinearLayout saveLinearLayout = holder.saveLinearLayout;
            LinearLayout caseContainer = holder.caseContainer;
            LinearLayout contentContainer = holder.contentContainer;
            final LinearLayout myAnswerContainer = holder.myAnswerContainer;
            LinearLayout caseNameContainer = holder.caseNameContainer;
            RelativeLayout acceptedContainer = holder.acceptContainer;
            RelativeLayout numberOfAnswersLayout = holder.numberOfAnswersLayout;
            View view = holder.view;

            caseImages.setLayoutManager(new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false));
            casePosition = position;
            if (!data.post.getHeading().isEmpty()) {
                caseHeading.setTypeface(font_bold);
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
                    caseDescription.setTypeface(font);
                    caseDescription.setText(data.post.getDescription());
                    caseDescription.post(new Runnable() {
                        @Override
                        public void run() {
                            int lineCnt = caseDescription.getLineCount();
                            // Perform any actions you want based on the line count here.
                            if(lineCnt < 3) {

                            } else {
                                caseDescription.setText(data.post.getDescription() + "...");
                            }
                        }
                    });
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
                    showSelectedAnswer(view, selectedAnswer, isAnswerSelected, selectedAnswerUserName);
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
                    hideSelectedAnswer(view, selectedAnswer, isAnswerSelected, selectedAnswerUserName);
                }
            } else {
                ///hide accepted answer..
                hideSelectedAnswer(view, selectedAnswer, isAnswerSelected, selectedAnswerUserName);
            }


            //TOTAL LIKE
            numberOfLike.setText(String.valueOf((int) data.postDetail.getTotalLike()));
            //TOTAL SAVE..
            numberOfSave.setText(String.valueOf((int) data.postDetail.getTotalSave()));
            // TOTAL ANSWERS
            numberOfAnswers.setText(String.valueOf((int)data.postDetail.getTotalAnswer()));


            if (Constants.SELECTED_TAB.equals(Constants.POSTED)) {
                delete.setVisibility(View.VISIBLE);
                edit.setVisibility(View.VISIBLE);
                numberOfAnswersLayout.setVisibility(View.GONE);
            } else {
                delete.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
                numberOfAnswersLayout.setVisibility(View.VISIBLE);

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

            myAnswerContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //No network Connection
                    if(!mainActivity.snaphyHelper.isNetworkAvailable()){
                        Snackbar.make(myAnswerContainer,"No Network Connection! Check Internet Connection and try again", Snackbar.LENGTH_SHORT).show();
                    } else {
                        if (data.post != null) {
                            //Prepare the data..
                            Presenter.getInstance().addModel(Constants.EDIT_IN_PROCESS_COMMENT_POST_MODEL, data.post);
                        }
                        Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
                        if (customer != null) {
                            final String MCINumber = customer.getMciNumber() != null ? customer.getMciNumber() : "";
                            if (customer.getStatus() != null) {
                                if (customer.getStatus().equals(Constants.ALLOW)) {
                                    mainActivity.replaceFragment(R.id.layout_case_list_linear_layout_my_answer, position);
                                } else {
                                    String mciNumber = customer.getMciNumber();
                                    if(mciNumber != null){
                                        if(mciNumber.isEmpty()) {
                                            TastyToast.makeText(mainActivity.getApplicationContext(), "Complete Your Profile", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
                                            Snackbar snackbar = Snackbar.make(caseHeading, Constants.MCI_VERIFICATION_TAG, Snackbar.LENGTH_SHORT);
                                            snackbar
                                                    .setAction("Profile", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            mainActivity.replaceFragment(R.layout.fragment_doctor_profile, ProfileFragment.TAG);
                                                        }
                                                    })
                                                    .show();
                                        } else {
                                            TastyToast.makeText(mainActivity.getApplicationContext(), "Verification is under process", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
                                        }
                                    } else {
                                        TastyToast.makeText(mainActivity.getApplicationContext(), "Complete Your Profile", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
                                        Snackbar snackbar = Snackbar.make(caseHeading, Constants.MCI_VERIFICATION_TAG, Snackbar.LENGTH_SHORT);
                                        snackbar
                                                .setAction("Profile", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        mainActivity.replaceFragment(R.layout.fragment_doctor_profile, ProfileFragment.TAG);
                                                    }
                                                })
                                                .show();
                                    }
                                }
                            }

                        }
                    }
                }
            });

            likeLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //No Network Connection
                    if(!mainActivity.snaphyHelper.isNetworkAvailable()){
                        Snackbar.make(likeLinearLayout,"No Network Connection! Check Internet Connection and try again", Snackbar.LENGTH_SHORT).show();
                    } else {
                        Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
                        if (customer != null) {
                            final String MCINumber = customer.getMciNumber() != null ? customer.getMciNumber() : "";
                            if (customer.getStatus() != null) {
                                if (customer.getStatus().equals(Constants.ALLOW)) {

                                    if (Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_LIKE).get(data.post.getId()) == null) {
                                        final TrackLike trackLike = new TrackLike();
                                        //Add like
                                        casePresenter.addLike((String) loginCustomer.getId(), (String) data.post.getId(), new ObjectCallback<LikePost>() {
                                            @Override
                                            public void onBefore() {
                                                trackLike.state = true;
                                                showLike(data.post, like, trackLike, numberOfLike, data.postDetail);
                                            }

                                            @Override
                                            public void onSuccess(LikePost object) {
                                                trackLike.likePost = object;
                                            }

                                            @Override
                                            public void onError(Throwable t) {
                                                trackLike.state = false;
                                                showLike(data.post, like, trackLike, numberOfLike, data.postDetail);
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
                                                    trackLike.likePost = null;
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
                                                    trackLike.likePost = object;
                                                }

                                                @Override
                                                public void onError(Throwable t) {
                                                    trackLike.state = false;
                                                    showLike(data.post, like, trackLike, numberOfLike, data.postDetail);
                                                }
                                            });
                                        }
                                    }
                                } else {
                                    String mciNumber = customer.getMciNumber();
                                    if(mciNumber != null){
                                        if(mciNumber.isEmpty()) {
                                            TastyToast.makeText(mainActivity.getApplicationContext(), "Complete Your Profile", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
                                            Snackbar snackbar = Snackbar.make(caseHeading, Constants.MCI_VERIFICATION_TAG, Snackbar.LENGTH_SHORT);
                                            snackbar
                                                    .setAction("Profile", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            mainActivity.replaceFragment(R.layout.fragment_doctor_profile, ProfileFragment.TAG);
                                                        }
                                                    })
                                                    .show();
                                        } else {
                                            TastyToast.makeText(mainActivity.getApplicationContext(), "Verification is under process", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
                                        }
                                    } else {
                                        TastyToast.makeText(mainActivity.getApplicationContext(), "Complete Your Profile", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
                                        Snackbar snackbar = Snackbar.make(caseHeading, Constants.MCI_VERIFICATION_TAG, Snackbar.LENGTH_SHORT);
                                        snackbar
                                                .setAction("Profile", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        mainActivity.replaceFragment(R.layout.fragment_doctor_profile, ProfileFragment.TAG);
                                                    }
                                                })
                                                .show();
                                    }

                                }
                            }

                        }
                    }
                }
            });




            saveLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mainActivity.snaphyHelper.isNetworkAvailable()) {
                        Snackbar.make(saveLinearLayout, "No Network Connection! Check Internet Connection and try again", Snackbar.LENGTH_SHORT).show();
                    } else {
                        Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
                        if (customer != null) {
                            final String MCINumber = customer.getMciNumber() != null ? customer.getMciNumber() : "";
                            if (customer.getStatus() != null) {
                                if (customer.getStatus().equals(Constants.ALLOW)) {
                                    if (Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_SAVE).get(data.post.getId()) == null) {
                                        final TrackSave trackSave = new TrackSave();
                                        //Add like
                                        casePresenter.addSave((String) loginCustomer.getId(), (String) data.post.getId(), new ObjectCallback<SavePost>() {
                                            @Override
                                            public void onBefore() {
                                                trackSave.state = true;
                                                showSave(data.post, saveCase, trackSave, numberOfSave, data.postDetail);
                                            }

                                            @Override
                                            public void onSuccess(SavePost object) {
                                                trackSave.savePost = object;
                                            }

                                            @Override
                                            public void onError(Throwable t) {
                                                Log.e(Constants.TAG, t.toString());
                                                trackSave.state = false;
                                                showSave(data.post, saveCase, trackSave, numberOfSave, data.postDetail);
                                            }
                                        });
                                    } else {
                                        final TrackSave trackSave = (TrackSave) Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_SAVE).get(data.post.getId());
                                        if (trackSave.state) {
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
                                                    Log.e(Constants.TAG, t.toString());
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
                                                    trackSave.savePost = object;
                                                }

                                                @Override
                                                public void onError(Throwable t) {
                                                    trackSave.state = false;
                                                    showSave(data.post, saveCase, trackSave, numberOfSave, data.postDetail);
                                                }
                                            });
                                        }
                                    }
                                } else {
                                    String mciNumber = customer.getMciNumber();
                                    if(mciNumber != null){
                                        if(mciNumber.isEmpty()) {
                                            TastyToast.makeText(mainActivity.getApplicationContext(), "Complete Your Profile", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
                                            Snackbar snackbar = Snackbar.make(caseHeading, Constants.MCI_VERIFICATION_TAG, Snackbar.LENGTH_SHORT);
                                            snackbar
                                                    .setAction("Profile", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            mainActivity.replaceFragment(R.layout.fragment_doctor_profile, ProfileFragment.TAG);
                                                        }
                                                    })
                                                    .show();
                                        } else {
                                            TastyToast.makeText(mainActivity.getApplicationContext(), "Verification is under process", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
                                        }
                                    } else {
                                        TastyToast.makeText(mainActivity.getApplicationContext(), "Complete Your Profile", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
                                        Snackbar snackbar = Snackbar.make(caseHeading, Constants.MCI_VERIFICATION_TAG, Snackbar.LENGTH_SHORT);
                                        snackbar
                                                .setAction("Profile", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        mainActivity.replaceFragment(R.layout.fragment_doctor_profile, ProfileFragment.TAG);
                                                    }
                                                })
                                                .show();
                                    }
                                }
                            }
                        }
                    }
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //remove the data..
                    if(mainActivity.snaphyHelper.isNetworkAvailable()) {
                        showDeleteDialog(data, trackListItem);
                    } else{
                        Snackbar.make(delete,"Cannot Delete! No Network Connection", Snackbar.LENGTH_SHORT).show();
                    }

                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //No Network Connection
                    if(!mainActivity.snaphyHelper.isNetworkAvailable()){
                        Snackbar.make(edit,"No Network Connection! Check Internet Connection and try again", Snackbar.LENGTH_SHORT);
                    }else {
                        if (data.post != null) {
                            //Add post to edit data..prepare for edit..
                            casePresenter.InitNewCaseObject(data.post);
                            mainActivity.replaceFragment(R.id.fragment_case_button4, null);
                        }
                    }
                }
            });


          /*  imageView.setOnClickListener(this);
            userName.setOnClickListener(this);
            caseHeading.setOnClickListener(this);
            caseContainer.setOnClickListener(this);
            contentContainer.setOnClickListener(this);
            acceptedContainer.setOnClickListener(this);
            caseNameContainer.setOnClickListener(this);*/

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openProfileFragment(data);
                }
            });

            userName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openProfileFragment(data);
                }
            });
            caseHeading.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCaseFragment(position, caseHeading);
                }
            });

            caseContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCaseFragment(position, caseHeading);
                }
            });

            contentContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCaseFragment(position, caseHeading);
                }
            });

            caseImages.addOnItemTouchListener(new RecyclerItemClickListener(mainActivity, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int myposition) {
                   openCaseFragment(position, caseHeading);
                }
            }));


            acceptedContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCaseFragment(position, caseHeading);
                }
            });

            caseNameContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCaseFragment(position, caseHeading);
                }
            });


        }//if data != null

    }

    //dialog for deleting the case

    public void showDeleteDialog(final Data data, final TrackList trackListItem){
        TextView tv = new TextView(mainActivity);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(0, 50, 0, 0);
        tv.setTextColor(Color.BLACK);
        tv.setText("Do you really want to remove the case?");
        new AlertDialog.Builder(mainActivity).setCustomTitle(tv).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataList<Post> posts = trackListItem.getPostDataList();
                if(posts != null){
                    if(data.post != null){
                        //remove the post item from list..
                        posts.remove(data.post);

                    }
                }
            }
        }).setNegativeButton(android.R.string.no, null).show();
    }

  public void openProfileFragment(Data data){
      setCustomerData(data);
  }

  public void openCaseFragment(int position, TextView caseHeading){
      if (!mainActivity.snaphyHelper.isNetworkAvailable()) {
          Toast.makeText(mainActivity, "Internet Not Connected", Toast.LENGTH_SHORT).show();
      } else {
          Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
          if (customer != null) {
              if (customer.getStatus() != null) {
                  if (customer.getStatus().equals(Constants.ALLOW)) {
                      mainActivity.replaceFragment(R.id.layout_case_list_textview4, position);
                  }  else {

                      String mciNumber = customer.getMciNumber();
                      if(mciNumber != null){
                          if(mciNumber.isEmpty()) {
                              TastyToast.makeText(mainActivity.getApplicationContext(), "Complete Your Profile", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
                              Snackbar snackbar = Snackbar.make(caseHeading, Constants.MCI_VERIFICATION_TAG, Snackbar.LENGTH_SHORT);
                              snackbar
                                      .setAction("Profile", new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              mainActivity.replaceFragment(R.layout.fragment_doctor_profile, ProfileFragment.TAG);
                                          }
                                      })
                                      .show();
                          } else {
                              TastyToast.makeText(mainActivity.getApplicationContext(), "Verification is under process", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
                          }
                      } else {
                          TastyToast.makeText(mainActivity.getApplicationContext(), "Complete Your Profile", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
                          Snackbar snackbar = Snackbar.make(caseHeading, Constants.MCI_VERIFICATION_TAG, Snackbar.LENGTH_SHORT);
                          snackbar
                                  .setAction("Profile", new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          mainActivity.replaceFragment(R.layout.fragment_doctor_profile, ProfileFragment.TAG);
                                      }
                                  })
                                  .show();
                      }
                  }
              } else {
                  String mciNumber = customer.getMciNumber();
                  if(mciNumber != null){
                      if(mciNumber.isEmpty()) {
                          TastyToast.makeText(mainActivity.getApplicationContext(), "Complete Your Profile", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
                          Snackbar snackbar = Snackbar.make(caseHeading, Constants.MCI_VERIFICATION_TAG, Snackbar.LENGTH_SHORT);
                          snackbar
                                  .setAction("Profile", new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          mainActivity.replaceFragment(R.layout.fragment_doctor_profile, ProfileFragment.TAG);
                                      }
                                  })
                                  .show();
                      } else {
                          TastyToast.makeText(mainActivity.getApplicationContext(), "Verification is under process", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
                      }
                  } else {
                      TastyToast.makeText(mainActivity.getApplicationContext(), "Complete Your Profile", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
                      Snackbar snackbar = Snackbar.make(caseHeading, Constants.MCI_VERIFICATION_TAG, Snackbar.LENGTH_SHORT);
                      snackbar
                              .setAction("Profile", new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      mainActivity.replaceFragment(R.layout.fragment_doctor_profile, ProfileFragment.TAG);
                                  }
                              })
                              .show();
                  }
              }
          }
      }
  }

    public void setCustomerData(Data data){
        if(data.post.getAnonymous()){

        } else{
            if(data.customer != null) {
                HashMap<String, Object> filter = new HashMap<>();
                CustomerRepository customerRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(CustomerRepository.class);
                Customer customer = customerRepository.createObject(filter);
                customer.setId(data.customer.getId());
                customer.setEmail(data.customer.getEmail());
                customer.setFirstName(data.post.getCustomer().getFirstName());
                customer.setLastName(data.post.getCustomer().getLastName());
                getOtherProfileData(data.post.getCustomer(), customer);
            }

        }
    }

    public void getOtherProfileData(final Customer customer, final Customer customer1){
        final String customerId = String.valueOf(customer.getId());
        HashMap<String, Object> filter = new HashMap<>();
        final CustomerRepository customerRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(CustomerRepository.class);
        customerRepository.addStorage(mainActivity);
        if(mainActivity.snaphyHelper.isNetworkAvailable()) {
            customerRepository.findById(customerId, filter, new ObjectCallback<Customer>() {
                @Override
                public void onSuccess(Customer object) {
                    super.onSuccess(object);
                    getAndSetCustomerData(customer1, object);
                    customerRepository.getDb().upsert__db(customerId, object);
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    Log.e(Constants.TAG, t.toString());
                }
            });
        } else{
            Customer customer2 = customerRepository.getDb().get__db(customerId);
            if(customer2!=null){
                getAndSetCustomerData(customer2, customer1);
            }
        }
    }

    public void getAndSetCustomerData(Customer customer, Customer customerData){
        if(customerData!=null) {

            if(customerData.getProfilePic()!=null){
                customer.setProfilePic(customerData.getProfilePic());
            }

            if (customerData.getEmail() != null) {
                if (!customerData.getEmail().isEmpty()) {
                    customer.setEmail(customerData.getEmail());
                }
            }
            if (customerData.getCurrentCity() != null) {
                if (!customerData.getCurrentCity().isEmpty()) {
                    customer.setCurrentCity(customerData.getCurrentCity());
                }
            }

            if (customerData.getMciNumber() != null) {
                if (!customerData.getMciNumber().isEmpty()) {
                    customer.setMciNumber(customerData.getMciNumber());
                }
            }

            if (String.valueOf(customerData.getWorkExperience()) != null) {
                if (!String.valueOf(customerData.getWorkExperience()).isEmpty()) {
                    customer.setWorkExperience(customerData.getWorkExperience());
                }
            }

            if (customerData.getSpecialities() != null) {
                if (customerData.getSpecialities().size() != 0) {
                    customer.setSpecialities(customerData.getSpecialities());
                }
            }

            if (customerData.getQualifications() != null) {
                if (customerData.getQualifications().size() != 0) {
                    customer.setQualifications(customerData.getQualifications());
                }
            }

            Presenter.getInstance().addModel(Constants.CASE_PROFILE_DATA, customer);
            mainActivity.replaceFragment(R.layout.fragment_doctor_profile, CaseFragment.TAG);
        }
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
                like.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.like_selected));
                postDetail.setTotalLike(postDetail.getTotalLike() + 1);
                String parsedLike = parseLikeAndSave((int)postDetail.getTotalLike());
                numberOfLike.setText(parsedLike);
                Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_LIKE).put(post.getId(), trackLike);
            }else{
                trackLike.likePost = null;
               like.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.like_unselected));
                if(postDetail.getTotalLike() == 0){
                } else {
                    postDetail.setTotalLike(postDetail.getTotalLike() - 1);
                }
                String parsedLike = parseLikeAndSave((int)postDetail.getTotalLike());
                Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_LIKE).put(post.getId(), trackLike);
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
                saveCase.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.save_unselected));
                trackSave.savePost = null;
                if(postDetail.getTotalSave() == 0) {
                } else {
                    postDetail.setTotalSave(postDetail.getTotalSave() - 1);
                }
                String parsedSave = parseLikeAndSave((int)postDetail.getTotalSave());
                numberOfSave.setText(parsedSave);
                Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_SAVE).put(post.getId(), trackSave);
            }
        }
    }



    @OnClick(R.id.layout_case_list_image) void onProfile(){
        mainActivity.replaceFragment(R.layout.fragment_other_profile, null);
    }

    @OnClick(R.id.layout_case_list_textview2) void onProfileClick(){
        mainActivity.replaceFragment(R.layout.fragment_other_profile, null);
    }


    public void hideSelectedAnswer(View view, TextView selectedAnswer, RelativeLayout grenTick, TextView userName){
        selectedAnswer.setVisibility(View.GONE);
        grenTick.setVisibility(View.GONE);
        userName.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
    }

    public void showSelectedAnswer(View view, TextView selectedAnswer, RelativeLayout grenTick, TextView userName){
        selectedAnswer.setVisibility(View.VISIBLE);
        grenTick.setVisibility(View.VISIBLE);
        userName.setVisibility(View.VISIBLE);
        view.setVisibility(View.VISIBLE);
    }


    public void setTime(TextView casePostedTime, String date){
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
        @Bind(R.id.layout_case_list_imageview1) RelativeLayout isAnswerSelected;
        @Bind(R.id.layout_case_list_textview6) TextView selectedAnswerUserName;
        @Bind(R.id.layout_case_list_textview7) TextView selectedAnswer;
        @Bind(R.id.layout_case_list_button2) TextView deleteButton;
        @Bind(R.id.layout_case_list_button1) TextView editButton;
        @Bind(R.id.layout_case_list_textview8) TextView numberOfLikes;
        @Bind(R.id.layout_case_list_textview9) TextView numberOfSave;
        @Bind(R.id.layout_case_list_textview10) TextView numberOfAnswers;
        @Bind(R.id.layout_case_list_reltive_layout2) RelativeLayout numberOfAnswersLayout;

        @Bind(R.id.layout_case_list_linear_layout) LinearLayout linearLayout;
        @Bind(R.id.layout_case_list_linear_layout2) LinearLayout linearLayout2;
        @Bind(R.id.layout_case_list_linear_layout_like) LinearLayout likeLinearLayout;
        @Bind(R.id.layout_case_list_linear_layout_save) LinearLayout saveLinearLayout;
        @Bind(R.id.layout_case_list_linear_layout3) LinearLayout caseContainer;
        @Bind(R.id.layout_case_list_linear_layout4) LinearLayout contentContainer;
        @Bind(R.id.layout_case_list_linear_layout5) LinearLayout caseNameContainer;
        @Bind(R.id.layout_case_list_linear_layout_my_answer) LinearLayout myAnswerContainer;
        @Bind(R.id.layout_case_list_reltive_layout1) RelativeLayout acceptContainer;
        @Bind(R.id.layout_case_list_view2) View view;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
