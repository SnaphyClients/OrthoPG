package com.orthopg.snaphy.orthopg.Fragment.NewCase;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.androidsdk.snaphy.snaphyandroidsdk.list.DataList;
import com.orthopg.snaphy.orthopg.CustomModel.TrackImage;
import com.orthopg.snaphy.orthopg.MainActivity;
import com.orthopg.snaphy.orthopg.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ravi-Gupta on 9/30/2016.
 */
public class CaseUploadImageFragmentAdapter extends RecyclerView.Adapter<CaseUploadImageFragmentAdapter.ViewHolder>{

    //List<Uri> imageURI;
    DataList<TrackImage> trackImages;
    MainActivity mainActivity;

    public CaseUploadImageFragmentAdapter(MainActivity mainActivity, DataList<TrackImage> trackImages) {
        this.trackImages = trackImages;
        this.mainActivity = mainActivity;
    }

    @Override
    public CaseUploadImageFragmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View caseImageView = inflater.inflate(R.layout.layout_upload_image_recycler_view, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(caseImageView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CaseUploadImageFragmentAdapter.ViewHolder holder, final int position) {

        TrackImage trackImage = trackImages.get(position);

        ImageView imageView = holder.imageView;
        ImageButton delete = holder.delete;

        if(trackImage.isDownloaded()){
            //Load using url..
            if(trackImage.getImageModel() != null){
                //Load image..
                mainActivity.snaphyHelper.loadUnsignedUrl(trackImage.getImageModel().getHashMap(), imageView);
            }
        }else{
            //Load from local file..
            if(trackImage.getUri() != null){
                imageView.setImageURI(trackImage.getUri());
            }
        }
        //On delete just remove the image..
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Just remove the trackImage from list..
                trackImages.remove(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return trackImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.layout_upload_image_recycler_view_imageview1) ImageView imageView;
        @Bind(R.id.layout_upload_image_recycler_view_button1)
        ImageButton delete;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
