package com.orthopg.snaphy.orthopg.Fragment.BooksFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.list.Listen;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Book;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BooksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BooksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BooksFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    @Bind(R.id.fragment_books_recycler_view) RecyclerView recyclerView;
    @Bind(R.id.fragment_books_progressBar) CircleProgressBar progressBar;
    LinearLayoutManager linearLayoutManager;
    BooksListAdapter booksListAdapter;
    List<BooksModel> booksModelList = new ArrayList<>();
    MainActivity mainActivity;
    DataList<Book> bookDataList;
    BooksPresenter booksPresenter;
    public BooksFragment() {
        // Required empty public constructor
    }


    public static BooksFragment newInstance() {
        BooksFragment fragment = new BooksFragment();
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
        View view = inflater.inflate(R.layout.fragment_books, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        //setInitialData();

        loadPresenter();
        return view;
    }

    public void setInitialData() {
        booksModelList.add(new BooksModel(getActivity().getResources().getDrawable(R.drawable.demo_book_5),getActivity().getResources().getDrawable(R.drawable.demo_book_6),"The Doctor In War","All treated orthodontic cases display some deficiencies. Also, case records may not conform to exact ABO specifications." +
                " The example case presentations represent those that successfully completed the ABO Clinical Examination. ",true));
        booksModelList.add(new BooksModel(getActivity().getResources().getDrawable(R.drawable.demo_book_5),getActivity().getResources().getDrawable(R.drawable.demo_book_6),"Plant Metabolic Networks","These examples are intended only as a guide, as presentation requirements are subject to change." +
                " Examinees should carefully follow the current exam year requirements when preparing case reports and records.",true));
        booksModelList.add(new BooksModel(getActivity().getResources().getDrawable(R.drawable.demo_book_5),getActivity().getResources().getDrawable(R.drawable.demo_book_6),"Dreaming Of Sweden","A collage of study model images is not part of the case presentation. Initial and/or interim study models will be submitted" +
                " in plaster or in digital format according to current year specifications.",true));
        booksModelList.add(new BooksModel(getActivity().getResources().getDrawable(R.drawable.demo_book_5),getActivity().getResources().getDrawable(R.drawable.demo_book_6),"Callum Chapman","All examinees will electronically submit the Case Report Work File (CRWF) for each case using the ABO website electronic" +
                " submission portal.",true));
    }


    public void loadPresenter() {
        booksPresenter = new BooksPresenter(mainActivity.snaphyHelper.getLoopBackAdapter(), progressBar, mainActivity);
        bookDataList = Presenter.getInstance().getList(Book.class, Constants.BOOK_LIST_BOOKS_FRAGMENT);

        bookDataList.subscribe(this, new Listen<Book>() {
                    @Override
                    public void onInit(DataList<Book> dataList) {
                        super.onInit(dataList);
                        booksListAdapter = new BooksListAdapter(mainActivity, dataList);
                        recyclerView.setAdapter(booksListAdapter);
                    }

                    @Override
                    public void onChange(DataList<Book> dataList) {
                        super.onChange(dataList);
                        booksListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onClear() {
                        super.onClear();
                        booksListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onRemove(Book element, DataList<Book> dataList) {
                        super.onRemove(element, dataList);
                    }
                } );

                booksPresenter.fetchBooks(true);
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
