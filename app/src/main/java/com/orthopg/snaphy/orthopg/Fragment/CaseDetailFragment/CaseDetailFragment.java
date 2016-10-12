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
import com.orthopg.snaphy.orthopg.Fragment.CaseFragment.CaseImageAdapter;
import com.orthopg.snaphy.orthopg.Fragment.CaseFragment.CasePresenter;
import com.orthopg.snaphy.orthopg.Fragment.CaseFragment.TrackLike;
import com.orthopg.snaphy.orthopg.Fragment.CaseFragment.TrackSave;
import com.orthopg.snaphy.orthopg.ImageZoom.ImageZoomDialog;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;
import com.orthopg.snaphy.orthopg.RecyclerItemClickListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

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
    boolean isLiked = false;
    boolean isSaved = false;
    MainActivity mainActivity;
    Post post;
    CaseImageAdapter caseImageAdapter;
    List<CommentModel> commentModelList = new ArrayList<>();
    CaseDetailFragmentCommentAdapter caseDetailFragmentCommentAdapter;
    int position;
    PostDetail postDetail;
    CasePresenter casePresenter;
    Customer loginCustomer;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_case_detail, container, false);
        ButterKnife.bind(this, view);

        imageRecyclerView.setLayoutManager(new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false));
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
        likeButtonClickListener();
        saveButtonClickListener();
        loadPostData(position);
        return view;
    }

    public void loadPostData(int position){
        //casePresenter = new CasePresenter(mainActivity.snaphyHelper.getLoopBackAdapter(), progressBar, mainActivity);
        if(Presenter.getInstance().getList(PostDetail.class, Constants.POST_DETAIL_LIST_CASE_FRAGMENT) != null){
            DataList<PostDetail> postDetails = Presenter.getInstance().getList(PostDetail.class, Constants.POST_DETAIL_LIST_CASE_FRAGMENT);
            postDetail = postDetails.get(position);
        }

        if(postDetail != null){
            if(postDetail.getPost() != null){
                post = postDetail.getPost();
            }else{
                return;
            }
        }else{
            return;
        }
        loadPost();
    }

    public void loadPost(){
        caseHeading.setText(post.getHeading());
        userName.setText(mainActivity.snaphyHelper.getName(post.getCustomer().getFirstName(), post.getCustomer().getLastName()));

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

        /** Adding Like and Save**/

        loginCustomer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
        casePresenter = Presenter.getInstance().getModel(CasePresenter.class, Constants.CASE_PRESENTER_ID);

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

        /** Adding Like and Save End Here**/

        //TODO ADD COMMENTS LATER..

        caseDetailFragmentCommentAdapter = new CaseDetailFragmentCommentAdapter(mainActivity, commentModelList);
        commentsRecyclerView.setAdapter(caseDetailFragmentCommentAdapter);


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


    @OnClick(R.id.fragment_case_detail_image_button1) void backButton() {
        mainActivity.onBackPressed();
    }

    @OnClick(R.id.layout_case_detail_imagebutton1) void savedButton() {
        if(isSaved) {
            saveButton.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.save_unselected));
            isSaved = false;
        } else {
            saveButton.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.save_selected));
            isSaved = true;
        }
    }

    @OnClick(R.id.layout_case_detail_imagebutton2) void likeButton() {
        if(isLiked) {
            likeButton.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.like_unselected));
            isLiked = false;
        } else {
            likeButton.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.like_selected));
            isLiked = true;
        }
    }

    @OnClick(R.id.fragment_case_detail_button4) void postAnswer() {
        mainActivity.replaceFragment(R.id.fragment_case_detail_button4, null);
    }

    public void likeButtonClickListener() {
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_LIKE).get(post.getId()) == null) {
                    //Add like
                    casePresenter.addLike((String) loginCustomer.getId(), (String) post.getId(), new ObjectCallback<LikePost>() {
                        @Override
                        public void onBefore() {
                            TrackLike trackLike = new TrackLike();
                            trackLike.state = true;
                            showLike(post, likeButton, trackLike, numberOfLike, postDetail);
                        }

                        @Override
                        public void onSuccess(LikePost object) {
                            TrackLike trackLike = new TrackLike();
                            trackLike.state = true;
                            trackLike.likePost = object;
                            //showLike(post, like, trackLike, numberOfLike, postDetail);
                        }

                        @Override
                        public void onError(Throwable t) {
                            TrackLike trackLike = new TrackLike();
                            trackLike.state = false;
                            showLike(post, likeButton, trackLike, numberOfLike, postDetail);
                            //TODO add toast..
                        }
                    });
                } else {
                    final TrackLike trackLike = (TrackLike) Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_LIKE).get(post.getId());
                    if (trackLike.state) {
                        trackLike.state = false;
                        //showLike(post, like, trackLike, numberOfLike, postDetail);
                        //delete like
                        casePresenter.removeLike(trackLike.likePost, new ObjectCallback<JSONObject>() {
                            @Override
                            public void onBefore() {
                                trackLike.state = false;
                                showLike(post, likeButton, trackLike, numberOfLike, postDetail);
                            }

                            @Override
                            public void onSuccess(JSONObject object) {
                                trackLike.state = false;
                                //showLike(post, like, trackLike, numberOfLike, postDetail);
                            }

                            @Override
                            public void onError(Throwable t) {
                                trackLike.state = true;
                                showLike(post, likeButton, trackLike, numberOfLike, postDetail);
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
                                trackLike.state = true;
                                trackLike.likePost = object;
                                //showLike(post, like, trackLike, numberOfLike, postDetail);
                            }

                            @Override
                            public void onError(Throwable t) {
                                trackLike.state = false;
                                showLike(post, likeButton, trackLike, numberOfLike, postDetail);
                                //TODO add toast..
                            }
                        });
                    }
                }
            }
        });
    }

    public void saveButtonClickListener() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_SAVE).get(post.getId()) == null){
                    //Add like
                    casePresenter.addSave((String) loginCustomer.getId(), (String) post.getId(), new ObjectCallback<SavePost>() {
                        @Override
                        public void onBefore() {
                            TrackSave trackSave = new TrackSave();
                            trackSave.state = true;
                            showSave(post, saveButton, trackSave, numberOfSave, postDetail);
                        }

                        @Override
                        public void onSuccess(SavePost object) {
                            TrackSave trackSave = new TrackSave();
                            trackSave.state = true;
                            trackSave.savePost = object;
                            //showSave(post, saveCase, trackSave, numberOfSave, postDetail);
                        }

                        @Override
                        public void onError(Throwable t) {
                            TrackSave trackSave = new TrackSave();
                            trackSave.state = false;
                            showSave(post, saveButton, trackSave, numberOfSave, postDetail);
                            //TODO add toast..
                        }
                    });
                }else{
                    final TrackSave trackSave = (TrackSave)Presenter.getInstance().getModel(HashMap.class, Constants.TRACK_SAVE).get(post.getId());
                    if(trackSave.state){
                        trackSave.state = false;
                        //showSave(post, saveCase, trackSave, numberOfSave, postDetail);
                        //delete like
                        casePresenter.removeSave(trackSave.savePost, new ObjectCallback<JSONObject>() {
                            @Override
                            public void onBefore() {
                                trackSave.state = false;
                                showSave(post, saveButton, trackSave, numberOfSave, postDetail);
                            }

                            @Override
                            public void onSuccess(JSONObject object) {
                                trackSave.state = false;
                                //showSave(post, saveCase, trackSave, numberOfSave, postDetail);
                            }

                            @Override
                            public void onError(Throwable t) {
                                trackSave.state = true;
                                showSave(post, saveButton, trackSave, numberOfSave, postDetail);
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
                                trackSave.state = true;
                                trackSave.savePost = object;
                                //showSave(post, saveCase, trackSave, numberOfSave, postDetail);
                            }

                            @Override
                            public void onError(Throwable t) {
                                trackSave.state = false;
                                showSave(post, saveButton, trackSave, numberOfSave, postDetail);
                                //TODO add toast..
                            }
                        });
                    }
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
