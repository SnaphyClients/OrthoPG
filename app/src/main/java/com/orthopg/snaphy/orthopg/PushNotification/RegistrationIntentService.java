package com.orthopg.snaphy.orthopg.PushNotification;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.R;

import java.io.IOException;

/**
 * Created by Ravi-Gupta on 10/23/2016.
 */

public class RegistrationIntentService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public RegistrationIntentService(String name) {
        super(name);
    }

    public RegistrationIntentService() {
        super("RegistrationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.v(Constants.TAG, "Device Token = " + token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
