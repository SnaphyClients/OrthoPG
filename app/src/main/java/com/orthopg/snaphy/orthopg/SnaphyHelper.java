package com.orthopg.snaphy.orthopg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.ObjectCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.models.CustomContainer;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.models.ImageModel;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.AmazonImageRepository;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CustomContainerRepository;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CustomFileRepository;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CustomerRepository;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.strongloop.android.loopback.LocalInstallation;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.VoidCallback;
import com.strongloop.android.remoting.JsonUtil;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ravi-Gupta on 10/4/2016.
 */
public class SnaphyHelper {
    MainActivity mainActivity;

    public SnaphyHelper(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public RestAdapter getLoopBackAdapter() {
        if (mainActivity.restAdapter == null) {

            mainActivity.restAdapter = new RestAdapter(
                    mainActivity.getApplicationContext(),
                    Constants.apiUrl);
        }
        return mainActivity.restAdapter;
    }

    /**
     * Updates the registration for push notifications.
     */
    public void updateRegistration(String userId) {
        mainActivity.gcm = GoogleCloudMessaging.getInstance(mainActivity);
        final RestAdapter adapter = getLoopBackAdapter();
        // 2. Create LocalInstallation instance
        final LocalInstallation installation =  new LocalInstallation(mainActivity.context, adapter);

        // 3. Update Installation properties that were not pre-filled
        /*TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        LOOPBACK_APP_ID = mngr.getDeviceId();*/
        // Enter the id of the LoopBack Application
        installation.setAppId(Constants.LOOPBACK_APP_ID);
       /* Log.i(TAG, LOOPBACK_APP_ID);*/
        // Substitute a real id of the user logged in this application
        installation.setUserId(userId);

        // 4. Check if we have a valid GCM registration id
        if (installation.getDeviceToken() != null) {
            // 5a. We have a valid GCM token, all we need to do now
            //     is to save the installation to the server
            saveInstallation(installation);
        } else {
            // 5b. We don't have a valid GCM token. Get one from GCM
            // and save the installation afterwards.
            registerInBackground(installation);
        }
    }

    /**
     * Checks the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    public boolean checkPlayServices() {
        final int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mainActivity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, mainActivity,
                        Constants.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(mainActivity, "This device is not supported.", Toast.LENGTH_SHORT).show();
                Log.i(Constants.TAG, "This device is not supported.");
                mainActivity.finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID in the provided LocalInstallation
     */
    private void registerInBackground(final LocalInstallation installation) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(final Void... params) {
                try {
                    final String regid = mainActivity.gcm.register(Constants.SENDER_ID);
                    installation.setDeviceToken(regid);
                    return "Device registered, registration ID=" + regid;
                } catch (final IOException ex) {
                    Log.e(Constants.TAG, "GCM registration failed.", ex);
                    return "Cannot register with GCM:" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
            }

            @Override
            protected void onPostExecute(final String msg) {

                saveInstallation(installation);
            }
        }.execute(null, null, null);
    }

    /**
     * Saves the Installation to the LoopBack server and reports the result.
     * @param installation_
     */
    void saveInstallation(final LocalInstallation installation_) {
        installation_.save(new VoidCallback() {

            @Override
            public void onSuccess() {
                final Object id = installation_.getId();
                mainActivity.installation = installation_;
                final String msg = "Installation saved with id " + id;
                Log.i(Constants.TAG, msg);
                Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
                //Now save the installation id with customer..
                if (customer != null) {
                    //Add registration id to customer..
                    customer.setRegistrationId((String) id);
                    updateCustomer(customer);
                }
            }

            @Override
            public void onError(final Throwable t) {
                Log.e(Constants.TAG, "Error saving Installation.", t);

            }
        });
    }


    public void updateCustomer(Customer customer){
        Map<String, ? extends Object> data = customer.convertMap();
        //Remove the password field..
        data.remove("password");
        CustomerRepository customerRepository = getLoopBackAdapter().createRepository(CustomerRepository.class);
        customerRepository.updateAttributes((String) customer.getId(), data, new ObjectCallback<Customer>() {
            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onSuccess(Customer object) {
                Log.v(Constants.TAG, "Customer Profile is Updated");
            }

            @Override
            public void onError(Throwable t) {
                Log.e(Constants.TAG, t.toString());
                Log.v(Constants.TAG, "Error in update Customer Method");
            }

            @Override
            public void onFinally() {
                super.onFinally();
            }
        });
    }


    public void registerInstallation(Customer customer){
        if (checkPlayServices()) {
            if (customer != null) {
                // logged in
                Log.d(Constants.TAG, "User logged in successfully");
                updateRegistration((String)customer.getId());
            } else {
                // anonymous user
                Log.d(Constants.TAG, "User not logged in ");
                updateRegistration("Anonymous User");
            }
        } else {
            Log.e(Constants.TAG, "No valid Google Play Services APK found.");
        }
    }






    public void loadImage(final String imageUri,final ImageView imageView){
        final int retries = 0;
        try {
            Glide.with(mainActivity)
                    .load(imageUri)
                    .placeholder(R.drawable.default_image)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                            if (retries <= 3) {
                                int track = retries + 1;
                                Log.e(Constants.TAG, "Image downloading failed. retrying." + track);
                                loadImage(imageUri, imageView, track);

                            } else {
                                Log.e(Constants.TAG, "Maximum retries of image downloading done. Cannot download image.");
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(imageView);
        } catch (Exception e) {
            Log.e(Constants.TAG, "Error downloading image from server.");
            Log.e(Constants.TAG, e.toString());
        }

    }

    public void loadImage(final String imageUri,final ImageView imageView, int mipmap){
        final int retries = 0;
        try {
            Glide.with(mainActivity)
                    .load(imageUri)
                    .placeholder(mainActivity.getResources().getDrawable(mipmap))
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                            if (retries <= 3) {
                                int track = retries + 1;
                                Log.e(Constants.TAG, "Image downloading failed. retrying." + track);
                                loadImage(imageUri, imageView, track);

                            } else {
                                Log.e(Constants.TAG, "Maximum retries of image downloading done. Cannot download image.");
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(imageView);
        } catch (Exception e) {
            Log.e(Constants.TAG, "Error downloading image from server.");
            Log.e(Constants.TAG, e.toString());
        }

    }


    public void fetchThumb(Map<String, Object> fileObj, final ImageView imageView){
        if(fileObj != null){
            String container = (String)fileObj.get("container");
            String file = (String)fileObj.get("name");
            fetchUrl(container, file, imageView, "thumb_");
        }

    }


    public void fetchMedium(Map<String, Object> fileObj, final ImageView imageView){
        if(fileObj != null) {
            String container = (String) fileObj.get("container");
            String file = (String) fileObj.get("name");

            fetchUrl(container, file, imageView, "medium_");
        }
    }


    public void fetchSmall(Map<String, Object> fileObj, final ImageView imageView) {
        if (fileObj != null) {
            String container = (String) fileObj.get("container");
            String file = (String) fileObj.get("name");
            fetchUrl(container, file, imageView, "small_");
        }
    }


    public void loadUnSignedThumbnailImage(Map<String, Object> imageObj, final ImageView imageView, int defaultImage){
        String file = (String) imageObj.get("name");
        String url = Constants.AMAZON_CLOUD_FRONT_URL + "/thumb_" + file;
        loadImage(url, imageView, defaultImage);
    }


    public void loadUrlWithDefaultImage(Map<String, Object> imageObj, final ImageView imageView){
        if(imageObj != null){
            if(imageObj.get("url") != null){
                Map<String, String> hashMap;
                try{
                    hashMap   = (Map<String, String>)imageObj.get("url");
                    if(hashMap.get("unSignedUrl") != null){
                        String unsignedUrl = hashMap.get("unSignedUrl");
                        if(unsignedUrl != null){
                            try{
                                loadImage(unsignedUrl, imageView);
                            }
                            catch (Exception e){
                                Log.e(Constants.TAG, e +"");
                            }

                        }
                        else{
                            fetchSmall(imageObj, imageView);
                        }
                    }else{
                        String defaultUrl = hashMap.get("defaultUrl");
                        if(defaultUrl != null){
                            try{
                                loadImage(Constants.baseUrl + defaultUrl, imageView);
                            }
                            catch (Exception e){
                                Log.e(Constants.TAG, e +"");
                            }
                        }
                        else{
                            fetchSmall(imageObj, imageView);
                        }
                    }
                }catch (Exception e){
                    fetchSmall(imageObj, imageView);
                }


            }
            else{
                fetchSmall(imageObj, imageView);
            }
        }else{
            fetchSmall(imageObj, imageView);
        }
    }


    //Fetch url from server..
    public void loadUnsignedUrl(Map<String, Object> imageObj, final ImageView imageView){
        if(imageObj != null){
            if(imageObj.get("url") != null){
                Map<String, String> hashMap;
                try{
                    hashMap   = (Map<String, String>)imageObj.get("url");
                    if(hashMap.get("unSignedUrl") != null){
                        String unsignedUrl = hashMap.get("unSignedUrl");
                        if(unsignedUrl != null){
                            try{
                                loadImage(unsignedUrl, imageView);
                            }
                            catch (Exception e){
                                Log.e(Constants.TAG, e +"");
                            }

                        }
                        else{
                            fetchSmall(imageObj, imageView);
                        }
                    }else{
                        String defaultUrl = hashMap.get("defaultUrl");
                        if(defaultUrl != null){
                            try{
                                loadImage(Constants.baseUrl + defaultUrl, imageView);
                            }
                            catch (Exception e){
                                Log.e(Constants.TAG, e +"");
                            }
                        }
                        else{
                            fetchSmall(imageObj, imageView);
                        }
                    }
                }catch (Exception e){
                    fetchSmall(imageObj, imageView);
                }


            }
            else{
                fetchSmall(imageObj, imageView);
            }
        }else{
            fetchSmall(imageObj, imageView);
        }

    }

    //Fetch url from server..
    public void loadUnsignedUrl(Map<String, Object> imageObj, final ImageView imageView, int defaultImage){
        if(imageObj != null){
            if(imageObj.get("url") != null){
                Map<String, String> hashMap;
                try{
                    hashMap   = (Map<String, String>)imageObj.get("url");
                    if(hashMap.get("unSignedUrl") != null){
                        String unsignedUrl = hashMap.get("unSignedUrl");
                        if(unsignedUrl != null){
                            try{
                                loadImage(unsignedUrl, imageView);
                            }
                            catch (Exception e){
                                Log.e(Constants.TAG, e +"");
                            }

                        }
                        else{
                            fetchSmall(imageObj, imageView);
                        }
                    }else{
                        String defaultUrl = hashMap.get("defaultUrl");
                        if(defaultUrl != null){
                            try{
                                loadImage(Constants.baseUrl + defaultUrl, imageView);
                            }
                            catch (Exception e){
                                Log.e(Constants.TAG, e +"");
                            }
                        }
                        else{
                            fetchSmall(imageObj, imageView);
                        }
                    }
                }catch (Exception e){
                    fetchSmall(imageObj, imageView);
                }


            }
            else{
                fetchSmall(imageObj, imageView);
            }
        }else{
            fetchSmall(imageObj, imageView);
        }

    }


    //Fetch url from server..
    public void fetchUrl(String container, String file, final ImageView imageView, String prefix){
        AmazonImageRepository amazonImageRepository = getLoopBackAdapter().createRepository(AmazonImageRepository.class);
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("type", "prefix");
        hashMap.put("value", prefix);
        amazonImageRepository.getUrl(container, file, hashMap, new com.androidsdk.snaphy.snaphyandroidsdk.callbacks.ObjectCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response != null) {
                    Map<String, Object> objectHashMap = (Map) JsonUtil.fromJson(response);
                    String defaultUrl = (String) objectHashMap.get("defaultUrl");
                    String signedUrl = (String) objectHashMap.get("signedUrl");
                    if (!signedUrl.isEmpty()) {
                        try {
                            loadImage(signedUrl, imageView);
                        } catch (Exception e) {
                            Log.e(Constants.TAG, e + "");
                        }
                    } else {
                        try {
                            loadImage(Constants.baseUrl + defaultUrl, imageView);
                        } catch (Exception e) {
                            Log.e(Constants.TAG, e + "");
                        }
                    }
                }

            }

            @Override
            public void onError(Throwable t) {

                Log.v(Constants.TAG, "Could not fetch url. Please try again later..");
            }
        });
    }



    public void uploadWithCallback(String containerName, File imageFile, final ObjectCallback<ImageModel> callback){
        Date date = new Date();
        String fileName = String.valueOf(date.getTime());
        //create a file to write bitmap data
        File file = new File(mainActivity.getCacheDir(), fileName + ".jpg");

        try{
            file.createNewFile();
            //Now converting image to bitmap..
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, out);
            //Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
            byte[] bitmapdata = out.toByteArray();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            //Flushing bitmap..
            bitmap.recycle();
            bitmap = null;



            CustomContainerRepository containerRepo = getLoopBackAdapter().createRepository(CustomContainerRepository.class);
            CustomFileRepository customFileRepository = getLoopBackAdapter().createRepository(CustomFileRepository.class);
            Map<String, String> objectHashMap = new HashMap<>();
            objectHashMap.put("name", containerName);
            CustomContainer container1 = containerRepo.createObject(objectHashMap);
            container1.UploadAmazon(file, new com.androidsdk.snaphy.snaphyandroidsdk.callbacks.ObjectCallback<ImageModel>() {
                @Override
                public void onSuccess(ImageModel object) {
                    // object
                    callback.onSuccess(object);
                }

                @Override
                public void onError(Throwable t) {

                    callback.onError(t);
                }
            });
        }
        catch (IOException e){
            //TODO SHOW MESSAGE TRY AGAIN UPLOADING IMAGE..
            Log.e(Constants.TAG, e.toString());
        }

    }

    public void  uploadImageToContainer(String containerName, File imageFile, final String TAG) {
        Date date = new Date();
        String fileName = String.valueOf(date.getTime());
        //create a file to write bitmap data
        File file = new File(mainActivity.getCacheDir(), fileName + ".jpg");

        try{
            file.createNewFile();
            //Now converting image to bitmap..
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, out);
            //Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
            byte[] bitmapdata = out.toByteArray();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            //Flushing bitmap..
            bitmap.recycle();
            bitmap = null;


            CustomContainerRepository containerRepo = getLoopBackAdapter().createRepository(CustomContainerRepository.class);
            CustomFileRepository customFileRepository = getLoopBackAdapter().createRepository(CustomFileRepository.class);
            Map<String, String> objectHashMap = new HashMap<>();
            objectHashMap.put("name", containerName);
            CustomContainer container1 = containerRepo.createObject(objectHashMap);
            container1.UploadAmazon(file, new ObjectCallback<ImageModel>() {
                @Override
                public void onSuccess(ImageModel object) {
                    // object
                    //EventBus.getDefault().post(object, TAG);
                }

                @Override
                public void onError(Throwable t) {

                    Log.e(Constants.TAG, t.toString());
                    ImageModel imageModel = new ImageModel();
                    //EventBus.getDefault().post(imageModel, TAG);
                }
            });
        }
        catch (IOException e){
            //TODO SHOW MESSAGE TRY AGAIN UPLOADING IMAGE..
            Log.e(Constants.TAG, e.toString());
            Log.e(Constants.TAG, "Error Downloading Image");
        }
    }


    public String getName(String firstName, String lastName){
        String name = "";
        if(!firstName.isEmpty()) {
            name = firstName;
            if(!lastName.isEmpty()) {
                name = name + " "+ lastName;
            }
        }
        return WordUtils.capitalize(name);
    }

    public boolean isNetworkAvailable() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }



}
