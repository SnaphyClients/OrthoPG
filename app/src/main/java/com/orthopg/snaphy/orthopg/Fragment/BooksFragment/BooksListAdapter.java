package com.orthopg.snaphy.orthopg.Fragment.BooksFragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ravi-Gupta on 9/28/2016.
 */
public class BooksListAdapter extends RecyclerView.Adapter<BooksListAdapter.ViewHolder> {

    MainActivity mainActivity;
    List<BooksModel> booksModelList;

    public BooksListAdapter(MainActivity mainActivity, List<BooksModel> booksModelList) {
        this.mainActivity = mainActivity;
        this.booksModelList = booksModelList;
    }

    @Override
    public BooksListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View booksView = inflater.inflate(R.layout.layout_books, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(booksView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BooksListAdapter.ViewHolder holder, int position) {
        BooksModel booksModel = booksModelList.get(position);
        ImageView bookImage = holder.imageView;
        TextView bookName = holder.bookName;
        TextView bookDescription = holder.booksDescription;
        Button downloadBook = holder.downloadBook;

        bookImage.setImageDrawable(booksModel.getBookImage());
        bookName.setText(booksModel.getBookName());
        bookDescription.setText(booksModel.getBookDescription());

        downloadBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mainActivity,"Book will download here", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return booksModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.layout_book_imageview1) ImageView imageView;
        @Bind(R.id.layout_book_textview1) TextView bookName;
        @Bind(R.id.layout_book_textview2) TextView booksDescription;
        @Bind(R.id.layout_book_button1) Button downloadBook;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
