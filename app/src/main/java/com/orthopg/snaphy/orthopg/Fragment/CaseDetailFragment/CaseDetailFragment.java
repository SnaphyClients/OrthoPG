package com.orthopg.snaphy.orthopg.Fragment.CaseDetailFragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.orthopg.snaphy.orthopg.Fragment.CaseFragment.CaseImageAdapter;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    boolean isLiked = false;
    boolean isSaved = false;
    MainActivity mainActivity;
    CaseImageAdapter caseImageAdapter;
    List<Drawable> imageList = new ArrayList<>();
    List<CommentModel> commentModelList = new ArrayList<>();
    CaseDetailFragmentCommentAdapter caseDetailFragmentCommentAdapter;

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
        setInitialData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_case_detail, container, false);
        ButterKnife.bind(this, view);

        imageRecyclerView.setLayoutManager(new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false));
        //caseImageAdapter = new CaseImageAdapter(imageList);
        //imageRecyclerView.setAdapter(caseImageAdapter);

        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        caseDetailFragmentCommentAdapter = new CaseDetailFragmentCommentAdapter(mainActivity, commentModelList);
        commentsRecyclerView.setAdapter(caseDetailFragmentCommentAdapter);

        return view;
    }

    public void setInitialData() {
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
