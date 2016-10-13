package com.orthopg.snaphy.orthopg.CustomModel;

import android.util.Log;

import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.DataListCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.callbacks.ObjectCallback;
import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.androidsdk.snaphy.snaphyandroidsdk.list.Listen;
import com.androidsdk.snaphy.snaphyandroidsdk.models.ImageModel;
import com.androidsdk.snaphy.snaphyandroidsdk.models.Post;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    DataList<Map<String, Object>> postImages = new DataList<>();

    public DataList<ImageModel> getDeletedModels() {
        return deletedModels;
    }

    public void setDeletedModels(DataList<ImageModel> deletedModels) {
        this.deletedModels = deletedModels;
    }

    //A callback to indicate when all data is saved..
    public void saveAllImages(/*inal DataListCallback<Map<String, Object>> callback*/){
        //callback.onBefore();
        if(trackImages != null){
            if(trackImages.size() != 0){
                postImages.subscribe(this, new Listen<Map<String, Object>>() {
                    @Override
                    public void onInit(DataList<Map<String, Object>> dataList) {
                        //Start saving image..

                    }

                    @Override
                    public void onChange(DataList<Map<String, Object>> dataList) {
                        //check if the data..
                        boolean done = true;
                        for(TrackImage trackImage : trackImages){
                            if(!trackImage.isDownloaded()){
                                done = false;
                            }
                        }

                        if(done){
                            /*callback.onSuccess(postImages);
                            callback.onFinally();*/
                        }
                    }

                    @Override
                    public void onClear() {
                        super.onClear();
                    }

                    @Override
                    public void onRemove(Map<String, Object> element, DataList<Map<String, Object>> dataList) {
                        super.onRemove(element, dataList);
                    }
                });
            }else{
                /*DataList<Map<String, Object>> success = new DataList<>();
                callback.onSuccess(success);
                callback.onFinally();*/

            }
        }else{
            /*DataList<Map<String, Object>> success = new DataList<>();
            callback.onSuccess(success);
            callback.onFinally();*/
        }
        getConvertImageToHashMap();

    }

    public void getConvertImageToHashMap(){
        final List<Integer> removedIndex = new ArrayList<>();
        if(trackImages != null){
            int i = 0;
            for(final TrackImage trackImage : trackImages){
                if(trackImage != null){
                    if(trackImage.isDownloaded()){
                        if(trackImage.getImageModel() != null){
                            postImages.add(trackImage.getImageModel().getHashMap());
                        }else{
                            //Removed this index item..
                            removedIndex.add(i);
                        }
                    }else{
                        File file = trackImage.getFile();
                        if(file != null){
                            //Upload the images first..
                            mainActivity.snaphyHelper.uploadWithCallback(Constants.CONTAINER, file, new ObjectCallback<ImageModel>() {
                                @Override
                                public void onBefore() {
                                    super.onBefore();
                                }

                                @Override
                                public void onSuccess(ImageModel object) {
                                    if(object != null){
                                        trackImage.setDownloaded(true);
                                        trackImage.setImageModel(object);
                                        postImages.add(object.getHashMap());
                                    }
                                    //TODO LAter check the else cond and remove the index here too..
                                }
                                @Override
                                public void onError(Throwable t) {
                                    super.onError(t);
                                    Log.e(Constants.TAG, t.toString());
                                }

                                @Override
                                public void onFinally() {
                                    super.onFinally();
                                }
                            });
                        }else{
                            //Removed this index item..
                            removedIndex.add(i);
                        }

                    }
                }else{
                    //Removed this index item..
                    removedIndex.add(i);
                }

                i++;
            }


            for(int index: removedIndex){
                //Removning wrong uploaded data images..if present..
                trackImages.remove(index);
            }


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
