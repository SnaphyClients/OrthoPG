package com.orthopg.snaphy.orthopg.Fragment.BooksFragment;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.list.Listen;
import com.androidsdk.snaphy.snaphyandroidsdk.models.BookCategory;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
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
    List<BookModel> bookModelList = new ArrayList<>();
    List<BookListModel> bookListModelList2 = new ArrayList<>();
    List<BookListModel> bookListModelList3 = new ArrayList<>();
    List<BookListModel> bookListModelList4 = new ArrayList<>();
    BookTestAdapter bookTestAdapter;
    BookListTestAdapter bookListTestAdapter;
    //@Bind(R.id.fragment_books_textview2) TextView viewAll;
    //@Bind(R.id.fragment_books_textview1) TextView savedBooks;
    @Bind(R.id.fragment_books_recycler_view) RecyclerView recyclerView;
    //@Bind(R.id.fragment_books_recycler_view1) RecyclerView recyclerView_saved_books;

    public BookTestFragment() {
        // Required empty public constructor
    }

    List<BookListModel> bookListModelList = new ArrayList<>();
    List<BookListModel> bookListModelList1 = new ArrayList<>();
    public static BookTestFragment newInstance() {
        BookTestFragment fragment = new BookTestFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadPresenter();
        /*initializeBookListData();
        initializeBookListData1();
        initializeBookListData2();
        initializeBookListData3();
        initializeBookListData4();
        initializeBookData();*/
    }

    public void initializeBookListData(){

        bookListModelList.add(new BookListModel("Theory Of Everything",getResources().getDrawable(R.drawable.bookcover1)));
        bookListModelList.add(new BookListModel("Alchemist",getResources().getDrawable(R.drawable.bookcover2)));
        bookListModelList.add(new BookListModel("Angels and Demons",getResources().getDrawable(R.drawable.bookcover3)));
        bookListModelList.add(new BookListModel("Road Not Taken",getResources().getDrawable(R.drawable.bookcover4)));

    }

    public void initializeBookListData1(){

        bookListModelList1.add(new BookListModel("Brief history of time",getResources().getDrawable(R.drawable.bookcover5)));
        bookListModelList1.add(new BookListModel("GooseBumps",getResources().getDrawable(R.drawable.bookcover6)));
        bookListModelList1.add(new BookListModel("Oliver twist",getResources().getDrawable(R.drawable.bookcover7)));
        bookListModelList1.add(new BookListModel("Brief history of time",getResources().getDrawable(R.drawable.bookcover8)));
        bookListModelList1.add(new BookListModel("Brief history of time",getResources().getDrawable(R.drawable.sampleimage)));
    }

    public void initializeBookListData2(){

        bookListModelList2.add(new BookListModel("Brief history of time",getResources().getDrawable(R.drawable.bookcover3)));
        bookListModelList2.add(new BookListModel("GooseBumps",getResources().getDrawable(R.drawable.bookcover1)));
        bookListModelList2.add(new BookListModel("Oliver twist",getResources().getDrawable(R.drawable.bookcover4)));
        bookListModelList2.add(new BookListModel("Brief history of time",getResources().getDrawable(R.drawable.bookcover5)));
        bookListModelList2.add(new BookListModel("Brief history of time",getResources().getDrawable(R.drawable.sampleimage)));
    }

    public void initializeBookListData3(){

        bookListModelList3.add(new BookListModel("Brief history of time",getResources().getDrawable(R.drawable.bookcover5)));
        bookListModelList3.add(new BookListModel("GooseBumps",getResources().getDrawable(R.drawable.bookcover6)));
        bookListModelList3.add(new BookListModel("Oliver twist",getResources().getDrawable(R.drawable.bookcover7)));
        bookListModelList3.add(new BookListModel("Brief history of time",getResources().getDrawable(R.drawable.bookcover8)));
        bookListModelList3.add(new BookListModel("Brief history of time",getResources().getDrawable(R.drawable.sampleimage)));
    }

    public void initializeBookListData4(){

        bookListModelList4.add(new BookListModel("Brief history of time",getResources().getDrawable(R.drawable.bookcover8)));
        bookListModelList4.add(new BookListModel("GooseBumps",getResources().getDrawable(R.drawable.bookcover3)));
        bookListModelList4.add(new BookListModel("Oliver twist",getResources().getDrawable(R.drawable.bookcover7)));
        bookListModelList4.add(new BookListModel("Brief history of time",getResources().getDrawable(R.drawable.bookcover1)));
        bookListModelList4.add(new BookListModel("Brief history of time",getResources().getDrawable(R.drawable.sampleimage)));
    }





    public void initializeBookData(){
      bookModelList.add(new BookModel("Downloaded Books",bookListModelList,"Saved Books"));
      bookModelList.add(new BookModel("Cardiac",bookListModelList1,"New Books"));
      bookModelList.add(new BookModel("Ortho",bookListModelList2,"New Books"));
      bookModelList.add(new BookModel("Dental",bookListModelList3, "New Books"));
      bookModelList.add(new BookModel("NeuroLogy",bookListModelList4, "New Books"));
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
        /*Typeface font = Typeface.createFromAsset(mainActivity.getAssets(), "fonts/OpenSans-Bold.ttf");
        savedBooks.setTypeface(font);
        recyclerView_saved_books.setLayoutManager(new LinearLayoutManager(mainActivity,LinearLayoutManager.HORIZONTAL,false));*/
        //bookListTestAdapter = new BookListTestAdapter(mainActivity,bookListModelList);
        //recyclerView_saved_books.setAdapter(bookListTestAdapter);
       /* recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        bookTestAdapter = new BookTestAdapter(mainActivity,bookModelList);
        recyclerView.setAdapter(bookTestAdapter);*/
        return view;
    }

    public void loadPresenter(){

        booksPresenter = new BooksPresenter(mainActivity.snaphyHelper.getLoopBackAdapter(), mainActivity.progressBar,mainActivity);
        booksPresenter.fetchBooks(true);
    }

    public void subscribe(){

        bookCategoryDataList = Presenter.getInstance().getList(BookCategory.class, Constants.BOOK_LIST_BOOKS_FRAGMENT);
        bookCategoryDataList.subscribe(this, new Listen<BookCategory>() {
            @Override
            public void onInit(DataList<BookCategory> dataList) {
                super.onInit(dataList);
                bookTestAdapter = new BookTestAdapter(mainActivity, bookCategoryDataList);
                recyclerView.setAdapter(bookListTestAdapter);
            }

            @Override
            public void onChange(DataList<BookCategory> dataList) {
                super.onChange(dataList);
                bookListTestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onClear() {
                super.onClear();
                bookListTestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onRemove(BookCategory element, int index, DataList<BookCategory> dataList) {
                super.onRemove(element, index, dataList);
            }
        });
    }

   /* @OnClick(R.id.fragment_books_textview2) void onViewAll(){

        mainActivity.replaceFragment(R.layout.fragment_view_all_books,null);
    }*/

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
