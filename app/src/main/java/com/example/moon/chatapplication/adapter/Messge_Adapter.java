package com.example.moon.chatapplication.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.moon.chatapplication.DBHelper.FireBaseHelper;
import com.example.moon.chatapplication.R;
import com.example.moon.chatapplication.models.Message;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Messge_Adapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Message> messageArrayList;
    CircleImageView circleImageView;
    TextView tv_time;
    TextView tv_msg;
    TextView tv_seen;

    public Messge_Adapter(Context context, ArrayList<Message> messageArrayList) {
        this.context = context;
        this.messageArrayList = messageArrayList;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return messageArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.message_adapter_layout,null);
        tv_msg = (TextView)view.findViewById(R.id.tvmsg);
        tv_time = (TextView)view.findViewById(R.id.tvtime);
        tv_seen = (TextView)view.findViewById(R.id.tvseen);
        circleImageView = (CircleImageView)view.findViewById(R.id.ivsender);

        if(messageArrayList.get(position).getUid().equals( FireBaseHelper.getUID())){
            //i am sender
            tv_msg.setGravity(Gravity.RIGHT);
            tv_msg.setText(messageArrayList.get(position).getUser_msg());
            tv_time.setGravity(Gravity.RIGHT);
            tv_time.setText(messageArrayList.get(position).getTime());
            if(messageArrayList.get(position).getSeen()!=0){
                tv_seen.setText("seen");
            }
        }else{
            //i am receiver
            tv_time.setText(messageArrayList.get(position).getTime());
            tv_msg.setText(messageArrayList.get(position).getUser_msg());
            Picasso.get().load(messageArrayList.get(position).getUser_photo_url()).into(circleImageView);

        }

        return view;
    }
}
