package com.orthopg.snaphy.orthopg.Fragment.CaseFragment;

import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * Created by Ravi-Gupta on 9/29/2016.
 */
public class CaseModel {

    private Drawable doctorImage;
    private String postHeading;
    private String doctorName;
    private String postTime;
    private boolean isLiked;
    private boolean isSaved;
    private List<Drawable> caseImage;
    private String caseDescription;
    private String tag;
    private boolean isAnswerSelected;
    private String selectedAnswerUserName;
    private String selectedAnswer;

    public CaseModel(Drawable doctorImage, String postHeading, String doctorName, String postTime,boolean isSaved, boolean isLiked,
                     List<Drawable> caseImage, String caseDescription, String tag, boolean isAnswerSelected,
                     String selectedAnswerUserName, String selectedAnswer) {
        this.doctorImage = doctorImage;
        this.postHeading = postHeading;
        this.doctorName = doctorName;
        this.postTime = postTime;
        this.isLiked = isLiked;
        this.isSaved = isSaved;
        this.caseImage = caseImage;
        this.caseDescription = caseDescription;
        this.tag = tag;
        this.isAnswerSelected = isAnswerSelected;
        this.selectedAnswerUserName = selectedAnswerUserName;
        this.selectedAnswer = selectedAnswer;

    }

    public Drawable getDoctorImage() {
        return doctorImage;
    }

    public void setDoctorImage(Drawable doctorImage) {
        this.doctorImage = doctorImage;
    }

    public String getPostHeading() {
        return postHeading;
    }

    public void setPostHeading(String postHeading) {
        this.postHeading = postHeading;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setIsLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setIsSaved(boolean isSaved) {
        this.isSaved = isSaved;
    }

    public List<Drawable> getCaseImage() {
        return caseImage;
    }

    public void setCaseImage(List<Drawable> caseImage) {
        this.caseImage = caseImage;
    }

    public String getCaseDescription() {
        return caseDescription;
    }

    public void setCaseDescription(String caseDescription) {
        this.caseDescription = caseDescription;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean getIsAnswerSelected() {
        return isAnswerSelected;
    }

    public void setIsAnswerSelected(boolean isAnswerSelected) {
        this.isAnswerSelected = isAnswerSelected;
    }

    public String getSelectedAnswerUserName() {
        return selectedAnswerUserName;
    }

    public void setSelectedAnswerUserName(String selectedAnswerUserName) {
        this.selectedAnswerUserName = selectedAnswerUserName;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }






}
