package com.orthopg.snaphy.orthopg.CustomModel;

import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.ImageModel;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Post;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by snaphy on 13/10/16.
 */

public class NewCase {
    //Stores the list of images in edit or upload..
    DataList<TrackImage> trackImages = new DataList<>();
    Post post;
    DataList<ImageModel> deletedModels = new DataList<>();

    public DataList<ImageModel> getDeletedModels() {
        return deletedModels;
    }

    public void setDeletedModels(DataList<ImageModel> deletedModels) {
        this.deletedModels = deletedModels;
    }




    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public DataList<TrackImage> getTrackImages() {
        return trackImages;
    }



    public void setTrackImages(DataList<TrackImage> trackImages) {
        this.trackImages = trackImages;
    }



    public NewCase(Post post){
        this.post = post;
        //Load all the pre downloaded images..
        loadTrackImages();
    }

    private void loadTrackImages(){
        //clear the data..
        trackImages.clear();
        if(post != null){
            if(post.getPostImages() != null){
                if(post.getPostImages().size() != 0){
                    for(Map<String, Object> downloadImage : post.getPostImages()){
                        ImageModel imageModel = new ImageModel();
                        if(downloadImage.get("id") != null){
                            imageModel.setId((String)downloadImage.get("id"));
                        }

                        if(downloadImage.get("url") != null){
                            imageModel.setUrl((HashMap<String, Object>)downloadImage.get("url"));
                        }

                        if(downloadImage.get("name") != null){
                            imageModel.setName((String)downloadImage.get("name"));
                        }

                        if(downloadImage.get("container") != null){
                            imageModel.setContainer((String)downloadImage.get("container"));
                        }

                        //Now add image model to track image..
                        if(imageModel.getName() != null){
                            //Add model to trackImages list
                            TrackImage trackImage = new TrackImage();
                            trackImage.setImageModel(imageModel);
                            trackImage.setDownloaded(true);
                            //Now add to list..
                            trackImages.add(trackImage);
                        }
                    }
                }
            }
        }
    }
}
