package com.orthopg.snaphy.orthopg.Fragment.CaseFragment;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ravi-Gupta on 9/21/2016.
 */
public class CaseListAdapter extends RecyclerView.Adapter<CaseListAdapter.ViewHolder> {

    MainActivity mainActivity;
    List<CaseModel> caseModelList;

    public CaseListAdapter(MainActivity mainActivity, List<CaseModel> caseModelList) {
        this.mainActivity = mainActivity;
        this.caseModelList = caseModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View caseView = inflater.inflate(R.layout.layout_case_list, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(caseView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CaseModel caseModel = caseModelList.get(position);
        CaseImageAdapter caseImageAdapter;

        ImageView imageView = holder.userImage;
        TextView caseHeading = holder.caseHeading;
        TextView userName = holder.userName;
        TextView casePostedTime = holder.casePostedTime;
        ImageButton like = holder.like;
        ImageButton saveCase = holder.saveCase;
        RecyclerView caseImages = holder.caseImages;
        TextView caseDescription = holder.caseDescription;
        TextView tag = holder.tag;
        ImageView isAnswerSelected = holder.isAnswerSelected;
        TextView selectedAnswerUserName = holder.selectedAnswerUserName;
        TextView selectedAnswer = holder.selectedAnswer;

        caseImages.setLayoutManager(new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false));
        caseImageAdapter = new CaseImageAdapter(caseModel.getCaseImage());
        caseImages.setAdapter(caseImageAdapter);

        imageView.setImageDrawable(caseModel.getDoctorImage());
        caseHeading.setText(caseModel.getPostHeading());
        userName.setText(caseModel.getDoctorName());
        casePostedTime.setText(caseModel.getPostTime());
        if(caseModel.isLiked()) {

        }
        if(caseModel.isSaved()){

        }
        caseDescription.setText(caseModel.getCaseDescription());
        tag.setText(caseModel.getTag());
        if(caseModel.getIsAnswerSelected()) {

        }
        selectedAnswerUserName.setText(caseModel.getSelectedAnswerUserName());
        selectedAnswer.setText(caseModel.getSelectedAnswer());

    }

    @Override
    public int getItemCount() {
        return caseModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.layout_case_list_image) ImageView userImage;
        @Bind(R.id.layout_case_list_textview1) TextView caseHeading;
        @Bind(R.id.layout_case_list_textview2) TextView userName;
        @Bind(R.id.layout_case_list_textview3) TextView casePostedTime;
        @Bind(R.id.layout_case_list_imagebutton1) ImageButton like;
        @Bind(R.id.layout_case_list_imagebutton2) ImageButton saveCase;
        @Bind(R.id.layout_case_list_recycler_view) RecyclerView caseImages;
        @Bind(R.id.layout_case_list_textview4) TextView caseDescription;
        @Bind(R.id.layout_case_list_textview5) TextView tag;
        @Bind(R.id.layout_case_list_imageview1) ImageView isAnswerSelected;
        @Bind(R.id.layout_case_list_textview6) TextView selectedAnswerUserName;
        @Bind(R.id.layout_case_list_textview7) TextView selectedAnswer;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
