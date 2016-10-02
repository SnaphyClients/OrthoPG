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

import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;
import com.orthopg.snaphy.orthopg.RecyclerItemClickListener;

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
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    NewsListAdapter newsListAdapter;
    MainActivity mainActivity;
    List<NewsModel> newsModelList = new ArrayList<>();

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
        setInitialData();
        newsListAdapter = new NewsListAdapter(mainActivity, newsModelList);
        recyclerView.setAdapter(newsListAdapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(mainActivity, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent= new Intent(Intent.ACTION_VIEW,Uri.parse("http://timesofindia.indiatimes.com/business/india-business/Revenue-gained-from-Income-Declaration-Scheme-to-be-spent-on-infrastructure-rural-economy/articleshow/54635163.cms"));
                        startActivity(intent);
                    }
                })
        );

        return view;
    }

    public void setInitialData() {
        newsModelList.add(new NewsModel(getActivity().getResources().getDrawable(R.drawable.demo_news_image_1), "news", "Foot and Ankel", "An elderly frail gentleman presented to casualty after having a fall." +
                " Shortly after, he developed chest pain. He had fractured his proximal right femur."));
        newsModelList.add(new NewsModel(getActivity().getResources().getDrawable(R.drawable.demo_news_image_2), "news", "Shoulder and Elbow", "He was assessed by the cardiology team, whom he was known to, stabilized and prepared him for operation." +
                " The following day he developed further chest pain and ECG changes, which resulted in postponing the surgery."));
        newsModelList.add(new NewsModel(getActivity().getResources().getDrawable(R.drawable.demo_news_image_3), "news", "Pelvic Trauma", "In the case mentioned above, the patient presented with proximal femur fracture and myocardial infarction. "));
        newsModelList.add(new NewsModel(getActivity().getResources().getDrawable(R.drawable.demo_news_image_4), "Adv", "Zeal Cough Syrup", "The cardiologists treated him conservatively because the risk of intervention and surgery was higher than the surgery alone." +
                " The patient was well informed of the potential high risk of mortality in view of his underlying medical problems."));
        newsModelList.add(new NewsModel(getActivity().getResources().getDrawable(R.drawable.demo_news_image_5), "news", "Knee Dislocation", "He disliked the conservative management of his fractured hip, by means of analgesia and physiotherapy. He was very keen to undergo the operation," +
                " as he wanted to get back on his feet and independent life. Hence he made his mind against all the odds."));
        newsModelList.add(new NewsModel(getActivity().getResources().getDrawable(R.drawable.demo_news_image_6), "news", "Bilateral Femoral Fractures", "Patient was prepared for the surgery and had a successful dynamic hip screw operation."));
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
