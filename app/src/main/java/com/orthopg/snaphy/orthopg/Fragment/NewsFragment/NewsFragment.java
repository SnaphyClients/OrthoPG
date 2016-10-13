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

import java.util.ArrayList;
import java.util.List;

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
    List<NewsModel> newsModelList = new ArrayList<>();

    NewsPresenter newsPresenter;
    DataList<News> newsDataList;

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

        final Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
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
            public void onRemove(News element, DataList<News> dataList) {
                super.onRemove(element, dataList);
            }
        });

        newsPresenter.fetchNews(true);
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
