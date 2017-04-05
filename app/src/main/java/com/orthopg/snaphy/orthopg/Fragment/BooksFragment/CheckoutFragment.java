package com.orthopg.snaphy.orthopg.Fragment.BooksFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;
import com.payUMoney.sdk.PayUmoneySdkInitilizer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CheckoutFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CheckoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckoutFragment extends android.support.v4.app.Fragment {

    MainActivity mainActivity;
    private OnFragmentInteractionListener mListener;
    public final static String TAG = "CheckoutFragment";
    @Bind(R.id.fragment_checkout_textview1) TextView nameTxt;
    @Bind(R.id.fragment_checkout_textview2) TextView mobileTxt;
    @Bind(R.id.fragment_checkout_textview3) TextView pincodeTxt;
    @Bind(R.id.fragment_checkout_textview4) TextView houseTxt;
    @Bind(R.id.fragment_checkout_textview5) TextView streetTxt;
    @Bind(R.id.fragment_checkout_textview6) TextView landmarkTxt;
    @Bind(R.id.fragment_checkout_textview7) TextView cityTxt;
    @Bind(R.id.fragment_checkout_editText1) EditText name;
    @Bind(R.id.fragment_checkout_editText2) EditText mobileNo;
    @Bind(R.id.fragment_checkout_editText3) EditText pincode;
    @Bind(R.id.fragment_checkout_editText4) EditText houseNo;
    @Bind(R.id.fragment_checkout_editText5) EditText street;
    @Bind(R.id.fragment_checkout_editText6) EditText landmark;
    @Bind(R.id.fragment_checkout_editText7) EditText city;
    public CheckoutFragment() {
        // Required empty public constructor
    }

    public static CheckoutFragment newInstance() {
        CheckoutFragment fragment = new CheckoutFragment();
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
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);
        ButterKnife.bind(this,view);
        getCustomerData();
        return view;
    }

    @OnClick(R.id.fragment_checkout_imageview1) void onBack(){
        mainActivity.onBackPressed();
    }

    @OnClick(R.id.fragment_checkout_button1) void onPay(){
        PayUmoneySdkInitilizer.PaymentParam.Builder builder = new PayUmoneySdkInitilizer.PaymentParam.Builder()
                .setMerchantId("XXX")
                .setKey("YYY")
                .setIsDebug(true)
                .setAmount(10)
                .setTnxId("Onf7" + System.currentTimeMillis())
                .setPhone("9876543210")
                .setProductName("Product name")
                .setFirstName("Name")
                .setEmail("test@payu.com")
                .setsUrl("https://www.PayUmoney.com/mobileapp/PayUmoney/success.php")
                .setfUrl("https://www.PayUmoney.com/mobileapp/PayUmoney/failure.php")
                .setUdf1("")
                .setUdf2("")
                .setUdf3("")
                .setUdf4("")
                .setUdf5("");
        PayUmoneySdkInitilizer.PaymentParam paymentParam = builder.build();
        String hashSequence = "key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|salt";
        String serverCalulatedHash = hashCal("SHA-512", hashSequence);
        paymentParam.setMerchantHash(serverCalulatedHash);
        PayUmoneySdkInitilizer.startPaymentActivityForResult(mainActivity, paymentParam);
    }

    public static String hashCal(String type, String str){

        byte[] hashSeq = str.getBytes();
        StringBuffer hexString = new StringBuffer();
        try{
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashSeq);
            byte[] messageDigest = algorithm.digest();
            for(int i=0;i<messageDigest.length;i++){
                String hex = Integer.toHexString(0xFF &messageDigest[i]);
                if(hex.length() == 1){
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        }catch (NoSuchAlgorithmException e){

        }
        return hexString.toString();
    }

    public void getCustomerData(){
        Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
        if(customer!=null){
            if(customer.getFirstName()!=null) {
                if (!customer.getFirstName().isEmpty()) {
                    if (customer.getLastName() != null) {
                        if (!customer.getLastName().isEmpty()){
                            name.setText(customer.getFirstName().toString() + " " + customer.getLastName().toString());
                        }
                    } else{
                        name.setText(customer.getFirstName().toString());
                    }
                }
            }

            if(customer.getPhoneNumber()!=null){
                if(!customer.getPhoneNumber().isEmpty()){
                    mobileNo.setText(customer.getPhoneNumber());
                }
            }

            if(customer.getCurrentCity()!=null){
                if(!customer.getCurrentCity().isEmpty()){
                    city.setText(customer.getCurrentCity());
                }
            }

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
        mainActivity = (MainActivity)getActivity();
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
