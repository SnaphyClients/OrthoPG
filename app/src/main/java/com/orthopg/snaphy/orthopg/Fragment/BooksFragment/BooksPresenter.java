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
    public double limit = 5;
    public double skip = 0;
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

    public void fetchBooks(boolean reset){
        if(reset){
            skip = 0;
            //Clear the list..
            bookDataList.clear();
        }

        if(skip > 0){
            skip = skip + limit;
        }
        HashMap<String, Object> filter = new HashMap<>();
        filter.put("skip", skip);
        filter.put("limit", limit);
        //TODO ADD WHERE..
        BookRepository bookRepository =  restAdapter.createRepository(BookRepository.class);
        bookRepository.find(filter, new DataListCallback<Book>() {
            @Override
            public void onBefore() {
                //ADD PROGRESS BAR
                mainActivity.startProgressBar(circleProgressBar);
            }

            @Override
            public void onSuccess(DataList<Book> objects) {
                super.onSuccess(objects);
                bookDataList.addAll(objects);
            }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
            }

            @Override
            public void onFinally() {
                super.onFinally();
                //STOP PROGRESS BAR
                mainActivity.stopProgressBar(circleProgressBar);

            }
        });
    }
}
