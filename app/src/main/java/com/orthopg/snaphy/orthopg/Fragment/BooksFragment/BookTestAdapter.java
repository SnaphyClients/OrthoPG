package com.orthopg.snaphy.orthopg.Fragment.BooksFragment;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.DataListCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.ObjectCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Book;
import com.androidsdk.snaphy.snaphyandroidsdk.models.BookCategory;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Order;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Payment;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.PaymentRepository;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;
import com.orthopg.snaphy.orthopg.RecyclerItemClickListener;
import com.orthopg.snaphy.orthopg.WordUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nikita on 8/3/17.
 */

public class BookTestAdapter extends RecyclerView.Adapter<BookTestAdapter.ViewHolder> {

    MainActivity mainActivity;
    DataList<BookModel> bookModelDataList;
    DataList<Payment> paymentDataList;
    //List<BookModel> bookModelList;
    DataList<BookCategory> bookCategoryDataList;
    BookListTestAdapter bookListTestAdapter;

   /* public BookTestAdapter(MainActivity mainActivity, List<BookModel> bookModelList){

        this.mainActivity = mainActivity;
        this.bookModelList = bookModelList;

    }*/

    public BookTestAdapter(MainActivity mainActivity, DataList<BookCategory> bookCategoryDataList) {

        this.mainActivity = mainActivity;
        this.bookCategoryDataList = bookCategoryDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View bookView = inflater.inflate(R.layout.layout_books_type, parent, false);
        BookTestAdapter.ViewHolder viewHolder = new BookTestAdapter.ViewHolder(bookView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //BookModel bookModel = bookModelList.get(position);
        //BookModel bookModel = bookModelDataList.get(position);
        final BookCategory bookCategory_ = bookCategoryDataList.get(position);
        final TextView bookCategory = holder.bookCategory;
        final RecyclerView recyclerView = holder.recyclerView;
        final TextView viewAll = holder.viewAll;
        Typeface font = Typeface.createFromAsset(mainActivity.getAssets(), "fonts/OpenSans-Bold.ttf");
        bookCategory.setTypeface(font);
        if (bookCategory_ != null) {
            if(bookCategory_.getBooks()!=null){
                if(bookCategory_.getBooks().size()!=0){
                    if (bookCategory_.getName() != null) {
                        if (!bookCategory_.getName().isEmpty()) {
                            viewAll.setVisibility(View.VISIBLE);
                            bookCategory.setText(WordUtils.capitalize(bookCategory_.getName().toString()));

                        } else {
                            bookCategory.setVisibility(View.GONE);
                            viewAll.setVisibility(View.GONE);
                        }
                    } else{
                        bookCategory.setVisibility(View.GONE);
                        viewAll.setVisibility(View.GONE);
                    }

                    booksData(recyclerView, bookCategory_.getBooks());
                } else{
                    bookCategory.setVisibility(View.GONE);
                    viewAll.setVisibility(View.GONE);
                }
            } else{
                bookCategory.setVisibility(View.GONE);
                viewAll.setVisibility(View.GONE);
            }

        }

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mainActivity, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(bookCategory_.getName().toString().equalsIgnoreCase("Saved Books")){
                    String name = "Saved Books";
                    Presenter.getInstance().addModel(Constants.SAVED_BOOKS_DATA, name);
                } else{
                    Presenter.getInstance().removeModelFromList(Constants.SAVED_BOOKS_DATA);
                }
            }
        }));


       /* if(bookModel!=null){

            if(bookModel.getBookType().equals("Saved Books")){

                if(bookModel.getBookCategory()!=null){
                    if(!bookModel.getBookCategory().isEmpty()){
                        bookCategory.setText(bookModel.getBookCategory().toString());
                    }
                }

                if(bookModel.getBookListModelList()!=null){
                    if(bookModel.getBookListModelList().size()!=0){
                        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity,LinearLayoutManager.HORIZONTAL,false));
                        bookListTestAdapter = new BookListTestAdapter(mainActivity,bookModel.getBookListModelList());
                        recyclerView.setAdapter(bookListTestAdapter);
                    }
                }
            } else {
                if (bookModel.getBookCategory() != null) {
                    if (!bookModel.getBookCategory().isEmpty()) {
                        bookCategory.setText(bookModel.getBookCategory().toString());
                    }
                }

                if (bookModel.getBookListModelList() != null) {
                    if (bookModel.getBookListModelList().size() != 0) {
                        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false));
                        bookListTestAdapter = new BookListTestAdapter(mainActivity, bookModel.getBookListModelList());
                        recyclerView.setAdapter(bookListTestAdapter);
                    }
                }
            }
        }*/

    viewAll.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        Presenter.getInstance().addModel(Constants.BOOK_CATEGORY_ID, bookCategory_);
        mainActivity.replaceFragment(R.layout.fragment_view_all_books, null);
    }
    });
}





    public void booksData(RecyclerView recyclerView, DataList<Book> objects){

        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false));
        bookListTestAdapter = new BookListTestAdapter(mainActivity, objects);
        recyclerView.setAdapter(bookListTestAdapter);
    }



    @Override
    public int getItemCount() {
        return bookCategoryDataList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.layout_books_type_textview1) TextView bookCategory;
        @Bind(R.id.layout_books_type_textview2) TextView viewAll;
        @Bind(R.id.layout_books_type_recyclerView) RecyclerView recyclerView;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
