package com.example.sangeevan.dslr;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mtoggle;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);







        mDrawerLayout = (DrawerLayout) findViewById(R.id.homepage);
        mtoggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new homepage()).commit();
            navigationView.setCheckedItem(R.id.home);
        }


    }



    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mtoggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public boolean onNavigationItemSelected(MenuItem item) {


        int id = item.getItemId();

        if (id == R.id.profile) {

            getSupportFragmentManager().beginTransaction().replace(R.id.container,new myprofilepage()).commit();

        } else if (id == R.id.home) {

            getSupportFragmentManager().beginTransaction().replace(R.id.container,new homepage()).commit();

        } else if (id == R.id.post) {

            getSupportFragmentManager().beginTransaction().replace(R.id.container,new addpostpage()).commit();

        } else if (id == R.id.logout) {

            FirebaseAuth.getInstance().signOut();
            finish();

            Toast.makeText(Home.this, "Logged Out", Toast.LENGTH_SHORT).show();


            Intent intlogout = new Intent(Home.this,MainActivity.class);
            startActivity(intlogout);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.homepage);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
