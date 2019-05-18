package com.example.sangeevan.dslr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText mailid;
    private EditText password;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener AuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        AuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null){
                    Intent intentlogin = new Intent(MainActivity.this,Home.class);
                    startActivity(intentlogin);
                    Toast.makeText(MainActivity.this, "LogIn Successful !!!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        mailid = (EditText)findViewById(R.id.editText2);
        password = (EditText)findViewById(R.id.editText3);
        firebaseAuth = FirebaseAuth.getInstance();

        Button btncreacc = (Button)findViewById(R.id.button2);
        btncreacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intcreacc = new Intent(MainActivity.this,CreateAccount.class);
                startActivity(intcreacc);
            }
        });

        Button btnlogin = (Button)findViewById(R.id.button);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    public void onStart(){
        super.onStart();
        firebaseAuth.addAuthStateListener(AuthListener);
    }
    public void onStop(){
        super.onStop();
        if (AuthListener != null) {
            firebaseAuth.removeAuthStateListener(AuthListener);

        }
    }

    public void login(){

        String strmailid = mailid.getText().toString().trim();
        String strpassword = password.getText().toString().trim();

        if(strmailid.isEmpty()) {
            mailid.setError("Mail ID is required");
            mailid.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(strmailid).matches()){
            mailid.setError("Enter correct Mail ID");
            mailid.requestFocus();
            return;
        }
        if(strpassword.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return;
        }

        final ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this,"Please Wait","Processing",true);
        (firebaseAuth.signInWithEmailAndPassword(mailid.getText().toString(),password.getText().toString())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "LogIn Successful !!!", Toast.LENGTH_SHORT).show();
                    Intent intlogin = new Intent(MainActivity.this,Home.class);
                    startActivity(intlogin);
                }
                else {
                    Log.e("Error",task.getException().toString());
                    Toast.makeText(MainActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
