package com.example.moon.chatapplication.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moon.chatapplication.DBHelper.DBConstants;
import com.example.moon.chatapplication.DBHelper.FireBaseHelper;
import com.example.moon.chatapplication.DBHelper.SharedPref_constants;
import com.example.moon.chatapplication.R;
import com.example.moon.chatapplication.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class LoginActivity extends AppCompatActivity {


    TextInputEditText et_email,et_pass;
    Button btn_login;
    TextView tv_signUp;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init_view();
        init_variables();
        init_functions();
        init_listeners();
    }

    private void init_listeners() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(et_email.getText().toString()) && !TextUtils.isEmpty(et_pass.getText().toString())){
                    firebaseAuth.signInWithEmailAndPassword(et_email.getText().toString(),et_pass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FireBaseHelper.getReference(DBConstants.Users).child(FireBaseHelper.getUID()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    User current_user = dataSnapshot.getValue(User.class);
                                    String name = current_user.getName();
                                    String image_url = current_user.getPhotourl();
                                    SharedPreferences sharedPreferences = getSharedPreferences(SharedPref_constants.Shared_pref_name,MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(SharedPref_constants.user_name,name);
                                    editor.putString(SharedPref_constants.image_url,image_url);
                                    boolean commit = editor.commit();
                                    if(commit){
                                        FireBaseHelper.getReference(DBConstants.Users).child(FireBaseHelper.getUID()).child(DBConstants.islive).setValue(1);
                                        Intent intent = new Intent(LoginActivity.this,HomePage.class);
                                        startActivity(intent);
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });




                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"User not found or poor internet Connection",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    if(TextUtils.isEmpty(et_email.getText().toString())){
                       et_email.setError("Enter a valid Email");
                    }else
                        et_pass.setError("Enter a valid password");
                }

            }
        });

        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // go for registration..........................
                if((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                    ||(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)) {
                    ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},109);
                }else {
                    Intent intent = new Intent(LoginActivity.this, SignUp.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==109 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(LoginActivity.this, SignUp.class);
            startActivity(intent);
        }
    }

    private void init_functions() {
       currentUser = firebaseAuth.getCurrentUser();
       if(currentUser!=null){
           FireBaseHelper.getReference(DBConstants.Users).child(FireBaseHelper.getUID()).child(DBConstants.islive).setValue(1);
           Intent intent = new Intent(LoginActivity.this,HomePage.class);
           startActivity(intent);
       }

    }

    private void init_variables() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void init_view() {
        et_email = (TextInputEditText)findViewById(R.id.etemail);
        et_pass = (TextInputEditText)findViewById(R.id.etpass);
        tv_signUp = (TextView)findViewById(R.id.sign_up);
        btn_login = (Button)findViewById(R.id.btnlogin);
    }
}
