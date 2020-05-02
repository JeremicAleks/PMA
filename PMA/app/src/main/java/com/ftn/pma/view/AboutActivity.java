package com.ftn.pma.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ftn.pma.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AboutActivity extends AppCompatActivity implements OnMapReadyCallback {

    private SupportMapFragment mapFragment;
    private EditText name_service;
    private EditText email_service;
    private EditText telefone_service;
    private ImageView img_email;
    private ImageView img_telephone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar = findViewById(R.id.toolbar_about);
        toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        setSupportActionBar(toolbar);

        //provera da li je internet ukljucen
        proveraInterneta();

        //google maps
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        name_service = findViewById(R.id.pt_name_service);
        email_service = findViewById(R.id.pt_email_service);
        telefone_service = findViewById(R.id.pt_telefone_service);

        name_service.setFocusable(false);
        email_service.setFocusable(false);
        telefone_service.setFocusable(false);

        img_email = findViewById(R.id.img_email);
        img_telephone = findViewById(R.id.img_telephone);
        int color = Color.parseColor("#000000");
        img_email.setColorFilter(color);
        img_telephone.setColorFilter(color);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng fax_NS = new LatLng(45.245557, 19.851231);
        googleMap.addMarker(new MarkerOptions().position(fax_NS).title("Car Repair Service"));

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(fax_NS,15),2000,null);

    }

    public void proveraInterneta()
    {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo aktivnaMreza = manager.getActiveNetworkInfo();

        if(aktivnaMreza!=null)
        {
            Toast.makeText(this,"Data Network Enabled", Toast.LENGTH_LONG).show();
        }else
        {
            Toast.makeText(this,"No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }
}
