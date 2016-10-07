package com.orthopg.snaphy.orthopg.Fragment.CaseFragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
    @Bind(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager linearLayoutManager;
    CaseListAdapter caseListAdapter;
    MainActivity mainActivity;;
    public static String TAG = "CaseFragment";
    CasePresenter casePresenter;
    DataList<PostDetail> postDetails;

    boolean isTrendingSelected = true;
    boolean isNewSelected = false;
    boolean isUnsolvedSelected = false;

    /*Infinite Loading dataset*/
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 3;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    /*Infinite Loading data set*/


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
        swipeRefreshLayoutListener();
        loadPresenter();
        //recyclerViewLoadMoreEventData();
        return view;
    }

    public void swipeRefreshLayoutListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh Items here
                if(isTrendingSelected) {
                    casePresenter.fetchPost(Constants.TRENDING, true);
                }

                if(isNewSelected) {
                    casePresenter.fetchPost(Constants.LATEST, true);
                }

                if(isUnsolvedSelected) {
                    casePresenter.fetchPost(Constants.UNSOLVED, true);
                }
            }
        });
    }//http://sapandiwakar.in/pull-to-refresh-for-android-recyclerview-or-any-other-vertically-scrolling-view/

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

    public void recyclerViewLoadMoreEventData() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    visibleItemCount = recyclerView.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount)
                            <= (firstVisibleItem + visibleThreshold)) {
                        //Fetch more data here
                        //EventBus.getDefault().post(TrackCollection.progressBar, Constants.REQUEST_LOAD_MORE_EVENT_FROM_HOME_FRAGMENT);
                        loading = true;
                    }
                }
            }
        });
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
                swipeRefreshLayout.setRefreshing(false);
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

        isTrendingSelected = false;
        isNewSelected = false;
        isUnsolvedSelected = false;

        if(trending) {
            trendingButton.setTextColor(Color.parseColor("#3F51B5"));
            isTrendingSelected = true;
        }

        if(newCase) {
            newCaseButton.setTextColor(Color.parseColor("#3F51B5"));
            isNewSelected = true;
        }

        if(unsolved) {
            unsolvedCaseButton.setTextColor(Color.parseColor("#3F51B5"));
            isUnsolvedSelected = true;
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
        Presenter.getInstance().removeFromList(Constants.POST_DETAIL_LIST_CASE_FRAGMENT);
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
