package com.example.moon.chatapplication.DBHelper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireBaseHelper {
   public static String getUID(){
       return FirebaseAuth.getInstance().getUid();
    }

    public static DatabaseReference getReference(String reference){
       return FirebaseDatabase.getInstance().getReference().child(reference);
    }
}
