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

import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Book;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ravi-Gupta on 9/28/2016.
 */
public class BooksListAdapter extends RecyclerView.Adapter<BooksListAdapter.ViewHolder> {

    MainActivity mainActivity;
    DataList<Book> booksModelList;

    public BooksListAdapter(MainActivity mainActivity, DataList<Book> booksModelList) {
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
        final Book booksModel = booksModelList.get(position);
        ImageView frontCover = holder.frontCover;
        ImageView backCover = holder.backCover;
        TextView bookName = holder.bookName;
        TextView bookDescription = holder.booksDescription;
        Button downloadBook = holder.downloadBook;
        if(booksModel.getFrontCover() != null){
            frontCover.setVisibility(View.VISIBLE);
            mainActivity.snaphyHelper.loadUnsignedUrl(booksModel.getFrontCover(), frontCover );
        }else{
            frontCover.setVisibility(View.GONE);
        }

        if(booksModel.getBackCover() != null){
            backCover.setVisibility(View.VISIBLE);
            mainActivity.snaphyHelper.loadUnsignedUrl(booksModel.getBackCover(), backCover);
        }else{
            backCover.setVisibility(View.GONE);
        }
        if(booksModel.getTitle()!= null){
            bookName.setText(booksModel.getTitle());
        }

        if(booksModel.getDescription()!= null){
            bookDescription.setText(booksModel.getDescription());
        }

        if(booksModel.getUploadBook() != null){
            downloadBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File direct = new File(Environment.getExternalStorageDirectory()
                            + "/OrthoPG");
                    Map<String, Object> hashMap =  (Map<String, Object>)booksModel.getUploadBook();
                    if(hashMap.get("url") !=  null){
                        HashMap<String, String> url = (HashMap<String, String>)hashMap.get("url");
                        if(url.get("unSignedUrl") != null){
                            String unSignedUrl = url.get("unSignedUrl");
                            Uri uri = Uri
                                    .parse(unSignedUrl);
                            DownloadManager.Request request = new DownloadManager.Request(uri);

                            request.setDescription("OrthoPG");
                            request.setMimeType("application/pdf");

                            if(booksModel.getTitle() != null) {
                                request.setDescription(booksModel.getTitle().toString());
                            }
                            // in order for this if to run, you must use the android 3.2 to compile your app
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                request.allowScanningByMediaScanner();
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            }
                            if(booksModel.getTitle() != null) {
                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, booksModel.getTitle()+".pdf");
                            }


                            // get download service and enqueue file
                            DownloadManager manager = (DownloadManager) mainActivity.getSystemService(Context.DOWNLOAD_SERVICE);
                            manager.enqueue(request);
                        }
                    }


                    }
                });
        }else{
            downloadBook.setVisibility(View.GONE);
        }


    }

    /*@Override
protected void onResume() {
    super.onResume();
    IntentFilter intentFilter = new IntentFilter(
            DownloadManager.ACTION_DOWNLOAD_COMPLETE);
    registerReceiver(broadcast, intentFilter);
}

public void showPdf() {
    try {
        File file = new File(Environment.getExternalStorageDirectory()
                + "/Download/" + name + "CV.pdf");//name here is the name of any string you want to pass to the method
        if (!file.isDirectory())
            file.mkdir();
        Intent testIntent = new Intent("com.adobe.reader");
        testIntent.setType("application/pdf");
        testIntent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        testIntent.setDataAndType(uri, "application/pdf");
        startActivity(testIntent);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

BroadcastReceiver broadcast = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        showPdf();
    }
};*/

    @Override
    public int getItemCount() {
        return booksModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.layout_book_imageview1) ImageView frontCover;
        @Bind(R.id.layout_book_imageview2) ImageView backCover;
        @Bind(R.id.layout_book_textview1) TextView bookName;
        @Bind(R.id.layout_book_textview2) TextView booksDescription;
        @Bind(R.id.layout_book_button1) Button downloadBook;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
