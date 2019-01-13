package com.example.david.desafioweekeng;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
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
    final SingletonClima singletonClima = SingletonClima.getInstance();
    LocationManager locationManager;
    LocationListenerGPS locationListenerGPS;

    private ProgressDialog conectando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela01);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListenerGPS = new LocationListenerGPS();
    }

    private void checkClimate(String url) {
        JsonObjectRequest json = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject weather = response.getJSONArray("weather").getJSONObject(0);
                    String weatherMain = weather.getString("main");
                    singletonClima.setWeather(weatherMain);
                    Toast toast = Toast.makeText(Tela01.this, getString(R.string.weather) + weatherMain, Toast.LENGTH_SHORT);
                    toast.show();
                    Intent intent = new Intent(Tela01.this, Tela02.class);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(Tela01.this, getString(R.string.city_name_error), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(json);
    }

    public void clickConfirm(View v) {
        String cityName = ((EditText) findViewById(R.id.cityName)).getText().toString();
        String url = singletonClima.getUrlByCityName(cityName);
        checkClimate(url);
    }

    public void clickByLocalization(View v) {
        if (isLocationEnabled(locationManager)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Tela01.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
                ActivityCompat.requestPermissions(Tela01.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        2);
                return;
            }
            locationManager.requestLocationUpdates(locationManager.getBestProvider(new Criteria(),true), 2, 10, locationListenerGPS);
            conectando = new ProgressDialog(this);
            conectando.setMessage("Tentando Encontrar Localização");
            conectando.show();

        }
        else {
            showAlert();
        }
    }


    private boolean isLocationEnabled(LocationManager locationManager) {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.enable_localization))
                .setMessage(getString(R.string.localization_off))
                .setPositiveButton(getString(R.string.localization_setings), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private void updatedLocation(Double lat,Double lon){
        locationManager.removeUpdates(locationListenerGPS);
        conectando.setMessage("Localização Encontrada");
        conectando.dismiss();
        checkClimate(singletonClima.getUrlByLatLon(lat,lon));
    }

    private final class  LocationListenerGPS implements LocationListener {
        public void onLocationChanged(Location location) {
            Double longitudeGPS = location.getLongitude();
            Double latitudeGPS = location.getLatitude();
            updatedLocation(latitudeGPS,longitudeGPS);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };

}
