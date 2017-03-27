package com.orthopg.snaphy.orthopg.Fragment.BooksFragment;

import android.content.Context;
import android.graphics.Typeface;
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

import org.w3c.dom.Text;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nikita on 8/3/17.
 */

public class ViewAllBooksAdapter extends RecyclerView.Adapter<ViewAllBooksAdapter.ViewHolder> {

    MainActivity mainActivity;
    List<BookListModel> bookListModelList;
    DataList<Book> bookDataList;

    /*public ViewAllBooksAdapter(MainActivity mainActivity, List<BookListModel> bookListModelList){

        this.mainActivity = mainActivity;
        this.bookListModelList = bookListModelList;
    }*/

    public ViewAllBooksAdapter(MainActivity mainActivity, DataList<Book> bookDataList){
        this.mainActivity = mainActivity;
        this.bookDataList = bookDataList;
    }


    @Override
    public ViewAllBooksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View viewBookView = inflater.inflate(R.layout.layout_view_all_books,parent,false);
        ViewAllBooksAdapter.ViewHolder viewHolder = new ViewAllBooksAdapter.ViewHolder(viewBookView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //BookListModel bookListModel = bookListModelList.get(position);
        Book book = bookDataList.get(position);

        TextView bookName = holder.bookName;
        ImageView bookCover = holder.bookCover;
        TextView bookDescription = holder.bookDescription;
       /* TextView hardCopy = holder.hardCopy;
        TextView hardCopyPrice = holder.hardCopyPrice;
        TextView softCopy = holder.softCopy;
        TextView softCopyPrice = holder.softCopyPrice;*/

        Typeface typefaceHeading = Typeface.createFromAsset( mainActivity.getAssets(),"fonts/OpenSans-Bold.ttf");
        Typeface typefaceDescription = Typeface.createFromAsset( mainActivity.getAssets(),"fonts/OpenSans-Regular.ttf");

        bookName.setTypeface(typefaceHeading);
        /*hardCopy.setTypeface(typefaceHeading);
        softCopy.setTypeface(typefaceHeading);
        hardCopyPrice.setTypeface(typefaceDescription);
        softCopyPrice.setTypeface(typefaceDescription);*/

        if(book!=null){
            if(book.getFrontCover()!=null){
                bookCover.setVisibility(View.VISIBLE);
                mainActivity.snaphyHelper.loadUnsignedUrl(book.getFrontCover(), bookCover);
            } else{
                bookCover.setVisibility(View.VISIBLE);
            }

            if(book.getTitle()!=null){
                if(!book.getTitle().isEmpty()){
                    bookName.setVisibility(View.VISIBLE);
                    bookName.setText(book.getTitle());
                } else{
                    bookName.setVisibility(View.GONE);
                }
            } else{
                bookName.setVisibility(View.GONE);
            }

            if(book.getDescription()!=null){
                if(!book.getDescription().isEmpty()){
                    bookDescription.setVisibility(View.VISIBLE);
                    bookDescription.setText(book.getDescription());
                } else{
                    bookDescription.setVisibility(View.GONE);
                }
            } else{
                bookDescription.setVisibility(View.GONE);
            }
        }

        /*if(bookListModel!=null){

            if(bookListModel.getName()!=null){
                if(!bookListModel.getName().isEmpty()){
                    bookName.setText(bookListModel.getName().toString());
                }
            }

            if(bookListModel.getDrawable()!=null){
                bookCover.setImageDrawable(bookListModel.getDrawable());
            }
        }*/
    }

    @Override
    public int getItemCount() {
        return bookDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.layout_view_all_books_textview1) TextView bookName;
        @Bind(R.id.layout_view_all_books_textview2) TextView bookDescription;
        /*@Bind(R.id.layout_view_all_books_textview2) TextView hardCopy;
        @Bind(R.id.layout_view_all_books_textview3) TextView hardCopyPrice;
        @Bind(R.id.layout_view_all_books_textview4) TextView softCopy;
        @Bind(R.id.layout_view_all_books_textview5) TextView softCopyPrice;*/
        @Bind(R.id.layout_view_all_books_imageview1) ImageView bookCover;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
