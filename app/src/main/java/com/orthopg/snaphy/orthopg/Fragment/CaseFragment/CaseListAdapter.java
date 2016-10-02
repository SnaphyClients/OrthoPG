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

import com.orthopg.snaphy.orthopg.Fragment.PostedCasesFragment.PostedCasesFragment;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;
import com.orthopg.snaphy.orthopg.RecyclerItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ravi-Gupta on 9/21/2016.
 */
public class CaseListAdapter extends RecyclerView.Adapter<CaseListAdapter.ViewHolder> {

    MainActivity mainActivity;
    List<CaseModel> caseModelList;
    String TAG;

    public CaseListAdapter(MainActivity mainActivity, List<CaseModel> caseModelList, String TAG) {
        this.mainActivity = mainActivity;
        this.caseModelList = caseModelList;
        this.TAG = TAG;
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
        final CaseModel caseModel = caseModelList.get(position);
        CaseImageAdapter caseImageAdapter;

        ImageView imageView = holder.userImage;
        TextView caseHeading = holder.caseHeading;
        TextView userName = holder.userName;
        TextView casePostedTime = holder.casePostedTime;
        final ImageButton like = holder.like;
        final ImageButton saveCase = holder.saveCase;
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

        caseDescription.setText(caseModel.getCaseDescription());
        tag.setText(caseModel.getTag());
        if(caseModel.getIsAnswerSelected()) {

        }
        selectedAnswerUserName.setText(caseModel.getSelectedAnswerUserName());
        selectedAnswer.setText(caseModel.getSelectedAnswer());

        if(caseModel.isLiked()) {
            like.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.like_selected));
        } else {
            like.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.like_unselected));
        }

        if(caseModel.isSaved()) {
            saveCase.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.save_selected));
        } else {
            saveCase.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.save_unselected));
        }

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(caseModel.isLiked()) {
                    like.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.like_unselected));
                    caseModel.setIsLiked(false);
                } else {
                    like.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.like_selected));
                    caseModel.setIsLiked(true);
                }
            }
        });

        saveCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (caseModel.isSaved()) {
                    saveCase.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.save_unselected));
                    caseModel.setIsSaved(false);
                } else {
                    saveCase.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.save_selected));
                    caseModel.setIsSaved(true);
                }
            }
        });


        caseImages.addOnItemTouchListener(
                new RecyclerItemClickListener(mainActivity, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (TAG.equals(PostedCasesFragment.TAG)) {
                            mainActivity.replaceFragment(R.id.fragment_case_button4, null);
                        } else {
                            mainActivity.replaceFragment(R.id.layout_case_list_textview4, null);
                        }

                    }
                })
        );

        caseDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TAG.equals(PostedCasesFragment.TAG)) {
                    mainActivity.replaceFragment(R.id.fragment_case_button4, null);
                } else {
                    mainActivity.replaceFragment(R.id.layout_case_list_textview4, null);
                }
            }
        });

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
        @Bind(R.id.layout_case_list_imagebutton2) ImageButton like;
        @Bind(R.id.layout_case_list_imagebutton1) ImageButton saveCase;
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
