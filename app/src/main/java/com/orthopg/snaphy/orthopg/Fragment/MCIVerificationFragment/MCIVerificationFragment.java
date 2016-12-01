package com.orthopg.snaphy.orthopg.Fragment.MCIVerificationFragment;

import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.ObjectCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CustomerRepository;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.Map;

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
    @Bind(R.id.fragment_mci_verification_edittext1) EditText mciCode;
    MainActivity mainActivity;
    @Bind(R.id.fragment_mci_verification_imageview1) ImageView unlockView;
    @Bind(R.id.fragment_mci_verification_linearlayout1) LinearLayout linearLayout;
    @Bind(R.id.fragment_mci_verification_relative_layout1) RelativeLayout relativeLayout;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mciverification, container, false);
        ButterKnife.bind(this, view);
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
            unlockView.setVisibility(View.GONE);
        } else {
            linearLayout.setVisibility(View.VISIBLE);
            unlockView.setVisibility(View.VISIBLE);
        }
    }


    @OnClick(R.id.fragment_mci_verification_button1) void submitButton() {
        InputMethodManager imm = (InputMethodManager)mainActivity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mciCode.getWindowToken(), 0);
        String code = mciCode.getText().toString();
        if(code != null){
            if(!code.trim().isEmpty()){
                Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
                customer.setMciNumber(code);
                updateCustomer(customer);
            }
        }
        //
    }

    public void updateCustomer(Customer customer){
        Map<String, ? extends Object> data = customer.convertMap();
        //Remove the password field..
        data.remove("password");
        CustomerRepository customerRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(CustomerRepository.class);
        customerRepository.updateAttributes((String) customer.getId(), data, new ObjectCallback<Customer>() {
            @Override
            public void onBefore() {
                //TODO SHOW PROGRESS BAR..
            }

            @Override
            public void onSuccess(Customer object) {
                mainActivity.replaceFragment(R.layout.fragment_main, null);
                TastyToast.makeText(mainActivity.getApplicationContext(), "Verification is under process", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);

            }

            @Override
            public void onError(Throwable t) {
                Log.e(Constants.TAG, t.toString());
                Log.v(Constants.TAG, "Error in update Customer Method");
                //TODO SHOW TRY AGAIN TOAST..
            }

            @Override
            public void onFinally() {
                //TODO STOP PROGRESS BAR...
            }
        });
    }



    @OnClick(R.id.fragment_mci_verification_button2) void skipButton() {
        InputMethodManager imm = (InputMethodManager)mainActivity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mciCode.getWindowToken(), 0);
        mainActivity.replaceFragment(R.layout.fragment_main, null);
        TastyToast.makeText(mainActivity.getApplicationContext(), "Verification is under process", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);

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
