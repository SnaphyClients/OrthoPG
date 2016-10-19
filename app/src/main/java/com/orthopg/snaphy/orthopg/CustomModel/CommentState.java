package com.orthopg.snaphy.orthopg.CustomModel;

import android.widget.ImageView;

import com.androidsdk.snaphy.snaphyandroidsdk.models.Comment;

/**
 * Created by snaphy on 18/10/16.
 */

public class CommentState {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public ImageView getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(ImageView isSelected) {
        this.isSelected = isSelected;
    }

    private Comment comment;
    private boolean state = false;
    ImageView isSelected;

    public CommentState(Comment comment){
        if(comment == null){
            return;
        }
        if(comment.getId() != null) {
            this.id = (String) comment.getId();
        }
        this.comment = comment;
    }
}


