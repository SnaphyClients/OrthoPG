package com.orthopg.snaphy.orthopg.Fragment.BooksFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;
import com.payUMoney.sdk.PayUmoneySdkInitilizer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookDescriptionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookDescriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookDescriptionFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    MainActivity mainActivity;
    public final static String TAG = "BookDescriptionFragment";

    public BookDescriptionFragment() {
        // Required empty public constructor
    }

    public static BookDescriptionFragment newInstance() {
        BookDescriptionFragment fragment = new BookDescriptionFragment();
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
        View view = inflater.inflate(R.layout.fragment_book_description, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @OnClick(R.id.fragment_book_description_button3) void onHardCopyBuy(){

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

    @OnClick(R.id.fragment_book_description_button4) void onEbookBuy(){

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
