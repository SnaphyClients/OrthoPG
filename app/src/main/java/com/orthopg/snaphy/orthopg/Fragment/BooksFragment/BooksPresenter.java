package com.orthopg.snaphy.orthopg.Fragment.BooksFragment;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.DataListCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Book;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.BookRepository;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.strongloop.android.loopback.RestAdapter;

import java.util.HashMap;

/**
 * Created by Ravi-Gupta on 10/6/2016.
 */

public class BooksPresenter {

    RestAdapter restAdapter;
    DataList<Book> bookDataList;
    public int limit = 5;
    CircleProgressBar circleProgressBar;
    MainActivity mainActivity;

    public BooksPresenter(RestAdapter restAdapter, CircleProgressBar progressBar, MainActivity mainActivity){
        this.restAdapter = restAdapter;
        circleProgressBar = progressBar;
        this.mainActivity = mainActivity;
        //Only add if not initialized already..
        if(Presenter.getInstance().getList(Book.class, Constants.BOOK_LIST_BOOKS_FRAGMENT) == null){
            bookDataList = new DataList<>();
            Presenter.getInstance().addList(Constants.BOOK_LIST_BOOKS_FRAGMENT, bookDataList);
        }else{
            bookDataList = Presenter.getInstance().getList(Book.class, Constants.BOOK_LIST_BOOKS_FRAGMENT);
        }
    }

    public void fetchBooks(){
        HashMap<String, Object> filter = new HashMap<>();
        BookRepository bookRepository =  restAdapter.createRepository(BookRepository.class);
        bookRepository.find(filter, new DataListCallback<Book>() {
            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onSuccess(DataList<Book> objects) {
                super.onSuccess(objects);
            }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
            }

            @Override
            public void onFinally() {
                super.onFinally();
            }
        });
    }
}
