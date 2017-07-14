package com.orthopg.snaphy.orthopg;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.ObjectCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Order;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Payment;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.PaymentRepository;
import com.orthopg.snaphy.orthopg.Fragment.BooksFragment.CheckoutFragment;
import com.orthopg.snaphy.orthopg.Fragment.BooksFragment.FailureFragment;
import com.orthopg.snaphy.orthopg.Fragment.BooksFragment.SuccessFragment;
import com.orthopg.snaphy.orthopg.Interface.OnFragmentChange;
import com.payUMoney.sdk.PayUmoneySdkInitilizer;
import com.payUMoney.sdk.SdkConstants;

import java.util.HashMap;

import nl.siegmann.epublib.epub.Main;

public class BookPurchaseActivity extends AppCompatActivity implements OnFragmentChange,
        CheckoutFragment.OnFragmentInteractionListener,
        SuccessFragment.OnFragmentInteractionListener, FailureFragment.OnFragmentInteractionListener {

    MainActivity mainActivity;
    String paymentIdNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_purchase);
        mainActivity = Presenter.getInstance().getModel(MainActivity.class, Constants.MAINACTIVITY_INSTANCE);
        replaceFragment(R.layout.fragment_checkout, null);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void replaceFragment(int id, Object object) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (id) {
            case R.layout.fragment_checkout:
                openCheckoutFragment(fragmentTransaction);
                break;

            case R.layout.fragment_success:
                openSuccessFragment(fragmentTransaction);
                break;

            case R.layout.fragment_failure:
                openFailureFragment(fragmentTransaction);
                break;
        }
    }

    public void openCheckoutFragment(FragmentTransaction fragmentTransaction){
        CheckoutFragment checkoutFragment = (CheckoutFragment)getSupportFragmentManager().findFragmentByTag(CheckoutFragment.TAG);
        if(checkoutFragment == null){
            checkoutFragment = checkoutFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.activity_book_purchase, checkoutFragment, CheckoutFragment.TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void openSuccessFragment(FragmentTransaction fragmentTransaction){
        SuccessFragment successFragment = (SuccessFragment)getSupportFragmentManager().findFragmentByTag(SuccessFragment.TAG);
        if(successFragment == null){
            successFragment = SuccessFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.activity_book_purchase, successFragment, SuccessFragment.TAG).addToBackStack(SuccessFragment.TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void openFailureFragment(FragmentTransaction fragmentTransaction){
        FailureFragment failureFragment = (FailureFragment)getSupportFragmentManager().findFragmentByTag(FailureFragment.TAG);
        if(failureFragment == null){
            failureFragment = FailureFragment.newInstance();
        }
        fragmentTransaction.replace(R.id.activity_book_purchase, failureFragment, FailureFragment.TAG).addToBackStack(FailureFragment.TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PayUmoneySdkInitilizer.PAYU_SDK_PAYMENT_REQUEST_CODE) {
            paymentIdNumber = data.getStringExtra(SdkConstants.PAYMENT_ID);
            verifyPaymentFromServer(requestCode);
        }
    }

    public void verifyPaymentFromServer(final int resultCode){
        Payment payment = Presenter.getInstance().getModel(Payment.class, Constants.PAYMENT_MODEL_DATA);
        String paymentId = payment.getId().toString();
        PaymentRepository paymentRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(PaymentRepository.class);
        paymentRepository.getPaymentStatus(new HashMap<String, Object>(), paymentIdNumber, paymentId, new ObjectCallback<Order>() {
            @Override
            public void onBefore() {
                super.onBefore();
                mainActivity.startProgressBar(mainActivity.progressBar);
            }

            @Override
            public void onSuccess(Order object) {
                super.onSuccess(object);
                if(resultCode == RESULT_OK) {
                    mainActivity.replaceFragment(R.layout.fragment_success, null);
                }
            }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
                Log.e(Constants.TAG, t.toString());
                if(resultCode == RESULT_OK){
                    Toast.makeText(mainActivity, "Contact orthopg", Toast.LENGTH_SHORT).show();
                } else{
                    mainActivity.replaceFragment(R.layout.fragment_failure, null);
                }
            }

            @Override
            public void onFinally() {
                super.onFinally();
                mainActivity.stopProgressBar(mainActivity.progressBar);
            }
        });
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
           finish();
        }
        else {
            getSupportFragmentManager().popBackStack();
        }

    }

}
