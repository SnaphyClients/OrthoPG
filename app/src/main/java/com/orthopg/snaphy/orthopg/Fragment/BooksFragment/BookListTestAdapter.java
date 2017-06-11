package com.orthopg.snaphy.orthopg.Fragment.BooksFragment;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Book;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.BookRepository;
import com.folioreader.activity.FolioActivity;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;

import com.orthopg.snaphy.orthopg.R;
import com.orthopg.snaphy.orthopg.WordUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingFormatArgumentException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nikita on 8/3/17.
 */

public class BookListTestAdapter extends RecyclerView.Adapter<BookListTestAdapter.ViewHolder> {

    MainActivity mainActivity;
   // List<BookListModel> bookListModelList;
    DataList<Book> bookDataList;
    String bookNameTxt;
    Map<String,Object> bookCoverMap;
    String bookDescriptionTxt;
    SharedPreferences sharedPreferences;
    public static byte[] key, iv;
    int read;
    String bookId = "";
    NotificationManager notificationManager;
    NotificationCompat.Builder mBuilder;
    int id = 1;

   /* public BookListTestAdapter(MainActivity mainActivity, List<BookListModel> bookListModelList){

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
    public void onBindViewHolder(ViewHolder holder, final int position) {

        //BookListModel bookListModel = bookListModelList.get(position);
        final Book book = bookDataList.get(position);
        sharedPreferences = mainActivity.getSharedPreferences(Constants.BOOK_SHARED_PREFERENCE,Context.MODE_PRIVATE);
        TextView bookName = holder.bookName;
        ImageView bookCover = holder.bookCover;
        final CardView cardView = holder.cardView;

        if(book!=null){
            bookId = String.valueOf(book.getId());
            bookNameTxt = book.getTitle();
            if(book.getBookCover()!=null){
                bookCover.setVisibility(View.VISIBLE);
                bookCoverMap = book.getBookCover();
                mainActivity.snaphyHelper.loadUnsignedUrl(book.getBookCover(),bookCover);
            } else{
                bookCover.setVisibility(View.GONE);
            }

            if(book.getTitle()!=null){
                if(!book.getTitle().isEmpty()){
                    bookName.setVisibility(View.VISIBLE);
                    bookNameTxt = book.getTitle().toString();
                    bookName.setText(WordUtils.capitalize(book.getTitle().toString()));
                } else{
                    bookName.setVisibility(View.GONE);
                }
            } else{
                bookName.setVisibility(View.GONE);
            }

            if(book.getDescription()!=null){
                if(!book.getDescription().isEmpty()){
                    bookDescriptionTxt = book.getDescription().toString();
                }
            }

        }

          cardView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  String bookcategory = Presenter.getInstance().getModel(String.class, Constants.SAVED_BOOKS_DATA);
                  String bookName = book.getTitle();
                  Presenter.getInstance().addModel(Constants.DOWNLOADED_BOOK_ID, bookName);
                  File outFile = new File(Environment.getExternalStorageDirectory() + "/OrthoPg/" + bookName + ".epub");
                  if(bookcategory!=null){
                      String bookKey = sharedPreferences.getString(String.valueOf(book.getId()),"");
                      String bookIv = sharedPreferences.getString(String.valueOf(book.getId()) + "iv", "");
                      if(bookKey.isEmpty() || bookIv.isEmpty() || !outFile.exists()){
                          //Check for network connection
                          if(!mainActivity.snaphyHelper.isNetworkAvailable()){
                              Snackbar.make(cardView,"Can't download! Check for network connection",Snackbar.LENGTH_SHORT).show();
                              outFile.delete();
                          } else{
                              downloadBook(book);
                          }
                      } else{
                          try {
                              decryptFile();
                          } catch (NoSuchPaddingException e) {
                              e.printStackTrace();
                          } catch (NoSuchAlgorithmException e) {
                              e.printStackTrace();
                          } catch (InvalidAlgorithmParameterException e) {
                              e.printStackTrace();
                          } catch (InvalidKeyException e) {
                              e.printStackTrace();
                          } catch (IOException e) {
                              e.printStackTrace();
                          }
                      }
                  } else{
                      setBookData(position);
                      mainActivity.replaceFragment(R.layout.fragment_book_description, null);
                  }
              }
          });
    }


    public void downloadBook(Book book){

        key = getKey();
        iv = getIV();

        notificationManager =
                (NotificationManager) mainActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(mainActivity);
        mBuilder.setContentTitle("PDF Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.mipmap.ic_launcher);
        Log.v(Constants.TAG, book+"");
        if(book.getBookDetails().getBookPdf()!=null){
            if(book.getBookDetails().getBookPdf().get("url")!=null){
                Map<String, Object> bookHashMap = (Map<String, Object>)book.getBookDetails().getBookPdf().get("url");
                if(bookHashMap != null) {
                    String bookUnsignedUrl = (String)bookHashMap.get("unSignedUrl");
                    Log.v(Constants.TAG, bookUnsignedUrl);
                    String  bookName = book.getTitle();
                    new DownloadFile().execute(bookUnsignedUrl, bookName+".epub");
                }
            }
        }
    }


    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "OrthoPg");
            folder.mkdir();

            File epubFile = new File(folder, fileName);

            try {
                epubFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            downloadFile(fileUrl, epubFile);
            //downloadEncryptedFile(fileUrl, pdfFile);
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(mainActivity, "Download Completed..", Toast.LENGTH_SHORT).show();
            mBuilder.setContentText("Download complete")
                    // Removes the progress bar
                    .setProgress(0,0,false);
            notificationManager.notify(id, mBuilder.build());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String byteKey = Base64.encodeToString(key, Base64.DEFAULT);
            String byteIv = Base64.encodeToString(iv, Base64.DEFAULT);
            editor.putString(bookId,byteKey);
            editor.putString(bookId + "iv", byteIv);
            editor.commit();
            try {
                decryptFile();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public void downloadFile(String fileUrl, File outFile){
        try {
            Uri uri = Uri.parse(fileUrl);
            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fos = new FileOutputStream(outFile);
            Cipher encipher = Cipher.getInstance("AES");
            SecretKeySpec specKey = new SecretKeySpec(key, "AES");
            encipher.init(Cipher.ENCRYPT_MODE,specKey,new IvParameterSpec(iv));
            CipherInputStream cis = new CipherInputStream(inputStream,encipher);
            notificationManager.notify(id, mBuilder.build());
            byte[] buffer = new byte[1024*1024];
            while((read = cis.read(buffer))!=-1){
                fos.write(buffer, 0, read);
                fos.flush();}
            fos.close();
            //Toast.makeText(mainActivity,"Encryption completed",Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void decryptFile() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IOException {

        File outFile = new File(Environment.getExternalStorageDirectory() + "/OrthoPg/" + bookNameTxt + ".epub");
        File decFile = new File(Environment.getExternalStorageDirectory() + "/OrthoPg/" + "dec" + bookNameTxt + ".epub" );
        FileInputStream enfis = new FileInputStream(outFile);
        FileOutputStream defos = new FileOutputStream(decFile);
        Cipher decipher = Cipher.getInstance("AES");
        String bookKeyString = sharedPreferences.getString(bookId,"");
        byte[] keyArray = Base64.decode(bookKeyString,Base64.DEFAULT);
        String bookIvString = sharedPreferences.getString(bookId + "iv","");
        byte[] ivArray = Base64.decode(bookIvString,Base64.DEFAULT);
        SecretKeySpec specKey = new SecretKeySpec(keyArray, "AES");
        decipher.init(Cipher.DECRYPT_MODE,specKey,new IvParameterSpec(ivArray));
        CipherOutputStream cos = new CipherOutputStream(defos,decipher);
        byte[] d = new byte[1024*1024];
        while((read = enfis.read(d))!=-1){
            cos.write(d,0,read);
            cos.flush();
        }
        cos.close();
        // mainActivity.stopProgressBar(mainActivity.progressBar);
        Intent intent = new Intent(mainActivity, FolioActivity.class);
        intent.putExtra(FolioActivity.INTENT_EPUB_SOURCE_TYPE, FolioActivity.EpubSourceType.SD_CARD);
        intent.putExtra(FolioActivity.INTENT_EPUB_SOURCE_PATH,  Environment.getExternalStorageDirectory() + "/OrthoPg/" + "dec" + bookNameTxt + ".epub");
        mainActivity.startActivity(intent);
    }



    private static byte[]  getKey(){
        KeyGenerator keyGen;
        byte[] dataKey=null;
        try {
            // Generate 256-bit key
            keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);
            SecretKey secretKey = keyGen.generateKey();
            dataKey=secretKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return dataKey;
    }



    private static byte[] getIV(){
        SecureRandom random = new SecureRandom();
        byte[] iv = random.generateSeed(16);
        return iv;
    }

    public void setBookData(int position){
        Book book = bookDataList.get(position);
        book.setTitle(book.getTitle());
        book.setBookCover(book.getBookCover());
        book.setDescription(book.getDescription());
        Presenter.getInstance().addModel(Constants.BOOK_DESCRIPTION_ID,book);
    }

    @Override
    public int getItemCount() {
        return bookDataList.size();
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
