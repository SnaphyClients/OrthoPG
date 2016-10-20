package com.orthopg.snaphy.orthopg.Fragment.NewsFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.list.Listen;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.models.News;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;
import com.orthopg.snaphy.orthopg.RecyclerItemClickListener;
import com.sdsmdg.tastytoast.TastyToast;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    @Bind(R.id.fragment_news_recycler_view) RecyclerView recyclerView;
    @Bind(R.id.fragment_news_progressBar) CircleProgressBar progressBar;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    NewsListAdapter newsListAdapter;
    MainActivity mainActivity;
    NewsPresenter newsPresenter;
    DataList<News> newsDataList;

    /*Infinite Loading dataset*/
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 3;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    /*Infinite Loading data set*/

    public NewsFragment() {
        // Required empty public constructor
    }

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
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
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        loadPresenter();
        recyclerViewLoadMoreEventData();

        final Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
        if(customer != null){
            final String MCINumber = customer.getMciNumber() != null ? customer.getMciNumber() : "";
            recyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(mainActivity, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            News news = newsDataList.get(position);
                            if(news.getUrl() != null) {
                                if(!news.getUrl().isEmpty()) {
                                    if(!MCINumber.isEmpty()) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.getUrl()));
                                        startActivity(intent);
                                    } else {
                                        TastyToast.makeText(mainActivity.getApplicationContext(), "Verification is under process", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
                                    }
                                }
                            }

                        }
                    })
            );
        }



        return view;
    }

    public void loadPresenter() {
        newsPresenter = new NewsPresenter(mainActivity.snaphyHelper.getLoopBackAdapter(), progressBar, mainActivity);
        newsDataList = Presenter.getInstance().getList(News.class, Constants.NEWS_LIST_NEWS_FRAGMENT);

        newsDataList.subscribe(this, new Listen<News>() {
            @Override
            public void onInit(DataList<News> dataList) {
                newsListAdapter = new NewsListAdapter(mainActivity, dataList);
                recyclerView.setAdapter(newsListAdapter);
            }

            @Override
            public void onChange(DataList<News> dataList) {
                newsListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onClear() {
                newsListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onRemove(News element, int index, DataList<News> dataList) {
                super.onRemove(element, index, dataList);
            }
        });

        newsPresenter.fetchNews(true);
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
                    totalItemCount = staggeredGridLayoutManager.getItemCount();
                    firstVisibleItem = staggeredGridLayoutManager.findFirstVisibleItemPositions(null)[0];

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
                        newsPresenter.fetchNews(false);
                        loading = true;
                    }
                }
            }
        });
    }



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
        void onFragmentInteraction(Uri uri);
    }
}
