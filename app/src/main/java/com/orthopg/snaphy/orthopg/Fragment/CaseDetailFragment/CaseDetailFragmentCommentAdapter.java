package com.orthopg.snaphy.orthopg.Fragment.CaseDetailFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Comment;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Post;
import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.CustomModel.CommentState;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ravi-Gupta on 10/1/2016.
 */
public class CaseDetailFragmentCommentAdapter extends RecyclerView.Adapter<CaseDetailFragmentCommentAdapter.ViewHolder> {

    Post post;
    MainActivity mainActivity;
    CaseDetailPresenter caseDetailPresenter;
    HashMap<String, CommentState> commentStateDataList;


    public CaseDetailFragmentCommentAdapter(MainActivity mainActivity, Post post , CaseDetailPresenter caseDetailPresenter, HashMap<String, CommentState> commentStateDataList) {
        this.mainActivity = mainActivity;
        this.post = post;
        this.caseDetailPresenter = caseDetailPresenter;
        this.commentStateDataList = commentStateDataList;
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
        final CommentState commentState;
        if(commentStateDataList.get((String)comment.getId()) == null){
             commentState = new CommentState(comment);
             commentStateDataList.put((String)comment.getId(), commentState);
        }else{
            commentState = commentStateDataList.get((String)comment.getId());
        }

        final ImageView isSelected = holder.isSelected;
        TextView userName = holder.userName;
        final TextView answer = holder.answer;
        makeTextViewResizable(answer, 2, "View More", true);
        TextView editComment = holder.editComment;
        TextView deleteComment = holder.deleteComment;
        final LinearLayout linearLayout = holder.linearLayout;

        //Add isSelected tab.
        commentState.setIsSelected(isSelected);

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
                            @SuppressLint("NewApi")
                            @Override
                            public void onClick(View v) {
                                if(commentState.isState()){
                                    //Remove accepted answer...
                                    //Display the accept answer option..
                                    isSelected.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.unselected));
                                    final int sdk = android.os.Build.VERSION.SDK_INT;
                                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                        linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.default_curved_rectangle));
                                    } else {
                                        linearLayout.setBackground(ContextCompat.getDrawable(mainActivity, R.drawable.default_curved_rectangle));
                                    }
                                    //Remove the answer..
                                    caseDetailPresenter.acceptAnswer((String)post.getId(), (String) comment.getId(), false);
                                    //Reload the case presenter after the ..load..
                                }else{
                                    //ACCEPT ANSWER
                                    isSelected.setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.selected));
                                    final int sdk = android.os.Build.VERSION.SDK_INT;
                                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                        linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.like_save_curved_rectangle));
                                    } else {
                                        linearLayout.setBackground(ContextCompat.getDrawable(mainActivity, R.drawable.like_save_curved_rectangle));
                                    }
                                    caseDetailPresenter.acceptAnswer((String)post.getId(), (String) comment.getId(), true);
                                }

                                //Now change the state.
                                commentState.setState(!commentState.isState());
                                removeOtherAcceptedAnswerState(commentState.getComment());
                            }
                        });
                    }else{
                        isSelected.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.GONE);
                    }
                }else{
                    isSelected.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);
                }
            }else{
                isSelected.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
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
                            showCommentDialog(comment);
                        }
                    });

                    //show delete button..
                    deleteComment.setVisibility(View.VISIBLE);
                    deleteComment.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            //ASK FOR DIALOG..
                            //Remove the comment from list..
                            new AlertDialog.Builder(mainActivity)
                                    .setMessage("Delete this comment?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                            post.getComments().remove(comment);
                                            dialog.cancel();
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                            dialog.cancel();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();



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




    public void showCommentDialog(Comment comment) {

        if(post != null){
            //Prepare the data..
            Presenter.getInstance().addModel(Constants.EDIT_IN_PROCESS_COMMENT_POST_MODEL, post);
        }

        Presenter.getInstance().addModel(Constants.EDIT_IN_PROCESS_COMMENT_MODEL, comment);
        mainActivity.replaceFragment(R.id.fragment_case_detail_button4, null);
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


    public void removeOtherAcceptedAnswerState(Comment exceptComment){
        if(exceptComment != null && commentStateDataList != null){
            for(String key: commentStateDataList.keySet()){
                //Change except comment...
                if(!key.toString().equals((String)exceptComment.getId())){
                    //Now set all state to false..
                    CommentState commentState = commentStateDataList.get(key);
                    commentState.setState(false);
                    //Replace the highlighted tick mark with default tick..
                    commentState.getIsSelected().setImageDrawable(mainActivity.getResources().getDrawable(R.mipmap.unselected));
                }
            }
        }
    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {

                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "View Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, "View More", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.layout_comment_imageview1) ImageView isSelected;
        @Bind(R.id.layout_comment_textview1) TextView userName;
        @Bind(R.id.layout_comment_textview2) TextView answer;
        @Bind(R.id.layout_comment_imagebutton1) TextView editComment;
        @Bind(R.id.layout_comment_imagebutton2) TextView deleteComment;
        @Bind(R.id.layout_comment_linear_layout_1)
        LinearLayout linearLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
