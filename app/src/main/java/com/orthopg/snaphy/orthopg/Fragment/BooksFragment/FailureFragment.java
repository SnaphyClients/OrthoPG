package com.orthopg.snaphy.orthopg.Fragment.BooksFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.models.Book;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Payment;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.orthopg.snaphy.orthopg.BookPurchaseActivity;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FailureFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FailureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FailureFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    MainActivity mainActivity;
    BookPurchaseActivity bookPurchaseActivity;
    public final static String TAG = "FailureFragment";
    @Bind(R.id.fragment_failure_textview2) TextView txnIdText;
    @Bind(R.id.fragment_failure_textview3) TextView price;
    @Bind(R.id.fragment_failure_textview4) TextView bookName;

    public FailureFragment() {
        // Required empty public constructor
    }

    public static FailureFragment newInstance() {
        FailureFragment fragment = new FailureFragment();
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
        View view = inflater.inflate(R.layout.fragment_failure, container, false);
        ButterKnife.bind(this, view);
        mainActivity = Presenter.getInstance().getModel(MainActivity.class, Constants.MAINACTIVITY_INSTANCE);
        setFailureData();
        return view;
    }

    public void setFailureData(){
        Payment payment = Presenter.getInstance().getModel(Payment.class, Constants.PAYMENT_MODEL_DATA);
        String txnId = Presenter.getInstance().getModel(String.class, Constants.GENERATED_TRANSACTION_ID);
        txnIdText.setText(txnId);
        price.setText(String.valueOf(payment.getAmount()));
        Book book = Presenter.getInstance().getModel(Book.class, Constants.BOOK_DESCRIPTION_ID);
        bookName.setText(book.getTitle());
    }


    @OnClick(R.id.fragment_failure_button1) void gotoHome(){
        bookPurchaseActivity.getSupportFragmentManager().popBackStack();
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
        bookPurchaseActivity = (BookPurchaseActivity) getActivity();
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
