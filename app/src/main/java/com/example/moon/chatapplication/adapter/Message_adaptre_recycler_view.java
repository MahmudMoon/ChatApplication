package com.example.moon.chatapplication.adapter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.moon.chatapplication.DBHelper.FireBaseHelper;
import com.example.moon.chatapplication.R;
import com.example.moon.chatapplication.models.Message;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class Message_adaptre_recycler_view extends RecyclerView.Adapter<Message_adaptre_recycler_view.MyViewHolder> {

    ArrayList<Message> messageArrayList;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    public static final String TAG = "MyTag";
    //ImageLoader imageLoader;


    public Message_adaptre_recycler_view(ArrayList<Message> messageArrayList) {
        this.messageArrayList = messageArrayList;
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true).build();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView tv_time;
        TextView tv_msg;
        TextView tv_seen;

        RelativeLayout relativeLayout;
        ProgressBar progressBar;


        public MyViewHolder(View view) {
            super(view);
            tv_msg = (TextView)view.findViewById(R.id.tvmsg);
            tv_time = (TextView)view.findViewById(R.id.tvtime);
            tv_seen = (TextView)view.findViewById(R.id.tvseen);
            circleImageView = (CircleImageView)view.findViewById(R.id.ivsender);
            relativeLayout = (RelativeLayout)view.findViewById(R.id.relative_layout_message_container);
            progressBar = (ProgressBar)view.findViewById(R.id.progressBar2);
            imageLoader = ImageLoader.getInstance();
            options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true).build();

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_adapter_layout, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Message message = messageArrayList.get(position);

        if(messageArrayList.get(position).getUid().equals( FireBaseHelper.getUID())){
            //i am sender
            holder.relativeLayout.setBackgroundColor(Color.TRANSPARENT);
            holder.tv_msg.setGravity(Gravity.END);
            holder.tv_msg.setText(message.getUser_msg());
            holder.tv_time.setGravity(Gravity.END);
            holder.tv_time.setText(message.getTime());
            holder.circleImageView.setVisibility(View.INVISIBLE);
            if(message.getSeen()!=0){
                holder.tv_seen.setText("seen");
            }
        }else{
            //i am receiver
            holder.relativeLayout.setBackgroundColor(Color.GREEN);
            holder.tv_msg.setGravity(Gravity.START);
            holder.tv_msg.setText(message.getUser_msg());
            holder.tv_time.setGravity(Gravity.START);
            holder.tv_time.setText(message.getTime());
            holder.tv_seen.setText("");
           // imageLoader = ImageLoader.getInstance();
           // imageLoader.displayImage(message.getUser_photo_url(),holder.circleImageView);
            //Picasso.get().load(message.getUser_photo_url()).into(holder.circleImageView);
            imageLoader.loadImage(messageArrayList.get(position).getUser_photo_url(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    holder.progressBar.setVisibility(View.VISIBLE);
                    holder.progressBar.setIndeterminate(true);
                    Log.i(TAG, "onLoadingStarted: ");
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    holder.progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    holder.progressBar.setVisibility(View.INVISIBLE);
                    holder.circleImageView.setImageBitmap(loadedImage);
                    Log.i(TAG, "onLoadingComplete: ");
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    holder.progressBar.setVisibility(View.INVISIBLE);
                }
            });

           // imageLoader.displayImage(messageArrayList.get(position).getUser_photo_url(), holder.circleImageView, options);



        }


    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }


}
