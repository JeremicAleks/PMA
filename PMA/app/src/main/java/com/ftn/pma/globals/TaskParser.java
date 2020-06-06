package com.ftn.pma.globals;

import android.graphics.Color;
import android.os.AsyncTask;

import com.ftn.pma.view.AboutActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String,String>>>> {

    private GoogleMap googleMap;

    public TaskParser(GoogleMap maps) {
        googleMap = maps;
    }

    @Override
    protected List<List<HashMap<String,String>>> doInBackground(String... strings) {
        JSONObject jsonObject = null;
        List<List<HashMap<String,String>>> routes = null;
        try
        {
            jsonObject = new JSONObject(strings[0]);
            System.out.println("JSON OBJECT " + jsonObject.toString());
            DirectionsParser directionsParser = new DirectionsParser();
            routes = directionsParser.parse(jsonObject);
        }catch(Exception e)
        {
            e.printStackTrace();
        }

        return routes;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
        //iscrtavanje na mapi
        ArrayList points = null;
        PolylineOptions polylineOptions = null;
        for(List<HashMap<String, String>> path : lists)
        {
            points = new ArrayList();
            polylineOptions = new PolylineOptions();

            for(HashMap<String, String> point: path)
            {
                double lat = Double.parseDouble(point.get("lat"));
                double lon = Double.parseDouble(point.get("lng"));

                points.add(new LatLng(lat,lon));
            }
            System.out.println("PROSO DO OVDE");
            polylineOptions.addAll(points);
            polylineOptions.width(15);
            polylineOptions.color(Color.BLUE);
            polylineOptions.geodesic(true);
        }

        if(polylineOptions != null)
        {
            googleMap.addPolyline(polylineOptions);
        }
    }
}
