package com.example.david.desafioweekeng;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Tela01 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela01);
    }

    public void clickConfirm(View v){
        SingletonClima singletonClima = SingletonClima.getInstance();
        String cityName = ((EditText)findViewById(R.id.cityName)).getText().toString();
        String url = singletonClima.getUrlByCityName(cityName);

        JsonObjectRequest json = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject weather = response.getJSONArray("weather").getJSONObject(0);
                    String weatherMain = weather.getString("main");
                    Toast toast = Toast.makeText(Tela01.this, "certo: "+weatherMain,Toast.LENGTH_SHORT);
                    toast.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(Tela01.this, getString(R.string.city_name_error),Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(json);


    }
}
