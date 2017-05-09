package com.orthopg.snaphy.orthopg.Fragment.MCIVerificationFragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.ObjectCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CustomerRepository;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MCIVerificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MCIVerificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MCIVerificationFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    public static String TAG = "MCIVerificationFragment";
   /* @Bind(R.id.fragment_mci_verification_edittext1) EditText mciCode;
    MainActivity mainActivity;
    @Bind(R.id.fragment_mci_verification_imageview1) ImageView unlockView;*/
    MainActivity mainActivity;
    static String OTP;
    static EditText otpCode;
    String phoneNumber;
    View rootview;
    @Bind(R.id.fragment_mci_verification_linearlayout1) LinearLayout linearLayout;
    @Bind(R.id.fragment_verification_button1) Button goButton;
    @Bind(R.id.fragment_verification_button2) Button okButton;
    @Bind(R.id.fragment_mci_verification_relative_layout1) RelativeLayout relativeLayout;
    EditText mciCode;
    @Bind(R.id.fragment_verification_edittext2) EditText mobileNumber;
    @Bind(R.id.fragment_verification_textview3) TextView countDown;
    @Bind(R.id.fragment_verification_progressBar) ProgressBar progressBar;

    public MCIVerificationFragment() {
        // Required empty public constructor
    }

    public static MCIVerificationFragment newInstance() {
        MCIVerificationFragment fragment = new MCIVerificationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InputMethodManager imm = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_SMS};

        if(!hasPermissions(mainActivity, PERMISSIONS)){
            ActivityCompat.requestPermissions(mainActivity, PERMISSIONS, PERMISSION_ALL);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mciverification, container, false);
        ButterKnife.bind(this, view);
        stopProgressBar(progressBar);
        rootview = view;
        otpCode = (EditText) view.findViewById(R.id.fragment_verification_edittext3);
        mciCode = (EditText) view.findViewById(R.id.fragment_verification_edittext1);
        addChangeListener();
        checkIfKeyboardIsOpen(view);
        return view;
    }

    public void setRelativeLayoutHeight(boolean isOpen) {
        if(!isOpen) {
            ViewGroup.LayoutParams params = relativeLayout.getLayoutParams();
            // Changes the height and width to the specified *pixels*
            params.height = 0;
            relativeLayout.setLayoutParams(params);
        }
            else {
            ViewGroup.LayoutParams params = relativeLayout.getLayoutParams();
            // Changes the height and width to the specified *pixels*
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            relativeLayout.setLayoutParams(params);
        }
    }

    public void checkIfKeyboardIsOpen(final View view) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                view.getWindowVisibleDisplayFrame(r);
                int screenHeight = view.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

                Log.d(TAG, "keypadHeight = " + keypadHeight);

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    showKeyboard(true);
                    setRelativeLayoutHeight(true);
                }
                else {
                    // keyboard is closed
                    showKeyboard(false);
                    setRelativeLayoutHeight(false);
                }
            }
        });
    }

    public void showKeyboard(boolean  showKeyBoard) {
        if(showKeyBoard) {
            linearLayout.setVisibility(View.GONE);
            /*unlockView.setVisibility(View.GONE);*/
        } else {
            linearLayout.setVisibility(View.VISIBLE);
            /*unlockView.setVisibility(View.VISIBLE);*/
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @OnClick(R.id.fragment_verification_button1) void requestOTP() {
        if(!mainActivity.snaphyHelper.isNetworkAvailable()){
            Snackbar.make(goButton, "Check your network connection and try again!!", Snackbar.LENGTH_SHORT).show();
        } else {
            if (isPhoneValidate(mobileNumber.getText().toString())) {
                phoneNumber = mobileNumber.getText().toString();
                requestOTPFromServer(mobileNumber.getText().toString());
                setCountDown();
                enableOkButton(true);
                enableGoButton(false);
                //View view1 = mainActivity.getCurrentFocus();
                otpCode.setFocusable(true);
                mainActivity.hideSoftKeyboard(otpCode);
            /*if (view1 != null) {
                InputMethodManager imm = (InputMethodManager)mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
            }*/


            } else {
                View view1 = mainActivity.getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }
                if (rootview != null) {
                    Snackbar.make(rootview, "Enter Valid Mobile Number", Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    }


    @OnClick(R.id.fragment_verification_button2) void goButton() {
        if(otpCode.getText().toString() != null) {
            if(!otpCode.getText().toString().isEmpty()) {
                enableOkButton(false);
                if (phoneNumber != null) {
                    String mciNumber = mciCode.getText().toString().trim();
                    if (mciNumber != null) {
                        if (!mciNumber.isEmpty()) {
                            Presenter.getInstance().addModel(Constants.MCI_NUMBER, mciNumber);
                        }
                    }
                    LoginToGoogle(otpCode.getText().toString(), phoneNumber);
                    okButton.requestFocus();
                    mainActivity.hideSoftKeyboard(okButton);
                }
            } else {
                TastyToast.makeText(mainActivity.getApplicationContext(), Constants.OTP, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }
        } else {
            TastyToast.makeText(mainActivity.getApplicationContext(), Constants.OTP, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
        }
    }


    private void requestOTPFromServer(String number){
        CustomerRepository customerRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(CustomerRepository.class);
        customerRepository.requestOtp(number, new ObjectCallback<JSONObject>() {
            @Override
            public void onBefore() {
                enableGoButton(false);
                startProgressBar(progressBar);
            }

            @Override
            public void onSuccess(JSONObject object) {
                super.onSuccess(object);

            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, t.toString());
                enableGoButton(true);
            }

            @Override
            public void onFinally() {
                stopProgressBar(progressBar);
            }
        });

    }



    public void startProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void stopProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
    }

    // BROAD CAST RECEIVER
    public static class IncomingSms extends BroadcastReceiver {
        // Get the object of SmsManager
        public Matcher m;

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v(Constants.TAG, "Broadcast is working");
            // Retrieves a map of extended data from the intent.
            final Bundle bundle = intent.getExtras();
            try {
                if (bundle != null) {
                    final Object[] pdusObj = (Object[]) bundle.get("pdus");
                    for (int i = 0; i < pdusObj.length; i++) {

                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                        String senderNum = phoneNumber;
                        String message = currentMessage.getDisplayMessageBody();





                        Boolean found = Arrays.asList(message.split(" ")).contains(Constants.TAG);
                        if(found){
                            Pattern p = Pattern.compile("\\b\\d{4}\\b");
                            m = p.matcher(message);
                            while (m.find()) {
                                OTP = m.group().toString();
                                otpCode.setText(OTP);
                            }
                        }









                    } // end for loop
                } // bundle is null

            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" + e);
            }
        }

    }

    // COUNT DOWN
    public void setCountDown() {
        new CountDownTimer(45000, 1000) {

            public void onTick(long millisUntilFinished) {
                countDown.setVisibility(View.VISIBLE);
                countDown.setText("" + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
               stopProgressBar(progressBar);
                enableGoButton(true);
                countDown.setText("Try Again");
            }

        }.start();
    }

    // CHANGE LISTENER
    public void addChangeListener() {
        otpCode.addTextChangedListener(getTextWatcher());
    }

    public TextWatcher getTextWatcher() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int codeLength = start + count;

                String code = otpCode.getText().toString().trim();
                if (codeLength == 4) {
                    if(phoneNumber != null){
                        String mciNumber = mciCode.getText().toString().trim();
                        if(mciNumber != null){
                            if(!mciNumber.isEmpty()){
                                Presenter.getInstance().addModel(Constants.MCI_NUMBER, mciNumber);
                            }
                        }
                        LoginToGoogle(code, phoneNumber);
                    }

                }
            }//onTextChanged


            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        return textWatcher;
    }


    public void LoginToGoogle(String code, String number){
        CustomerRepository customerRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(CustomerRepository.class);
        String accessToken = Presenter.getInstance().getModel(String.class, Constants.GOOGLE_ACCESS_TOKEN);
        if(accessToken != null){
            if(!accessToken.isEmpty()){
                customerRepository.loginWithCode(accessToken, code, number, new ObjectCallback<JSONObject>() {
                    @Override
                    public void onBefore() {
                        enableGoButton(false);
                        startProgressBar(progressBar);
                    }

                    @Override
                    public void onSuccess(JSONObject object) {
                        super.onSuccess(object);
                        mainActivity.addUser(object);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        TastyToast.makeText(mainActivity.getApplicationContext(), Constants.ERROR_MESSAGE, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        Log.e(TAG, t.toString());
                    }

                    @Override
                    public void onFinally() {
                        super.onFinally();
                        stopProgressBar(progressBar);
                    }
                });
            }
        }

    }


    public boolean isPhoneValidate(String phone) {
        boolean isPhoneValid;
        Pattern phonePattern = Pattern.compile("\\d{10}");
        Matcher phoneMatcher = phonePattern.matcher(phone);
        if (!phoneMatcher.matches()) {
            //Snackbar.make(getView(), "Enter Correct Phone Number", Snackbar.LENGTH_SHORT).show();
            isPhoneValid = false;
        } else {
            isPhoneValid = true;
        }
        return  isPhoneValid;
    }

    public void enableGoButton(boolean enable) {
        if(enable) {
            goButton.setEnabled(true);
            goButton.setBackgroundResource(R.drawable.round_corner_go_button);
        } else {
            goButton.setBackgroundResource(R.drawable.round_button_go_disabled);
            goButton.setEnabled(false);
        }
    }

    public void enableOkButton(boolean enable) {
        if(enable) {
            okButton.setEnabled(true);
            okButton.setBackgroundResource(R.drawable.round_corner_go_button);
        } else {
            okButton.setEnabled(false);
            okButton.setBackgroundResource(R.drawable.round_button_go_disabled);
        }
    }

    public void enableEditText(boolean enable) {
        if(enable) {
            mobileNumber.setFocusable(true);
        } else {
            mobileNumber.setFocusable(false);
        }
    }
}
