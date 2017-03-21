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
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Book;
import com.androidsdk.snaphy.snaphyandroidsdk.models.BookCategory;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nikita on 8/3/17.
 */

public class BookTestAdapter extends RecyclerView.Adapter<BookTestAdapter.ViewHolder> {

    MainActivity mainActivity;
    List<BookModel> bookModelList;
    DataList<BookCategory> bookCategoryDataList;
    BookListTestAdapter bookListTestAdapter;

    /*public BookTestAdapter(MainActivity mainActivity, List<BookModel> bookModelList){

        this.mainActivity = mainActivity;
        this.bookModelList = bookModelList;

    }*/

    public BookTestAdapter(MainActivity mainActivity, DataList<BookCategory> bookCategoryDataList){

        this.mainActivity = mainActivity;
        this.bookCategoryDataList = bookCategoryDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View bookView = inflater.inflate(R.layout.layout_books_type,parent,false);
        BookTestAdapter.ViewHolder viewHolder = new BookTestAdapter.ViewHolder(bookView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       //BookModel bookModel = bookModelList.get(position);
         BookCategory bookCategory_ = bookCategoryDataList.get(position);
        TextView bookCategory = holder.bookCategory;
        final RecyclerView recyclerView = holder.recyclerView;
        TextView viewAll = holder.viewAll;

        Typeface font = Typeface.createFromAsset(mainActivity.getAssets(), "fonts/OpenSans-Bold.ttf");
        bookCategory.setTypeface(font);

        if(bookCategory_!=null){
            if(bookCategory_.getName()!=null){
                if(!bookCategory_.getName().isEmpty()){
                    bookCategory.setText(bookCategory_.getName().toString());
                }
            }

            if(bookCategory_.getBooks()!=null){
                if(bookCategory_.getBooks().size()==0){
                    HashMap<String, Object> filter = new HashMap<>();
                    bookCategory_.get__books(filter, mainActivity.snaphyHelper.getLoopBackAdapter(), new DataListCallback<Book>() {
                        @Override
                        public void onBefore() {
                            super.onBefore();
                        }

                        @Override
                        public void onSuccess(DataList<Book> objects) {
                            super.onSuccess(objects);
                            if(objects!=null){

                                booksData(recyclerView, objects);
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            super.onError(t);
                            Log.e(Constants.TAG,t.toString());
                        }

                        @Override
                        public void onFinally() {
                            super.onFinally();
                        }
                    });
                }
                booksData(recyclerView, bookCategory_.getBooks());

            } else{
                HashMap<String, Object> filter = new HashMap<>();
                bookCategory_.get__books(filter, mainActivity.snaphyHelper.getLoopBackAdapter(), new DataListCallback<Book>() {
                    @Override
                    public void onBefore() {
                        super.onBefore();
                    }

                    @Override
                    public void onSuccess(DataList<Book> objects) {
                        super.onSuccess(objects);
                        if(objects!=null){

                            booksData(recyclerView, objects);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        Log.e(Constants.TAG,t.toString());
                    }

                    @Override
                    public void onFinally() {
                        super.onFinally();
                    }
                });
            }
        }

      /*  if(bookModel!=null){

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
        }
*/
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.replaceFragment(R.layout.fragment_view_all_books,null);
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
        return bookModelList.size();
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
