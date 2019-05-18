package com.example.sangeevan.dslr;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class homepage extends Fragment {

    private ProgressBar homeprogress;


    private RecyclerView mRecyclerView;
    private  ImageAdapter mImageAdapter;

    private DatabaseReference mdbf;
    private List<Post> mPosts;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homepage,container,false);


        homeprogress = view.findViewById(R.id.progresshome);


            mRecyclerView = view.findViewById(R.id.recyclehome);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            mPosts = new ArrayList<>();

              mImageAdapter = new ImageAdapter(getContext(),mPosts);

              mRecyclerView.setAdapter(mImageAdapter);


        mdbf=FirebaseDatabase.getInstance().getReference("Posts");

            mdbf.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    mPosts.clear();

                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        Post post = postSnapshot.getValue(Post.class);
                        mPosts.add(post);
                    }

                    mImageAdapter.notifyDataSetChanged();

                    homeprogress.setVisibility(View.INVISIBLE);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        return view;

    }
}
