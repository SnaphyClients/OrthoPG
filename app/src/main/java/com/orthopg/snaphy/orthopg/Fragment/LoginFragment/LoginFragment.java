package com.orthopg.snaphy.orthopg.Fragment.LoginFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.ObjectCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CustomerRepository;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    MainActivity mainActivity;
    public static String TAG = "LoginFragment";
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 0;
    GoogleSignInOptions gso;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Constants.CLIENT_ID)
                .requestEmail()
                .build();
        initializeGoogleApiClient();
        return view;
    }

    public void initializeGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(mainActivity)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Presenter.getInstance().addModel(Constants.GOOGLE_API_CLIENT, googleApiClient);
        googleApiClient.connect();
    }

    public void sendTokenToServer(String token) {
        CustomerRepository customerRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(CustomerRepository.class);
        customerRepository.loginWithGoogle(token, new ObjectCallback<JSONObject>() {
            @Override
            public void onBefore() {
                //TODO STart progress bar..
            }

            @Override
            public void onSuccess(JSONObject object) {
                if (object != null) {
                    Log.i(Constants.TAG, "Google = " + object.toString());
                    mainActivity.addUser(object);

                } else {
                    Log.v(Constants.TAG, "Null");

                }
            }

            @Override
            public void onError(Throwable t) {
                Log.e(Constants.TAG, t.toString());
            }

            @Override
            public void onFinally() {
                //TODO
                //STOP Progress bar..
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int responseCode,
                                 Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            if(acct.getIdToken() != null) {
                Log.v(Constants.TAG, acct.getIdToken());
                //BackgroundService.setAccessToken(acct.getIdToken());
                //mainActivity.replaceFragment(R.layout.fragment_mciverification, null);
                sendTokenToServer(acct.getIdToken());
            }

        } else {
            //Snackbar.make(rootView, Constants.ERROR_MESSAGE, Snackbar.LENGTH_SHORT).show();
        }
    }


   /* public void showStatusBar() {
        if (Build.VERSION.SDK_INT < 16) {
            mainActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else {
            View decorView = mainActivity.getWindow().getDecorView();
            // Show Status Bar.
            int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }*/

    @OnClick(R.id.fragment_login_button1) void loginButton() {
        //mainActivity.replaceFragment(R.layout.fragment_mciverification, null);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
