package com.ftn.pma.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ftn.pma.R;
import com.ftn.pma.globals.TaskRequestDirections;
import com.ftn.pma.model.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AboutActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private SupportMapFragment mapFragment;
    private EditText name_service;
    private EditText email_service;
    private EditText telefone_service;
    private ImageView img_email;
    private ImageView img_telephone;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private FusedLocationProviderClient fusedLocationProviderClient;
    User user;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //promena dark ili light modela
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            System.out.println("DARK MOD");
            setTheme(R.style.darkMode);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //ukinut je notch
        hideSystemUIImperativeMode();
        Toolbar toolbar = findViewById(R.id.toolbar_about);
        toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        setSupportActionBar(toolbar);

         user = (User) getIntent().getSerializableExtra("user");

        //provera da li je internet ukljucen
        proveraInterneta();

        //google maps
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
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

        //meni koji izlazi :D
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //postavljanje username u navigation View
        TextView txtProfileName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_user_name);
        txtProfileName.setText(user.getName() + " " + user.getSurname());
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(final GoogleMap googleMap) {

        final LatLng fax_NS = new LatLng(45.245557, 19.851231);
        //dodavanje markera na mapi (pozicija 1 markera)
        googleMap.addMarker(new MarkerOptions().position(fax_NS).title("Car Repair Service").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(fax_NS,15),2000,null);

        //dodavanje trenutne lokacije gde se korisnik nalazi
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null)
                {
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    //dodavanje markera na mapi (pozicija 1 markera)
                    googleMap.addMarker(new MarkerOptions().position(currentLocation).title("My Locatioon"));

//                    //kreiranje URL za dve lokacije koje su dodate na mapi
//                    String url = RequetsURL(fax_NS,currentLocation);
//                    System.out.println("POVRATNI STRING: " + url.toString());
//                    TaskRequestDirections taskRequestDirections = new TaskRequestDirections(googleMap);
//                    taskRequestDirections.execute(url);
                }
            }
        });

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

    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.nav_home:
            { Intent intent = new Intent(this,HomeActivity.class);
                intent.putExtra("openFragment",R.id.nav_home);
                intent.putExtra("openFragmentName",item.getTitle());
                intent.putExtra("user",user);
                startActivity(intent);}
            break;
            case R.id.nav_settings:
            { Intent intent = new Intent(this,HomeActivity.class);
                intent.putExtra("openFragment",R.id.nav_settings);
                intent.putExtra("openFragmentName",item.getTitle());
                intent.putExtra("user",user);
                startActivity(intent);
            }
            break;
            case R.id.nav_notifications:
            { Intent intent = new Intent(this,HomeActivity.class);
                intent.putExtra("openFragment",R.id.nav_notifications);
                intent.putExtra("openFragmentName",item.getTitle());
                intent.putExtra("user",user);
                startActivity(intent);}
            break;
            case R.id.nav_help:
            { Intent intent = new Intent(this,HomeActivity.class);
                intent.putExtra("openFragment",R.id.nav_help);
                intent.putExtra("openFragmentName",item.getTitle());
                intent.putExtra("user",user);
                startActivity(intent);}
            break;
            case R.id.nav_signOut:
            { Intent intent = new Intent(this,HomeActivity.class);
                intent.putExtra("openFragment",R.id.nav_signOut);
                intent.putExtra("openFragmentName",item.getTitle());
                intent.putExtra("user",user);
                startActivity(intent);}
            break;
        }
        return false;
    }

    private void hideSystemUIImperativeMode() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private String RequetsURL(LatLng destination, LatLng current)
    {
        //vrednost za current lokaciju
        String curr = "origin="+current.latitude+","+current.longitude;
        //vrednost za destinaciju
        String dest = "destination="+destination.latitude+","+destination.longitude;

        String sensor = "sensor=false";
        //selektovati mod za mapu
        String mode = "mode=driving";
        //spajanje parametara za url
        String full_param = curr+"&"+dest+"&"+sensor+"&"+mode;
        //izlazni format
        String output="json";
        //url
        String url ="https://maps.googleapis.com/maps/api/directions/"+output+"?"+full_param+"&key=AIzaSyAGrcEgELZiptQoAzaU7_MQqZGP_F-Ttdk";

        return url;

    }

    public static final String requestDirection(String urlPath) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try
        {
            URL url = new URL(urlPath);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line = "";
            while((line = bufferedReader.readLine())!=null)
            {
               responseString += line;
            }

            bufferedReader.close();
            inputStreamReader.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }finally {
            if(inputStream != null)
            {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }
}
