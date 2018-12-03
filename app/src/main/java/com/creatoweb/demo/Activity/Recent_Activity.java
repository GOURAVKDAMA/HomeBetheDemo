package com.creatoweb.demo.Activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.creatoweb.demo.Adapter.Audio_Adapter;
import com.creatoweb.demo.Beans.Audio_List_bean;
import com.creatoweb.demo.R;

import java.util.ArrayList;
import java.util.List;

public class Recent_Activity extends AppCompatActivity
{
    Local_Data local_data;
    RecyclerView rcv_recent_list;
    List<Audio_List_bean> audio_list=new ArrayList<>();
    Typeface MontReg;
    String name,url,image,tags;
    Audio_List_bean bean;
    ImageView iv_cross;
    TextView tv_recent,tv_total;
    int total;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_);

        local_data=new Local_Data(getApplicationContext());
        LinearLayoutManager layoutManager__ = new LinearLayoutManager(this);
        tv_total=(TextView) findViewById(R.id.tv_total);
        tv_recent=(TextView) findViewById(R.id.tv_recent);
        iv_cross=(ImageView) findViewById(R.id.iv_cross);
        rcv_recent_list=(RecyclerView)findViewById(R.id.rcv_recent_list);
        rcv_recent_list.setLayoutManager(layoutManager__);
        rcv_recent_list.setItemAnimator(new DefaultItemAnimator());
        fetch_local();
        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void fetch_local()
    {
        SQLiteDatabase db=local_data.getReadableDatabase();
        final Cursor c = db.rawQuery("select * from recent_table",null);
        if(c.moveToFirst())
        {
            name=c.getString(0);
            url=c.getString(1);
            image=c.getString(2);
            tags=c.getString(3);
            total = c.getCount();
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    bean = new Audio_List_bean();
                    bean.setImage(image);
                    bean.setName(name);
                    bean.setUrl(url);
                    bean.setTags(tags);
                    audio_list.add(bean);
                }
            });
            while (c.moveToNext())
            {
                name=c.getString(0);
                url=c.getString(1);
                image=c.getString(2);
                tags=c.getString(3);
                total = c.getCount();
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        bean = new Audio_List_bean();
                        bean.setImage(image);
                        bean.setName(name);
                        bean.setUrl(url);
                        bean.setTags(tags);
                        audio_list.add(bean);
                    }
                });
            }
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    MontReg = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Regular.otf");
                    Audio_Adapter audio_adapter=new Audio_Adapter(audio_list,Recent_Activity.this,MontReg);
                    rcv_recent_list.setAdapter(audio_adapter);
                    tv_recent.setTypeface(MontReg);
                    tv_total.setTypeface(MontReg);
                    tv_total.setText("Total : "+total);
                }
            });
        }
    }
}
