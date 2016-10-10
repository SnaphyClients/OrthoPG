package com.orthopg.snaphy.orthopg.ImageZoom;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.androidsdk.snaphy.snaphyandroidsdk.presenter.Presenter;
import com.orthopg.snaphy.orthopg.Constants;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import java.util.Map;

/**
 * Created by Ravi-Gupta on 6/22/2015.
 */
public class ImageZoomDialog extends DialogFragment {
    public static String TAG = "ImageZoomDialog";
    MainActivity mainActivity;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
        lp.copyFrom(window.getAttributes());
        //setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        //This makes the dialog take up the full width
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_image_zoom, container,false);
        //setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        mainActivity = (MainActivity) getActivity();
        final TouchImageView zoomImageview = (TouchImageView)view.findViewById(R.id.dialog_image_zoom_imageview1);
        Map<String, Object> drawableObj = Presenter.getInstance().getModel(Map.class, Constants.ZOOM_IMAGE_ID);
        if(drawableObj != null){
            mainActivity.snaphyHelper.loadUnsignedUrl(drawableObj, zoomImageview);
        }
      /*  BitmapFactory.Options options = new BitmapFactory.Options();
        options.inTempStorage = new byte[24*1024];
        options.inJustDecodeBounds = false;
        options.inSampleSize=8;
        Bitmap bitmap2 = BitmapFactory.decodeFile(image.getPath(), options);
        Bitmap bitmap = ThumbnailUtils.extractThumbnail(bitmap2, 400, 600);
        zoomImageview.setImageBitmap(bitmap);*/
        //Log.v("signin",d+"  Image  "+bitmap);
        return view;
    }
}
