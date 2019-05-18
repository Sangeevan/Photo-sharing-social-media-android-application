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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class myprofilepage extends Fragment implements com.example.sangeevan.dslr.MyImageAdapter.OnItemClickListner {

    private ProgressBar myprogress;


    private RecyclerView MyRecyclerView;
    private  MyImageAdapter MyImageAdapter;

    private DatabaseReference mydbf;
    private DatabaseReference mydbpost;
    private List<MyPost> myPosts;


    private FirebaseStorage mStorage;


    private ValueEventListener mDBListner;


    public TextView txtun;

    public TextView txte;

    public ImageView userimg;

    public String userfolder = FirebaseAuth.getInstance().getCurrentUser().getUid();


    public DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Users").child(userfolder).child("name");

    public DatabaseReference dbrefe = FirebaseDatabase.getInstance().getReference("Users").child(userfolder).child("mailid");

    public DatabaseReference dbrefimg = FirebaseDatabase.getInstance().getReference("Users").child(userfolder).child("imageurl");



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myprofilepage,container,false);


        myprogress = view.findViewById(R.id.progressprofile);

        MyRecyclerView = view.findViewById(R.id.recyclemyprofile);
        MyRecyclerView.setHasFixedSize(true);
        MyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        myPosts = new ArrayList<>();


        MyImageAdapter = new MyImageAdapter(getContext(),myPosts);

        MyRecyclerView.setAdapter(MyImageAdapter);

        MyImageAdapter.setOnItemClickListner(myprofilepage.this);



        mStorage = FirebaseStorage.getInstance();

        mydbf=FirebaseDatabase.getInstance().getReference("MyPosts").child(userfolder);

        mydbpost=FirebaseDatabase.getInstance().getReference("Posts");


        userimg = (ImageView) view.findViewById(R.id.mpi);

        txtun = (TextView)view.findViewById(R.id.mpun);

        txte = (TextView)view.findViewById(R.id.mpe);


        mDBListner = dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String usern = dataSnapshot.getValue(String.class);
                txtun.setText(usern);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dbrefe.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String usere = dataSnapshot.getValue(String.class);
                txte.setText(usere);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        dbrefimg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String useri = dataSnapshot.getValue(String.class);
                Picasso.with(getContext()).load(useri).placeholder(R.drawable.profile).fit().centerCrop().into(userimg);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        mydbf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                myPosts.clear();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    MyPost mypost = postSnapshot.getValue(MyPost.class);
                    mypost.setMkey(postSnapshot.getKey());
                    myPosts.add(mypost);
                }

                MyImageAdapter.notifyDataSetChanged();

                myprogress.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                myprogress.setVisibility(View.INVISIBLE);
            }
        });


        return view;
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {

        MyPost selectedItem = myPosts.get(position);
        final String selectedkey = selectedItem.getMkey();

        StorageReference imageref = mStorage.getReferenceFromUrl(selectedItem.getPlaceimageurl());
        imageref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mydbf.child(selectedkey).removeValue();

                mydbpost.child(selectedkey+userfolder).removeValue();

                Toast.makeText(getContext(), "Post Deleted !!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mydbf.removeEventListener(mDBListner);
    }
}
