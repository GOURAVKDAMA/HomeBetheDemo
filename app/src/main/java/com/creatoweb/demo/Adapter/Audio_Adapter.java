package com.creatoweb.demo.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.creatoweb.demo.Activity.Audio_Player_Activity;
import com.creatoweb.demo.Activity.Local_Data;
import com.creatoweb.demo.Beans.Audio_List_bean;
import com.creatoweb.demo.R;
import java.util.List;

public class Audio_Adapter extends RecyclerView.Adapter<Audio_Adapter.Holds>
{
    List<Audio_List_bean> beanList;
    Activity activity;
    Typeface MontReg;
    Local_Data local_data;
    public Audio_Adapter(List<Audio_List_bean> beanList, Activity activity, Typeface MontReg)
    {
        this.beanList=beanList;
        this.activity=activity;
        this.MontReg=MontReg;
    }
    public class Holds extends RecyclerView.ViewHolder
    {

        TextView tv_name,tv_tags;
        ImageView iv_photo;
        RelativeLayout rl_onclick;
        public Holds(View views)
        {
            super(views);
            iv_photo = (ImageView) views.findViewById(R.id.iv_photo);
            tv_name = (TextView) views.findViewById(R.id.tv_name);
            tv_tags = (TextView) views.findViewById(R.id.tv_tags);
            rl_onclick = (RelativeLayout) views.findViewById(R.id.rl_onclick);
            local_data = new Local_Data(activity);
        }
    }
    @Override
    public Audio_Adapter.Holds onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_dynamic_lay,parent,false);
        Audio_Adapter.Holds hold=new Audio_Adapter.Holds(view);
        return  hold;
    }

    @Override
    public void onBindViewHolder(Audio_Adapter.Holds holder, int position)
    {
        System.out.println("here position "+position);
        final Audio_List_bean bean = beanList.get(position);

        if(bean.getName().trim().equalsIgnoreCase("null"))
        {
            holder.tv_name.setText("");
        }
        else
        {
            holder.tv_name.setText(bean.getName());
        }
        if(bean.getTags().trim().equalsIgnoreCase("null"))
        {
            holder.tv_tags.setText("");
        }
        else
        {
            holder.tv_tags.setText(bean.getTags());
        }
        Glide.with(activity).load(bean.getImage()).placeholder(R.drawable.no_image).crossFade().into(holder.iv_photo);
        holder.tv_name.setTypeface(MontReg);
        holder.tv_tags.setTypeface(MontReg);
        holder.rl_onclick.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                local_data.Insert_Recent(bean.getName(),bean.getUrl(),bean.getImage(),bean.getTags());
                Intent intent = new Intent(activity,Audio_Player_Activity.class);
                intent.putExtra("url",bean.getUrl());
                intent.putExtra("name",bean.getName());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return beanList.size();
    }
}

