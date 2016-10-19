package com.orthopg.snaphy.orthopg.CustomModel;

import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Post;
import com.androidsdk.snaphy.snaphyandroidsdk.models.PostDetail;

/**
 * Created by snaphy on 14/10/16.
 */

public class TrackList {
    private String listType;
    private Double skip = 0.0;
    private int limit = 5;
    private DataList<PostDetail> postDetails;
    private DataList<Post> postDataList;

    public TrackList(String listType){
        this.listType = listType;
        this.postDetails = new DataList<>();
        this.postDataList = new DataList<>();
    }


    public int getLimit() {
        return limit;
    }


    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public Double getSkip() {
        return skip;
    }

    public void setSkip(Double skip) {
        this.skip = skip;
    }

    public DataList<PostDetail> getPostDetails() {
        return postDetails;
    }

    public void setPostDetails(DataList<PostDetail> postDetails) {
        this.postDetails = postDetails;
    }

    public DataList<Post> getPostDataList() {
        return postDataList;
    }

    public void setPostDataList(DataList<Post> postDataList) {
        this.postDataList = postDataList;
    }

    public void reset(){
        postDetails.clear();
        postDataList.clear();
        skip = 0.0;
    }

    public void incrementSkip(double newAdded){
        skip = skip + newAdded;
    }

}
