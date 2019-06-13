package com.example.moon.chatapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.moon.chatapplication.R;
import com.example.moon.chatapplication.models.User;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Active_user_adapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    Context context;
    ArrayList<User> arrayList;
    TextView tvname;
    CircleImageView circleImageView_user;

    public Active_user_adapter(Context context, ArrayList<User> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.active_user_adapter_layout     ,null);
        tvname = (TextView)view.findViewById(R.id.active_users_name);
        circleImageView_user = (CircleImageView)view.findViewById(R.id.active_user_pro_pic);

        tvname.setText(arrayList.get(position).getName());
        Picasso.get().load(arrayList.get(position).getPhotourl()).into(circleImageView_user);

        return view;
    }
}
