package com.orthopg.snaphy.orthopg.Fragment.MenuFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.DataListCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.CompanyInfo;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CompanyInfoRepository;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FAQsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FAQsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FAQsFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    public static String TAG = "FAQsFragment";
    MainActivity mainActivity;
    @Bind(R.id.fragment_faqs_progressBar) CircleProgressBar progressBar;
    @Bind(R.id.fragment_faqs_textview2)
    TextView faqText;
    CompanyInfoRepository companyInfoRepository;
    String faqs;

    public FAQsFragment() {
        // Required empty public constructor
    }


    public static FAQsFragment newInstance() {
        FAQsFragment fragment = new FAQsFragment();
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
        View view = inflater.inflate(R.layout.fragment_faqs, container, false);
        ButterKnife.bind(this, view);
        faqText.setMovementMethod(new ScrollingMovementMethod());
        setFAQsText();
        return view;
    }

    public void setFAQsText() {
        companyInfoRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(CompanyInfoRepository.class);
        Map<String, Object> filter = new HashMap<>();
        Map<String, String> where = new HashMap<>();
        where.put("type","faqs");
        filter.put("where", where);

        companyInfoRepository.find(filter, new DataListCallback<CompanyInfo>() {
            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onSuccess(DataList<CompanyInfo> objects) {
                super.onSuccess(objects);
                if (objects != null) {
                    if (objects.size() != 0) {
                        faqs = objects.get(0).getHtml().toString();
                        faqText.setText(Html.fromHtml(faqs));
                        mainActivity.stopProgressBar(progressBar);
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
                Log.e(Constants.TAG, t + "");
               /* if(rootview != null) {
                    Snackbar.make(rootview, "Error fetching data", Snackbar.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onFinally() {
                super.onFinally();
                mainActivity.stopProgressBar(progressBar);
            }
        });
    }

    @OnClick(R.id.fragment_faqs_image_button1) void backButton() {
        mainActivity.onBackPressed();
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
