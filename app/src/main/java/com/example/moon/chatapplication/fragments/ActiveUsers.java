package com.example.moon.chatapplication.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.moon.chatapplication.DBHelper.DBConstants;
import com.example.moon.chatapplication.DBHelper.FireBaseHelper;
import com.example.moon.chatapplication.R;
import com.example.moon.chatapplication.adapter.Active_user_adapter;
import com.example.moon.chatapplication.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ActiveUsers extends Fragment {


    private OnFragmentInteractionListener mListener;
    ListView listviewActiveUsers;
    public static final String TAG = "MyTag";
    ArrayList<User> arrayList ;

    public ActiveUsers() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayList = new ArrayList<>();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FireBaseHelper.getReference(DBConstants.Users).orderByChild(DBConstants.islive).equalTo(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "onDataChange: "+dataSnapshot.getChildrenCount());
                String uid_value = FireBaseHelper.getUID();
                arrayList.clear();
                for (DataSnapshot data:dataSnapshot.getChildren()) {
                    if(!TextUtils.equals(data.getKey(),uid_value)){
                        User user_info = data.getValue(User.class);
                        arrayList.add(user_info);
                    }
                }
                Active_user_adapter active_user_adapter = new Active_user_adapter(getContext(),arrayList);
                listviewActiveUsers.setAdapter(active_user_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(TAG, "onCancelled: "+databaseError.toString());
            }
        });





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_active_users, container, false);
        listviewActiveUsers = (ListView)view.findViewById(R.id.list_view_active_users);


        return view;



    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
}
