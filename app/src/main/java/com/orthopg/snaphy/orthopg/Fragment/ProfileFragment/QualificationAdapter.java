package com.orthopg.snaphy.orthopg.Fragment.ProfileFragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Qualification;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nikita on 21/3/17.
 */

public class QualificationAdapter extends RecyclerView.Adapter<QualificationAdapter.ViewHolder> {

    MainActivity mainActivity;
    DataList<Qualification> qualificationDataList;

    public QualificationAdapter(MainActivity mainActivity, DataList<Qualification> qualificationDataList){

        this.mainActivity = mainActivity;
        this.qualificationDataList = qualificationDataList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View qualificationView = inflater.inflate(R.layout.layout_qualification,null);
        QualificationAdapter.ViewHolder viewHolder = new QualificationAdapter.ViewHolder(qualificationView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
          Qualification qualification = qualificationDataList.get(position);

          TextView textView = holder.textView;
          CheckBox checkBox = holder.checkBox;

        if(qualification!=null){
            if(qualification.getName()!=null){
                if(!qualification.getName().isEmpty()){
                    textView.setText(qualification.getName().toString());
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return qualificationDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.layout_qualification_textview1) TextView textView;
        @Bind(R.id.layout_qualification_checkbox1) CheckBox checkBox;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
