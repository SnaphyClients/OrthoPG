package com.orthopg.snaphy.orthopg.Fragment.BooksFragment;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.Presentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
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
    String bookId = "";
    public final static String TAG = "BookDescriptionFragment";
    int read;
    File inputFile, outFile, decFile;
    String bookKey, bookIv;
    String bookName;
    NotificationManager notificationManager;
    NotificationCompat.Builder mBuilder;
    int id = 1;

    public BookDescriptionFragment() {
        // Required empty public constructor
    }

    public static BookDescriptionFragment newInstance() {
        BookDescriptionFragment fragment = new BookDescriptionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = mainActivity.getSharedPreferences(Constants.BOOK_SHARED_PREFERENCE,Context.MODE_PRIVATE);
        key = getKey();
        iv = getIV();
        //checkPurchasedBook(book);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_description, container, false);
        ButterKnife.bind(this,view);
        getBookData();
        getBookDetail();
        checkPurchased();
        //String key = mainActivity.getSharedPreferences.getString("sample.pdf","");
        checkDownloadSample();
        outFile = new File(Environment.getExternalStorageDirectory() + "/OrthoPg/" + "sample.pdf");
        decFile = new File(Environment.getExternalStorageDirectory() + "/OrthoPg/" + "dsample.pdf");
        return view;
    }

    public void checkDownloadSample(){
        Book book = Presenter.getInstance().getModel(Book.class, Constants.BOOK_DESCRIPTION_ID);
        if(book!=null){
            bookName = book.getTitle();
            File sampleEpubFile = new File(Environment.getExternalStorageDirectory() + "/OrthoPg/" + bookName + "_sample.epub");
            if(sampleEpubFile.exists()){
                downloadSample.setText("View Sample");
            }else{
                downloadSample.setText("Download Sample");
            }
        }
    }

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
                }

                @Override
                public void onSuccess(BookDetail object) {
                    super.onSuccess(object);
                    if(object!=null){
                        Presenter.getInstance().addModel(Constants.CHECK_SAVED_BOOK_DATA, object);
                        checkIfBookPresentLocally(book);
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


    public void checkIfBookPresentLocally(Book book)  {
        String bookId = book.getId().toString();
        String bookKey = sharedPreferences.getString(bookId, "");
        String bookIv = sharedPreferences.getString(bookId + "iv", "");
        String bookName = book.getTitle();
        File file = new File(Environment.getExternalStorageDirectory() + "/OrthoPg/" + bookName + ".epub");
        if(bookKey.isEmpty()||bookIv.isEmpty()||!file.exists()){
            eBookDownload.setText("Download");
        } else{
            eBookDownload.setText("View");
        }
    }

    public void getBookDetail(){
        Book book = Presenter.getInstance().getModel(Book.class,Constants.BOOK_DESCRIPTION_ID);
        if(book!=null){
            bookId = String.valueOf(book.getId());
            BookRepository bookRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(BookRepository.class);
            bookRepository.fetchBookDetail(new HashMap<String, Object>(), bookId, new ObjectCallback<BookDetail>() {
                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onSuccess(BookDetail object) {
                    super.onSuccess(object);

                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                }

                @Override
                public void onFinally() {
                    super.onFinally();
                    //getBookData();
                }
            });
            /*bookDetailRepository.findOne(bookDetail.toMap(), new DataListCallback<BookDetail>() {
                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onSuccess(DataList<BookDetail> objects) {
                    super.onSuccess(objects);
                    BookDetail bookDetail1 = objects.get(0);
                    Presenter.getInstance().addModel(Constants.BOOK_DETAIL_MODEL_VALUE, bookDetail1);
                    getBookData();

                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                }

                @Override
                public void onFinally() {
                    super.onFinally();
                }
            });  */
        }
    }

    public void getBookData(){
        Book book = Presenter.getInstance().getModel(Book.class,Constants.BOOK_DESCRIPTION_ID);
        if(book!=null){
            bookId = String.valueOf(book.getId());
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

            if(book.getEbookPrice()!=null){
                if(!book.getEbookPrice().isEmpty()){
                    if(Double.parseDouble(book.getEbookPrice())==0){
                        eBookDownload.setText("Read Book For Free");
                        downloadSample.setVisibility(View.GONE);
                        downloadSampleText.setVisibility(View.GONE);
                    } else{
                        downloadSample.setVisibility(View.VISIBLE);
                        downloadSampleText.setVisibility(View.VISIBLE);
                        eBookDownload.setText("Ebook @ INR " + book.getEbookPrice());
                    }
                } else{
                    eBookDownload.setText("Read Book");
                    downloadSample.setVisibility(View.GONE);
                    downloadSampleText.setVisibility(View.GONE);
                }
            } else{
                eBookDownload.setText("Read Book");
                downloadSample.setVisibility(View.GONE);
                downloadSampleText.setVisibility(View.GONE);
            }

            if(book.getHardCopyPrice()!=null){
                if(!book.getHardCopyPrice().isEmpty()){
                    if(Double.parseDouble(book.getHardCopyPrice())==0){
                        hardCopyDownload.setText(" Hardcopy Not Available");
                        hardCopyDownload.setEnabled(false);
                        hardCopyDownload.setBackground(getResources().getDrawable(R.drawable.curved_rectangle_disabled_filled));
                    } else{
                        hardCopyDownload.setEnabled(false);
                        hardCopyDownload.setText("Hardcopy @ INR " + book.getHardCopyPrice());
                    }
                } else{
                    hardCopyDownload.setText("Not Available");
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

 /*   public boolean checkPurchasedBook(Book book){
        HashMap<String, Object> filter = new HashMap<>();
        HashMap<String, Object> where = new HashMap<>();
        final boolean[] isBookPresent = {false};
        Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
        String customerId = String.valueOf(customer.getId());
        String bookId = String.valueOf(book.getId());
        if(customerId!=null && bookId!=null){
            where.put("customerId", customerId);
            filter.put("where", where);
            PaymentRepository paymentRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(PaymentRepository.class);
            paymentRepository.find(filter, new DataListCallback<Payment>() {
                @Override
                public void onSuccess(DataList<Payment> objects) {
                    super.onSuccess(objects);
                    if(objects!=null){
                        isBookPresent[0] = true;
                    }
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    Log.e(Constants.TAG, t.toString());
                    isBookPresent[0] = false;
                }
            });
            return isBookPresent[0];
        }
        return false;
    }*/

    @OnClick(R.id.fragment_book_description_imageview1) void onBack(){
        mainActivity.onBackPressed();
    }

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
                intent.putExtra(FolioActivity.INTENT_EPUB_SOURCE_PATH,  Environment.getExternalStorageDirectory() + "/OrthoPg/" + bookName + "_sample.epub");
                startActivity(intent);
            }
        }
    }

    public void downloadBookSample(){
        Book book = Presenter.getInstance().getModel(Book.class, Constants.BOOK_DESCRIPTION_ID);
        if(book!=null){
            if (book.getUploadSampleBook() != null) {
                if (book.getUploadSampleBook().get("url") != null) {
                    Map<String, Object> bookHashMap = (Map<String, Object>) book.getUploadSampleBook().get("url");
                    if (bookHashMap != null) {
                        String bookUnsignedUrl = (String) bookHashMap.get("unSignedUrl");

                        new DownloadSampleFile().execute(bookUnsignedUrl, bookName + "_sample.epub");
                    }
                }
            }
        }
    }
    @OnClick(R.id.fragment_book_description_button4) void onHardCopyBuy(){
        //Check Internet Connection
        if(!mainActivity.snaphyHelper.isNetworkAvailable()){
            Snackbar.make(hardCopyDownload,"No Network Connection! Check Internet Connection and try again", Snackbar.LENGTH_SHORT).show();
        } else{
              Presenter.getInstance().addModel(Constants.BOOK_TYPE, Constants.HARDCOPY_BOOK_TYPE);
            mainActivity.replaceFragment(R.layout.fragment_checkout, null);
            Presenter.getInstance().addModel(Constants.BOOK_TYPE, Constants.HARDCOPY_BOOK_TYPE);
            /*Book book = Presenter.getInstance().getModel(Book.class, Constants.BOOK_DESCRIPTION_ID);
            BookRepository bookRepository = mainActivity.snaphyHelper.getLoopBackAdapter().createRepository(BookRepository.class);
            bookRepository.fetchBookDetail();
            if(book.getUploadBook()!=null){

            }*/
        }
       /* if(hardCopyDownload.getText().toString().equals("Download")) {
            new DownloadFile().execute("http://www.damtp.cam.ac.uk/user/tong/string/string.pdf","sample.pdf");
        }
        else if(hardCopyDownload.getText().toString().equals("View")){
            try {
                FileInputStream enfis = new FileInputStream(outFile);
                FileOutputStream defos = new FileOutputStream(decFile);
                Cipher decipher = Cipher.getInstance("AES");
                String bookKey = sharedPreferences.getString(bookId,"");
                byte[] keyArray = Base64.decode(bookKey,Base64.DEFAULT);
                String bookIv = sharedPreferences.getString(bookId + "iv","");
                byte[] ivArray = Base64.decode(bookIv,Base64.DEFAULT);
                SecretKeySpec specKey = new SecretKeySpec(keyArray, "AES");
                decipher.init(Cipher.DECRYPT_MODE,specKey,new IvParameterSpec(ivArray));
                CipherOutputStream cos = new CipherOutputStream(defos,decipher);
                while((read = enfis.read())!=-1){
                    cos.write(read);
                    cos.flush();
                }
                cos.close();
                Toast.makeText(mainActivity,"Decrytption completed",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mainActivity,PDFReaderActivity.class);
                startActivity(intent);
            }catch (Exception e){
                e.printStackTrace();
            }
            // mainActivity.replaceFragment(R.layout.fragment_book_pdfrender, null);

        }*/
/*
        PayUmoneySdkInitilizer.PaymentParam.Builder builder = new PayUmoneySdkInitilizer.PaymentParam.Builder()
                .setMerchantId("XXX")
                .setKey("YYY")
                .setIsDebug(true)
                .setAmount(10)
                .setTnxId("Onf7" + System.currentTimeMillis())
                .setPhone("9876543210")
                .setProductName("Product name")
                .setFirstName("Name")
                .setEmail("test@payu.com")
                .setsUrl("https://www.PayUmoney.com/mobileapp/PayUmoney/success.php")
                .setfUrl("https://www.PayUmoney.com/mobileapp/PayUmoney/failure.php")
                .setUdf1("")
                .setUdf2("")
                .setUdf3("")
                .setUdf4("")
                .setUdf5("");
        PayUmoneySdkInitilizer.PaymentParam paymentParam = builder.build();
        String hashSequence = "key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|salt";
        String serverCalulatedHash = hashCal("SHA-512", hashSequence);
        paymentParam.setMerchantHash(serverCalulatedHash);
        PayUmoneySdkInitilizer.startPaymentActivityForResult(mainActivity, paymentParam);*/
    }


    private class DownloadSampleFile extends AsyncTask<String, Void, Void> {

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
            downloadSampleFile(fileUrl, epubFile);
            //downloadEncryptedFile(fileUrl, pdfFile);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(mainActivity, "Download Sample of Book Completed..", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mainActivity, FolioActivity.class);
            intent.putExtra(FolioActivity.INTENT_EPUB_SOURCE_TYPE, FolioActivity.EpubSourceType.SD_CARD);
            intent.putExtra(FolioActivity.INTENT_EPUB_SOURCE_PATH,  Environment.getExternalStorageDirectory() + "/OrthoPg/" + bookName + "_sample.epub");
            startActivity(intent);
        }
    }


    public void downloadSampleFile(String fileUrl, File outFile){
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
           // hardCopyDownload.setText("View");
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
            String bookId = bookDetail.getBookId();
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
        } else if(mainActivity.snaphyHelper.isNetworkAvailable()){
            if(eBookDownload.getText().toString().equals("Download")){
                downloadBook();
            } else{
                Presenter.getInstance().addModel(Constants.BOOK_TYPE, Constants.EBOOK_BOOK_TYPE);
                mainActivity.replaceFragment(R.layout.fragment_checkout, null);
            }
        } else{
            Snackbar.make(eBookDownload,"No Network Connection! Check Internet Connection and try again", Snackbar.LENGTH_SHORT).show();
        }
        //No network Connection
       /* if(!mainActivity.snaphyHelper.isNetworkAvailable()){
            Snackbar.make(eBookDownload,"No Network Connection! Check Internet Connection and try again", Snackbar.LENGTH_SHORT).show();
        } else if(eBookDownload.getText().toString().equals("View")){
             BookDetail bookDetail = Presenter.getInstance().getModel(BookDetail.class, Constants.CHECK_SAVED_BOOK_DATA);
             String bookId = bookDetail.getBookId();
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
        } else if(eBookDownload.getText().toString().equals("Read Book For Free")){
             BookDetail bookDetail = Presenter.getInstance().getModel(BookDetail.class, Constants.BOOK_DETAIL_MODEL_VALUE);
             if(bookDetail.getBookPdf()!=null){
                 if(bookDetail.getBookPdf().get("url")!=null){
                     Map<String, Object> bookHashMap = (Map<String, Object>)bookDetail.getBookPdf().get("url");
                     if(bookHashMap!=null){
                          String bookUnsignedUrl = (String)bookHashMap.get("unSignedUrl");
                          new DownloadFile().execute(bookUnsignedUrl, bookName+".epub");
                     }
                 }
             }
        }
        else {
            Presenter.getInstance().addModel(Constants.BOOK_TYPE, Constants.EBOOK_BOOK_TYPE);
            mainActivity.replaceFragment(R.layout.fragment_checkout, null);
        }  */

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
                    new DownloadFile().execute(bookUnsignedUrl, bookName+".epub");
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
        Toast.makeText(mainActivity,"Decrytption completed",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(mainActivity, FolioActivity.class);
        intent.putExtra(FolioActivity.INTENT_EPUB_SOURCE_TYPE, FolioActivity.EpubSourceType.SD_CARD);
        intent.putExtra(FolioActivity.INTENT_EPUB_SOURCE_PATH,  Environment.getExternalStorageDirectory() + "/OrthoPg/" + "dec" + bookNameTxt + ".epub");
        mainActivity.startActivity(intent);
    }

    @OnClick(R.id.fragment_book_description_imageview3) void onShare(){

        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                .setCanonicalIdentifier("item/12345")
                .setTitle("My Content Title")
                .setContentDescription("My Content Description")
                .setContentImageUrl("https://example.com/mycontent-12345.png")
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
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
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
