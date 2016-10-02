package com.orthopg.snaphy.orthopg.Fragment.BooksFragment;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

                Uri uri = Uri
                        .parse("http://www.pdf995.com/samples/pdf.pdf");
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setDescription("OrthoPG Books");
                request.setTitle("OrthoPG");
                // in order for this if to run, you must use the android 3.2 to compile your app
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "name-of-the-file.ext");

                // get download service and enqueue file
                DownloadManager manager = (DownloadManager) mainActivity.getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
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
