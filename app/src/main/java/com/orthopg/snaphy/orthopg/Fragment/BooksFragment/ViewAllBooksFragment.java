package com.orthopg.snaphy.orthopg.Fragment.BooksFragment;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.list.Listen;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Book;
import com.androidsdk.snaphy.snaphyandroidsdk.models.BookCategory;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingFormatArgumentException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewAllBooksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewAllBooksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewAllBooksFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    MainActivity mainActivity;
    public final static String TAG = "ViewAllBooksFragment";
    List<BookListModel> bookListModelList = new ArrayList<>();
    ViewAllBooksAdapter viewAllBooksAdapter;
    LinearLayoutManager linearLayoutManager;
    ViewAllBooksPresenter viewAllBooksPresenter;
    DataList<Book> bookDataList = new DataList<>();
    @Bind(R.id.fragment_view_all_books_recyclerview) RecyclerView recyclerView;
    @Bind(R.id.fragment_view_all_books_textview1) TextView categoryName;

    public ViewAllBooksFragment() {
        // Required empty public constructor
    }

    public static ViewAllBooksFragment newInstance() {
        ViewAllBooksFragment fragment = new ViewAllBooksFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadPresenter();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_all_books, container, false);
        ButterKnife.bind(this,view);
        setTypeface();
        linearLayoutManager = new LinearLayoutManager(mainActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        subscribe();
        BookCategory bookCategory = Presenter.getInstance().getModel(BookCategory.class, Constants.BOOK_CATEGORY_ID);
        if(bookCategory != null) {
            if(bookCategory.getName() != null) {
                categoryName.setText(bookCategory.getName());
            }
        }
        return view;
    }

    public void loadPresenter(){
        viewAllBooksPresenter = new ViewAllBooksPresenter(mainActivity.snaphyHelper.getLoopBackAdapter(), mainActivity);
        viewAllBooksPresenter.fetchAllBooks(true);
    }

    public void subscribe(){

        bookDataList = Presenter.getInstance().getList(Book.class, Constants.VIEW_ALL_BOOKS_LIST);
        bookDataList.subscribe(this, new Listen<Book>() {
            @Override
            public void onInit(DataList<Book> dataList) {
                super.onInit(dataList);
                viewAllBooksAdapter = new ViewAllBooksAdapter(mainActivity, bookDataList);
                recyclerView.setAdapter(viewAllBooksAdapter);
            }

            @Override
            public void onChange(DataList<Book> dataList) {
                super.onChange(dataList);
                viewAllBooksAdapter.notifyDataSetChanged();
            }

            @Override
            public void onClear() {
                super.onClear();
                viewAllBooksAdapter.notifyDataSetChanged();
            }

            @Override
            public void onRemove(Book element, int index, DataList<Book> dataList) {
                super.onRemove(element, index, dataList);
            }
        });
    }


    public void setTypeface(){
        Typeface typeface = Typeface.createFromAsset(mainActivity.getAssets(),"fonts/OpenSans-Bold.ttf");
        categoryName.setTypeface(typeface);
    }

    @OnClick(R.id.fragment_view_all_books_imageview1) void onBack(){
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
