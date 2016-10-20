package com.orthopg.snaphy.orthopg.Fragment.NewCase;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.CustomModel.NewCase;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;
import com.sdsmdg.tastytoast.TastyToast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CaseHeadingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CaseHeadingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CaseHeadingFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    public static String TAG = "CaseHeadingFragment";
    @Bind(R.id.fragment_case_heading_edittext1) EditText heading;
    MainActivity mainActivity;

    public CaseHeadingFragment() {
        // Required empty public constructor
    }

    public static CaseHeadingFragment newInstance() {
        CaseHeadingFragment fragment = new CaseHeadingFragment();
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
        View view = inflater.inflate(R.layout.fragment_case_heading, container, false);
        ButterKnife.bind(this, view);
        //Load previous data..
        loadHeading();
        return view;
    }


    public void loadHeading(){
        if(Presenter.getInstance().getModel(NewCase.class, Constants.ADD_NEW_CASE) != null){
            NewCase newCase = Presenter.getInstance().getModel(NewCase.class, Constants.ADD_NEW_CASE);
            if(newCase.getPost() != null){
                if(newCase.getPost().getHeading() != null){
                    if(!newCase.getPost().getHeading().isEmpty()){
                        //Load the heading..
                        heading.setText(newCase.getPost().getHeading());
                    }
                }
            }

        }
    }

    @OnClick(R.id.fragment_case_heading_image_button1) void crossButton() {
        InputMethodManager imm = (InputMethodManager)mainActivity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(heading.getWindowToken(), 0);
        mainActivity.onBackPressed();
    }

    @OnClick(R.id.fragment_case_heading_button1) void nextButton() {
        InputMethodManager imm = (InputMethodManager)mainActivity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(heading.getWindowToken(), 0);
        moveNext(heading);
    }


    public void saveHeading(String title){
        if(Presenter.getInstance().getModel(NewCase.class, Constants.ADD_NEW_CASE) != null){
            NewCase newCase = Presenter.getInstance().getModel(NewCase.class, Constants.ADD_NEW_CASE);
            if(newCase.getPost() != null){
                newCase.getPost().setHeading(title);
            }
        }
    }



    public void moveNext(TextView heading){
        if(heading.getText() != null){
            String title = heading.getText().toString();
            if(title != null){
                title = title.trim();
                if(!title.isEmpty()){
                    //Now load title to post.
                    saveHeading(title);
                    //Now add to Presenter..
                    mainActivity.replaceFragment(R.id.fragment_case_heading_button1, null);
                }else{
                    TastyToast.makeText(mainActivity.getApplicationContext(), Constants.HEADING_REQUIRED_MESSAGE, TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                }
            }else{
                TastyToast.makeText(mainActivity.getApplicationContext(), Constants.HEADING_REQUIRED_MESSAGE, TastyToast.LENGTH_SHORT, TastyToast.WARNING);
            }
        }else{
            TastyToast.makeText(mainActivity.getApplicationContext(), Constants.HEADING_REQUIRED_MESSAGE, TastyToast.LENGTH_SHORT, TastyToast.WARNING);
        }
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
