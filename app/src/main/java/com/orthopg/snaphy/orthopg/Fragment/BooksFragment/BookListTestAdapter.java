package com.orthopg.snaphy.orthopg.Fragment.BooksFragment;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Book;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import java.util.List;
import java.util.MissingFormatArgumentException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nikita on 8/3/17.
 */

public class BookListTestAdapter extends RecyclerView.Adapter<BookListTestAdapter.ViewHolder> {

    MainActivity mainActivity;
    List<BookListModel> bookListModelList;
    DataList<Book> bookDataList;

  /*  public BookListTestAdapter(MainActivity mainActivity, List<BookListModel> bookListModelList){

        this.mainActivity = mainActivity;
        this.bookListModelList = bookListModelList;
    }*/

    public BookListTestAdapter(MainActivity mainActivity, DataList<Book> bookDataList){

        this.mainActivity = mainActivity;
        this.bookDataList = bookDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View bookListView = inflater.inflate(R.layout.layout_books_list,parent,false);
        BookListTestAdapter.ViewHolder viewHolder = new BookListTestAdapter.ViewHolder(bookListView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //BookListModel bookListModel = bookListModelList.get(position);
        Book book = bookDataList.get(position);

        TextView bookName = holder.bookName;
        ImageView bookCover = holder.bookCover;
        CardView cardView = holder.cardView;

        if(book!=null){
            if(book.getBackCover()!=null){

            }

            if(book.getTitle()!=null){
                if(!book.getTitle().isEmpty()){
                    bookName.setText(book.getTitle().toString());
                }
            }
        }

      /*  if (bookListModel != null) {
            if(bookListModel.getName()!=null){
                if(!bookListModel.getName().isEmpty()){
                    bookName.setText(bookListModel.getName().toString());
                }
            }

            if(bookListModel.getDrawable()!=null){
                bookCover.setImageDrawable(bookListModel.getDrawable());
            }
        }*/

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.replaceFragment(R.layout.fragment_book_description,null);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookListModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.layout_books_list_imageview1) ImageView bookCover;
        @Bind(R.id.layout_books_list_textview1) TextView bookName;
        @Bind(R.id.layout_books_list_cardview1) CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
