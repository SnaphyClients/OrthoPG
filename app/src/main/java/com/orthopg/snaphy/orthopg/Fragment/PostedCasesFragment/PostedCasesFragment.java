package com.orthopg.snaphy.orthopg.Fragment.PostedCasesFragment;

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

import com.orthopg.snaphy.orthopg.Fragment.CaseFragment.CaseListAdapter;
import com.orthopg.snaphy.orthopg.Fragment.CaseFragment.CaseModel;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostedCasesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PostedCasesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostedCasesFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    MainActivity mainActivity;
    @Bind(R.id.fragment_posted_case_recycler_view) RecyclerView recyclerView;
    CaseListAdapter caseListAdapter;
    List<CaseModel> caseModelList = new ArrayList<>();
    public static String TAG = "PostedCasesFragment";
    List<Drawable> imageList = new ArrayList<>();

    public PostedCasesFragment() {
        // Required empty public constructor
    }

    public static PostedCasesFragment newInstance() {
        PostedCasesFragment fragment = new PostedCasesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_posted_cases, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setInitialData();
        /*caseListAdapter = new CaseListAdapter(mainActivity, caseModelList, TAG);
        recyclerView.setAdapter(caseListAdapter);*/
        return view;
    }

    public void setInitialData() {

        imageList.add((getActivity().getResources().getDrawable(R.drawable.demo_books_image_1)));
        imageList.add((getActivity().getResources().getDrawable(R.drawable.demo_books_image_2)));
        imageList.add((getActivity().getResources().getDrawable(R.drawable.demo_books_image_3)));

        caseModelList.add(new CaseModel(getActivity().getResources().getDrawable(R.drawable.profile_pic),
                "Medical Epigontilitis (Golfer and Baseball Elbow", "Dr Ravi Gupta", "9 hours ago", false, false,
                imageList, "Originally, the term orthopedics meant the correcting of musculoskeletal deformities in children. Nicolas Andry," +
                " a French professor at the University of Paris coined the term in the first textbook written on the subject in 1741.", "case", true, "Aadish Surana",
                "Many developments in orthopedic surgery have resulted from experiences during wartime."));

        caseModelList.add(new CaseModel(getActivity().getResources().getDrawable(R.drawable.profile_pic),
                "Medical Epigontilitis (Golfer and Baseball Elbow", "Dr Ravi Gupta", "9 hours ago", true, false,
                imageList, "Originally, the term orthopedics meant the correcting of musculoskeletal deformities in children. Nicolas Andry," +
                " a French professor at the University of Paris coined the term in the first textbook written on the subject in 1741.", "case", true, "Aadish Surana",
                "Many developments in orthopedic surgery have resulted from experiences during wartime."));

        caseModelList.add(new CaseModel(getActivity().getResources().getDrawable(R.drawable.profile_pic),
                "Medical Epigontilitis (Golfer and Baseball Elbow","Dr Ravi Gupta", "9 hours ago", false, true,
                imageList, "Originally, the term orthopedics meant the correcting of musculoskeletal deformities in children. Nicolas Andry," +
                " a French professor at the University of Paris coined the term in the first textbook written on the subject in 1741.","case", true, "Aadish Surana",
                "Many developments in orthopedic surgery have resulted from experiences during wartime."));

        caseModelList.add(new CaseModel(getActivity().getResources().getDrawable(R.drawable.profile_pic),
                "Medical Epigontilitis (Golfer and Baseball Elbow", "Dr Ravi Gupta", "9 hours ago", true, true,
                imageList, "Originally, the term orthopedics meant the correcting of musculoskeletal deformities in children. Nicolas Andry," +
                " a French professor at the University of Paris coined the term in the first textbook written on the subject in 1741.", "case", true, "Aadish Surana",
                "Many developments in orthopedic surgery have resulted from experiences during wartime."));
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
