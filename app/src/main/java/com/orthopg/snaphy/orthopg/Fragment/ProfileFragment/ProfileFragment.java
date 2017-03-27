package com.orthopg.snaphy.orthopg.Fragment.ProfileFragment;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.VoidCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CustomerRepository;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;
import com.sdsmdg.tastytoast.TastyToast;

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
    //@Bind(R.id.fragment_profile_textview3) TextView mciNumber;
    @Bind(R.id.fragment_profile_button1) Button viewProfile;
    /*@Bind(R.id.fragment_profile_textview4) TextView speciality;*/
    @Bind(R.id.fragment_profile_textview5) TextView name;
    @Bind(R.id.layout_profile_image) ImageView profileImage;
    MainActivity mainActivity;
    Customer loginCustomer;
    public final static String TAG = "ProfileFragment";
    SharedPreferences sharedPreferences;

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
        loginCustomer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutButton();
            }
        });
        displayData();
        sharedPreferences = mainActivity.getApplicationContext().getSharedPreferences("DisableNotification", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean enableNotification = sharedPreferences.getBoolean("enableNotification", true);
        enableNotificationCheckBoxClickListener(editor);
        return view;
    }

    public void enableNotificationCheckBoxClickListener(SharedPreferences.Editor editor) {
        editor.putBoolean("enableNotification", true);
        editor.commit();
    }

    /*@OnClick(R.id.fragment_profile_imagebutton1) void editMCINumber() {
        showMCIDialog();
    }

    @OnClick(R.id.fragment_profile_imagebutton2) void editName() {
        showNameDialog();
    }
*/
    @OnClick(R.id.fragment_profile_button1) void onViewProfile(){

        mainActivity.replaceFragment(R.layout.fragment_doctor_profile,ProfileFragment.TAG);
    }

    public void displayData(){
        if(loginCustomer != null){
            if(loginCustomer.getEmail() != null){
                if(!loginCustomer.getEmail().isEmpty()){
                    email.setVisibility(View.VISIBLE);
                    email.setText(loginCustomer.getEmail());
                }else{
                    email.setVisibility(View.GONE);
                }
            }else{
                email.setVisibility(View.GONE);
            }

            if(loginCustomer.getMciNumber() != null){
                if(!loginCustomer.getMciNumber().isEmpty()){
                   /* mciNumber.setVisibility(View.VISIBLE);
                    mciNumber.setText(loginCustomer.getMciNumber());*/
                }else{
                    //mciNumber.setVisibility(View.GONE);
                }
            }else{
                //mciNumber.setVisibility(View.GONE);
            }

            String userName = mainActivity.snaphyHelper.getName(loginCustomer.getFirstName(), loginCustomer.getLastName());
            userName = Constants.Doctor + userName.replace("^[Dd][Rr]", "");
            if(!userName.isEmpty()){
                name.setVisibility(View.VISIBLE);
                name.setText(userName);
            }else{
                name.setVisibility(View.GONE);
            }

            if(loginCustomer.getProfilePic() != null){
                mainActivity.snaphyHelper.loadUnSignedThumbnailImage(loginCustomer.getProfilePic(), profileImage, R.mipmap.anonymous);
            }else{
                profileImage.setImageResource(R.mipmap.anonymous);
            }

        }else{
            email.setVisibility(View.GONE);
            //mciNumber.setVisibility(View.GONE);
            name.setVisibility(View.GONE);
        }

    }

    public void showMCIDialog() {

        final Dialog dialog = new Dialog(mainActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_mci);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Button okButton = (Button) dialog.findViewById(R.id.dialog_add_text_button1);
        final EditText editText = (EditText) dialog.findViewById(R.id.dialog_add_text_editMCI);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);

        if(loginCustomer.getMciNumber() != null){
            if(!loginCustomer.getMciNumber().isEmpty()){
                editText.setText(loginCustomer.getMciNumber().trim());
            }
        }


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginCustomer.setMciNumber(editText.getText().toString().trim());
                //final String oldMCINUMBER = mciNumber.getText().toString();
               // mciNumber.setText(loginCustomer.getMciNumber());
                loginCustomer.save(new com.strongloop.android.loopback.callbacks.VoidCallback() {
                    @Override
                    public void onSuccess() {
/*
                        TastyToast.makeText(mainActivity.getApplicationContext(), "Verification is under process", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
*/
                    }

                    @Override
                    public void onError(Throwable t) {
                       // mciNumber.setText(oldMCINUMBER);
                        TastyToast.makeText(mainActivity.getApplicationContext(), Constants.ERROR_UPDATING_MCI, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }





    public void showNameDialog() {

        final Dialog dialog = new Dialog(mainActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_name);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Button okButton = (Button) dialog.findViewById(R.id.dialog_add_text_button1);
        final EditText firstName = (EditText) dialog.findViewById(R.id.dialog_add_text_edittext1);
        final EditText lastName = (EditText) dialog.findViewById(R.id.dialog_add_text_edittext2);
        firstName.setInputType(InputType.TYPE_CLASS_TEXT);
        lastName.setInputType(InputType.TYPE_CLASS_TEXT);

        if(loginCustomer != null){
            if(loginCustomer.getFirstName() != null){
                if(!loginCustomer.getFirstName().isEmpty()){
                    firstName.setText(loginCustomer.getFirstName().trim());
                }

                if(!loginCustomer.getLastName().isEmpty()){
                    lastName.setText(loginCustomer.getLastName().trim());
                }
            }
        }


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstName.getText() != null){
                    if(firstName.getText().toString() != null){
                        if(firstName.getText().toString().trim().isEmpty()){
                            TastyToast.makeText(mainActivity.getApplicationContext(), Constants.ERROR_FIRST_NAME_EMPTY, TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                            return;
                        }else{
                            loginCustomer.setFirstName(firstName.getText().toString().trim());
                        }
                    }
                }

                if(lastName.getText() != null){
                    if(lastName.getText().toString() != null){
                        loginCustomer.setLastName(lastName.getText().toString().trim());
                    }
                }

                String userName = mainActivity.snaphyHelper.getName(loginCustomer.getFirstName(), loginCustomer.getLastName());
                userName = Constants.Doctor + userName.replace("^[Dd][Rr]", "");
                final String oldName = name.getText().toString();
                name.setText(userName);

                //Now update the customer..
                loginCustomer.save(new com.strongloop.android.loopback.callbacks.VoidCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Throwable t) {
                        name.setText(oldName);
                        TastyToast.makeText(mainActivity.getApplicationContext(), Constants.ERROR_UPDATING_NAME, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                });
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
                mainActivity.snaphyHelper.registerInstallation(null);
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
