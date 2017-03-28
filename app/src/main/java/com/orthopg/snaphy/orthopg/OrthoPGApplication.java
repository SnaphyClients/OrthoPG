package com.orthopg.snaphy.orthopg;

import android.app.Application;

import io.branch.referral.Branch;

/**
 * Created by nikita on 28/3/17.
 */

public class OrthoPGApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Branch.getAutoInstance(this);
    }
}
