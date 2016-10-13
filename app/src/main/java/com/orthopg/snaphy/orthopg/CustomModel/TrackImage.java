package com.orthopg.snaphy.orthopg.CustomModel;

import com.androidsdk.snaphy.snaphyandroidsdk.models.ImageModel;
import java.io.File;
import android.net.Uri;

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
