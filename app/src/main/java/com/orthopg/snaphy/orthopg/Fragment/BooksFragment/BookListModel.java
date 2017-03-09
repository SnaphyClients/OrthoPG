package com.orthopg.snaphy.orthopg.Fragment.BooksFragment;

import android.graphics.drawable.Drawable;

/**
 * Created by nikita on 8/3/17.
 */

public class BookListModel {

    private String name;
    private Drawable drawable;

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public BookListModel(String name, Drawable drawable){

        this.name = name;
        this.drawable = drawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
