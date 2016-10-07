package com.orthopg.snaphy.orthopg.Fragment.ProfileFragment;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
    @Bind(R.id.fragment_profile_tab_layout) TabLayout tabLayout;
    @Bind(R.id.fragment_profile_view_pager) ViewPager viewPager;
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
        viewPager.setAdapter(new ProfileFragmentTabLayoutAdapter(mainActivity.getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        setTextInTabLayout();
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

    /**
     * Set Text for Tab Layout
     */
    public void setTextInTabLayout() {
        tabLayout.getTabAt(0).setText("Saved Cases");
        tabLayout.getTabAt(1).setText("Posted Cases");
        tabLayout.getTabAt(2).setText("Menu");
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
