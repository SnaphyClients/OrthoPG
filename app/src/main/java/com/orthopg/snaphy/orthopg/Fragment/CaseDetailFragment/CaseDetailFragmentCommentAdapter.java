package com.orthopg.snaphy.orthopg.Fragment.CaseDetailFragment;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Comment;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Post;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ravi-Gupta on 10/1/2016.
 */
public class CaseDetailFragmentCommentAdapter extends RecyclerView.Adapter<CaseDetailFragmentCommentAdapter.ViewHolder> {

    Post post;
    MainActivity mainActivity;
    HashMap<Object, Boolean> trackCommentSelected;

    public CaseDetailFragmentCommentAdapter(MainActivity mainActivity, Post post) {
        this.mainActivity = mainActivity;
        this.post = post;
        this.trackCommentSelected = new HashMap<>();
    }

    @Override
    public CaseDetailFragmentCommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View caseCommentView = inflater.inflate(R.layout.layout_comment, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(caseCommentView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CaseDetailFragmentCommentAdapter.ViewHolder holder, int position) {
        if(post == null){
            return;
        }
        DataList<Comment> commentDataList = post.getComments();
        if(commentDataList == null){
            return;
        }

        final Comment comment = commentDataList.get(position);


        final ImageView isSelected = holder.isSelected;
        TextView userName = holder.userName;
        final TextView answer = holder.answer;
        TextView editComment = holder.editComment;
        TextView deleteComment = holder.deleteComment;

        if(comment.getId() != null){
            trackCommentSelected.put(comment.getId(), false);
        }

        ///Set not solved tick mark if customer has posted the Post..
        Customer loginCustomer = Presenter.getInstance().getModel(Customer.class, Constants.LOGIN_CUSTOMER);
        if(loginCustomer != null){
            if(post.getCustomer() != null){
                if(post.getCustomer().getId() != null){
                    if(post.getCustomer().getId().toString().equals(loginCustomer.getId().toString())){
                        //Display the accept answer option..
                        isSelected.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.unselected));

                        //Also add the click listener..
                        isSelected.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boolean isAnswerStateSelectedPreviously = trackCommentSelected.get(comment.getId());

                                if(isAnswerStateSelectedPreviously){
                                    //TODO: remove accepted answer...
                                    //Display the accept answer option..
                                    isSelected.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.unselected));
                                }else{
                                    //TODO: add accepted answer...
                                    isSelected.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.selected));
                                }

                                //Now change the state.
                                trackCommentSelected.put(comment.getId(), !isAnswerStateSelectedPreviously);
                            }
                        });
                    }
                }
            }


            //Add edit button on comments..
            if(comment.getCustomer() != null){
                if(comment.getCustomer().getId().toString().equals(loginCustomer.getId().toString())){
                    //Displays button
                    editComment.setVisibility(View.VISIBLE);
                    //Display the edit comment button..
                    editComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO: HANDLE EDIT COMMENT LOGIC..
                            showCommentDialog(answer);
                        }
                    });

                    //show delete button..
                    deleteComment.setVisibility(View.VISIBLE);
                    deleteComment.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            //TODO: HANDLE DELETE COMMENT LOGIC..

                        }
                    });
                }else{
                    editComment.setVisibility(View.GONE);
                    deleteComment.setVisibility(View.GONE);
                }
            }else{
                editComment.setVisibility(View.GONE);
                deleteComment.setVisibility(View.GONE);
            }

        }else{
            editComment.setVisibility(View.GONE);
            deleteComment.setVisibility(View.GONE);
        }


        if(comment != null & mainActivity != null){
            if(comment.getCustomer() != null){
                String name = mainActivity.snaphyHelper.getName(comment.getCustomer().getFirstName(), comment.getCustomer().getLastName());
                if(!name.isEmpty()){
                    userName.setVisibility(View.VISIBLE);
                    name = Constants.Doctor + name.replace("^[Dd][Rr]", "");
                    userName.setText(name);
                }else{
                    userName.setVisibility(View.GONE);
                }
            }

            if(comment.getAnswer() != null){
                if(!comment.getAnswer().isEmpty()){
                    answer.setVisibility(View.VISIBLE);
                    answer.setText(comment.getAnswer().trim());
                }else{
                    answer.setVisibility(View.GONE);
                }
            }

        }

    }

    public void showCommentDialog(final TextView answer) {

        final Dialog dialog = new Dialog(mainActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_text);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Button okButton = (Button) dialog.findViewById(R.id.dialog_add_text_button1);
        final EditText editText = (EditText) dialog.findViewById(R.id.dialog_add_text_edittext1);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setText(answer.getText());

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer.setText(editText.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public int getItemCount() {
        if(post == null){
            return 0;
        }
        DataList<Comment> commentDataList = post.getComments();
        if(commentDataList == null){
            return 0;
        }
        return commentDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.layout_comment_imageview1) ImageView isSelected;
        @Bind(R.id.layout_comment_textview1) TextView userName;
        @Bind(R.id.layout_comment_textview2) TextView answer;
        @Bind(R.id.layout_comment_imagebutton1) TextView editComment;
        @Bind(R.id.layout_comment_imagebutton2) TextView deleteComment;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
