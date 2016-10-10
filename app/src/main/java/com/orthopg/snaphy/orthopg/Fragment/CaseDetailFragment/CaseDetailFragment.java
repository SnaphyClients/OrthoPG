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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Post;
import com.androidsdk.snaphy.snaphyandroidsdk.models.PostDetail;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.Fragment.CaseFragment.CaseImageAdapter;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

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
    @Bind(R.id.layout_case_detail_imagebutton1) ImageButton saveButton;
    @Bind(R.id.layout_case_detail_imagebutton2) ImageButton likeButton;
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
    List<Drawable> imageList = new ArrayList<>();
    List<CommentModel> commentModelList = new ArrayList<>();
    CaseDetailFragmentCommentAdapter caseDetailFragmentCommentAdapter;
    int position;
    PostDetail postDetail;

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

        loadPostData(position);
        return view;
    }

    public void loadPostData(int position){
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

      /*  //Add accepted answer
        if(postDetail.getAcceptedAnswer() != null) {
            // Add Selected Answer
            if(!postDetail.getAcceptedAnswer().getAnswer().isEmpty()){
                showSelectedAnswer(selectedAnswer, isAnswerSelected,selectedAnswerUserName);
                selectedAnswer.setText(postDetail.getAcceptedAnswer().getAnswer());
                if(postDetail.getAcceptedAnswer().getCustomer() != null){
                    String name= mainActivity.snaphyHelper.getName(postDetail.getAcceptedAnswer().getCustomer().getFirstName(), postDetail.getAcceptedAnswer().getCustomer().getLastName());
                    if(!name.isEmpty()){
                        name = Constants.Doctor + name.replace("^[Dd][Rr]", "");
                    }
                    selectedAnswerUserName.setText(name);
                }
            }else{
                hideSelectedAnswer(selectedAnswer, isAnswerSelected,selectedAnswerUserName);
            }
        }else{
            hideSelectedAnswer(selectedAnswer, isAnswerSelected,selectedAnswerUserName);
        }*/

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

    /*public void setInitialData() {
        imageList.add((getActivity().getResources().getDrawable(R.drawable.demo_books_image_1)));
        imageList.add((getActivity().getResources().getDrawable(R.drawable.demo_books_image_2)));
        imageList.add((getActivity().getResources().getDrawable(R.drawable.demo_books_image_3)));

        commentModelList.add(new CommentModel(true, "Aadish Surana", "Medial epicondylitis, also known as golfer's elbow, baseball elbow, suitcase elbow, or forehand tennis elbow, is characterized by pain" +
                " from the elbow to the wrist on the inside (medial side) of the elbow. "));
        commentModelList.add(new CommentModel(false, "Ravi Gupta", "The pain is caused by damage to the tendons that bend the wrist toward the palm." +
                " A tendon is a tough cord of tissue that connects muscles to bones."));
        commentModelList.add(new CommentModel(false, "Pulkit Dubey", "Medial epicondylitis, also known as golfer's elbow, baseball elbow, suitcase elbow, or forehand tennis elbow, is characterized by pain" +
                " from the elbow to the wrist on the inside (medial side) of the elbow. "));
        commentModelList.add(new CommentModel(false, "Robins Gupta", "The pain is caused by damage to the tendons that bend the wrist toward the palm." +
                " A tendon is a tough cord of tissue that connects muscles to bones."));
        commentModelList.add(new CommentModel(false, "Imran Sajid", "Medial epicondylitis, also known as golfer's elbow, baseball elbow, suitcase elbow, or forehand tennis elbow, is characterized by pain" +
                " from the elbow to the wrist on the inside (medial side) of the elbow. "));
    }*/

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

    // TODO: Rename method, update argument and hook method into UI event
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
