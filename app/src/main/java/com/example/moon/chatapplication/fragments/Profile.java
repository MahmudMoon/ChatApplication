package com.example.moon.chatapplication.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moon.chatapplication.DBHelper.DBConstants;
import com.example.moon.chatapplication.DBHelper.FireBaseHelper;
import com.example.moon.chatapplication.DBHelper.SharedPref_constants;
import com.example.moon.chatapplication.R;
import com.example.moon.chatapplication.activities.HomePage;
import com.example.moon.chatapplication.activities.LoginActivity;
import com.example.moon.chatapplication.models.Typing;
import com.example.moon.chatapplication.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class Profile extends Fragment {

    private OnFragmentInteractionListener mListener;

    TextView  tv_name;
    CircleImageView circleImageView;
    Button logout;
    SharedPreferences sharedPreferences;
    public static final String TAG = "MyTag";
    public Profile() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        tv_name  = (TextView)view.findViewById(R.id.user_name);
        circleImageView = (CircleImageView)view.findViewById(R.id.pro_image);
        logout = (Button)view.findViewById(R.id.logout);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedPreferences = getContext().getSharedPreferences(SharedPref_constants.Shared_pref_name,Context.MODE_PRIVATE);
        String user_name = sharedPreferences.getString(SharedPref_constants.user_name, "NAME NOT FOUND");
        String image_url = sharedPreferences.getString(SharedPref_constants.image_url,"");


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireBaseHelper.getReference(DBConstants.Users).child(FireBaseHelper.getUID()).child(DBConstants.islive).setValue(0);
                clearTyping();
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        tv_name.setText(user_name);
        if(image_url.length()>0)
        Picasso.get().load(image_url).into(circleImageView);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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

}
