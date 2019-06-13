package com.example.moon.chatapplication.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.moon.chatapplication.DBHelper.FireBaseHelper;
import com.example.moon.chatapplication.R;
import com.example.moon.chatapplication.models.Message;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Message_adaptre_recycler_view extends RecyclerView.Adapter<Message_adaptre_recycler_view.MyViewHolder> {

    ArrayList<Message> messageArrayList;


    public Message_adaptre_recycler_view(ArrayList<Message> messageArrayList) {
        this.messageArrayList = messageArrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView tv_time;
        TextView tv_msg;
        TextView tv_seen;
        RelativeLayout relativeLayout;

        public MyViewHolder(View view) {
            super(view);
            tv_msg = (TextView)view.findViewById(R.id.tvmsg);
            tv_time = (TextView)view.findViewById(R.id.tvtime);
            tv_seen = (TextView)view.findViewById(R.id.tvseen);
            circleImageView = (CircleImageView)view.findViewById(R.id.ivsender);
            relativeLayout = (RelativeLayout)view.findViewById(R.id.relative_layout_message_container);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_adapter_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Message message = messageArrayList.get(position);

        if(messageArrayList.get(position).getUid().equals( FireBaseHelper.getUID())){
            //i am sender
            holder.relativeLayout.setBackgroundColor(Color.TRANSPARENT);
            holder.tv_msg.setGravity(Gravity.RIGHT);
            holder.tv_msg.setText(message.getUser_msg());
            holder.tv_time.setGravity(Gravity.RIGHT);
            holder.tv_time.setText(message.getTime());
            if(message.getSeen()!=0){
                holder.tv_seen.setText("seen");
            }
        }else{
            //i am receiver
            holder.relativeLayout.setBackgroundColor(Color.GREEN);
            holder.tv_time.setText(message.getTime());
            holder.tv_msg.setText(message.getUser_msg());
            Picasso.get().load(message.getUser_photo_url()).into(holder.circleImageView);

        }


    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }


}
