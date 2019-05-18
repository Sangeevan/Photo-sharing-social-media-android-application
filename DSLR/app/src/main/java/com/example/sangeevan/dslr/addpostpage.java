package com.example.sangeevan.dslr;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;
import static com.example.sangeevan.dslr.R.id.postimage;
import static java.lang.Long.valueOf;


public class addpostpage extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView postimgpre;
    private Uri uripostimg;
    private ProgressBar propostimg;

    private StorageReference strref;


    public String unp,pip;

    public String userfolder = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public DatabaseReference dbrefpn = FirebaseDatabase.getInstance().getReference("Users").child(userfolder).child("name");

    public DatabaseReference dbrefpimg = FirebaseDatabase.getInstance().getReference("Users").child(userfolder).child("imageurl");

    EditText place,description;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addpostpage,container,false);

        postimgpre = (ImageView) view.findViewById(R.id.postimage);

        strref = FirebaseStorage.getInstance().getReference("Posts");

        postimgpre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openfilechoser();
            }
        });

        propostimg = (ProgressBar)view.findViewById(R.id.progresspostimg);



        place=(EditText)view.findViewById(R.id.etplace);
        description=(EditText)view.findViewById(R.id.etdes);

        Button post = (Button)view.findViewById(R.id.btnpost);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posting();
            }
        });



        dbrefpn.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String usern = dataSnapshot.getValue(String.class);
                unp=usern;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dbrefpimg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String useri = dataSnapshot.getValue(String.class);
                pip=useri;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return view;
    }



    public void posting(){

        final String placestr = place.getText().toString().trim();
        final String descriptionstr = description.getText().toString().trim();

        if(placestr.isEmpty()) {
            place.setError("Name of the place is required");
            place.requestFocus();
            return;
        }
        if(descriptionstr.isEmpty()) {
            description.setError("Description about the place is required");
            description.requestFocus();
            return;
        }
        if(uripostimg == null){
            Toast.makeText(getContext(), "              No Image Selected\nPlease select an image of the place", Toast.LENGTH_SHORT).show();
            return;
        }



        final ProgressDialog progressDialog = ProgressDialog.show(getContext(),"Uploading Post","Processing",true);

        if(uripostimg != null){
            StorageReference fileref = strref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(System.currentTimeMillis()+"."+getfileextension(uripostimg));
            fileref.putFile(uripostimg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            propostimg.setProgress(0);
                        }
                    },500);



                    Post post = new Post(unp,pip,placestr,descriptionstr,taskSnapshot.getDownloadUrl().toString());

                    MyPost mypost = new MyPost(placestr,descriptionstr,taskSnapshot.getDownloadUrl().toString());

                    String postname = createname();

                    FirebaseDatabase.getInstance().getReference("MyPosts")
                            .child(userfolder).child(postname)
                            .setValue(mypost);

                    FirebaseDatabase.getInstance().getReference("Posts")
                            .child(postname+userfolder)
                            .setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();

                            if(task.isSuccessful()){

                                Toast.makeText(getContext(), "Posting Successful !!!", Toast.LENGTH_SHORT).show();

                                Intent intpost = new Intent(getContext(),Home.class);
                                startActivity(intpost);

                            }
                            else {
                                Log.e("Error",task.getException().toString());
                                Toast.makeText(getContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    propostimg.setProgress((int)progress);
                }
            });
        }else{
            Toast.makeText(getContext(), "              No Image Selected\nPlease select an image of the place", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }


    }



    public String createname(){

        String time = String.valueOf(System.currentTimeMillis());
        String contime="";
        String DBstrver;

        for(int counter=0;counter<time.length();counter+=1) {

            int timesingle =time.charAt(counter);

            int timesub = 57-timesingle;

            String contimesin = String.valueOf(timesub);

            contime = contime+contimesin;

        }

        DBstrver = contime;

        return DBstrver;

    }



    public String getfileextension(Uri uri){
        ContentResolver cR =getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void openfilechoser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            uripostimg = data.getData();
            Picasso.with(getContext()).load(uripostimg).placeholder(R.drawable.bg).fit().centerCrop().into(postimgpre);
        }

    }


}
