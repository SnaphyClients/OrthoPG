package com.orthopg.snaphy.orthopg.Fragment.BooksFragment;

import android.graphics.drawable.Drawable;

/**
 * Created by Ravi-Gupta on 9/28/2016.
 */
public class BooksModel {

    public BooksModel(Drawable bookImage, String bookName,String bookDescription, boolean isDownloadable) {
        this.bookImage = bookImage;
        this.bookName = bookName;
        this.bookDescription = bookDescription;
        this.isDownloadable = isDownloadable;
    }

    private Drawable bookImage;
    private String bookName;
    private String bookDescription;

    public Drawable getBookImage() {
        return bookImage;
    }

    public void setBookImage(Drawable bookImage) {
        this.bookImage = bookImage;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public boolean isDownloadable() {
        return isDownloadable;
    }

    public void setIsDownloadable(boolean isDownloadable) {
        this.isDownloadable = isDownloadable;
    }

    private boolean isDownloadable;
}
