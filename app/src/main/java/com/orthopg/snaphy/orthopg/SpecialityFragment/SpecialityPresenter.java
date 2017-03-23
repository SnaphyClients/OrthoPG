package com.orthopg.snaphy.orthopg.SpecialityFragment;

import android.util.Log;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.DataListCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Speciality;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.SpecialityRepository;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.strongloop.android.loopback.RestAdapter;

import java.util.HashMap;

/**
 * Created by nikita on 21/3/17.
 */

public class SpecialityPresenter {

    RestAdapter restAdapter;
    MainActivity  mainActivity;
    DataList<Speciality> specialityDataList;
    public double limit = 7;
    public double skip = 0;

    public SpecialityPresenter(RestAdapter restAdapter, MainActivity mainActivity){

        this.restAdapter = restAdapter;
        this.mainActivity = mainActivity;

        if(Presenter.getInstance().getList(Speciality.class, Constants.SPECIALITY_LIST) == null){
            specialityDataList = new DataList<>();
            Presenter.getInstance().addList(Constants.SPECIALITY_LIST,specialityDataList);
        }
        else{
            specialityDataList = Presenter.getInstance().getList(Speciality.class, Constants.SPECIALITY_LIST);
        }
    }

    public void fetchSpecialities(final boolean reset){
        if(reset){
            skip = 0;
        }

        HashMap<String, Object> filter = new HashMap<>();
        filter.put("limit", limit);
        filter.put("skip", skip);
        SpecialityRepository specialityRepository = restAdapter.createRepository(SpecialityRepository.class);
        specialityRepository.find(filter, new DataListCallback<Speciality>() {
            @Override
            public void onBefore() {
                super.onBefore();
                mainActivity.startProgressBar(mainActivity.progressBar);
            }

            @Override
            public void onSuccess(DataList<Speciality> objects) {
                super.onSuccess(objects);
                if(objects!=null){
                    if(reset){
                        specialityDataList.clear();
                    }
                    specialityDataList.addAll(objects);
                    skip = skip + objects.size();
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
