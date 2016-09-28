package com.orthopg.snaphy.orthopg.Fragment.MenuFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MenuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    @Bind(R.id.fragment_menu_button1) Button aboutUs;
    @Bind(R.id.fragment_menu_button2) Button contactUs;
    @Bind(R.id.fragment_menu_button3) Button faqs;
    @Bind(R.id.fragment_menu_button4) Button feedback;
    @Bind(R.id.fragment_menu_button5) Button share;
    @Bind(R.id.fragment_menu_button6) Button termsAndConditions;
    MainActivity mainActivity;

    public MenuFragment() {
        // Required empty public constructor
    }


    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
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
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.fragment_menu_button1) void openAboutUs(){
        mainActivity.replaceFragment(R.id.fragment_menu_button1, null);
    }

    @OnClick(R.id.fragment_menu_button2) void openContactUs(){
        mainActivity.replaceFragment(R.id.fragment_menu_button2, null);
    }

    @OnClick(R.id.fragment_menu_button3) void openFAQS(){
        mainActivity.replaceFragment(R.id.fragment_menu_button3, null);
    }

    @OnClick(R.id.fragment_menu_button4) void openFeedback(){

    }

    @OnClick(R.id.fragment_menu_button5) void openShare(){

    }

    @OnClick(R.id.fragment_menu_button6) void openTermsAndConditions(){
        mainActivity.replaceFragment(R.id.fragment_menu_button6, null);
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
