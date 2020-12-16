package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    RelativeLayout ml;
    EditText et;
    Button btn;

    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8,tv9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ml= findViewById(R.id.mLayout);
        et = findViewById(R.id.editText);
        btn = findViewById(R.id.btn);
        tv1 = findViewById(R.id.temperature);
        tv2 = findViewById(R.id.city);
        tv3 = findViewById(R.id.country);
        tv4 = findViewById(R.id.sunrise);
        tv5 = findViewById(R.id.sunset);
        tv6 = findViewById(R.id.humidity);
        tv7 = findViewById(R.id.pressure);
        tv8 = findViewById(R.id.description);
        tv9 = findViewById(R.id.wind);
        Load_setting();

    }

    private void Load_setting(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean chk_night = sp.getBoolean("NIGHT",false);
        if(chk_night){
            ml.setBackgroundResource(R.drawable.gradient_bk);
            btn.setBackgroundColor(Color.parseColor("#3CF1EBF1"));
        }else{
            ml.setBackgroundResource(R.drawable.gradient1_bg);
            btn.setBackgroundColor(Color.parseColor("#3CF1EBF1"));
        }

        String orientation = sp.getString("ORIENTATION","false");
        if("1".equals(orientation)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
        }else if ("2".equals(orientation)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else if ("3".equals(orientation)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.settings_menu:
                Intent i =new Intent(this,Preference.class);
                startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }

    public void getWeather(View view) {
        String city = et.getText().toString();
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&units=imperial&appid=e20cdb577eb4eeac92af55ad6f521ac5";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj1 = response.getJSONObject("main");
                    String humidity = obj1.getString("humidity");
                    String pressure = obj1.getString("pressure");
                    String temp = String.valueOf(obj1.getDouble("temp"));
                    double temp_int = Double.parseDouble(temp);
                    double centi = (temp_int - 32)/1.8000;
                    int i= (int)centi;

                    JSONArray arr = response.getJSONArray("weather");
                    JSONObject obj = arr.getJSONObject(0);
                    String desc = obj.getString("description");

                    JSONObject obj3 = response.getJSONObject("wind");
                    String speed = obj3.getString("speed");

                    JSONObject obj4 = response.getJSONObject("sys");
                    String country = obj4.getString("country");



                    long unixTimestamp = Long.parseLong(String.valueOf(obj4.getLong("sunrise")));;
                    long javaTimestamp = unixTimestamp * 1000L;
                    Date date = new Date(javaTimestamp);
                    String sunrise= new SimpleDateFormat("h:mm a").format(date);

                    long unixTimestamp1 = Long.parseLong(String.valueOf(obj4.getLong("sunset")));;
                    long javaTimestamp1 = unixTimestamp1 * 1000L;
                    Date date1 = new Date(javaTimestamp1);
                    String sunset= new SimpleDateFormat("hh:mm a").format(date1);


                    String city = response.getString("name");
                    tv1.setText(String.valueOf(i)+" °C");//temp
                    tv2.setText(city);//city
                    tv3.setText(country);
                    tv4.setText(sunrise);
                    tv5.setText(sunset);
                    tv6.setText(humidity+" %");//humidity
                    tv7.setText(pressure+" hPa");//pressure
                    tv8.setText(desc.toUpperCase());//description
                    tv9.setText(speed+" km/h");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jor);
    }

    @Override
    protected void onResume() {
        Load_setting();
        super.onResume();
    }
}