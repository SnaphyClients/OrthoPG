package com.orthopg.snaphy.orthopg.Fragment.BooksFragment;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.Presentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.DataListCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.ObjectCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Book;
import com.androidsdk.snaphy.snaphyandroidsdk.models.BookDetail;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Payment;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.BookDetailRepository;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.BookRepository;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.PaymentRepository;
import com.folioreader.activity.FolioActivity;
import com.orthopg.snaphy.orthopg.BookPurchaseActivity;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.Fragment.ProfileFragment.ProfileFragment;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.Manifest;
import com.orthopg.snaphy.orthopg.PDFReaderActivity;
import com.orthopg.snaphy.orthopg.PDFViewPagerActivity;
import com.orthopg.snaphy.orthopg.R;
import com.orthopg.snaphy.orthopg.WordUtils;
import com.payUMoney.sdk.PayUmoneySdkInitilizer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

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
import butterknife.OnClick;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookDescriptionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookDescriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookDescriptionFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    MainActivity mainActivity;
    SharedPreferences sharedPreferences;
    public static byte[] key, iv;
    private static final int  MEGABYTE = 1024 * 1024;
    @Bind(R.id.fragment_book_description_button3) Button eBookDownload;
    @Bind(R.id.fragment_book_description_button4) Button hardCopyDownload;
    @Bind(R.id.fragment_view_all_books_textview1) TextView bookHeading;
    @Bind(R.id.fragment_book_description_imageview2) ImageView bookCover;
    @Bind(R.id.fragment_book_description_cover) ImageView bookCover2;
    @Bind(R.id.fragment_book_description_textview1) TextView bookTitle;
    @Bind(R.id.fragment_book_description_textview7) TextView bookDescription;
    @Bind(R.id.fragment_book_description_button1) TextView downloadSample;
    @Bind(R.id.fragment_book_description_textview2) TextView downloadSampleText;
    @Bind(R.id.fragment_book_description_progressBar) ProgressBar progressBar;
    String bookId = "";
    String bookDesscription_ = "";
    String bookURL_ = "";
    public final static String TAG = "BookDescriptionFragment";
    int read;
    String bookKey, bookIv;
    String bookName;
    NotificationManager notificationManager;
    NotificationCompat.Builder mBuilder;
    int id = 1;
    private static final int PERMISSION_REQUEST_CODE = 1;
    String bookUnsignedUrl;
    String bookImageUrl;

    public BookDescriptionFragment() {
        // Required empty public constructor
    }

    public static BookDescriptionFragment newInstance() {
        BookDescriptionFragment fragment = new BookDescriptionFragment();
        return fragment;
    }

    public void startProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void stopProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = mainActivity.getSharedPreferences(Constants.BOOK_SHARED_PREFERENCE,Context.MODE_PRIVATE);
        key = getKey();
        iv = getIV();
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {}
            else {
                requestPermission(); // Code for permission
            }
        }
        else {
        }
        //checkPurchasedBook(book);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(mainActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(mainActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(mainActivity, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(mainActivity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    //Checked
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_description, container, false);
        ButterKnife.bind(this,view);
        getBookData();
        checkPurchased();
        checkDownloadSample();
        return view;
    }

    //Checked
    public void checkDownloadSample(){
        Book book = Presenter.getInstance().getModel(Book.class, Constants.BOOK_DESCRIPTION_ID);
        if(book!=null){
            bookName = book.getTitle();
            File sampleEpubFile = new File(Environment.getExternalStorageDirectory() + "/OrthoPg/" + bookName + ".epub");
            double fileSize  = sampleEpubFile.length();
            if(sampleEpubFile.exists() && fileSize > 0){
                downloadSample.setText("View Sample");
            }else{
                downloadSample.setText("Download Sample");
            }
        }
    }

    //Checked
    public void checkPurchased(){

        final Book book = Presenter.getInstance().getModel(Book.class, Constants.BOOK_DESCRIPTION_ID);
        String bookId = String.valueOf(book.getId());
        if(bookId!=null){
            BookRepository bookRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(BookRepository.class);
            bookRepository.addStorage(mainActivity);
            bookRepository.fetchBookDetail(new HashMap<String, Object>(), bookId, new ObjectCallback<BookDetail>() {
                @Override
                public void onBefore() {
                    super.onBefore();
                    startProgressBar(progressBar);
                }

                @Override
                public void onSuccess(BookDetail object) {
                    super.onSuccess(object);
                    if(object!=null){
                        Presenter.getInstance().addModel(Constants.CHECK_SAVED_BOOK_DATA, object);
                        checkIfBookPresentLocally(book);
                        if(book.getIsEbookAvail().equals("ebook not present")){
                            eBookDownload.setText("Ebook not available");
                            eBookDownload.setEnabled(false);
                            eBookDownload.setBackgroundColor(Color.parseColor("#777777"));
                        }
                        stopProgressBar(progressBar);
                        //eBookDownload.setText("View");
                    }
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    Log.e(Constants.TAG, t.toString());
                    //getBookDetail();
                }

                @Override
                public void onFinally() {
                    super.onFinally();
                    //getBookData();
                }
            });
        }
    }


    //Checked
    public void checkIfBookPresentLocally(Book book)  {
        String bookId = book.getId().toString();
        String bookKey = sharedPreferences.getString(bookId, "");
        String bookIv = sharedPreferences.getString(bookId + "iv", "");
        String bookName = book.getTitle();
        File file = new File(Environment.getExternalStorageDirectory() + "/OrthoPg/" + bookName + ".epub");
        if(bookKey.isEmpty() || bookIv.isEmpty() || !file.exists() || file.length() == 0){
            eBookDownload.setText("Download");
        } else{
            eBookDownload.setText("View");
        }
    }

    //Checked (Handle Progress Bar)
    public void getBookData(){
        Book book = Presenter.getInstance().getModel(Book.class,Constants.BOOK_DESCRIPTION_ID);
        if(book!=null){
            bookId = String.valueOf(book.getId());
            bookDesscription_ = book.getDescription();
            if (book.getUploadSampleBook() != null) {
                if (book.getUploadSampleBook().get("url") != null) {
                    Map<String, Object> bookHashMap = (Map<String, Object>) book.getUploadSampleBook().get("url");
                    if (bookHashMap != null) {
                        bookURL_ = (String) bookHashMap.get("unSignedUrl");
                    }
                }
            }

            if(book.getTitle()!=null){
                if(!book.getTitle().isEmpty()){
                    bookTitle.setVisibility(View.VISIBLE);
                    bookTitle.setText(WordUtils.capitalize(book.getTitle().toString()));
                    bookHeading.setText(WordUtils.capitalize(book.getTitle().toString()));
                } else{
                    bookTitle.setVisibility(View.GONE);
                    bookHeading.setVisibility(View.GONE);
                }
            } else{
                bookTitle.setVisibility(View.GONE);
                bookHeading.setVisibility(View.GONE);
            }

            if(book.getDescription()!=null){
                if(!book.getDescription().isEmpty()){
                    bookDescription.setVisibility(View.VISIBLE);
                    bookDescription.setText(book.getDescription().toString());
                } else{
                    bookDescription.setVisibility(View.GONE);
                }
            } else{
                bookDescription.setVisibility(View.GONE);
            }

            if(book.getBookCover()!=null){
                bookCover.setVisibility(View.VISIBLE);
                mainActivity.snaphyHelper.loadUnsignedUrl(book.getBookCover(),bookCover);
                mainActivity.snaphyHelper.loadUnsignedUrl(book.getBookCover(), bookCover2);
                if (book.getBookCover().get("url") != null) {
                    Map<String, Object> bookHashMap = (Map<String, Object>) book.getBookCover().get("url");
                    if (bookHashMap != null) {
                        if(bookHashMap.get("unSignedUrl") != null) {
                            bookImageUrl = (String) bookHashMap.get("unSignedUrl");
                        }

                    }
                }
            } else{
                bookCover.setVisibility(View.GONE);
            }

            if(book.getUploadSampleBook()!=null){
                if(!book.getUploadSampleBook().isEmpty()){
                    downloadSample.setVisibility(View.VISIBLE);
                    downloadSampleText.setVisibility(View.VISIBLE);
                } else{
                    downloadSample.setVisibility(View.GONE);
                    downloadSampleText.setVisibility(View.GONE);
                }
            } else{
                downloadSample.setVisibility(View.GONE);
                downloadSampleText.setVisibility(View.GONE);
            }

            if(String.valueOf(book.getEbookPrice())!=null){
                if(!String.valueOf(book.getEbookPrice()).isEmpty()){
                    if(book.getEbookPrice()==0){
                        String bookId = book.getId().toString();
                        String bookKey = sharedPreferences.getString(bookId, "");
                        String bookIv = sharedPreferences.getString(bookId + "iv", "");
                        String bookName = book.getTitle();
                        File file = new File(Environment.getExternalStorageDirectory() + "/OrthoPg/" + bookName + ".epub");
                        if(bookKey.isEmpty() || bookIv.isEmpty() || !file.exists() || file.length() == 0){
                            eBookDownload.setText("Read Book For Free");
                        } else{
                            eBookDownload.setText("View Book");
                        }
                    } else if(book.getIsEbookAvail().equals("ebook not present")){
                        eBookDownload.setText("Ebook not available");
                        eBookDownload.setEnabled(false);
                        eBookDownload.setBackgroundColor(Color.parseColor("#777777"));
                    } else{
                        downloadSample.setVisibility(View.VISIBLE);
                        downloadSampleText.setVisibility(View.VISIBLE);
                        eBookDownload.setText("Ebook @ INR " + book.getEbookPrice());
                    }
                } else{
                    eBookDownload.setText("Read Book");
                }
            } else{
                eBookDownload.setText("Read Book");
            }

            if(String.valueOf(book.getHardCopyPrice())!=null){
                if(!String.valueOf(book.getHardCopyPrice()).isEmpty()){
                    if(book.getHardCopyPrice()==0){
                        hardCopyDownload.setText(" Hardcopy Not Available");
                        hardCopyDownload.setEnabled(false);
                        hardCopyDownload.setBackground(getResources().getDrawable(R.drawable.curved_rectangle_disabled_filled));
                    } else{
                        hardCopyDownload.setEnabled(true);
                        hardCopyDownload.setText("Hardcopy @ INR " + book.getHardCopyPrice());
                    }
                } else{
                    hardCopyDownload.setText("Hard Copy Not Available");
                    hardCopyDownload.setEnabled(false);
                    hardCopyDownload.setBackgroundColor(Color.parseColor("#777777"));
                }
            } else{
                hardCopyDownload.setText("Not Available");
                hardCopyDownload.setEnabled(false);
                hardCopyDownload.setBackgroundColor(Color.parseColor("#777777"));
            }


            bookKey = sharedPreferences.getString(bookId,"");
            bookIv = sharedPreferences.getString(bookId + "iv", "");

               /* if(bookKey.isEmpty() || bookIv.isEmpty()) {
                    hardCopyDownload.setText("Download");
                } else{
                    hardCopyDownload.setText("View");
                }*/
            } /*else if(!bookKey.isEmpty() && !bookIv.isEmpty()&& outFile.exists()){
                hardCopyDownload.setText("View");
            }*/
    }


    //Checked
    @OnClick(R.id.fragment_book_description_imageview1) void onBack(){
        mainActivity.onBackPressed();
    }

    //Checked
    @OnClick(R.id.fragment_book_description_button1) void onDownloadSample(){
        //Check Internet Connection
        if(!mainActivity.snaphyHelper.isNetworkAvailable()){
            Snackbar.make(downloadSample,"No Network Connection! Check Internet Connection and try again", Snackbar.LENGTH_SHORT).show();
        } else{
            if(downloadSample.getText().toString().equals("Download Sample")){
                downloadBookSample();
            }else{
                Intent intent = new Intent(mainActivity, FolioActivity.class);
                intent.putExtra(FolioActivity.INTENT_EPUB_SOURCE_TYPE, FolioActivity.EpubSourceType.SD_CARD);
                intent.putExtra(FolioActivity.INTENT_EPUB_SOURCE_PATH,  Environment.getExternalStorageDirectory() + "/OrthoPg/" + bookName + ".epub");
                startActivity(intent);
            }
        }
    }

    //Checked
    public void downloadBookSample(){
        Book book = Presenter.getInstance().getModel(Book.class, Constants.BOOK_DESCRIPTION_ID);
        if(book!=null){
            if (book.getUploadSampleBook() != null) {
                if (book.getUploadSampleBook().get("url") != null) {
                    Map<String, Object> bookHashMap = (Map<String, Object>) book.getUploadSampleBook().get("url");
                    if (bookHashMap != null) {
                        bookUnsignedUrl = (String) bookHashMap.get("unSignedUrl");
                        new DownloadSampleFile().execute(bookUnsignedUrl, bookName + ".epub", "SAMPLE");
                    }
                }
            }
        }
    }

    //Checked
    @OnClick(R.id.fragment_book_description_button4) void onHardCopyBuy(){
        //Check Internet Connection
        if(!mainActivity.snaphyHelper.isNetworkAvailable()){
            Snackbar.make(hardCopyDownload,"No Network Connection! Check Internet Connection and try again", Snackbar.LENGTH_SHORT).show();
        } else {
            Presenter.getInstance().addModel(Constants.BOOK_TYPE, Constants.HARDCOPY_BOOK_TYPE);
            //mainActivity.replaceFragment(R.layout.fragment_checkout, null);
            Intent i = new Intent(mainActivity, BookPurchaseActivity.class);
            startActivity(i);
            Presenter.getInstance().addModel(Constants.BOOK_TYPE, Constants.HARDCOPY_BOOK_TYPE);
        }
    }


    //Checked
    private class DownloadSampleFile extends AsyncTask<String, Boolean, Boolean> {
        File epubFile;
        String FROM = "";
        @Override
        protected Boolean doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            FROM = strings[2];
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "OrthoPg");
            folder.mkdir();

            epubFile = new File(folder, fileName);

            try {
                epubFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            boolean isBookCompleteDownloaded = downloadSampleFile(fileUrl, epubFile);
            //downloadEncryptedFile(fileUrl, pdfFile);
            return isBookCompleteDownloaded;
        }

        @Override
        protected void onPreExecute() {
            startProgressBar(progressBar);
        }

        @Override
        protected void onPostExecute(Boolean isBookCompleteDownloaded) {
            super.onPostExecute(isBookCompleteDownloaded);
            stopProgressBar(progressBar);
            if(isBookCompleteDownloaded) {
                if(FROM.equals("BOOK")) {
                    Toast.makeText(mainActivity, "Download Book Completed", Toast.LENGTH_SHORT).show();
                    eBookDownload.setText("View Book");
                } else {
                    Toast.makeText(mainActivity, "Download Sample Completed", Toast.LENGTH_SHORT).show();
                    downloadSample.setText("View Sample");
                }
                Intent intent = new Intent(mainActivity, FolioActivity.class);
                intent.putExtra(FolioActivity.INTENT_EPUB_SOURCE_TYPE, FolioActivity.EpubSourceType.SD_CARD);
                intent.putExtra(FolioActivity.INTENT_EPUB_SOURCE_PATH, Environment.getExternalStorageDirectory() + "/OrthoPg/" + bookName + ".epub");
                startActivity(intent);

            } else {
                // ERROR DOWNLOADING FILE
                Snackbar snackbar = Snackbar.make(bookHeading, Constants.ERROR_DOWNLOADING_BOOK, Snackbar.LENGTH_LONG);
                snackbar.setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // RETRY DOWNLOADING
                        epubFile.delete();
                        if(FROM.equals("BOOK")) {
                            new DownloadSampleFile().execute(bookUnsignedUrl, bookName + ".epub", "BOOK");
                        } else {
                            new DownloadSampleFile().execute(bookUnsignedUrl, bookName + ".epub", "SAMPLE");
                        }

                    }
                })
                        .show();
            }
        }
    }


    //Checked
    public boolean downloadSampleFile(String fileUrl, File outFile){
        boolean downloadSuccess = true;
        try {
            Uri uri = Uri.parse(fileUrl);
            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fos = new FileOutputStream(outFile);
            while((read = inputStream.read())!=-1){
                fos.write((char)read);
                fos.flush();
            }
            downloadSuccess = true;
            fos.close();
            //Toast.makeText(mainActivity,"Encryption completed",Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            downloadSuccess = false;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            downloadSuccess = false;
        } catch (IOException e) {
            e.printStackTrace();
            downloadSuccess = false;
        } catch (Exception e){
            e.printStackTrace();
            downloadSuccess = false;
        }
        return  downloadSuccess;
    }


    private class DownloadFile extends AsyncTask<String, Boolean, Boolean> {

        @Override
        protected void onPreExecute() {
            startProgressBar(progressBar);
        }

        @Override
        protected Boolean doInBackground(String... strings) {
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

            boolean isBookCompleteDownloaded = downloadFile(fileUrl, epubFile);
            //downloadEncryptedFile(fileUrl, pdfFile);
            return isBookCompleteDownloaded;
            //downloadEncryptedFile(fileUrl, pdfFile);
        }


        @Override
        protected void onPostExecute(Boolean isBookCompleteDownloaded) {
            super.onPostExecute(isBookCompleteDownloaded);
            stopProgressBar(progressBar);
            if(isBookCompleteDownloaded) {
                Toast.makeText(mainActivity, "Download Completed..", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String byteKey = Base64.encodeToString(key, Base64.DEFAULT);
                String byteIv = Base64.encodeToString(iv, Base64.DEFAULT);
                editor.putString(bookId,byteKey);
                editor.putString(bookId + "iv", byteIv);
                editor.commit();
                try {
                    decryptFile(byteKey, byteIv);
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
                eBookDownload.setText("View");
                // hardCopyDownload.setText("View");
            } else {
                // ERROR DOWNLOADING FILE
                Snackbar snackbar = Snackbar.make(bookHeading, Constants.ERROR_DOWNLOADING_BOOK, Snackbar.LENGTH_LONG);
                snackbar.setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DownloadFile().execute(bookUnsignedUrl, bookName+".epub");
                    }
                })
                        .show();
            }
        }
    }




    public boolean downloadFile(String fileUrl, File outFile){
        boolean downloadSuccess = true;
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
            downloadSuccess = true;
            fos.close();
            //Toast.makeText(mainActivity,"Encryption completed",Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            downloadSuccess = false;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            downloadSuccess = false;
        } catch (IOException e) {
            e.printStackTrace();
            downloadSuccess = false;
        } catch (Exception e){
            e.printStackTrace();
            downloadSuccess = false;
        }
        return downloadSuccess;
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

    @OnClick(R.id.fragment_book_description_button3) void onEbookBuy()  {

        if(eBookDownload.getText().toString().equals("View")){
            BookDetail bookDetail = Presenter.getInstance().getModel(BookDetail.class, Constants.CHECK_SAVED_BOOK_DATA);
            Book book = Presenter.getInstance().getModel(Book.class, Constants.BOOK_DESCRIPTION_ID);
            String bookId = book.getId().toString();
            String bookKey = sharedPreferences.getString(bookId,"");
            String bookIv = sharedPreferences.getString(bookId + "iv", "");
            try {
                decryptFile(bookKey, bookIv);
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
        } else if(eBookDownload.getText().toString().equals("View Book"))  {
            Intent intent = new Intent(mainActivity, FolioActivity.class);
            intent.putExtra(FolioActivity.INTENT_EPUB_SOURCE_TYPE, FolioActivity.EpubSourceType.SD_CARD);
            intent.putExtra(FolioActivity.INTENT_EPUB_SOURCE_PATH,  Environment.getExternalStorageDirectory() + "/OrthoPg/" + bookName + ".epub");
            startActivity(intent);
        }else if(mainActivity.snaphyHelper.isNetworkAvailable()){
            if(eBookDownload.getText().toString().equals("Download")){
                downloadBook();
            } else if(eBookDownload.getText().toString().equalsIgnoreCase("Read Book For Free")) {
                downloadFreeBook();
            }else{
                Presenter.getInstance().addModel(Constants.BOOK_TYPE, Constants.EBOOK_BOOK_TYPE);
                /*mainActivity.replaceFragment(R.layout.fragment_checkout, null);*/
                Intent i = new Intent(mainActivity, BookPurchaseActivity.class);
                startActivity(i);
            }
        } else{
            Snackbar.make(eBookDownload,"No Network Connection! Check Internet Connection and try again", Snackbar.LENGTH_SHORT).show();
        }


    }

    public void downloadBook(){
        BookDetail bookDetail = Presenter.getInstance().getModel(BookDetail.class, Constants.CHECK_SAVED_BOOK_DATA);
        Book book = Presenter.getInstance().getModel(Book.class, Constants.BOOK_DESCRIPTION_ID);
        String bookName = book.getTitle();
        key = getKey();
        iv = getIV();

        notificationManager =
                (NotificationManager) mainActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(mainActivity);
        mBuilder.setContentTitle("Book Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.mipmap.ic_launcher);
        Log.v(Constants.TAG, book+"");
        if(bookDetail.getBookPdf()!=null){
            if(bookDetail.getBookPdf().get("url")!=null){
                Map<String, Object> bookHashMap = (Map<String, Object>)bookDetail.getBookPdf().get("url");
                if(bookHashMap != null) {
                    String bookUnsignedUrl = (String)bookHashMap.get("unSignedUrl");
                    Log.v(Constants.TAG, bookUnsignedUrl);
                    new DownloadFile().execute(bookUnsignedUrl, bookName+".epub");
                }
            }
        }
    }


    public void downloadFreeBook(){
        BookDetail bookDetail = Presenter.getInstance().getModel(BookDetail.class, Constants.CHECK_SAVED_BOOK_DATA);
        Book book = Presenter.getInstance().getModel(Book.class, Constants.BOOK_DESCRIPTION_ID);
        String bookName = book.getTitle();
        key = getKey();
        iv = getIV();

        notificationManager =
                (NotificationManager) mainActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(mainActivity);
        mBuilder.setContentTitle("PDF Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.mipmap.ic_launcher);
        Log.v(Constants.TAG, book+"");
        if(bookDetail.getBookPdf()!=null){
            if(bookDetail.getBookPdf().get("url")!=null){
                Map<String, Object> bookHashMap = (Map<String, Object>)bookDetail.getBookPdf().get("url");
                if(bookHashMap != null) {
                    String bookUnsignedUrl = (String)bookHashMap.get("unSignedUrl");
                    Log.v(Constants.TAG, bookUnsignedUrl);
                    new DownloadSampleFile().execute(bookUnsignedUrl, bookName+".epub", "BOOK");
                }
            }
        }
    }



    public void decryptFile(String bookKey, String bookIv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IOException {
        Book book = Presenter.getInstance().getModel(Book.class,Constants.BOOK_DESCRIPTION_ID);
        String bookNameTxt = book.getTitle();
        File outFile = new File(Environment.getExternalStorageDirectory() + "/OrthoPg/" + bookNameTxt + ".epub");
        File decFile = new File(Environment.getExternalStorageDirectory() + "/OrthoPg/" + "dec" + bookNameTxt + ".epub" );
        FileInputStream enfis = new FileInputStream(outFile);
        FileOutputStream defos = new FileOutputStream(decFile);
        Cipher decipher = Cipher.getInstance("AES");
        byte[] keyArray = Base64.decode(bookKey,Base64.DEFAULT);
        byte[] ivArray = Base64.decode(bookIv,Base64.DEFAULT);
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

    @OnClick(R.id.fragment_book_description_imageview3) void onShare(){

        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                .setCanonicalIdentifier(bookId)
                .setTitle(bookName)
                .setContentDescription(bookDesscription_)
                .setContentImageUrl(bookImageUrl)
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .addContentMetadata("type", "book")
                .addContentMetadata("id", bookId);

        LinkProperties linkProperties = new LinkProperties()
                .setFeature("sharing");
        branchUniversalObject.generateShortUrl(mainActivity, linkProperties, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error == null) {
                    Log.i("MyApp", "got my Branch link to share: " + url);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Checkout this book from OrthoPG");
                    intent.putExtra(Intent.EXTRA_TEXT, url);
                    startActivity(Intent.createChooser(intent, "Share URL"));
                } else {
                    Log.e(Constants.TAG,error.toString());
                }
            }
        });
    }

    public static String hashCal(String type, String str){

        byte[] hashSeq = str.getBytes();
        StringBuffer hexString = new StringBuffer();
        try{
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashSeq);
            byte[] messageDigest = algorithm.digest();
            for(int i=0;i<messageDigest.length;i++){
                String hex = Integer.toHexString(0xFF &messageDigest[i]);
                if(hex.length() == 1){
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        }catch (NoSuchAlgorithmException e){

        }
        return hexString.toString();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity)getActivity();
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Presenter.getInstance().removeModelFromList(Constants.BOOK_DESCRIPTION_ID);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        Presenter.getInstance().removeModelFromList(Constants.BOOK_DESCRIPTION_ID);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
