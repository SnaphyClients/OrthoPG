package com.orthopg.snaphy.orthopg.Fragment.ProfileFragment;

import android.content.Context;
import android.util.Log;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.DataListCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Qualification;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.QualificationRepository;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.strongloop.android.loopback.RestAdapter;

import java.util.HashMap;

/**
 * Created by nikita on 21/3/17.
 */

public class QualificationPresenter {

    RestAdapter restAdapter;
    MainActivity mainActivity;
    DataList<Qualification> qualificationDataList;
    public int limit = 7;
    public int skip = 0;

    public QualificationPresenter(RestAdapter restAdapter, MainActivity mainActivity){
         this.restAdapter = restAdapter;
         this.mainActivity = mainActivity;

        if(Presenter.getInstance().getList(Qualification.class, Constants.QUALIFICATION_LIST)==null){
            qualificationDataList = new DataList<>();
            Presenter.getInstance().addList(Constants.QUALIFICATION_LIST,qualificationDataList);
        } else{
            qualificationDataList = Presenter.getInstance().getList(Qualification.class, Constants.QUALIFICATION_LIST);
        }
    }

    public void fetchQualification(final boolean reset){

        if(reset){
            skip = 0;
        }

        HashMap<String, Object> filter = new HashMap<>();
        QualificationRepository qualificationRepository = restAdapter.createRepository(QualificationRepository.class);
        qualificationRepository.find(filter, new DataListCallback<Qualification>() {
            @Override
            public void onBefore() {
                super.onBefore();
                mainActivity.startProgressBar(mainActivity.progressBar);
            }

            @Override
            public void onSuccess(DataList<Qualification> objects) {
                super.onSuccess(objects);
                if(objects!=null){
                    qualificationDataList.addAll(objects);
                }
            }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
                Log.e(Constants.TAG, t.toString());
            }

            @Override
            public void onFinally() {
                super.onFinally();
                mainActivity.stopProgressBar(mainActivity.progressBar);
            }
        });
    }
}
