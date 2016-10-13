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
 * {@link TermsAndConditionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TermsAndConditionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TermsAndConditionsFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    public static String TAG = "TermsAndConditionsFragment";
    MainActivity mainActivity;
    @Bind(R.id.fragment_tandc_textview2) TextView termsAndConditions;
    @Bind(R.id.fragment_tandc_progressBar) CircleProgressBar progressBar;
    CompanyInfoRepository companyInfoRepository;
    String tandc;

    public TermsAndConditionsFragment() {
        // Required empty public constructor
    }


    public static TermsAndConditionsFragment newInstance() {
        TermsAndConditionsFragment fragment = new TermsAndConditionsFragment();
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
        View view = inflater.inflate(R.layout.fragment_terms_and_conditions, container, false);
        ButterKnife.bind(this, view);
        termsAndConditions.setMovementMethod(new ScrollingMovementMethod());
        setTermsAndConditionsText();
        return view;
    }

    public void setTermsAndConditionsText() {
        companyInfoRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(CompanyInfoRepository.class);
        Map<String, Object> filter = new HashMap<>();
        Map<String, String> where = new HashMap<>();
        where.put("type","t&c");
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
                        tandc = objects.get(0).getHtml().toString();
                        termsAndConditions.setText(Html.fromHtml(tandc));
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

    @OnClick(R.id.fragment_tandc_image_button1) void onBackPressed() {
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
