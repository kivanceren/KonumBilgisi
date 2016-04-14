package com.location.konumbilgisi;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class LocationActivity extends AppCompatActivity {
    Button location;
    GPSTracker gps;
    Context c = this;
    TextView tw;
    double latitude=0.0;
    double longitude=0.0;
    double temp,humidity,wind;
    static String weather_key = "520264f0eadc564b1359bc8621f5c785";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_activity_location);

        location =(Button) findViewById(R.id.btn_location);
        tw =(TextView)findViewById(R.id.tempeture);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new GPSTracker(LocationActivity.this);
                if(gps.canGetLocation())
                {
                     latitude = gps.getLatitude();
                     longitude = gps.getLongitude();
                      new getWeatherCondition().execute();

                  //  Toast.makeText(getApplicationContext(),"Your location is \n Lat:" + latitude + "\n Long:" + longitude + "City: " +  city_name , Toast.LENGTH_LONG).show();
                }
                else
                {
                    gps.showSettingsAlert();
                }
            }
        });

    }
    private class getWeatherCondition extends AsyncTask<Void,Void,Void>
    {

        String state;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
              String weatherUrl = "http://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&units=metric&appid="+weather_key;

            JSONObject jsonObject=null;
            try{
                 String json = JSONParser.getJSONFromUrl(weatherUrl);

                 try{
                     jsonObject=new JSONObject(json);
                 }catch (JSONException e)
                 {
                     Log.d("JSON Parser" ,"Error creating json objecy" + e.toString());
                 }
                JSONArray jsonArray = jsonObject.getJSONArray("weather");
                JSONObject jsonSkyState=jsonArray.getJSONObject(0);
                 state = jsonSkyState.getString("main");

                JSONObject mainObj = jsonObject.getJSONObject("main");
                temp= mainObj.getDouble("temp");
                humidity=mainObj.getDouble("humidity");

                JSONObject _wind = jsonObject.getJSONObject("wind");
                wind = _wind.getDouble("speed");

            }catch (JSONException e)
            {
                Log.e("json","doInBackground");
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
              tw.setText("Aloo :\n" + temp + " - " + humidity + "-" +wind +" +"+ latitude +"+ " +longitude);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

