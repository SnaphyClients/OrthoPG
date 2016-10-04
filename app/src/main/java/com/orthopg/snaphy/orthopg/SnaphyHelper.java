package com.orthopg.snaphy.orthopg;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.strongloop.android.loopback.LocalInstallation;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import java.io.IOException;

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
            }

            @Override
            public void onError(final Throwable t) {
                Log.e(Constants.TAG, "Error saving Installation.", t);

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

}
