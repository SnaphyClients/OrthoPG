package com.orthopg.snaphy.orthopg.Fragment.CaseFragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.list.Listen;
import com.androidsdk.snaphy.snaphyandroidsdk.models.PostDetail;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CaseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CaseFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    @Bind(R.id.fragment_case_recycler_view) RecyclerView recyclerView;
    @Bind(R.id.fragment_case_progressBar) CircleProgressBar progressBar;
    LinearLayoutManager linearLayoutManager;
    CaseListAdapter caseListAdapter;
    MainActivity mainActivity;
    List<CaseModel> caseModelList = new ArrayList<>();
    List<Drawable> imageList = new ArrayList<>();
    public static String TAG = "CaseFragment";
    CasePresenter casePresenter;
    DataList<PostDetail> postDetails;


    @Bind(R.id.fragment_case_button1) Button trendingButton;
    @Bind(R.id.fragment_case_button2) Button newCaseButton;
    @Bind(R.id.fragment_case_button3) Button unsolvedCaseButton;

    public CaseFragment() {
        // Required empty public constructor
    }

    public static CaseFragment newInstance() {
        CaseFragment fragment = new CaseFragment();
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
        View view = inflater.inflate(R.layout.fragment_case, container, false);
        ButterKnife.bind(this, view);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        loadPresenter();
        return view;
    }

    @OnClick(R.id.fragment_case_button1) void trendingButtonClick() {
        changeButtonColor(true, false, false);
        casePresenter.fetchPost(Constants.TRENDING, true);
    }

    @OnClick(R.id.fragment_case_button2) void newButtonClick() {
        changeButtonColor(false, true, false);
        casePresenter.fetchPost(Constants.LATEST, true);
    }

    @OnClick(R.id.fragment_case_button3) void unsolvedButtonClick() {
        changeButtonColor(false, false, true);
        casePresenter.fetchPost(Constants.UNSOLVED, true);
    }


    private void loadPresenter(){
        casePresenter = new CasePresenter(mainActivity.snaphyHelper.getLoopBackAdapter(), progressBar, mainActivity);
        postDetails = Presenter.getInstance().getList(PostDetail.class, Constants.POST_DETAIL_LIST_CASE_FRAGMENT);
        //By default fetch the trending list..
        trendingButtonClick();
        postDetails.subscribe(this, new Listen<PostDetail>() {
            @Override
            public void onInit(DataList<PostDetail> dataList) {
                super.onInit(dataList);
                caseListAdapter = new CaseListAdapter(mainActivity, dataList, TAG);
                recyclerView.setAdapter(caseListAdapter);
            }

            @Override
            public void onChange(DataList<PostDetail> dataList) {
                super.onChange(dataList);
                caseListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onClear() {
                super.onClear();
                caseListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onRemove(PostDetail element, DataList<PostDetail> dataList) {
                super.onRemove(element, dataList);
            }
        });
    }




    public void changeButtonColor(boolean trending, boolean newCase, boolean unsolved) {
        trendingButton.setTextColor(Color.parseColor("#777777"));
        newCaseButton.setTextColor(Color.parseColor("#777777"));
        unsolvedCaseButton.setTextColor(Color.parseColor("#777777"));

        if(trending) {
            trendingButton.setTextColor(Color.parseColor("#3F51B5"));
        }

        if(newCase) {
            newCaseButton.setTextColor(Color.parseColor("#3F51B5"));
        }

        if(unsolved) {
            unsolvedCaseButton.setTextColor(Color.parseColor("#3F51B5"));
        }
    }

    @OnClick(R.id.fragment_case_button4) void postCaseButtonClick() {
        mainActivity.replaceFragment(R.id.fragment_case_button4, null);
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

    @Override
    public void onDestroy(){
        super.onDestroy();
        postDetails.unsubscribe(this);
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
