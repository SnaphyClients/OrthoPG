package com.orthopg.snaphy.orthopg.CustomModel;

import android.net.Uri;
import android.util.Log;

import com.androidsdk.snaphy.snaphyandroidsdk.models.ImageModel;
import com.orthopg.snaphy.orthopg.Constants;

import java.io.File;

/**
 * Created by snaphy on 13/10/16.
 */

public class TrackImage {
    private ImageModel imageModel;
    private boolean downloaded = false;
    private File file;
    private Uri uri;

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
        //Add the file..
        file = new File(uri.getPath());
        Log.v(Constants.TAG, file+"");
    }

    public File getFile() {
        return file;
    }
    //Disable the external file set method
    /*public void setFile(File file) {
        this.file = file;
    }
*/
    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public ImageModel getImageModel() {
        return imageModel;
    }

    public void setImageModel(ImageModel imageModel) {
        this.imageModel = imageModel;
    }


}
