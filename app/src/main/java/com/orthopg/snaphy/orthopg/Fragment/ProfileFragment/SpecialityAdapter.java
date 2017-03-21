package com.orthopg.snaphy.orthopg.Fragment.ProfileFragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Speciality;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

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

    @Override
    public SpecialityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View specialityView = inflater.inflate(R.layout.layout_speciality,parent, false);
        SpecialityAdapter.ViewHolder viewHolder = new SpecialityAdapter.ViewHolder(specialityView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SpecialityAdapter.ViewHolder holder, int position) {
        Speciality speciality_ = specialities.get(position);

        TextView speciality = holder.speciality;
        CheckBox checkBox = holder.checkBox;

        if(speciality!=null){
           if(speciality_.getName()!=null){
               if(!speciality_.getName().isEmpty()){
                   speciality.setText(speciality_.getName());
               }
           }
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
