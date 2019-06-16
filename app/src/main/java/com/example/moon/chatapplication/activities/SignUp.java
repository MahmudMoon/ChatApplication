package com.example.moon.chatapplication.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.moon.chatapplication.DBHelper.DBConstants;
import com.example.moon.chatapplication.DBHelper.FireBaseHelper;
import com.example.moon.chatapplication.R;
import com.example.moon.chatapplication.models.User;
import com.example.moon.chatapplication.utils.PathUtils;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import in.mayanknagwanshi.imagepicker.ImageSelectActivity;

public class SignUp extends AppCompatActivity {

    EditText et_user_name,et_user_email,et_user_pass,et_user_pass_retype;
    Button btn_signup;
    FirebaseAuth firebaseAuth;
    CircleImageView circleImageView;
    public static final int IMAGE_FILE_REQUST_CODE = 101;
    public boolean FLAG_IMAGE_SET = false;
    Uri uri;
    StorageReference ref;
    public static final String TAG = "MyTag";
    File compressor;
    File file;
    Uri final_path;
    ProgressBar progressBar_round;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init_view();
        init_variables();
        init_functions();
        init_listeners();
    }

    private void init_view() {
        et_user_email = (EditText)findViewById(R.id.etuseremail);
        et_user_name = (EditText)findViewById(R.id.etusername);
        et_user_pass = (EditText)findViewById(R.id.etuserpass);
        et_user_pass_retype = (EditText)findViewById(R.id.etuserpassretype);
        btn_signup = (Button)findViewById(R.id.email_sign_in_button);
        circleImageView = (CircleImageView)findViewById(R.id.profile_image);
        progressBar_round = (ProgressBar)findViewById(R.id.progress_circular_signup);

    }

    private void init_variables() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void init_functions() {

    }

    private void init_listeners() {
        circleImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FLAG_IMAGE_SET = false;
                Intent intent = new Intent(SignUp.this, ImageSelectActivity.class);
                intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, false);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
                startActivityForResult(intent, IMAGE_FILE_REQUST_CODE);

            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!TextUtils.isEmpty(et_user_name.getText().toString())
                        && !TextUtils.isEmpty(et_user_email.getText().toString())
                        && !TextUtils.isEmpty(et_user_pass.getText().toString())
                        && !TextUtils.isEmpty(et_user_pass_retype.getText().toString())
                ){
                    progressBar_round.setVisibility(View.VISIBLE);
                    progressBar_round.setIndeterminate(true);
                    if(TextUtils.equals(et_user_pass.getText().toString(),et_user_pass_retype.getText().toString())){
                        if(FLAG_IMAGE_SET) {
                            FLAG_IMAGE_SET = false;
                            firebaseAuth.createUserWithEmailAndPassword(et_user_email.getText().toString(), et_user_pass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    ref = FirebaseStorage.getInstance().getReference().child(FireBaseHelper.getUID() + ".jpg");
                                    if (final_path != null){
                                        UploadTask uploadTask = ref.putFile(final_path);
                                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                        @Override
                                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                            if (!task.isSuccessful()) {
                                                throw task.getException();
                                            }

                                            // Continue with the task to get the download URL
                                            return ref.getDownloadUrl();
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                Uri downloadUri = task.getResult();
                                                Log.i(TAG, "onComplete: " + downloadUri.toString());
                                                User user = new User(et_user_name.getText().toString(), downloadUri.toString(), 0);
                                                FireBaseHelper.getReference(DBConstants.Users).child(FireBaseHelper.getUID()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.i(TAG, "onSuccess: " + "Successfully Registered a new User");
                                                        Toast.makeText(getApplicationContext(), "Successfully Registered a new User", Toast.LENGTH_SHORT).show();
                                                        progressBar_round.setVisibility(View.INVISIBLE);

                                                        ////////////////MAKE INTENT TO GO TO LOGIN PAGE/////////////////////////////////////////
                                                        Intent intent = new Intent(SignUp.this, LoginActivity.class);
                                                        startActivity(intent);

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        progressBar_round.setVisibility(View.INVISIBLE);
                                                        Log.i(TAG, "onFailure: " + "Failed to Registered a new User" + e.toString());
                                                        Toast.makeText(getApplicationContext(), "Failed to Registered a new User", Toast.LENGTH_LONG).show();
                                                    }
                                                });


                                            } else {
                                                progressBar_round.setVisibility(View.INVISIBLE);
                                                Log.i(TAG, "onComplete: " + "FAILED TO GENERATE DOWNLOAD LINK");
                                            }
                                        }
                                    });

                                }else {
                                        progressBar_round.setVisibility(View.INVISIBLE);
                                        Log.i(TAG, "onSuccess: " + "Final path is null");
                                    }


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar_round.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), "Invalid email or password or poor internet Connection", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            progressBar_round.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(),"SELECT AN IMAGE",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        et_user_pass_retype.setText("");
                        et_user_pass_retype.setError("pass didn't match");
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_FILE_REQUST_CODE && resultCode == Activity.RESULT_OK) {
            String filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
            try {
                file = new File(filePath);
            } catch (Exception e) {
                Log.i(TAG, "onActivityResult: " + e.toString());
            }

            if (file.exists()) {
                Log.i(TAG, "onActivityResult: " + "FILE EXISTS");

                try {
                    compressor = new Compressor(this)
                            .setMaxHeight(100)
                            .setMaxWidth(100)
                            .setQuality(30)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .compressToFile(file);
                    final_path = Uri.fromFile(compressor);
                    FLAG_IMAGE_SET = true;

                } catch (IOException e) {
                    Log.i(TAG, "onActivityResult: " + e.toString());
                    e.printStackTrace();
                }

                Log.i(TAG, "onActivityResult: "+"FINAL PATH" + final_path);

                Log.i(TAG, "onActivityResult: "+filePath);
                Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                circleImageView.setImageBitmap(selectedImage);
            }else{
                Log.i(TAG, "onActivityResult: "+"File doesn't exist");
            }
        }

    }

}
