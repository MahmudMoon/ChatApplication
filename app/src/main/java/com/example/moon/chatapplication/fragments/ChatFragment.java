package com.example.moon.chatapplication.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.moon.chatapplication.DBHelper.DBConstants;
import com.example.moon.chatapplication.DBHelper.FireBaseHelper;
import com.example.moon.chatapplication.DBHelper.SharedPref_constants;
import com.example.moon.chatapplication.R;
import com.example.moon.chatapplication.adapter.Message_adaptre_recycler_view;
import com.example.moon.chatapplication.adapter.Typeing_adapter;
import com.example.moon.chatapplication.models.Message;
import com.example.moon.chatapplication.models.Typing;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ChatFragment extends Fragment {

    RecyclerView recyclerView_msg;
    EditText et_msg_pad;
    ImageButton ibtn_send;
    ListView listView;
    public static final String TAG = "MyTag";
    String url;
    ArrayList<Typing> arrayList;
    ArrayList<Message> arrayList_of_message;
    SharedPreferences sharedPreferences;
    String current_user_name, current_user_image;
    Context context;
    LinearLayout typing_layout;
    RecyclerView typing_view;
    boolean typing = true;

    private OnFragmentInteractionListener mListener;

    public ChatFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayList = new ArrayList<>();
        arrayList_of_message = new ArrayList<>();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView_msg = (RecyclerView) view.findViewById(R.id.recycler_view_meg);
        ibtn_send = (ImageButton)view.findViewById(R.id.ibtnsend);
        et_msg_pad = (EditText)view.findViewById(R.id.etmsgpad);
        typing_layout = (LinearLayout)view.findViewById(R.id.typingrecycler);
        typing_view = (RecyclerView)view.findViewById(R.id.recyclertypeView);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getContext();


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView_msg.setLayoutManager(mLayoutManager);
        recyclerView_msg.setItemAnimator(new DefaultItemAnimator());


        sharedPreferences = getContext().getSharedPreferences(SharedPref_constants.Shared_pref_name,Context.MODE_PRIVATE);
        current_user_image = sharedPreferences.getString(SharedPref_constants.image_url,"");
        current_user_name = sharedPreferences.getString(SharedPref_constants.user_name,"");
        et_msg_pad.clearFocus();
        FireBaseHelper.getReference(DBConstants.Chat).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList_of_message.clear();
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    Message message = data.getValue(Message.class);
                    if(message.getSeen()==0&&!(message.getUid().equals(FireBaseHelper.getUID()))){
                        FireBaseHelper.getReference(DBConstants.Chat).child(data.getKey()).child(DBConstants.Seen).setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i(TAG, "onSuccess: "+"SEEN");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i(TAG, "onFailure: "+"Faile to seen"+e);
                            }
                        });
                    }
                    arrayList_of_message.add(message);
                    Message_adaptre_recycler_view  messge_adapter = new Message_adaptre_recycler_view(arrayList_of_message);
                    recyclerView_msg.setAdapter(messge_adapter);
                    recyclerView_msg.scrollToPosition(arrayList_of_message.size()-1);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        FireBaseHelper.getReference(DBConstants.Typing).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    Typing typing = snapshot.getValue(Typing.class);
                    if(typing.getTyping().length()>0 && !(typing.getUid().equals(FireBaseHelper.getUID())))
                       arrayList.add(typing);
                }



                if(arrayList.size()>0 && (getContext()!=null)) {
                    typing_layout.setVisibility(View.VISIBLE);
                    /////////////////////////
                    ////////
                    Typeing_adapter typeing_adapter = new Typeing_adapter(arrayList);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    typing_view.setLayoutManager(layoutManager);

                    typing_view.setAdapter(typeing_adapter);
                    typing_view.scrollToPosition(arrayList.size()-1);

                    ////////////////////////

                }else{
                    typing_layout.setVisibility(View.GONE);
                    if(getContext()==null){
                        Log.i(TAG, "onDataChange: "+"getContext is null");
                    }

                    if(arrayList.size()==0){
                        Log.i(TAG, "onDataChange: "+"typing is empty");
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if(et_msg_pad!=null)/////////////we have changed in here////////////////////////
          et_msg_pad.addTextChangedListener(new TextWatcher() {
              @Override
              public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                  //Log.i(TAG, "beforeTextChanged: " + "Char "+ s + start +"// " + count + "// " + after);
              }

              @Override
              public void onTextChanged(CharSequence s, int start, int before, int count) {
                  //Log.i(TAG, "onTextChanged: "+ "Char "+ s + start +"// " + before + "// " + count);
                  if(count>0 && typing){
                      //typing.............
                      typing = false;
                      Log.i(TAG, "onTextChanged: "+"typing");
                      Typing typing = new Typing(current_user_image,"typing.....",FireBaseHelper.getUID());
                      FireBaseHelper.getReference(DBConstants.Typing).child(FireBaseHelper.getUID()).setValue(typing).addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {
                              Log.i(TAG, "onComplete: "+"TYPINH");

                          }
                      }).addOnFailureListener(new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull Exception e) {
                              Log.i(TAG, "onFailure: "+"FAILED TYPINH" + e.toString());
                          }
                      });




                  }
              }

              @Override
              public void afterTextChanged(Editable s) {
                 // Log.i(TAG, "afterTextChanged: " + s.toString());
                  if(s.toString().length()==0){
                      Log.i(TAG, "afterTextChanged: "+"EDIT TEXT IS CLEAR , No typing");
                      typing = true;

                      Typing typing = new Typing("","","");
                      FireBaseHelper.getReference(DBConstants.Typing).child(FireBaseHelper.getUID()).setValue(typing).addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {
                              Log.i(TAG, "onComplete: "+"not TYPINH");

                          }
                      }).addOnFailureListener(new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull Exception e) {
                              Log.i(TAG, "onFailure: "+"FAILED not TYPINH" + e.toString());
                          }
                      });
                  }


              }
          });//////////////////////////work till here//////////////////

        if(ibtn_send!=null)
            ibtn_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "onClick: "+"MSG SEND");

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    String cuurent_date_time = dateFormat.format(date);
                    Message message = new Message(current_user_name,et_msg_pad.getText().toString(),current_user_image,cuurent_date_time,0,FireBaseHelper.getUID());
                    et_msg_pad.setText("");
                    FireBaseHelper.getReference(DBConstants.Chat).child(String.valueOf(System.currentTimeMillis())).
                            setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(),"Message sent",Toast.LENGTH_SHORT).show();
                            et_msg_pad.clearFocus();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),"Message not sent",Toast.LENGTH_SHORT).show();
                        }
                    });



                }
            });
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
