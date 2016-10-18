package com.orthopg.snaphy.orthopg.Fragment.CaseDetailFragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.ObjectCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.list.Listen;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Comment;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.models.LikePost;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Post;
import com.androidsdk.snaphy.snaphyandroidsdk.models.PostDetail;
import com.androidsdk.snaphy.snaphyandroidsdk.models.SavePost;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CommentRepository;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.CustomModel.CommentState;
import com.orthopg.snaphy.orthopg.CustomModel.TrackList;
import com.orthopg.snaphy.orthopg.Fragment.CaseFragment.CaseImageAdapter;
import com.orthopg.snaphy.orthopg.Fragment.CaseFragment.CasePresenter;
import com.orthopg.snaphy.orthopg.Fragment.CaseFragment.TrackLike;
import com.orthopg.snaphy.orthopg.Fragment.CaseFragment.TrackSave;
import com.orthopg.snaphy.orthopg.ImageZoom.ImageZoomDialog;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;
import com.orthopg.snaphy.orthopg.RecyclerItemClickListener;
import com.sdsmdg.tastytoast.TastyToast;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.attr.name;
import static android.media.CamcorderProfile.get;
import static com.orthopg.snaphy.orthopg.R.mipmap.like;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CaseDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CaseDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CaseDetailFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    public static String TAG = "CaseDetailFragment";
    @Bind(R.id.layout_case_detail_recycler_view) RecyclerView imageRecyclerView;
    @Bind(R.id.layout_case_detail_recycler_view2) RecyclerView commentsRecyclerView;
    @Bind(R.id.fragment_case_detail_button4) Button answerButton;
    @Bind(R.id.layout_case_detail_imagebutton1) ImageView saveButton;
    @Bind(R.id.layout_case_detail_imagebutton2) ImageView likeButton;
    @Bind(R.id.layout_case_detail_textview1) TextView caseHeading;
    @Bind(R.id.layout_case_details_textview2) TextView userName;
    @Bind(R.id.layout_case_details_textview3) TextView time;
    @Bind(R.id.layout_case_details_textview4) TextView tag;
    @Bind(R.id.layout_case_details_textview5) TextView description;
    @Bind(R.id.layout_case_detail_imageview1) CircleImageView profilePic;
    @Bind(R.id.layout_case_details_textview6) TextView numberOfSave;
    @Bind(R.id.layout_case_details_textview7) TextView numberOfLike;
    @Bind(R.id.layout_case_details_textview8) TextView selectedAnswerUserName;
    @Bind(R.id.layout_case_details_textview9) TextView selectedAnswer;
    @Bind(R.id.layout_case_details_imageview1) ImageView isAnswerSelected;
    @Bind(R.id.fragment_case_detail_linearLayout1) LinearLayout saveLinearLayout;
    @Bind(R.id.fragment_case_detail_linearLayout2) LinearLayout likeLinearLayout;
    @Bind(R.id.fragment_case_progressBar) CircleProgressBar progressBar;
    boolean isLiked = false;
    boolean isSaved = false;
    MainActivity mainActivity;
    Post post;
    CaseImageAdapter caseImageAdapter;
    //List<CommentModel> commentModelList = new ArrayList<>();
    CaseDetailFragmentCommentAdapter caseDetailFragmentCommentAdapter;
    int position;
    PostDetail postDetail;
    DataList<PostDetail> getPostDataList;
    DataList<Post> postList;
    CasePresenter casePresenter;
    CaseDetailPresenter caseDetailPresenter;
    Customer loginCustomer;
    HashMap<String, CommentState> commentStateDataList;
    DataList<String> exceptIdNewAnswerList;

    LinearLayoutManager linearLayoutManager;
    /*Infinite Loading dataset*/
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 2;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    /*Infinite Loading data set*/

    public CaseDetailFragment() {
        // Required empty public constructor
    }

    public static CaseDetailFragment newInstance() {
        CaseDetailFragment fragment = new CaseDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        position = bundle.getInt("position");
        exceptIdNewAnswerList = new DataList<>();
        Presenter.getInstance().addModel(Constants.EXCEPTED_NEW_ANSWER_LIST, exceptIdNewAnswerList);
        //Remove the in edit data..
        if(Presenter.getInstance().getModel(Post.class, Constants.EDIT_IN_PROCESS_COMMENT_POST_MODEL) != null){
            //Remove the data..
            Presenter.getInstance().removeModelFromList(Constants.EDIT_IN_PROCESS_COMMENT_POST_MODEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_case_detail, container, false);
        ButterKnife.bind(this, view);
        mainActivity.stopProgressBar(progressBar);
        //Add progress bar..
        caseDetailPresenter = new CaseDetailPresenter(mainActivity.snaphyHelper.getLoopBackAdapter(), progressBar, mainActivity, post, position);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        imageRecyclerView.setLayoutManager(new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false));
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //Adding a list for tracking accepted answer datalist..
        commentStateDataList = new HashMap<>();

        likeButtonClickListener();
        saveButtonClickListener();
        loadPostData(position);
        loadComments();
        //Add load more..
        recyclerViewLoadMoreEventData();

        //addOnItemTouchListener
        imageRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(mainActivity, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if(post.getPostImages() != null ){
                            if(post.getPostImages().size() != 0) {
                                Map<String, Object> drawableObj = post.getPostImages().get(position);
                                ImageZoomDialog imageZoomDialog = new ImageZoomDialog();
                                Presenter.getInstance().addModel(Constants.ZOOM_IMAGE_ID, drawableObj);
                                imageZoomDialog.show(mainActivity.getFragmentManager(), ImageZoomDialog.TAG);
                            }
                        }
                    }
                })
        );
        return view;
    }

    public void loadPostData(int position){
        //casePresenter = new CasePresenter(mainActivity.snaphyHelper.getLoopBackAdapter(), progressBar, mainActivity);
        if(Presenter.getInstance().getModel(HashMap.class, Constants.LIST_CASE_FRAGMENT) != null){
            HashMap<String, TrackList> list = Presenter.getInstance().getModel(HashMap.class, Constants.LIST_CASE_FRAGMENT);
            try{
                if(list != null){
                    TrackList trackListItem = list.get(Constants.SELECTED_TAB);
                    if(Constants.SELECTED_TAB.equals(Constants.TRENDING)
                            || Constants.SELECTED_TAB.equals(Constants.LATEST)
                            || Constants.SELECTED_TAB.equals(Constants.UNSOLVED)){
                        getPostDataList = trackListItem.getPostDetails();
                        getPostDataList = trackListItem.getPostDetails();
                        postDetail  = trackListItem.getPostDetails().get(position);
                        if(postDetail != null){
                            post = postDetail.getPost();
                        }
                    }else{
                        if(trackListItem.getPostDataList() != null){
                            if(trackListItem.getPostDataList().size() != 0){
                                post  = trackListItem.getPostDataList().get(position);
                                postList = trackListItem.getPostDataList();
                                postDetail = post.getPostDetails();
                            }
                        }
                    }
                }
            }
            catch (java.lang.IndexOutOfBoundsException e){
                Log.e(Constants.TAG, e.toString());
                return;
            }


        }
        loadPost();
    }

    /*Notify Case Fragment about change..*/
    public void notifyCaseFragmentChange(){
        if(Constants.SELECTED_TAB.equals(Constants.TRENDING)
                || Constants.SELECTED_TAB.equals(Constants.LATEST)
                || Constants.SELECTED_TAB.equals(Constants.UNSOLVED)){

            if(getPostDataList != null){
                getPostDataList.publishOnChange();
            }
        }else{
            if(postList != null){
                postList.publishOnChange();
            }
        }
    }

    public void loadPost(){
        if(post == null){
            return;
        }
        loginCustomer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
        casePresenter = Presenter.getInstance().getModel(CasePresenter.class, Constants.CASE_PRESENTER_ID);

        caseHeading.setText(post.getHeading());
        String name1 = mainActivity.snaphyHelper.getName(post.getCustomer().getFirstName(), post.getCustomer().getLastName());
        if(!name1.isEmpty()){
            name1 = Constants.Doctor + name1.replace("^[Dd][Rr]", "");
        }

        userName.setText(name1);

        setTime(time, postDetail.getAdded());

        if(post.getCustomer() != null){
            Customer customer = post.getCustomer();
            if(customer.getProfilePic() != null){
                mainActivity.snaphyHelper.loadUnSignedThumbnailImage(customer.getProfilePic(), profilePic, R.mipmap.anonymous);
            }else{
                profilePic.setImageResource(R.mipmap.anonymous);
            }
        }

        if(!postDetail.getType().isEmpty()) {
            final int sdk = android.os.Build.VERSION.SDK_INT;
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



        if(post.getDescription() != null) {
            if (!post.getDescription().isEmpty()) {
                description.setVisibility(View.VISIBLE);
                description.setText(post.getDescription());
            } else {
                description.setVisibility(View.GONE);
            }
        } else {
            description.setVisibility(View.GONE);
        }
        //Now load case image list..
        if(post.getPostImages() != null ){
            if(post.getPostImages().size() == 0){
                imageRecyclerView.setVisibility(View.GONE);
            }else{
                imageRecyclerView.setVisibility(View.VISIBLE);
                caseImageAdapter = new CaseImageAdapter(mainActivity, post.getPostImages());
                imageRecyclerView.setAdapter(caseImageAdapter);
            }
        }else{
            imageRecyclerView.setVisibility(View.GONE);
        }

        //TOTAL LIKE
        numberOfLike.setText(String.valueOf((int)postDetail.getTotalLike()));
        //TOTAL SAVE..
        numberOfSave.setText(String.valueOf((int)postDetail.getTotalSave()));

        //Add accepted answer

        if(postDetail.getHasAcceptedAnswer()){
            //Show accepted answer..
            if(postDetail.getComment() != null){
                showSelectedAnswer(selectedAnswer, isAnswerSelected,selectedAnswerUserName, postDetail.getComment());
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
                //Adding login customer..
                if(loginCustomer != null) {
                    if (post.getCustomer() != null) {
                        if (post.getCustomer().getId() != null) {
                            if (post.getCustomer().getId().toString().equals(loginCustomer.getId().toString())) {
                                //Set click listener for selected answer..
                                isAnswerSelected.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //Add on click logic..
                                        CommentState commentState = commentStateDataList.get((String) postDetail.getComment().getId());
                                        if (commentState != null) {
                                            if (commentState.isState()) {
                                                //Remove accepted answer...
                                                //Display the accept answer option..
                                                isAnswerSelected.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.unselected));
                                                //Remove the answer..
                                                caseDetailPresenter.acceptAnswer((String) post.getId(), (String) postDetail.getComment().getId(), false);
                                                //Reload the case presenter after the ..load..
                                            } else {
                                                //ACCEPT ANSWER
                                                isAnswerSelected.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.selected));
                                                caseDetailPresenter.acceptAnswer((String) post.getId(), (String) postDetail.getComment().getId(), true);
                                            }

                                            //Now change the state.
                                            commentState.setState(!commentState.isState());
                                            removeOtherAcceptedAnswerState(postDetail.getComment());
                                        }

                                    }
                                });
                            }
                        }
                    }
                }

            }else{
                ///hide accepted answer..
                hideSelectedAnswer(selectedAnswer, isAnswerSelected,selectedAnswerUserName, postDetail.getComment());
            }
        }else{
            ///hide accepted answer..
            hideSelectedAnswer(selectedAnswer, isAnswerSelected,selectedAnswerUserName, postDetail.getComment());
        }

        /** Adding Like and Save**/



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
                        likeButton.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.like_selected));
                    }else{
                        TrackLike trackLike = new TrackLike();
                        trackLike.state = false;
                        Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_LIKE).put(post.getId(), trackLike);
                        likeButton.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.like_unselected));
                    }
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    Log.e(Constants.TAG, t.toString());
                    TrackLike trackLike = new TrackLike();
                    trackLike.state = false;
                    Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_LIKE).put(post.getId(), trackLike);
                    likeButton.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.like_unselected));
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
                        saveButton.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.save_selected));
                    }else{
                        TrackSave trackSave = new TrackSave();
                        trackSave.state = false;
                        Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_SAVE).put(post.getId(), trackSave);
                        saveButton.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.save_unselected));
                    }
                }

                @Override
                public void onError(Throwable t) {
                    Log.e(Constants.TAG, t.toString());
                    TrackSave trackSave = new TrackSave();
                    trackSave.state = false;
                    Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_SAVE).put(post.getId(), trackSave);
                    saveButton.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.save_unselected));
                }
            });
        }
    }



    public void removeOtherAcceptedAnswerState(Comment exceptComment){
        if(exceptComment != null && commentStateDataList != null){
            for(String key: commentStateDataList.keySet()){
                //Change except comment...
                if(!key.toString().equals((String)exceptComment.getId())){
                    //Now set all state to false..
                    CommentState commentState = commentStateDataList.get(key);
                    commentState.setState(false);
                    //Replace the highlighted tick mark with default tick..
                    commentState.getIsSelected().setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.unselected));
                }
            }
        }
    }


    public void loadComments() {
        if(post == null){
            return;
        }
        //ADD COMMENTS
        //Add post listener..
        if(post.getComments() == null){
            DataList<Comment> commentList = new DataList<>();
            post.setComments(commentList);
        }


        post.getComments().subscribe(this, new Listen<Comment>() {
            @Override
            public void onInit(DataList<Comment> dataList) {

                caseDetailFragmentCommentAdapter = new CaseDetailFragmentCommentAdapter(mainActivity, post, caseDetailPresenter, commentStateDataList);
                commentsRecyclerView.setAdapter(caseDetailFragmentCommentAdapter);
            }

            @Override
            public void onChange(DataList<Comment> dataList) {
                caseDetailFragmentCommentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onClear() {
                caseDetailFragmentCommentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onRemove(final Comment element, final int index, final DataList<Comment> dataList) {
                CommentRepository commentRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(CommentRepository.class);
                commentRepository.deleteById((String) element.getId(), new ObjectCallback<JSONObject>() {
                    @Override
                    public void onBefore() {
                        super.onBefore();
                    }

                    @Override
                    public void onSuccess(JSONObject object) {
                        super.onSuccess(object);
                    }

                    @Override
                    public void onError(Throwable t) {
                        dataList.add(index, element);
                        Log.e(Constants.TAG, t.toString());
                        TastyToast.makeText(mainActivity.getApplicationContext(), Constants.DELETE_ERROR_POST, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }

                    @Override
                    public void onFinally() {
                        super.onFinally();
                    }
                });
                caseDetailFragmentCommentAdapter.notifyDataSetChanged();
            }
        });

    }


    //Load more comment..
    public void recyclerViewLoadMoreEventData() {

        commentsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    visibleItemCount = recyclerView.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount)
                            <= (firstVisibleItem + visibleThreshold)) {
                        //Add more comment..
                        caseDetailPresenter.fetchMoreComment(post);
                        loading = true;
                    }
                }
            }
        });
    }

    public void setTime(TextView casePostedTime, String date){
        String parseDate = mainActivity.parseDate(date);
        casePostedTime.setText(parseDate + " ago");
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


    public void hideSelectedAnswer(TextView selectedAnswer, ImageView greenTick, TextView userName, Comment comment){
        selectedAnswer.setVisibility(View.GONE);
        greenTick.setVisibility(View.GONE);
        userName.setVisibility(View.GONE);
        //Add comment..
        if(commentStateDataList != null){
            CommentState commentState = new CommentState(comment);
            if(comment != null){
                commentState.setIsSelected(greenTick);
                commentStateDataList.put((String)comment.getId(), commentState);
            }
        }
    }
    public void showSelectedAnswer(TextView selectedAnswer, ImageView greenTick, TextView userName, Comment comment){
        selectedAnswer.setVisibility(View.VISIBLE);
        greenTick.setVisibility(View.VISIBLE);
        userName.setVisibility(View.VISIBLE);
        //Add comment..
        if(commentStateDataList != null){
            CommentState commentState = new CommentState(comment);
            commentState.setIsSelected(greenTick);
            commentState.setState(true);
            commentStateDataList.put((String)comment.getId(), commentState);
        }

    }


    @OnClick(R.id.fragment_case_detail_image_button1) void backButton() {
        mainActivity.onBackPressed();
    }


    @OnClick(R.id.fragment_case_detail_button4) void postAnswer() {
        if(post != null){
            //Prepare the data..
            Presenter.getInstance().addModel(Constants.EDIT_IN_PROCESS_COMMENT_POST_MODEL, post);
        }
        mainActivity.replaceFragment(R.id.fragment_case_detail_button4, null);
    }







    public void likeButtonClickListener() {
        likeLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<Object, TrackLike> trackLikeHashMap = Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_LIKE);
                if(trackLikeHashMap != null){
                    if(trackLikeHashMap.get(post.getId()) == null){
                        final TrackLike trackLike = new TrackLike();
                        //Add like
                        casePresenter.addLike((String) loginCustomer.getId(), (String) post.getId(), new ObjectCallback<LikePost>() {
                            @Override
                            public void onBefore() {
                                trackLike.state = true;
                                showLike(post, likeButton, trackLike, numberOfLike, postDetail);
                            }

                            @Override
                            public void onSuccess(LikePost object) {
                                trackLike.likePost = object;
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.e(Constants.TAG, t.toString());
                                trackLike.state = false;
                                if(t.getMessage() != null){
                                    if(!t.getMessage().equals("java.net.SocketTimeoutException")) {
                                        showLike(post, likeButton, trackLike, numberOfLike, postDetail);
                                    }
                                }
                                //highlightLike(likeButton, trackLike.state);

                            }
                        });
                    }else{
                        final TrackLike trackLike = (TrackLike) Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_LIKE).get(post.getId());
                        if (trackLike.state) {
                            trackLike.state = false;
                            casePresenter.removeLike(trackLike.likePost, new ObjectCallback<JSONObject>() {
                                @Override
                                public void onBefore() {
                                    trackLike.state = false;
                                    showLike(post, likeButton, trackLike, numberOfLike, postDetail);
                                }

                                @Override
                                public void onSuccess(JSONObject object) {
                                }

                                @Override
                                public void onError(Throwable t) {
                                    Log.e(Constants.TAG, t.toString());
                                    trackLike.state = true;
                                    if(t.getMessage() != null){
                                        //Dont add in case of socket exception
                                        if(!t.getMessage().equals("java.net.SocketTimeoutException")){
                                            showLike(post, likeButton, trackLike, numberOfLike, postDetail);
                                        }
                                    }
                                    //highlightLike(likeButton, trackLike.state);
                                }

                                @Override
                                public void onFinally() {
                                    super.onFinally();
                                }
                            });

                        } else {
                            //add like..
                            casePresenter.addLike((String) loginCustomer.getId(), (String) post.getId(), new ObjectCallback<LikePost>() {
                                @Override
                                public void onBefore() {
                                    trackLike.state = true;
                                    showLike(post, likeButton, trackLike, numberOfLike, postDetail);
                                }

                                @Override
                                public void onSuccess(LikePost object) {
                                    trackLike.likePost = object;
                                }

                                @Override
                                public void onError(Throwable t) {
                                    Log.e(Constants.TAG, t.toString());
                                    trackLike.state = false;
                                    //highlightLike(likeButton, trackLike.state);
                                    if(t.getMessage() != null){
                                        if(!t.getMessage().equals("java.net.SocketTimeoutException")) {
                                            showLike(post, likeButton, trackLike, numberOfLike, postDetail);
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    public void highlightLike(ImageView like, boolean state){
        if(state){
            like.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.like_selected));
        }else{
            like.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.like_unselected));
        }

    }

    public void highlightSave(ImageView saveCase, boolean state){
        if(state){
            saveCase.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.save_selected));
        }else{
            saveCase.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.save_unselected));
        }
    }


    public void showLike(Post post, ImageView like, TrackLike trackLike, TextView numberOfLike, PostDetail postDetail){
        if(trackLike != null){
            if(trackLike.state){
                postDetail.setTotalLike(postDetail.getTotalLike() + 1);
                String parsedLike = parseLikeAndSave((int)postDetail.getTotalLike());
                numberOfLike.setText(parsedLike);
                Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_LIKE).put(post.getId(), trackLike);
            }else{
                trackLike.likePost = null;

                if(postDetail.getTotalLike() == 0){

                } else {
                    postDetail.setTotalLike(postDetail.getTotalLike() - 1);
                }
                String parsedLike = parseLikeAndSave((int)postDetail.getTotalLike());
                numberOfLike.setText(parsedLike);
                Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_LIKE).put(post.getId(), trackLike);
            }

            highlightLike(like, trackLike.state);
        }
        //Now notify change..
        notifyCaseFragmentChange();
    }

    public void showSave(Post post, ImageView saveCase, TrackSave trackSave, TextView numberOfSave, PostDetail postDetail){
        if(trackSave != null){
            if(trackSave.state){
                //saveCase.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.save_selected));
                postDetail.setTotalSave(postDetail.getTotalSave() + 1);
                String parsedSave = parseLikeAndSave((int)postDetail.getTotalSave());
                numberOfSave.setText(parsedSave);
                Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_SAVE).put(post.getId(), trackSave);
            }else{
                //saveCase.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.save_unselected));
                if(postDetail.getTotalSave() == 0) {

                } else {
                    postDetail.setTotalSave(postDetail.getTotalSave() - 1);
                }
                String parsedSave = parseLikeAndSave((int)postDetail.getTotalSave());
                numberOfSave.setText(parsedSave);
                trackSave.savePost = null;
                Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_SAVE).put(post.getId(), trackSave);
            }
            //Highlight save
            highlightSave(saveCase, trackSave.state);
        }
        //Now notify change..
        notifyCaseFragmentChange();
    }

    public void saveButtonClickListener() {
        saveLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_SAVE).get(post.getId()) == null){
                    final TrackSave trackSave = new TrackSave();
                    //Add like
                    casePresenter.addSave((String) loginCustomer.getId(), (String) post.getId(), new ObjectCallback<SavePost>() {
                        @Override
                        public void onBefore() {
                            trackSave.state = true;
                            showSave(post, saveButton, trackSave, numberOfSave, postDetail);
                        }

                        @Override
                        public void onSuccess(SavePost object) {
                            trackSave.savePost = object;
                        }

                        @Override
                        public void onError(Throwable t) {
                            Log.e(Constants.TAG, t.toString());
                            trackSave.state = false;
                            //highlightSave(saveButton, trackSave.state );
                            if(t.getMessage() != null){
                                if(!t.getMessage().equals("java.net.SocketTimeoutException")) {
                                    showSave(post, saveButton, trackSave, numberOfSave, postDetail);
                                }
                            }
                        }
                    });
                }else{
                    final TrackSave trackSave = (TrackSave)Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_SAVE).get(post.getId());
                    if(trackSave.state){
                        trackSave.state = false;
                        casePresenter.removeSave(trackSave.savePost, new ObjectCallback<JSONObject>() {
                            @Override
                            public void onBefore() {
                                trackSave.state = false;
                                showSave(post, saveButton, trackSave, numberOfSave, postDetail);
                            }

                            @Override
                            public void onSuccess(JSONObject object) {
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.e(Constants.TAG, t.toString());
                                trackSave.state = true;
                                //highlightSave(saveButton, trackSave.state);
                                if(t.getMessage() != null){
                                    if(!t.getMessage().equals("java.net.SocketTimeoutException")) {
                                        showSave(post, saveButton, trackSave, numberOfSave, postDetail);
                                    }
                                }
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
                                showSave(post, saveButton, trackSave, numberOfSave, postDetail);
                            }

                            @Override
                            public void onSuccess(SavePost object) {
                                trackSave.savePost = object;
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.e(Constants.TAG, t.toString());
                                trackSave.state = false;
                                if(t.getMessage() != null){
                                    if(!t.getMessage().equals("java.net.SocketTimeoutException")) {
                                        showSave(post, saveButton, trackSave, numberOfSave, postDetail);
                                    }
                                }
                                //highlightSave(saveButton, trackSave.state);
                            }
                        });
                    }
                }
            }
        });
    }



    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
