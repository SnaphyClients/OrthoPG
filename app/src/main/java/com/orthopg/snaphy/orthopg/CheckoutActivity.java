package com.orthopg.snaphy.orthopg;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;

/**
 * Created by nikita on 5/5/17.
 */

public class CheckoutActivity extends Activity {

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
    String paymentIdNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_checkout);

    }
}
