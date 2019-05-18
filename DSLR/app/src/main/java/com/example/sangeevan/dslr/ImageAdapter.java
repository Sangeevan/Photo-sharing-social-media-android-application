package com.example.sangeevan.dslr;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Post> mPosts;

    public ImageAdapter (Context context , List<Post> posts){
        mContext = context;
        mPosts = posts;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.homepost, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        Post uploadcurrent = mPosts.get(i);
        imageViewHolder.username.setText(uploadcurrent.getProfilename());
        Picasso.with(mContext)
                .load(uploadcurrent.getProfileimageurl())
                .placeholder(R.drawable.profile)
                .fit()
                .centerCrop()
                .into(imageViewHolder.profileimg);
        imageViewHolder.placename.setText(uploadcurrent.getPlacename());
        Picasso.with(mContext)
                .load(uploadcurrent.getPlaceimageurl())
                .placeholder(R.drawable.bg)
                .fit()
                .centerCrop()
                .into(imageViewHolder.placeimg);
        imageViewHolder.placedes.setText(uploadcurrent.getPlacedescription());
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        public ImageView profileimg;
        public TextView username;
        public TextView placename;
        public ImageView placeimg;
        public  TextView placedes;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            profileimg = itemView.findViewById(R.id.cardprofileimgid);
            username = itemView.findViewById(R.id.cardprofilenameid);
            placename = itemView.findViewById(R.id.cardplacenameid);
            placeimg = itemView.findViewById(R.id.cardplaceimgid);
            placedes = itemView.findViewById(R.id.cardplacedesid);

        }
    }

}
