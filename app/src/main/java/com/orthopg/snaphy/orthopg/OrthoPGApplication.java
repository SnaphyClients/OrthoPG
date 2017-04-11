package com.orthopg.snaphy.orthopg;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import io.branch.referral.Branch;

/**
 * Created by nikita on 28/3/17.
 */

public class OrthoPGApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Branch.getAutoInstance(this);
    }
}
