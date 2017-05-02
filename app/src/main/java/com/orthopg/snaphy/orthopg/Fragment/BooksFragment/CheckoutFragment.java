package com.orthopg.snaphy.orthopg.Fragment.BooksFragment;

import android.content.Context;
import android.content.Intent;
import android.location.SettingInjectorService;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.ObjectCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Book;
import com.androidsdk.snaphy.snaphyandroidsdk.models.BookDetail;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Payment;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.PaymentRepository;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.PayUActivity;
import com.orthopg.snaphy.orthopg.R;
import com.payUMoney.sdk.PayUmoneySdkInitilizer;
import com.payUMoney.sdk.SdkConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.CONSUMER_IR_SERVICE;

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

    String merchantId;
    String key;
    String txnId;
    double amount;
    String productInfo;
    String firstName;
    String email;
    String salt;
    String address;
    String phoneNumber;
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
        if(pincode.getText().toString().isEmpty()){
            Snackbar.make(pincode, "Pincode can't be empty", Snackbar.LENGTH_SHORT).show();
        } else if(houseNo.getText().toString().isEmpty()){
            Snackbar.make(houseNo, "House No. can't be empty", Snackbar.LENGTH_SHORT).show();
        } else if(street.getText().toString().isEmpty()){
            Snackbar.make(street, "Street/Colony can't be empty", Snackbar.LENGTH_SHORT).show();
        } else if(city.getText().toString().isEmpty()){
            Snackbar.make(city, "City can't be empty", Snackbar.LENGTH_SHORT).show();
        } else{
            if(!landmark.getText().toString().isEmpty()) {
                address = houseNo.getText().toString() + " " + street.getText().toString() + " " + landmark.getText().toString() + " " + city.getText().toString() + " " + pincode.getText().toString();
            } else{
                address = houseNo.getText().toString() + " " + street.getText().toString() + " " + city.getText().toString() + " " + pincode.getText().toString();
            }

            Book book = Presenter.getInstance().getModel(Book.class, Constants.BOOK_DESCRIPTION_ID);
            Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
            BookDetail bookDetail = Presenter.getInstance().getModel(BookDetail.class, Constants.BOOK_DETAIL_MODEL_VALUE);
            String bookType = Presenter.getInstance().getModel(String.class, Constants.BOOK_TYPE);
            HashMap<String, Object> bookDetailObject = new HashMap<>();
            bookDetailObject.put("bookId", book.getTitle());
            bookDetailObject.put("type", bookType);
            PaymentRepository paymentRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(PaymentRepository.class);
            paymentRepository.addStorage(mainActivity);
            HashMap<String, Object> hashMap = new HashMap<>();
            Payment payment = paymentRepository.createObject(hashMap);
            payment.setAddress(address);
            payment.setPhoneNumber(customer.getPhoneNumber());
            payment.setEmail(customer.getEmail());
            payment.setBookId(book.getId());
            payment.setBookDetail(bookDetailObject);
            payment.setCustomerId(customer.getId());
            if(bookType.equals(Constants.EBOOK_BOOK_TYPE)){
                payment.setAmount(Double.parseDouble(book.getEbookPrice()));
            } else{
                payment.setAmount(Double.parseDouble(book.getHardCopyPrice()));
            }

            Map<String,? extends Object> paymentObj = payment.toMap();
            paymentObj.remove("book");
            paymentObj.remove("customer");
            paymentObj.remove("status");
            paymentObj.remove("id");

            paymentRepository.create(paymentObj, new ObjectCallback<Payment>() {
                @Override
                public void onSuccess(Payment object) {
                    super.onSuccess(object);
                    openPayUFragment();
                    Presenter.getInstance().addModel(Constants.PAYMENT_MODEL_DATA, object);
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    Log.e(Constants.TAG, t.toString());
                }
            });

        }


    }

    public void openPayUFragment(){
        Random rand = new Random();
        String randomString = Integer.toString(rand.nextInt()) + (System.currentTimeMillis() / 1000L);
        txnId = hashCal("SHA-256", randomString).substring(0, 20);
        Payment payment = Presenter.getInstance().getModel(Payment.class, Constants.PAYMENT_MODEL_DATA);
        Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
        merchantId = "5076054";
        key = "ADq70R";
        amount = payment.getAmount();
        productInfo = String.valueOf(payment.getBookDetail().get("bookName"));
        firstName = customer.getFirstName();
        email = payment.getEmail();
        phoneNumber = payment.getPhoneNumber();
        salt = "znBn6Zf1";
        PayUmoneySdkInitilizer.PaymentParam.Builder builder = new PayUmoneySdkInitilizer.PaymentParam.Builder()
                .setMerchantId(merchantId)
                .setKey(key)
                .setIsDebug(false)
                .setAmount(amount)
                .setTnxId(txnId)
                .setPhone(phoneNumber)
                .setProductName(productInfo)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl("https://www.PayUmoney.com/mobileapp/PayUmoney/success.php")
                .setfUrl("https://www.PayUmoney.com/mobileapp/PayUmoney/failure.php")
                .setUdf1("")
                .setUdf2("")
                .setUdf3("")
                .setUdf4("")
                .setUdf5("");
        PayUmoneySdkInitilizer.PaymentParam paymentParam = builder.build();

        String hashSequence = key + "|" + txnId + "|" + amount + "|" + productInfo + "|" + firstName + "|" + email + "|udf1|udf2|udf3|udf4|udf5|" + salt;
        String localCalulatedHash = hashCal("SHA-512", hashSequence);
        paymentParam.setMerchantHash(localCalulatedHash);
        PayUmoneySdkInitilizer.startPaymentActivityForResult(mainActivity, paymentParam);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent
            data) {
        if(requestCode ==
                PayUmoneySdkInitilizer.PAYU_SDK_PAYMENT_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                Log.i(TAG, "Success - Payment ID : " +
                        data.getStringExtra(SdkConstants.PAYMENT_ID));
                String paymentId =
                        data.getStringExtra(SdkConstants.PAYMENT_ID);
            } else if (resultCode == RESULT_CANCELED) {
                Log.i(TAG, "cancelled");
            } else if (resultCode == PayUmoneySdkInitilizer.RESULT_FAILED) {
                Log.i(TAG, "failure");
            } else if (resultCode == PayUmoneySdkInitilizer.RESULT_BACK) {
                Log.i(TAG, "User returned without login");
            }
        }
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


    private void calculateServerSideHashAndInitiatePayment(final PayUmoneySdkInitilizer.PaymentParam paymentParam, String localHash) {
        PayUmoneySdkInitilizer.startPaymentActivityForResult(mainActivity, paymentParam);
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
