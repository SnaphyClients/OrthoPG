package com.orthopg.snaphy.orthopg.Fragment.BooksFragment;

import android.util.Log;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.DataListCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.VoidCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Book;
import com.androidsdk.snaphy.snaphyandroidsdk.models.BookCategory;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.BookCategoryRepository;
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
    //DataList<Book> bookDataList;
    DataList<BookCategory> bookCategoryDataList;
    public double limit = 7;
    public double skip = 0;
    CircleProgressBar circleProgressBar;
    MainActivity mainActivity;

    public BooksPresenter(RestAdapter restAdapter, CircleProgressBar progressBar, MainActivity mainActivity){
        this.restAdapter = restAdapter;
        circleProgressBar = progressBar;
        this.mainActivity = mainActivity;
        //Only add if not initialized already..
        if(Presenter.getInstance().getList(Book.class, Constants.BOOK_LIST_BOOKS_FRAGMENT) == null){
            bookCategoryDataList = new DataList<>();
            Presenter.getInstance().addList(Constants.BOOK_LIST_BOOKS_FRAGMENT, bookCategoryDataList);
        }else{
            bookCategoryDataList = Presenter.getInstance().getList(BookCategory.class, Constants.BOOK_LIST_BOOKS_FRAGMENT);
        }
    }

    public void fetchBooks(final boolean reset){

        if(reset){
            skip =0;
        }

        Customer customer  = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
        String customerId = String.valueOf(customer.getId());
        if(customerId!=null){
            final BookCategoryRepository bookCategoryRepository = restAdapter.createRepository(BookCategoryRepository.class);
            bookCategoryRepository.fetchBookList(customerId, new DataListCallback<BookCategory>() {
                @Override
                public void onBefore() {
                    super.onBefore();
                    mainActivity.startProgressBar(mainActivity.progressBar);
                }

                @Override
                public void onSuccess(DataList<BookCategory> objects) {
                    super.onSuccess(objects);
                    if(objects!=null){
                        if(reset){
                            bookCategoryDataList.clear();
                        }
                        bookCategoryDataList.addAll(objects);
                    }
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    Log.e(Constants.TAG, t.toString());
                }

                @Override
                public void onFinally() {
                    super.onFinally();
                    mainActivity.stopProgressBar(mainActivity.progressBar);
                }
            });
        }


    }

   /* public void fetchBooks(boolean reset){
        if(reset){
            skip = 0;
            //Clear the list..
            bookDataList.clear();
        }

        HashMap<String, Object> filter = new HashMap<>();
        filter.put("skip", skip);
        filter.put("limit", limit);
        filter.put("order", "added DESC");
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
                if(objects != null){
                    bookDataList.addAll(objects);

                    //Now add skip..
                    skip = skip + objects.size();
                }
            }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
                Log.e(Constants.TAG, t.toString());
            }

            @Override
            public void onFinally() {
                super.onFinally();
                //STOP PROGRESS BAR
                mainActivity.stopProgressBar(circleProgressBar);

            }
        });
    }*/
}
