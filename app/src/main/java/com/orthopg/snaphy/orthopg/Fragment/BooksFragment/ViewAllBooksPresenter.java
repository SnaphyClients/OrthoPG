package com.orthopg.snaphy.orthopg.Fragment.BooksFragment;

import android.util.Log;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.DataListCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Book;
import com.androidsdk.snaphy.snaphyandroidsdk.models.BookCategory;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Payment;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.BookRepository;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.strongloop.android.loopback.RestAdapter;

import java.util.HashMap;

/**
 * Created by nikita on 23/3/17.
 */

public class ViewAllBooksPresenter {

    RestAdapter restAdapter;
    MainActivity mainActivity;
    DataList<Book> bookDataList;
    public double limit = 7;
    public double skip = 0;

    public ViewAllBooksPresenter(RestAdapter restAdapter, MainActivity mainActivity){

        this.restAdapter = restAdapter;
        this.mainActivity = mainActivity;

        if(Presenter.getInstance().getList(Book.class, Constants.VIEW_ALL_BOOKS_LIST)==null){
            bookDataList = new DataList<>();
            Presenter.getInstance().addList(Constants.VIEW_ALL_BOOKS_LIST, bookDataList);
        } else{
            bookDataList = Presenter.getInstance().getList(Book.class, Constants.VIEW_ALL_BOOKS_LIST);
        }
    }

    public void fetchAllBooks(final boolean reset){

        if(reset){
            skip = 0;
        }

        HashMap<String, Object> filter = new HashMap<>();
        HashMap<String, Object> where = new HashMap<>();
        BookCategory bookCategory = Presenter.getInstance().getModel(BookCategory.class, Constants.BOOK_CATEGORY_ID);
        String bookCategoryId = (String)bookCategory.getId();
        if(bookCategoryId!=null){
            where.put("bookCategoryId", bookCategoryId);
            filter.put("where", where);

            BookRepository bookRepository = restAdapter.createRepository(BookRepository.class);
            bookRepository.find(filter, new DataListCallback<Book>() {
                @Override
                public void onBefore() {
                    super.onBefore();
                    mainActivity.startProgressBar(mainActivity.progressBar);
                }

                @Override
                public void onSuccess(DataList<Book> objects) {
                    super.onSuccess(objects);
                    if(objects!=null) {
                        if(reset){
                            bookDataList.clear();
                        }
                        bookDataList.addAll(objects);

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
                    mainActivity.stopProgressBar(mainActivity.progressBar);
                }
            });
        }
    }
}
