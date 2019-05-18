package com.example.sangeevan.dslr;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyImageAdapter extends RecyclerView.Adapter<MyImageAdapter.MyImageViewHolder> {

    private Context myContext;
    private List<MyPost> myPosts;

    private OnItemClickListner mListner;

    public MyImageAdapter (Context context , List<MyPost> posts){
        myContext = context;
        myPosts = posts;
    }

    @NonNull
    @Override
    public MyImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mv = LayoutInflater.from(myContext).inflate(R.layout.mypost,viewGroup,false);
        return new MyImageViewHolder(mv);
    }

    @Override
    public void onBindViewHolder(@NonNull MyImageViewHolder myImageViewHolder, int i) {

        MyPost uploadcurrent = myPosts.get(i);

        myImageViewHolder.placename.setText(uploadcurrent.getPlacename());
        Picasso.with(myContext)
                .load(uploadcurrent.getPlaceimageurl())
                .placeholder(R.drawable.bg)
                .fit()
                .centerCrop()
                .into(myImageViewHolder.placeimg);
        myImageViewHolder.placedes.setText(uploadcurrent.getPlacedescription());

    }

    @Override
    public int getItemCount() {
        return myPosts.size();
    }

    public class MyImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener,
            MenuItem.OnMenuItemClickListener{

        public TextView placename;
        public ImageView placeimg;
        public  TextView placedes;

        public MyImageViewHolder(@NonNull View itemView) {
            super(itemView);

            placename = itemView.findViewById(R.id.mycardplacenameid);
            placeimg = itemView.findViewById(R.id.mycardplaceimgid);
            placedes = itemView.findViewById(R.id.mycardplacedesid);

            itemView.setOnClickListener(this);

            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onClick(View v) {
            if(mListner != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    mListner.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem delete = menu.add(Menu.NONE,1,1,"Delete Post");
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            if(mListner != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    mListner.onDeleteClick(position);
                }
            }

            return false;
        }
    }

    public interface OnItemClickListner {
        void onItemClick(int position);

        void onDeleteClick(int position);

    }
    public void setOnItemClickListner(OnItemClickListner listner){
        mListner =listner;
    }

}
