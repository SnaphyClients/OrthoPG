package com.orthopg.snaphy.orthopg.SpecialityFragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.DataListCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Speciality;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import java.sql.DatabaseMetaData;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nikita on 21/3/17.
 */

public class SpecialityAdapter extends RecyclerView.Adapter<SpecialityAdapter.ViewHolder> {

    MainActivity mainActivity;
    DataList<Speciality> specialities;
    public SpecialityAdapter(MainActivity mainActivity, DataList<Speciality> specialities){

        this.mainActivity = mainActivity;
        this.specialities = specialities;
    }

   /* public SpecialityAdapter(MainActivity mainActivity, DataList<SpecialityModel> specialityModelDataList){

        this.mainActivity = mainActivity;
        this.specialityModelDataList = specialityModelDataList;
    }*/

    @Override
    public SpecialityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View specialityView = inflater.inflate(R.layout.layout_speciality,parent, false);
        SpecialityAdapter.ViewHolder viewHolder = new SpecialityAdapter.ViewHolder(specialityView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SpecialityAdapter.ViewHolder holder, final int position) {
       final Speciality speciality_ = specialities.get(position);
        //final SpecialityModel specialityModel = specialityModelDataList.get(position);
        TextView speciality = holder.speciality;
        final CheckBox checkBox = holder.checkBox;



        if(speciality_!=null){
            //specialityModel.setSpeciality(speciality_);
           if(speciality_.getName()!=null){
               if(!speciality_.getName().isEmpty()){
                   speciality.setText(speciality_.getName());


               }
           }

            final DataList<Speciality> specialityDataList = Presenter.getInstance().getList(Speciality.class, Constants.CUSTOMER_SPECIALITY_LIST);
            if(specialityDataList!=null){
                if(specialityDataList.size()!=0){
                    for(Speciality speciality1:specialityDataList){
                        if(speciality1.getName().equals(speciality_.getName())){
                            //specialityModel.setSpecialityChecked(true);
                            checkBox.setChecked(true);
                            //specialityModel.setSpecialitySelected(true);
                        }
                    }
                }
            }

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(buttonView.isChecked()){
                        checkBox.setChecked(true);
                        specialityDataList.add(speciality_);
                        Presenter.getInstance().addList(Constants.CUSTOMER_SPECIALITY_LIST, specialityDataList);
                        //specialityModel.setSpecialitySelected(true);


                    } else{
                        checkBox.setChecked(false);
                        //specialityDataList.remove(speciality_);
                        specialityDataList.remove(position);
                        Presenter.getInstance().addList(Constants.CUSTOMER_SPECIALITY_LIST, specialityDataList);
                        //specialityModel.setSpecialitySelected(false);

                    }
                }
            });

           /* checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(buttonView.isChecked()){
                        buttonView.setChecked(false);
                        speciality_
                    }
                }
            });*/
        }
    }

    @Override
    public int getItemCount() {
        return specialities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.layout_speciality_textview1) TextView speciality;
        @Bind(R.id.layout_speciality_checkbox1) CheckBox checkBox;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
