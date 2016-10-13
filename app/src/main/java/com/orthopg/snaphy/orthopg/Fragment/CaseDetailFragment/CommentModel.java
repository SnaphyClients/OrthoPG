package com.orthopg.snaphy.orthopg.Fragment.CaseDetailFragment;

/**
 * Created by Ravi-Gupta on 10/1/2016.
 */
public class CommentModel {

    private boolean isSelected;
    private String name;

    public CommentModel(boolean isSelected, String name, String answer) {
        this.isSelected = isSelected;
        this.name = name;
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String answer;
}
