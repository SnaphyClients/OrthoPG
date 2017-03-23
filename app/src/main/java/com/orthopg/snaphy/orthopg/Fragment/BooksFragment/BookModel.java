package com.orthopg.snaphy.orthopg.Fragment.BooksFragment;

import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Book;
import com.androidsdk.snaphy.snaphyandroidsdk.models.BookCategory;

import java.util.List;

/**
 * Created by nikita on 8/3/17.
 */

public class BookModel {

   /* private String bookType;
    private BookCategory bookCategory;

    public BookModel(String bookType, BookCategory bookCategory){

        this.bookType = bookType;
        this.bookCategory = bookCategory;
    }

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    public BookCategory getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(BookCategory bookCategory) {
        this.bookCategory = bookCategory;
    }*/
     private String bookCategory;
    private List<BookListModel> bookListModelList;
    private String bookType;

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    public BookModel(String bookCategory, List<BookListModel> bookListModelList, String bookType){

        this.bookCategory = bookCategory;
        this.bookListModelList = bookListModelList;
        this.bookType = bookType;
    }

    public String getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }

    public List<BookListModel> getBookListModelList() {
        return bookListModelList;
    }

    public void setBookListModelList(List<BookListModel> bookListModelList) {
        this.bookListModelList = bookListModelList;
    }
}
