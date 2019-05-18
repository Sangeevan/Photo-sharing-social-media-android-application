package com.example.sangeevan.dslr;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class CreateAccount extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Button btnimg;
    private ImageView preimg;
    private ProgressBar proimg;
    private Uri uriimg;


    private StorageReference strref;
    private DatabaseReference dbref;

    private EditText etname,etmailid,etpassword,etconfirmpassword;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        etname=(EditText)findViewById(R.id.editText);
        etmailid=(EditText)findViewById(R.id.editText4);
        etpassword=(EditText)findViewById(R.id.editText5);
        etconfirmpassword=(EditText)findViewById(R.id.editText6);

        firebaseAuth=FirebaseAuth.getInstance();

        strref = FirebaseStorage.getInstance().getReference("Users");


        Button btncre = (Button)findViewById(R.id.button3);

        btncre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createaccount();
            }
        });


        btnimg = (Button)findViewById(R.id.buttonimage);
        preimg = (ImageView)findViewById(R.id.imgpre);
        proimg = (ProgressBar)findViewById(R.id.progressimg);

        btnimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openfilechoser();
            }
        });

    }

    public String getfileextension(Uri uri){
        ContentResolver cR =getContentResolver();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            uriimg = data.getData();
            Picasso.with(this).load(uriimg).placeholder(R.drawable.profile).fit().centerCrop().into(preimg);
        }

    }

    private void createaccount(){

        final String name = etname.getText().toString().trim();
        final String mailid = etmailid.getText().toString().trim();

        String password = etpassword.getText().toString().trim();
        String confirmpassword = etconfirmpassword.getText().toString().trim();

        if(name.isEmpty()) {
            etname.setError("Name is required");
            etname.requestFocus();
            return;
        }
        if(uriimg == null){
            Toast.makeText(CreateAccount.this, "Profile Image Not Selected", Toast.LENGTH_SHORT).show();
            return;
        }
        if(mailid.isEmpty()) {
            etmailid.setError("Mail ID is required");
            etmailid.requestFocus();
            return;
        }
        if(password.isEmpty()) {
            etpassword.setError("Password is required");
            etpassword.requestFocus();
            return;
        }
        if(confirmpassword.isEmpty()) {
            etconfirmpassword.setError("Confirm Password is required");
            etconfirmpassword.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(mailid).matches()){
            etmailid.setError("Enter correct Mail ID");
            etmailid.requestFocus();
            return;
        }

        if(password.length()<6){
            etpassword.setError("Password is too short");
            etpassword.requestFocus();
            return;
        }

        if(password.equals(confirmpassword)){

            final ProgressDialog progressDialog = ProgressDialog.show(CreateAccount.this,"Account Creating","Processing",true);
            (firebaseAuth.createUserWithEmailAndPassword(mailid,password)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {


                    if(task.isSuccessful()){


                            final String name = etname.getText().toString().trim();
                            final String mailid = etmailid.getText().toString().trim();

                            if(uriimg != null){
                                StorageReference fileref = strref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"."+getfileextension(uriimg));
                                fileref.putFile(uriimg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                proimg.setProgress(0);
                                            }
                                        },500);



                                        User user = new User(name,mailid,taskSnapshot.getDownloadUrl().toString());
                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.dismiss();

                                                if(task.isSuccessful()){

                                                    Toast.makeText(CreateAccount.this, "Create Successful !!!\n   Please Log In !!!!! ", Toast.LENGTH_SHORT).show();
                                                    FirebaseAuth.getInstance().signOut();
                                                    finish();
                                                    Intent intacccre = new Intent(CreateAccount.this,MainActivity.class);
                                                    startActivity(intacccre);

                                                }
                                                else {
                                                    Log.e("Error",task.getException().toString());
                                                    Toast.makeText(CreateAccount.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                }

                                            }
                                        });


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(CreateAccount.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                        proimg.setProgress((int)progress);
                                    }
                                });
                            }else{
                                Toast.makeText(CreateAccount.this, "Profile Image Not Selected", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                    }
                    else {
                        Log.e("Error",task.getException().toString());
                        Toast.makeText(CreateAccount.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                }
            });

        }
        else {
            etconfirmpassword.setError("Password not matches");
            etconfirmpassword.requestFocus();
            return;
        }
    }

}
