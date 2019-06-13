package com.example.moon.chatapplication.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moon.chatapplication.R;
import com.example.moon.chatapplication.models.Typing;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Typeing_adapter extends RecyclerView.Adapter<Typeing_adapter.MyViewHolder> {
    public ArrayList<Typing> arrayList;

    public Typeing_adapter(ArrayList<Typing> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.typing_interface, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Typing typing_info = arrayList.get(position);
        Picasso.get().load(typing_info.getPhotourl()).into(holder.circleImageView_typing);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView circleImageView_typing;
        public MyViewHolder(View itemView) {
            super(itemView);
            circleImageView_typing = (CircleImageView)itemView.findViewById(R.id.iv_user);
        }
    }

}
