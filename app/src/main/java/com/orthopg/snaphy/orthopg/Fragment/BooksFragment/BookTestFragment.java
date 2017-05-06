package com.orthopg.snaphy.orthopg.Fragment.BooksFragment;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.list.Listen;
import com.androidsdk.snaphy.snaphyandroidsdk.models.BookCategory;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Payment;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookTestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookTestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookTestFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    MainActivity mainActivity;
    BooksPresenter booksPresenter;
    LinearLayoutManager linearLayoutManager;
    DataList<BookCategory> bookCategoryDataList = new DataList<>();
    BookTestAdapter bookTestAdapter;
    @Bind(R.id.fragment_books_recycler_view) RecyclerView recyclerView;

    public BookTestFragment() {
        // Required empty public constructor
    }

    public static BookTestFragment newInstance() {
        BookTestFragment fragment = new BookTestFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadPresenter();
        Payment payment = new Payment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_books1, container, false);
        ButterKnife.bind(this,view);
        linearLayoutManager = new LinearLayoutManager(mainActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        subscribe();
        return view;
    }

    public void loadPresenter(){
        booksPresenter = new BooksPresenter(mainActivity.snaphyHelper.getLoopBackAdapter(),mainActivity);
        booksPresenter.fetchBooks(true);
    }

    public void subscribe(){

        bookCategoryDataList = Presenter.getInstance().getList(BookCategory.class, Constants.BOOK_LIST_BOOKS_FRAGMENT);
        bookCategoryDataList.subscribe(this, new Listen<BookCategory>() {
            @Override
            public void onInit(DataList<BookCategory> dataList) {
                super.onInit(dataList);
                bookTestAdapter = new BookTestAdapter(mainActivity, bookCategoryDataList);
                recyclerView.setAdapter(bookTestAdapter);
            }

            @Override
            public void onChange(DataList<BookCategory> dataList) {
                super.onChange(dataList);
                bookTestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onClear() {
                super.onClear();
                bookTestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onRemove(BookCategory element, int index, DataList<BookCategory> dataList) {
                super.onRemove(element, index, dataList);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

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
