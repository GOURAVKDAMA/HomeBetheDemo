package com.creatoweb.demo.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.creatoweb.demo.Adapter.Audio_Adapter;
import com.creatoweb.demo.Beans.Audio_List_bean;
import com.creatoweb.demo.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class List_Activity extends AppCompatActivity
{
    RecyclerView rcv_audio_list;
    List<Audio_List_bean> audio_list=new ArrayList<>();
    ProgressBar pb_audio_list;
    Typeface MontReg;
    TextView tv_app_name,tv_total;
    ImageView iv_recent;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_);

        LinearLayoutManager layoutManager__ = new LinearLayoutManager(this);
        iv_recent=(ImageView)findViewById(R.id.iv_recent);
        tv_total=(TextView)findViewById(R.id.tv_total);
        tv_app_name=(TextView)findViewById(R.id.tv_app_name);
        pb_audio_list=(ProgressBar)findViewById(R.id.pb_audio_list);
        rcv_audio_list=(RecyclerView)findViewById(R.id.rcv_audio_list);
        rcv_audio_list.setLayoutManager(layoutManager__);
        rcv_audio_list.setItemAnimator(new DefaultItemAnimator());
        new Audio_List_API(List_Activity.this, "").execute();
        MontReg = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Regular.otf");
        tv_app_name.setTypeface(MontReg);
        tv_total.setTypeface(MontReg);
        iv_recent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(List_Activity.this,Recent_Activity.class);
                startActivity(intent);
            }
        });
    }

    public class Audio_List_API extends AsyncTask<Void, Void, String>
    {
        String sInput;
        Context context;

        Audio_List_API(Context context, String sInput)
        {
            this.context = context;
            this.sInput = sInput;
            System.out.println("sInput="+sInput);
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params)
        {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;

            try
            {
                URL url = new URL("http://www.radio-browser.info/webservice/json/stations/bycountry/india");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(300000);
                urlConnection.setRequestMethod("GET");

                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("token", "1234567890");
                urlConnection.setRequestProperty("Accept", "application/json");

                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);

                DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
                os.writeBytes(sInput.toString());

                os.flush();
                os.close();

                int responseCode = urlConnection.getResponseCode();
                iStream = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
                StringBuffer sb = new StringBuffer();

                String line = "";
                while ((line = br.readLine()) != null)
                {
                    sb.append(line);
                }

                data = sb.toString();
                System.out.println("responseCode_FL="+data);
                try
                {
                    final JSONArray jsonArray = new JSONArray(data);
                    for(int a=0;a<jsonArray.length();a++)
                    {
                        JSONObject object = jsonArray.getJSONObject(a);
                        String id=object.getString("id");
                        String name=object.getString("name");
                        String url_=object.getString("url");
                        String favicon=object.getString("favicon");
                        String tags=object.getString("tags");
                        Audio_List_bean bean = new Audio_List_bean();
                        bean.setImage(favicon);
                        bean.setName(name);
                        bean.setUrl(url_);
                        bean.setTags(tags);
                        audio_list.add(bean);
                    }
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            tv_total.setText("Total : "+jsonArray.length());
                            pb_audio_list.setVisibility(View.GONE);
                            Audio_Adapter audio_adapter=new Audio_Adapter(audio_list,List_Activity.this,MontReg);
                            rcv_audio_list.setAdapter(audio_adapter);
                        }
                    });
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                br.close();

            }
            catch (Exception e)
            {
                data = e.toString();
            }
            return data;
        }
    }
}
