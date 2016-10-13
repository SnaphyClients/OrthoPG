package com.orthopg.snaphy.orthopg.Fragment.NewCase;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.Toast;

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
 * {@link CaseDescriptionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CaseDescriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CaseDescriptionFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    MainActivity mainActivity;
    public static String TAG = "CaseDescriptionFragment";
    @Bind(R.id.fragment_case_description_edittext1) EditText description;

    public CaseDescriptionFragment() {
        // Required empty public constructor
    }

    public static CaseDescriptionFragment newInstance() {
        CaseDescriptionFragment fragment = new CaseDescriptionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InputMethodManager imm = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_case_description, container, false);
        ButterKnife.bind(this, view);
        description.setScroller(new Scroller(mainActivity));
        description.setVerticalScrollBarEnabled(true);
        description.setMovementMethod(new ScrollingMovementMethod());
        //Load previous data..
        loadDescription();
        return view;
    }


    public void loadDescription(){
        if(Presenter.getInstance().getModel(NewCase.class, Constants.ADD_NEW_CASE) != null){
            NewCase newCase = Presenter.getInstance().getModel(NewCase.class, Constants.ADD_NEW_CASE);
            if(newCase.getPost() != null){
                if(newCase.getPost().getDescription() != null){
                    if(!newCase.getPost().getDescription().isEmpty()){
                        //Load the heading..
                        description.setText(newCase.getPost().getDescription());
                    }
                }
            }

        }
    }


    public void saveDescription(String desc){
        if(Presenter.getInstance().getModel(NewCase.class, Constants.ADD_NEW_CASE) != null){
            NewCase newCase = Presenter.getInstance().getModel(NewCase.class, Constants.ADD_NEW_CASE);
            if(newCase.getPost() != null){
                newCase.getPost().setDescription(desc);
            }
        }
    }



    @OnClick(R.id.fragment_case_description_button1) void postCase() {
        InputMethodManager im = (InputMethodManager)mainActivity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(description.getWindowToken(), 0);

        for(int i = 0; i< 3; i++) {
            mainActivity.onBackPressed();
        }
        InputMethodManager imm = (InputMethodManager)mainActivity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(description.getWindowToken(), 0);
        if(description.getText() != null) {
            String desc = description.getText().toString();
            if (desc != null) {
                desc = desc.trim();
                if (!desc.isEmpty()) {
                    //Now load desc to post.
                    saveDescription(desc);
                    //Now add to Presenter..
                    mainActivity.replaceFragment(R.id.fragment_case_heading_button1, null);
                }
            }
        }
        
        //ASK FOR ANONYMOUS POST AND SAVE THE CASE...
        Toast.makeText(mainActivity, "Case has been posted", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.fragment_case_description_image_button1) void backButton() {
        InputMethodManager imm = (InputMethodManager)mainActivity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(description.getWindowToken(), 0);
        mainActivity.onBackPressed();
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
