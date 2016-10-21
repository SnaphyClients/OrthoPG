package com.orthopg.snaphy.orthopg.CustomModel;

import android.util.Log;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.DataListCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.ObjectCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.VoidCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.models.ImageModel;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Post;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by snaphy on 13/10/16.
 */

public class NewCase {
    //Stores the list of images in edit or upload..
    DataList<TrackImage> trackImages = new DataList<>();
    Post post;
    MainActivity mainActivity;
    DataList<ImageModel> deletedModels = new DataList<>();
    //Stores the saved images list..
    //DataList<Map<String, Object>> postImages = new DataList<>();
    public DataList<ImageModel> getDeletedModels() {
        return deletedModels;
    }

    public void setDeletedModels(DataList<ImageModel> deletedModels) {
        this.deletedModels = deletedModels;
    }

    //A callback to indicate when all data is saved..
    public void saveAllImages(final DataListCallback<TrackImage> callback){
        callback.onBefore();
        if(trackImages != null){
            if(trackImages.size() != 0){
                boolean done = true;
                //Start saving image..
                for(TrackImage trackImage : trackImages){
                    if(!trackImage.isDownloaded()){
                        done = false;
                        getConvertImageToHashMap(trackImage, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                //recursive...call to same function..
                                saveAllImages(callback);
                            }

                            @Override
                            public void onError(Throwable t) {
                                callback.onError(t);
                            }

                        });
                        break;
                    }
                }
                if(done){
                    callback.onSuccess(trackImages);
                    callback.onFinally();
                }
            }else{
                callback.onSuccess(trackImages);
                callback.onFinally();

            }
        }else{
            callback.onSuccess(trackImages);
            callback.onFinally();
        }


    }



    public void getConvertImageToHashMap(final TrackImage trackImage, final VoidCallback callback){
        callback.onBefore();
        if(trackImages != null){
            if(trackImage != null) {
                File file = trackImage.getFile();
                if(trackImage.isDownloaded()){
                    callback.onSuccess();
                    callback.onFinally();
                    return;
                }
                if (file != null) {
                    //Upload the images first..
                    mainActivity.snaphyHelper.uploadWithCallback(Constants.CONTAINER, file, new ObjectCallback<ImageModel>() {
                        @Override
                        public void onSuccess(ImageModel object) {
                            if (object != null) {
                                trackImage.setDownloaded(true);
                                trackImage.setImageModel(object);
                                callback.onSuccess();
                            } else {
                                Throwable throwable = new Throwable("Error: Uploaded Image is not valid");
                                callback.onError(throwable);
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            callback.onError(t);
                            Log.e(Constants.TAG, t.toString());
                        }

                        @Override
                        public void onFinally() {
                            callback.onFinally();
                        }
                    });
                } else {
                    Throwable throwable = new Throwable("Error: Image file is not valid");
                    //Removed this index item..
                    callback.onError(throwable);
                    callback.onFinally();
                }
            }else{
                Throwable throwable = new Throwable("Error: Image file is not valid");
                //Removed this index item..
                callback.onError(throwable);
                callback.onFinally();
            }
        }else{
            Throwable throwable = new Throwable("Error: Image file list is not valid");
            //Removed this index item..
            callback.onError(throwable);
            callback.onFinally();
        }
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



    public NewCase(MainActivity mainActivity, Post post){
        this.post = post;
        this.mainActivity = mainActivity;
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
