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

import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ravi-Gupta on 10/1/2016.
 */
public class CaseDetailFragmentCommentAdapter extends RecyclerView.Adapter<CaseDetailFragmentCommentAdapter.ViewHolder> {

    List<CommentModel> commentModelList;
    MainActivity mainActivity;

    public CaseDetailFragmentCommentAdapter(MainActivity mainActivity, List<CommentModel> commentModelList) {
        this.mainActivity = mainActivity;
        this.commentModelList = commentModelList;
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
        CommentModel commentModel = commentModelList.get(position);

        ImageView isSelected = holder.isSelected;
        TextView userName = holder.userName;
        final TextView answer = holder.answer;
        ImageButton editComment = holder.editComment;

        if(commentModel.isSelected()) {
            isSelected.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.selected));
        } else {
            isSelected.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.unselected));
        }

        userName.setText(commentModel.getName());
        answer.setText(commentModel.getAnswer());

        isSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        editComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentDialog(answer);
            }
        });
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
        return commentModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.layout_comment_imageview1) ImageView isSelected;
        @Bind(R.id.layout_comment_textview1) TextView userName;
        @Bind(R.id.layout_comment_textview2) TextView answer;
        @Bind(R.id.layout_comment_imagebutton1) ImageButton editComment;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
