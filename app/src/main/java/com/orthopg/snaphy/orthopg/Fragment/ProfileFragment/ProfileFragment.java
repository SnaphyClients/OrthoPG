package com.orthopg.snaphy.orthopg.Fragment.ProfileFragment;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.VoidCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CustomerRepository;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    @Bind(R.id.fragment_profile_textview1) TextView logout;
    @Bind(R.id.fragment_profile_textview2) TextView email;
    @Bind(R.id.fragment_profile_textview3) TextView mciNumber;
    /*@Bind(R.id.fragment_profile_textview4) TextView speciality;*/
    @Bind(R.id.fragment_profile_textview5) TextView name;
    MainActivity mainActivity;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutButton();
            }
        });
        return view;
    }

    @OnClick(R.id.fragment_profile_imagebutton1) void editMCINumber() {
        showMCIDialog();
    }

    @OnClick(R.id.fragment_profile_imagebutton2) void editName() {
        showNameDialog();
    }

    public void showMCIDialog() {

        final Dialog dialog = new Dialog(mainActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_text);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Button okButton = (Button) dialog.findViewById(R.id.dialog_add_text_button1);
        final EditText editText = (EditText) dialog.findViewById(R.id.dialog_add_text_edittext1);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setText(mciNumber.getText());

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mciNumber.setText(editText.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void showNameDialog() {

        final Dialog dialog = new Dialog(mainActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_text);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Button okButton = (Button) dialog.findViewById(R.id.dialog_add_text_button1);
        final EditText editText = (EditText) dialog.findViewById(R.id.dialog_add_text_edittext1);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setText(name.getText());

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText(editText.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
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

    @OnClick(R.id.fragment_menu_button1) void openAboutUs(){
        mainActivity.replaceFragment(R.id.fragment_menu_button1, null);
    }

    @OnClick(R.id.fragment_menu_button2) void openContactUs(){
        mainActivity.replaceFragment(R.id.fragment_menu_button2, null);
    }

    @OnClick(R.id.fragment_menu_button3) void openFAQS(){
        mainActivity.replaceFragment(R.id.fragment_menu_button3, null);
    }

    @OnClick(R.id.fragment_menu_button4) void openFeedback() {
        Uri uri = Uri.parse("market://details?id=" + Constants.APP_PLAY_STORE);// this.getPackageName()
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + Constants.APP_PLAY_STORE)));
        }
    }

    @OnClick(R.id.fragment_menu_button5) void openShare() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = Constants.APP_SHARE_TEXT;
        shareBody = shareBody + "http://play.google.com/store/apps/details?id=" + Constants.APP_PLAY_STORE;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "OrthoPG");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share Via"));
    }

    @OnClick(R.id.fragment_menu_button6) void openTermsAndConditions(){
        mainActivity.replaceFragment(R.id.fragment_menu_button6, null);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void logoutButton() {
        final CustomerRepository customerRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(CustomerRepository.class);

        //TODO SHOW LOADING BAR
        customerRepository.logout(new VoidCallback() {
            @Override
            public void onBefore() {
                //TODO..Show progress bar..
            }

            @Override
            public void onSuccess() {
                Log.e(Constants.TAG, "Successfully Logout!!");
                mainActivity.googleLogout();
                mainActivity.moveToLogin();
                //Remove the customer..
                Presenter.getInstance().removeModelFromList(Constants.LOGIN_CUSTOMER);
            }


            @Override
            public void onError(Throwable t) {
                Log.e(Constants.TAG, t.toString());
                customerRepository.setCurrentUserId(null);
                mainActivity.snaphyHelper.getLoopBackAdapter().clearAccessToken();
                mainActivity.snaphyHelper.registerInstallation(null);
                //TODO Stop progress bar..
                mainActivity.googleLogout();
                mainActivity.moveToLogin();
                //Remove the customer..
                Presenter.getInstance().removeModelFromList(Constants.LOGIN_CUSTOMER);
            }

            @Override
            public void onFinally() {

            }
        });
        /*
        * new VoidCallback() {
            @Override
            public void onSuccess() {
                //TODO CLOSE LOADING BAR
                //Move to login fragment..
                googleLogout();
                mainActivity.stopService(new Intent(mainActivity, BackgroundService.class));
                mainActivity.moveToLogin();
            }

            @Override
            public void onError(Throwable t) {
                //TODO CLOSE LOADING BAR
                Log.e(Constants.TAG, t.toString());
                BackgroundService.getCustomerRepository().setCurrentUserId(null);
                //Snackbar.make(rootView,Constants.ERROR_MESSAGE, Snackbar.LENGTH_SHORT).show();
                mainActivity.getLoopBackAdapter().clearAccessToken();
                BackgroundService.setCustomer(null);
                mainActivity.registerInstallation(null);
                //ALSO ADD GOOGLE LOGOUT AND FACEBOOK LOGOUT HERE..
                googleLogout();
                mainActivity.stopService(new Intent(mainActivity, BackgroundService.class));
                mainActivity.moveToLogin();
                //Toast.makeText(mainActivity, Constants.ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        }*/
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
