package com.orthopg.snaphy.orthopg.Fragment.BooksFragment;

import android.os.Handler;
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
import java.util.logging.LogRecord;

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
    Handler handler = new Handler();
    String localOrderBy = "datetime(added) DESC";

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
            bookCategoryDataList.clear();
        }

        Customer customer  = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
        String customerId = String.valueOf(customer.getId());
        if(customerId!=null){
            final BookCategoryRepository bookCategoryRepository = restAdapter.createRepository(BookCategoryRepository.class);
            bookCategoryRepository.addStorage(mainActivity);
            bookCategoryRepository.fetchBookList(customerId, new DataListCallback<BookCategory>() {
                @Override
                public void onBefore() {
                    super.onBefore();
                    if(mainActivity!=null) {
                        mainActivity.startProgressBar(mainActivity.progressBar);
                    }

                    setOldFlag();
                }

                @Override
                public void onSuccess(DataList<BookCategory> objects) {
                    super.onSuccess(objects);
                    if(objects!=null){
                        if(reset){
                            bookCategoryDataList.clear();
                        }
                        for(BookCategory bookCategory : objects){
                            if(bookCategory!=null){
                                saveBookCategoryData(bookCategory);
                            }
                        }
                        bookCategoryDataList.addAll(objects);
                    }
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    Log.e(Constants.TAG, t.toString());
                    loadOfflineBookData();
                }

                @Override
                public void onFinally() {
                    super.onFinally();
                    //removeOldBookData();
                    mainActivity.stopProgressBar(mainActivity.progressBar);
                }
            });
        }


    }

    /**
     * Setting the flag on old books data
     */
    public void setOldFlag(){
        BookCategoryRepository bookCategoryRepository = restAdapter.createRepository(BookCategoryRepository.class);
        bookCategoryRepository.addStorage(mainActivity);
        bookCategoryRepository.getDb().checkOldData__db();
        BookRepository bookRepository = restAdapter.createRepository(BookRepository.class);
        bookRepository.addStorage(mainActivity);
        bookRepository.getDb().checkOldData__db();
    }

    public void saveBookCategoryData(BookCategory bookCategory){

        BookRepository bookRepository = restAdapter.createRepository(BookRepository.class);
        bookRepository.addStorage(mainActivity);
        if(bookCategory.getBooks()!=null){
            if(bookCategory.getBooks().size()!=0){
                for(Book book : bookCategory.getBooks()){
                    bookRepository.getDb().upsert__db(book.getId().toString(), book);
                }
            }
        }
        /*BookCategoryRepository bookCategoryRepository = restAdapter.createRepository(BookCategoryRepository.class);
        bookCategoryRepository.addStorage(mainActivity);
        bookCategoryRepository.getDb().upsert__db(bookCategory.getId().toString(), bookCategory);*/
    }

    public void loadOfflineBookData(){
        bookCategoryDataList.clear();
        BookCategoryRepository bookCategoryRepository = restAdapter.createRepository(BookCategoryRepository.class);
        BookRepository bookRepository = restAdapter.createRepository(BookRepository.class);
        bookCategoryRepository.addStorage(mainActivity);
        bookRepository.addStorage(mainActivity);
        //if(bookCategoryDataList.size()==0){
            HashMap<String, Object> localFlagQuery = new HashMap<>();
            if(bookCategoryRepository.getDb().count__db(localFlagQuery, localOrderBy,50)>0){
                bookCategoryDataList.addAll(bookCategoryRepository.getDb().getAll__db(localFlagQuery, localOrderBy,50));
            }

            for(BookCategory bookCategory: bookCategoryDataList){
                HashMap<String, Object> where = new HashMap<>();
                where.put("bookCategoryId", bookCategory.getId().toString());
                DataList<Book> books = bookRepository.getDb().getAll__db(where, localOrderBy, 50);
                if(books != null){
                    bookCategory.setBooks(books);
                }
            }

       // }
    }

    public void removeOldBookData(){

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                BookCategoryRepository bookCategoryRepository = restAdapter.createRepository(BookCategoryRepository.class);
                bookCategoryRepository.addStorage(mainActivity);
                BookRepository bookRepository = restAdapter.createRepository(BookRepository.class);
                bookRepository.addStorage(mainActivity);
                HashMap<String, Object> localFlagQuery = new HashMap<String, Object>();
                localFlagQuery.put(Constants.OLD_DB_FIELD_FLAG, 0);
                HashMap<String, Object> bookCategoryMap = new HashMap<String, Object>();
                BookCategory bookCategory = bookCategoryRepository.createObject(bookCategoryMap);
                HashMap<String, Object> bookMap = new HashMap<String, Object>();
                Book book = bookRepository.createObject(bookMap);
                bookRepository.getDb().updateAll__db(localFlagQuery, book);
                bookCategoryRepository.getDb().updateAll__db(localFlagQuery,bookCategory);
            }
        }, 3000);
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
