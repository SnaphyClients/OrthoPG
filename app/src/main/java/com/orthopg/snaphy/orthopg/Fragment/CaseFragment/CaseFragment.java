package com.orthopg.snaphy.orthopg.Fragment.CaseFragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.list.Listen;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Post;
import com.androidsdk.snaphy.snaphyandroidsdk.models.PostDetail;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.CustomModel.TrackList;
import com.orthopg.snaphy.orthopg.Fragment.BooksFragment.BookTestAdapter;
import com.orthopg.snaphy.orthopg.Fragment.ProfileFragment.ProfileFragment;
import com.orthopg.snaphy.orthopg.ItemClickSupport;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;
import com.orthopg.snaphy.orthopg.RecyclerItemClickListener;
import com.sdsmdg.tastytoast.TastyToast;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import java.util.HashMap;

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
   // @Bind(R.id.fragment_case_progressBar) CircleProgressBar progressBar;
    @Bind(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.fragment_case_textview1) TextView noCasePresentText;
    LinearLayoutManager linearLayoutManager;
    CaseListAdapter caseListAdapter;
    MainActivity mainActivity;
    public static String TAG = "CaseFragment";
    CasePresenter casePresenter;
    HashMap<String, TrackList> trackList;

    boolean isTrendingSelected = true;
    boolean isNewSelected = false;
    boolean isUnsolvedSelected = false;
    boolean isSavedSelected = false;
    boolean isPostedSelected = false;

    /*Infinite Loading dataset*/
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 3;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    /*Infinite Loading data set*/


    @Bind(R.id.fragment_case_button1) Button trendingButton;
    @Bind(R.id.fragment_case_button2) Button newCaseButton;
    @Bind(R.id.fragment_case_button3) Button unsolvedCaseButton;
    @Bind(R.id.fragment_case_button5) Button postedCaseButton;
    @Bind(R.id.fragment_case_button6) Button savedCaseButton;

    @Bind(R.id.fragment_case_imageview1) ImageView trendingImageView;
    @Bind(R.id.fragment_case_imageview2) ImageView newCaseImageView;
    @Bind(R.id.fragment_case_imageview3) ImageView unsolvedCaseImageView;
    @Bind(R.id.fragment_case_imageview5) ImageView postedCaseImageView;
    @Bind(R.id.fragment_case_imageview4) ImageView savedCaseImageView;

    @Bind(R.id.fragment_case_linear_layout1) LinearLayout trendingCaseLayout;
    @Bind(R.id.fragment_case_linear_layout2) LinearLayout newCaseLayout;
    @Bind(R.id.fragment_case_linear_layout3) LinearLayout unsolvedCaseLayout;
    @Bind(R.id.fragment_case_linear_layout5) LinearLayout postedCaseLayout;
    @Bind(R.id.fragment_case_linear_layout4) LinearLayout savedCaseLayout;

    @Bind(R.id.fragment_case_button4) Button postCase;

    public CaseFragment() {
        // Required empty public constructor
    }

    public static CaseFragment newInstance() {
        CaseFragment fragment = new CaseFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Default selecting trending option first..
        Constants.SELECTED_TAB = Constants.TRENDING;
        super.onCreate(savedInstanceState);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_SMS};

        if(!hasPermissions(mainActivity, PERMISSIONS)){
            ActivityCompat.requestPermissions(mainActivity, PERMISSIONS, PERMISSION_ALL);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void resetLoadingData() {
        previousTotal = 0;
        loading = true;
        visibleThreshold = 3;
        firstVisibleItem = 0;
        visibleItemCount = 0;
        totalItemCount = 0;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_case, container, false);
        ButterKnife.bind(this, view);
        Typeface typeface = Typeface.createFromAsset(mainActivity.getAssets(), "fonts/OpenSans-Regular.ttf");
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        swipeRefreshLayoutListener();
        loadPresenter();
        recyclerViewLoadMoreEventData();

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

                if(isSavedSelected) {
                    casePresenter.fetchSavedPost(Constants.SAVED, true);
                }

                if(isPostedSelected) {
                    casePresenter.fetchPostedPost(Constants.POSTED, true);
                }


            }
        });
    }//http://sapandiwakar.in/pull-to-refresh-for-android-recyclerview-or-any-other-vertically-scrolling-view/

    @OnClick(R.id.fragment_case_linear_layout1) void trendingButtonClick() {
        changeButtonColor(true, false, false, false, false);
        Constants.SELECTED_TAB = Constants.TRENDING;
        casePresenter.fetchPost(Constants.TRENDING, true);
        resetLoadingData();
    }

    @OnClick(R.id.fragment_case_linear_layout2) void newButtonClick() {
        changeButtonColor(false, true, false, false, false);
        Constants.SELECTED_TAB = Constants.LATEST;
        casePresenter.fetchPost(Constants.LATEST, true);
        resetLoadingData();

    }

    @OnClick(R.id.fragment_case_linear_layout3) void unsolvedButtonClick() {
        changeButtonColor(false, false, true, false, false);
        Constants.SELECTED_TAB = Constants.UNSOLVED;
        casePresenter.fetchPost(Constants.UNSOLVED, true);
        resetLoadingData();
    }

    @OnClick(R.id.fragment_case_linear_layout4) void savedButtonClick() {
        changeButtonColor(false, false, false, true, false);
        Constants.SELECTED_TAB = Constants.SAVED;
        casePresenter.fetchSavedPost(Constants.SAVED, true);
        resetLoadingData();
    }

    @OnClick(R.id.fragment_case_linear_layout5) void postedButtonClick() {
        changeButtonColor(false, false, false, false, true);
        Constants.SELECTED_TAB = Constants.POSTED;
        casePresenter.fetchPostedPost(Constants.POSTED, true);
        resetLoadingData();
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
                        // Refresh Items here
                        if(isTrendingSelected) {
                            casePresenter.fetchPost(Constants.TRENDING, false);
                        }

                        if(isNewSelected) {
                            casePresenter.fetchPost(Constants.LATEST, false);
                        }

                        if(isUnsolvedSelected) {
                            casePresenter.fetchPost(Constants.UNSOLVED, false);
                        }

                        if(isPostedSelected){
                            casePresenter.fetchPostedPost(Constants.POSTED, false);
                        }

                        if(isSavedSelected){
                            casePresenter.fetchSavedPost(Constants.SAVED, false);
                        }

                        loading = true;
                    }
                }
            }
        });
    }

    //Create all lists..
    private void loadPresenter(){
        //Trending list..
        casePresenter = new CasePresenter(mainActivity.snaphyHelper.getLoopBackAdapter(), mainActivity.progressBar, mainActivity, noCasePresentText);
        Presenter.getInstance().addModel(Constants.CASE_PRESENTER_ID, casePresenter);
        trackList = Presenter.getInstance().getModel(HashMap.class, Constants.LIST_CASE_FRAGMENT);
        unsubscribeAll();
        trackList.clear();
        caseListAdapter = new CaseListAdapter(mainActivity, trackList, TAG, casePresenter);
        recyclerView.setAdapter(caseListAdapter);

        if(trackList.get(Constants.TRENDING) == null){
            TrackList trendingListData = new TrackList(Constants.TRENDING);
            trackList.put(Constants.TRENDING, trendingListData);

            trendingListData.getPostDetails().subscribe(this, new Listen<PostDetail>() {
                @Override
                public void onInit(DataList<PostDetail> dataList) {
                    caseListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChange(DataList<PostDetail> dataList) {
                    super.onChange(dataList);
                    swipeRefreshLayout.setRefreshing(false);
                    caseListAdapter.notifyDataSetChanged();

                }
            });


        }

        if(trackList.get(Constants.LATEST) == null){
            TrackList latestListData = new TrackList(Constants.LATEST);
            trackList.put(Constants.LATEST, latestListData);
            latestListData.getPostDetails().subscribe(this, new Listen<PostDetail>() {
                @Override
                public void onInit(DataList<PostDetail> dataList) {
                    caseListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChange(DataList<PostDetail> dataList) {
                    super.onChange(dataList);
                    swipeRefreshLayout.setRefreshing(false);
                    caseListAdapter.notifyDataSetChanged();

                }
            });


        }


        if(trackList.get(Constants.UNSOLVED) == null){
            TrackList unsolvedListData = new TrackList(Constants.UNSOLVED);
            trackList.put(Constants.UNSOLVED, unsolvedListData);

            unsolvedListData.getPostDetails().subscribe(this, new Listen<PostDetail>() {
                @Override
                public void onInit(DataList<PostDetail> dataList) {
                    caseListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChange(DataList<PostDetail> dataList) {
                    super.onChange(dataList);
                    swipeRefreshLayout.setRefreshing(false);
                    caseListAdapter.notifyDataSetChanged();
                }
            });


        }


        if(trackList.get(Constants.SAVED) == null){
            TrackList savedListData = new TrackList(Constants.SAVED);
            trackList.put(Constants.SAVED, savedListData);

            savedListData.getPostDataList().subscribe(this, new Listen<Post>() {
                @Override
                public void onInit(DataList<Post> dataList) {
                    caseListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChange(DataList<Post> dataList) {
                    super.onChange(dataList);
                    swipeRefreshLayout.setRefreshing(false);
                    caseListAdapter.notifyDataSetChanged();
                }
            });


        }

        if(trackList.get(Constants.POSTED) == null){
            TrackList postedListData = new TrackList(Constants.POSTED);
            trackList.put(Constants.POSTED, postedListData);

            postedListData.getPostDataList().subscribe(this, new Listen<Post>() {
                @Override
                public void onInit(DataList<Post> dataList) {
                    //swipeRefreshLayout.setRefreshing(false);
                    caseListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChange(DataList<Post> dataList) {
                    super.onChange(dataList);
                    //swipeRefreshLayout.setRefreshing(false);
                    caseListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onClear() {
                    //swipeRefreshLayout.setRefreshing(false);
                    caseListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onRemove(final Post element, int index, final DataList<Post> dataList) {
                    super.onRemove(element, index, dataList);
                    caseListAdapter.notifyDataSetChanged();
                    element.destroy(new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            if(mainActivity != null){
                                TastyToast.makeText(mainActivity.getApplicationContext(), Constants.DELETE_SUCCESS_POST, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            dataList.add(element);
                            caseListAdapter.notifyDataSetChanged();
                            if(mainActivity != null){
                                TastyToast.makeText(mainActivity.getApplicationContext(), Constants.DELETE_ERROR_POST, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                            }

                        }
                    });
                }

            });

        }


        //By default fetch the trending list..
        trendingButtonClick();
    }


    public void changeButtonColor(boolean trending, boolean newCase, boolean unsolved, boolean saved, boolean posted) {
        trendingButton.setTextColor(Color.parseColor("#777777"));
        newCaseButton.setTextColor(Color.parseColor("#777777"));
        unsolvedCaseButton.setTextColor(Color.parseColor("#777777"));
        savedCaseButton.setTextColor(Color.parseColor("#777777"));
        postedCaseButton.setTextColor(Color.parseColor("#777777"));

        trendingImageView.setImageResource(R.drawable.trending);
        newCaseImageView.setImageResource(R.drawable.latest);
        unsolvedCaseImageView.setImageResource(R.drawable.unsolved);
        savedCaseImageView.setImageResource(R.drawable.saved_cases);
        postedCaseImageView.setImageResource(R.drawable.posted_cases);


        isTrendingSelected = false;
        isNewSelected = false;
        isUnsolvedSelected = false;
        isSavedSelected = false;
        isPostedSelected = false;

        trendingCaseLayout.setEnabled(true);
        newCaseLayout.setEnabled(true);
        postedCaseLayout.setEnabled(true);
        savedCaseLayout.setEnabled(true);
        unsolvedCaseLayout.setEnabled(true);


        if(trending) {
            trendingButton.setTextColor(Color.parseColor("#4686f2"));
            trendingImageView.setImageResource(R.drawable.trending_selected);
            trendingCaseLayout.setEnabled(false);
            isTrendingSelected = true;
        }

        if(newCase) {
            newCaseButton.setTextColor(Color.parseColor("#4686f2"));
            newCaseImageView.setImageResource(R.drawable.latest_selected);
            newCaseButton.setEnabled(false);
            isNewSelected = true;
        }

        if(unsolved) {
            unsolvedCaseButton.setTextColor(Color.parseColor("#4686f2"));
            unsolvedCaseImageView.setImageResource(R.drawable.unsolved_selected);
            unsolvedCaseButton.setEnabled(false);
            isUnsolvedSelected = true;
        }

        if(saved) {
            savedCaseButton.setTextColor(Color.parseColor("#4686f2"));
            savedCaseImageView.setImageResource(R.drawable.save_selected);
            savedCaseButton.setEnabled(false);
            isSavedSelected = true;
        }

        if(posted) {
            postedCaseButton.setTextColor(Color.parseColor("#4686f2"));
            postedCaseImageView.setImageResource(R.drawable.posted_cases_selected);
            postedCaseButton.setEnabled(false);
            isPostedSelected = true;
        }

    }

    @OnClick(R.id.fragment_case_button4) void postCaseButtonClick() {
        //No Network Connection
        if(!mainActivity.snaphyHelper.isNetworkAvailable()){
            Snackbar.make(postCase,"No Network Connection! Check Internet Connection and try again", Snackbar.LENGTH_SHORT).show();
        } else {
            //First create a post object then..move to fragment..
            Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
            if (customer != null) {
                if (customer.getStatus() != null) {
                    if (customer.getStatus().equals(Constants.ALLOW)) {
                        casePresenter.InitNewCaseObject();
                        mainActivity.replaceFragment(R.id.fragment_case_button4, null);
                    } else {
                        TastyToast.makeText(mainActivity.getApplicationContext(), "Verification is under process", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
                        Snackbar snackbar = Snackbar.make(postCase, Constants.MCI_VERIFICATION_TAG, Snackbar.LENGTH_SHORT);
                        snackbar
                                .setAction("Profile", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mainActivity.replaceFragment(R.layout.fragment_doctor_profile, ProfileFragment.TAG);
                                    }
                                })
                                .show();
                    }
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

    private void unsubscribeAll(){
        HashMap<String, TrackList> trackList = Presenter.getInstance().getModel(HashMap.class, Constants.LIST_CASE_FRAGMENT);
        if(trackList != null){
            if(trackList.get(Constants.TRENDING) != null){
                trackList.get(Constants.TRENDING).getPostDetails().unsubscribe(this);
            }
            if(trackList.get(Constants.LATEST) != null){
                trackList.get(Constants.LATEST).getPostDetails().unsubscribe(this);
            }

            if(trackList.get(Constants.UNSOLVED) != null){
                trackList.get(Constants.UNSOLVED).getPostDetails().unsubscribe(this);
            }

            if(trackList.get(Constants.POSTED) != null){
                trackList.get(Constants.POSTED).getPostDataList().unsubscribe(this);
            }

            if(trackList.get(Constants.SAVED) != null){
                trackList.get(Constants.SAVED).getPostDataList().unsubscribe(this);
            }


        }
        Presenter.getInstance().removeFromList(Constants.LIST_CASE_FRAGMENT);
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        unsubscribeAll();
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
