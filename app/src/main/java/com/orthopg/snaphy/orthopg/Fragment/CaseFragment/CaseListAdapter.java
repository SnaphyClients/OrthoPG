package com.orthopg.snaphy.orthopg.Fragment.CaseFragment;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Post;
import com.androidsdk.snaphy.snaphyandroidsdk.models.PostDetail;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.Fragment.PostedCasesFragment.PostedCasesFragment;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;
import com.sdsmdg.tastytoast.TastyToast;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ravi-Gupta on 9/21/2016.
 */
public class CaseListAdapter extends RecyclerView.Adapter<CaseListAdapter.ViewHolder> {

    MainActivity mainActivity;
    DataList<PostDetail> postDetailDataList;
    String TAG;

    public CaseListAdapter(MainActivity mainActivity,  DataList<PostDetail> postDetailDataList, String TAG) {
        this.mainActivity = mainActivity;
        this.postDetailDataList = postDetailDataList;
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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final PostDetail postDetail = postDetailDataList.get(position);
        Post post;
        if(postDetail != null){
            if(postDetail.getPost() != null){
                post = postDetail.getPost();
            }else{
                return;
            }
        }else{
            return;
        }
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
        ImageButton delete = holder.deleteButton;
        ImageButton edit = holder.editButton;
        ImageView isAnswerSelected = holder.isAnswerSelected;
        TextView selectedAnswerUserName = holder.selectedAnswerUserName;
        TextView selectedAnswer = holder.selectedAnswer;
        TextView numberOfLike = holder.numberOfLikes;
        TextView numberOfSave = holder.numberOfSave;
        LinearLayout linearLayout = holder.linearLayout;

        caseImages.setLayoutManager(new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false));


        if(!post.getHeading().isEmpty()){
            caseHeading.setText(post.getHeading());
        }


        //Now load case image list..
        if(post.getPostImages() != null ){
            if(post.getPostImages().size() == 0){
                caseImages.setVisibility(View.GONE);
            }else{
                caseImages.setVisibility(View.VISIBLE);
                caseImageAdapter = new CaseImageAdapter(mainActivity, post.getPostImages());
                caseImages.setAdapter(caseImageAdapter);
            }
        }else{
            caseImages.setVisibility(View.GONE);
        }

        if(post.getCustomer() != null) {
            //TODO ADD ANONYMOUS USER LATER..
            String name = mainActivity.snaphyHelper.getName(post.getCustomer().getFirstName(), post.getCustomer().getLastName());
            if(!name.isEmpty()){
                name = Constants.Doctor + name;
            }
            userName.setText(name);

            if(post.getCustomer().getProfilePic() != null){
                mainActivity.snaphyHelper.loadUnSignedThumbnailImage(post.getCustomer().getProfilePic(), imageView, R.mipmap.anonymous);
            }else{
                //Set deault image..
                //TODO CHANGE BACKGROUND COLOR TO BLUE..
                imageView.setImageResource(R.mipmap.anonymous);
            }
        }


        setTime(casePostedTime, postDetail.getAdded());

        if(!post.getDescription().isEmpty()) {
            caseDescription.setVisibility(View.VISIBLE);
            caseDescription.setText(post.getDescription());
        }else{
            caseDescription.setVisibility(View.GONE);
        }

        if(postDetail != null) {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(!postDetail.getType().isEmpty()) {
                tag.setText(postDetail.getType());
                if(postDetail.getType().equals(Constants.CASE)){
                    tag.setTextColor(Color.parseColor(Constants.PRIMARY));
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        tag.setBackgroundDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.curved_rectangle));
                    } else {
                        tag.setBackground(ContextCompat.getDrawable(mainActivity, R.drawable.curved_rectangle));
                    }

                } else if(postDetail.getType().equals(Constants.BOOK_REVIEW)) {
                    tag.setTextColor(Color.parseColor(Constants.WARNING));
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        tag.setBackgroundDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.curved_rectangle_warning));
                    } else {
                        tag.setBackground(ContextCompat.getDrawable(mainActivity, R.drawable.curved_rectangle_warning));
                    }
                } else if(postDetail.getType().equals(Constants.INTERVIEW)) {
                    tag.setTextColor(Color.parseColor(Constants.SUCCESS));
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        tag.setBackgroundDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.curved_rectangle_success));
                    } else {
                        tag.setBackground(ContextCompat.getDrawable(mainActivity, R.drawable.curved_rectangle_success));
                    }
                }

            }
        }

       /* if(postDetail.getAcceptedAnswer() != null) {
            // Add Selected Answer
            if(!postDetail.getAcceptedAnswer().getAnswer().isEmpty()){
                showSelectedAnswer(selectedAnswer, isAnswerSelected,selectedAnswerUserName);
                selectedAnswer.setText(postDetail.getAcceptedAnswer().getAnswer());
                if(postDetail.getAcceptedAnswer().getCustomer() != null){
                    String name= mainActivity.snaphyHelper.getName(postDetail.getAcceptedAnswer().getCustomer().getFirstName(), postDetail.getAcceptedAnswer().getCustomer().getLastName());
                    if(!name.isEmpty()){
                        name = Constants.Doctor + name.replace("^[Dd][Rr]", "");
                    }
                    selectedAnswerUserName.setText(name);
                }
            }else{
                hideSelectedAnswer(selectedAnswer, isAnswerSelected,selectedAnswerUserName);
            }
        }else{
            hideSelectedAnswer(selectedAnswer, isAnswerSelected,selectedAnswerUserName);
        }*/

        //TOTAL LIKE
        numberOfLike.setText(String.valueOf((int)postDetail.getTotalLike()));
        //TOTAL SAVE..
        numberOfSave.setText(String.valueOf((int)postDetail.getTotalSave()));


        if(TAG.equals(PostedCasesFragment.TAG)) {
            delete.setVisibility(View.VISIBLE);
            edit.setVisibility(View.VISIBLE);
        } else {
            delete.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
        }



        //TODO DESIGN IT AFTER LOGIN..
        /*if(post.getCustomer() != null) {
            if(post.getCustomer().getLikePosts() != null) {
                if(post.getCustomer().getLikePosts().contains(post.getId())) {
                    like.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.like_selected));
                } else {
                    like.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.like_unselected));
                }
            }
        }

        if(post.getCustomer() != null) {
            if(post.getCustomer().getSavePosts() != null) {
                if(post.getCustomer().getSavePosts().contains(post.getId())) {
                    saveCase.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.save_selected));
                } else {
                    saveCase.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.save_unselected));
                }
            }
        }*/


        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO DESIGN IT AFTER LOGIN..
                /*if(caseModel.isLiked()) {
                    like.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.like_unselected));
                    caseModel.setIsLiked(false);
                } else {
                    like.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.like_selected));
                    caseModel.setIsLiked(true);
                }*/
            }
        });

        saveCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO DESIGN IT AFTER LOGIN..
                /*if (caseModel.isSaved()) {
                    saveCase.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.save_unselected));
                    caseModel.setIsSaved(false);
                } else {
                    saveCase.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.save_selected));
                    caseModel.setIsSaved(true);
                }*/
            }
        });

       /* caseImages.addOnItemTouchListener(
                new RecyclerItemClickListener(mainActivity, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position_) {
                        if (TAG.equals(PostedCasesFragment.TAG)) {
                            mainActivity.replaceFragment(R.id.fragment_case_button4, null);
                        } else {
                            mainActivity.replaceFragment(R.id.layout_case_list_textview4, position);
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
                    mainActivity.replaceFragment(R.id.layout_case_list_textview4, position);
                }
            }
        });*/

        final Customer customer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
        final String MCINumber = customer.getMciNumber() != null ? customer.getMciNumber() : "";
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TAG.equals(PostedCasesFragment.TAG)) {
                    if (!MCINumber.isEmpty()) {
                        mainActivity.replaceFragment(R.id.fragment_case_button4, null);
                    } else {
                        mainActivity.replaceFragment(R.id.layout_case_list_textview4, position);
                    }
                } else {
                    //Show Toast
                    TastyToast.makeText(mainActivity.getApplicationContext(), "Verification is under process", TastyToast.LENGTH_LONG, TastyToast.CONFUSING);

                }
            }
        });

    }


    public void hideSelectedAnswer(TextView selectedAnswer, ImageView grenTick, TextView userName){
        selectedAnswer.setVisibility(View.GONE);
        grenTick.setVisibility(View.GONE);
        userName.setVisibility(View.GONE);
    }
    public void showSelectedAnswer(TextView selectedAnswer, ImageView grenTick, TextView userName){
        selectedAnswer.setVisibility(View.VISIBLE);
        grenTick.setVisibility(View.VISIBLE);
        userName.setVisibility(View.VISIBLE);
    }



    public void setTime(TextView casePostedTime, String date){
        //TODO DEFINE TIME HERE..
        //PARSE The javascript format date first..
        //casePostedTime.setText(caseModel.getPostTime());
    }




    @Override
    public int getItemCount() {
        return postDetailDataList.size();
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
        @Bind(R.id.layout_case_list_button1) ImageButton deleteButton;
        @Bind(R.id.layout_case_list_button2) ImageButton editButton;
        @Bind(R.id.layout_case_list_textview8) TextView numberOfLikes;
        @Bind(R.id.layout_case_list_textview9) TextView numberOfSave;
        @Bind(R.id.layout_case_list_linear_layout)
        LinearLayout linearLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
