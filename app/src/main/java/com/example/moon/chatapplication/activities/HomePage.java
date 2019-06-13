package com.example.moon.chatapplication.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.moon.chatapplication.DBHelper.DBConstants;
import com.example.moon.chatapplication.DBHelper.FireBaseHelper;
import com.example.moon.chatapplication.fragments.ActiveUsers;
import com.example.moon.chatapplication.fragments.ChatFragment;
import com.example.moon.chatapplication.R;
import com.example.moon.chatapplication.fragments.Profile;
import com.example.moon.chatapplication.models.Typing;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity implements Profile.OnFragmentInteractionListener,ChatFragment.OnFragmentInteractionListener,ActiveUsers.OnFragmentInteractionListener {
    private TextView mTextMessage;
    FrameLayout container_layout;
    public static final String TAG = "MyTag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        container_layout = (FrameLayout) findViewById(R.id.container_layout);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.hide();


        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ChatFragment chatFragment = new ChatFragment();
        loadFragment(chatFragment);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home: {
                    ChatFragment chatFragment = new ChatFragment();
                    loadFragment(chatFragment);
                    return true;
                }
                case R.id.navigation_dashboard: {
                    ActiveUsers activeUsers = new ActiveUsers();
                    loadFragment(activeUsers);
                    return true;
                }
                case R.id.navigation_notifications: {
                    Profile profile = new Profile();
                    loadFragment(profile);
                    return true;
                }
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
       getSupportFragmentManager().beginTransaction()
               .replace(R.id.container_layout, fragment)
               .addToBackStack(null)
               .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        final AlertDialog.Builder alertDialogbuilder =  new AlertDialog.Builder(this);
        alertDialogbuilder.setMessage("END CHAT . . . . ");
        alertDialogbuilder.setPositiveButton("Close . . . ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearTyping();
                finishAffinity();

            }
        });

        alertDialogbuilder.setNegativeButton("No ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        AlertDialog alertDialog = alertDialogbuilder.create();
        alertDialog.show();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void clearTyping(){
        Typing typing = new Typing("","","");
        FireBaseHelper.getReference(DBConstants.Typing).child(FireBaseHelper.getUID()).setValue(typing).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "onSuccess: "+"Successfully reset typing");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: "+"Fail reset typing" + e.toString());
            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);

    }
}