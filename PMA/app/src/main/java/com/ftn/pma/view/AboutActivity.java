package com.ftn.pma.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ftn.pma.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class AboutActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap maps;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mapFragment = getSupportFragmentManager().findFragmentById(R.id.mapApi);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
