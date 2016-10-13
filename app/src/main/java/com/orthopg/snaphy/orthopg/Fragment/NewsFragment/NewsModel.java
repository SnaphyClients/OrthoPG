package com.orthopg.snaphy.orthopg.Fragment.NewsFragment;

import android.graphics.drawable.Drawable;

/**
 * Created by Ravi-Gupta on 9/27/2016.
 */
public class NewsModel {

    public NewsModel(Drawable newsImage, String type, String newsHeading, String newsDescription) {
        this.newsImage = newsImage;
        this.newsHeading = newsHeading;
        this.newsDescription = newsDescription;
        this.type = type;
    }

    public Drawable getNewsImage() {
        return newsImage;
    }

    public void setNewsImage(Drawable newsImage) {
        this.newsImage = newsImage;
    }

    public String getNewsHeading() {
        return newsHeading;
    }

    public void setNewsHeading(String newsHeading) {
        this.newsHeading = newsHeading;
    }

    public String getNewsDescription() {
        return newsDescription;
    }

    public void setNewsDescription(String newsDescription) {
        this.newsDescription = newsDescription;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private Drawable newsImage;
    private String newsHeading;
    private String newsDescription;
    private String type;
}
