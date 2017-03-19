package com.orthopg.snaphy.orthopg.Fragment.BooksFragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.PDFViewPagerActivity;
import com.orthopg.snaphy.orthopg.R;
import com.payUMoney.sdk.PayUmoneySdkInitilizer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    public static byte[] key, iv;
    private static final int  MEGABYTE = 1024 * 1024;
    @Bind(R.id.fragment_book_description_button3) Button bookDownload;
    public final static String TAG = "BookDescriptionFragment";
    int read;
    File inputFile, outFile, decFile;

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
        key = getKey();
        iv = getIV();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_description, container, false);
        ButterKnife.bind(this,view);
        outFile = new File(Environment.getExternalStorageDirectory() + "/OrthoPg/" + "sample.pdf");
        decFile = new File(Environment.getExternalStorageDirectory() + "/OrthoPg/" + "dsample.pdf");
        return view;
    }

    @OnClick(R.id.fragment_book_description_button3) void onHardCopyBuy(){
        if(bookDownload.getText().toString().equals("Download")) {
            new DownloadFile().execute("http://www.damtp.cam.ac.uk/user/tong/string/string.pdf","sample.pdf");
        }
        else if(bookDownload.getText().toString().equals("View")){
            try {
                FileInputStream enfis = new FileInputStream(outFile);
                FileOutputStream defos = new FileOutputStream(decFile);
                Cipher decipher = Cipher.getInstance("AES");
                SecretKeySpec specKey = new SecretKeySpec(key, "AES");
                decipher.init(Cipher.DECRYPT_MODE,specKey,new IvParameterSpec(iv));
                CipherOutputStream cos = new CipherOutputStream(defos,decipher);
                while((read = enfis.read())!=-1){
                    cos.write(read);
                    cos.flush();
                }
                cos.close();
                Toast.makeText(mainActivity,"Decrtption completed",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mainActivity,PDFViewPagerActivity.class);
                startActivity(intent);
            }catch (Exception e){
                e.printStackTrace();
            }
            // mainActivity.replaceFragment(R.layout.fragment_book_pdfrender, null);

        }
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



    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "OrthoPg");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            downloadFile(fileUrl, pdfFile);
            //downloadEncryptedFile(fileUrl, pdfFile);
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(mainActivity, "Download Completed..", Toast.LENGTH_SHORT).show();
            bookDownload.setText("View");
        }
    }


    public void downloadFile(String fileUrl, File outFile){
        try {

            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fos = new FileOutputStream(outFile);
            Cipher encipher = Cipher.getInstance("AES");
            SecretKeySpec specKey = new SecretKeySpec(key, "AES");
            encipher.init(Cipher.ENCRYPT_MODE,specKey,new IvParameterSpec(iv));
            CipherInputStream cis = new CipherInputStream(inputStream,encipher);
            while((read = cis.read())!=-1){
                fos.write((char)read);
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

    @OnClick(R.id.fragment_book_description_button4) void onEbookBuy(){

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
        PayUmoneySdkInitilizer.startPaymentActivityForResult(mainActivity, paymentParam);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
