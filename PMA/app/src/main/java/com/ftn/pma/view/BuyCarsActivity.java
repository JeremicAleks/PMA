package com.ftn.pma.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;

import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ftn.pma.R;
import com.ftn.pma.db.Car_db;
import com.ftn.pma.helper.FirebaseDatabaseHelper;
import com.ftn.pma.model.Car;
import com.ftn.pma.model.Reservation;
import com.ftn.pma.model.ShoppingCart;
import com.ftn.pma.model.User;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class BuyCarsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    User user;
    Car_db car_db;
    @SuppressLint({"WrongConstant", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //promena dark ili light modela
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
        {

            setTheme(R.style.darkMode);
        }else
        {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        hideSystemUIImperativeMode();
        setContentView(R.layout.activity_buy_cars);

        car_db = new Car_db(this);
        Toolbar toolbar = findViewById(R.id.tb_buyCar);
        setSupportActionBar(toolbar);
         user = (User) getIntent().getSerializableExtra("user");

        new FirebaseDatabaseHelper("cars").readCars(new FirebaseDatabaseHelper.DataStatus() {
             @Override
             public void DataLoaded(List<Car> cars, List<String> keys) {
                 syncSqlLite(cars);
             }

             @Override
             public void DataInserted() {

             }

             @Override
             public void DataUpdated() {

             }

             @Override
             public void DataIsDeleted() {

             }

             @Override
             public void UserIsAdded() {

             }

             @Override
             public void UserLogin(User user) {

             }

             @Override
             public void ReservationAdd() {

             }

             @Override
             public void ReservationRead(List<Reservation> reservations) {

             }

             @Override
             public void ReservationUser(List<Reservation> reservations) {

             }

            @Override
            public void ShopingCart(List<ShoppingCart> shoppingCarts) {
                
            }

        });

         List<Car> cars = car_db.getAllCars();


         displayCars(cars);

        //meni koji izlazi :D
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //postavljanje username u navigation View
        TextView txtProfileName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_user_name);
        txtProfileName.setText(user.getName()+" "+user.getSurname());
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

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

    private void syncSqlLite(List<Car> cars){
        car_db.deleteTable();
        for(Car car: cars){
        car_db.insertDataCar(car, Base64.decode(car.getImageString(),Base64.DEFAULT));
        }
    }

    private void displayCars(List<Car> cars){
            LinearLayout linearLayout = findViewById(R.id.linearLayout);
            LinearLayout line = new LinearLayout(this);
            int dp = 5;
            int dpImage = 3;
        for (Car car : cars) {
            final Car carbtn = car;
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // In landscape
                ImageView imageCar = new ImageView(this);
                Bitmap bitmap = BitmapFactory.decodeByteArray(car.getImage(), 0, car.getImage().length);
                imageCar.setImageBitmap(bitmap);
                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(365*dpImage,300*dpImage);
                imageParams.setMarginStart(100*dpImage);
                imageCar.setLayoutParams(imageParams);
                linearLayout.addView(imageCar);
            } else {
                ImageView imageCar = new ImageView(this);
                Bitmap bitmap = BitmapFactory.decodeByteArray(car.getImage(), 0, car.getImage().length);
                imageCar.setImageBitmap(bitmap);
                imageCar.setLayoutParams(new LinearLayout.LayoutParams(365*dpImage,300*dpImage));
                linearLayout.addView(imageCar);
            }

            RatingBar ratingBar = new RatingBar(this);
            LinearLayout.LayoutParams ratingParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,30*dp);
            ratingParams.setMarginEnd(15*dp);
            ratingParams.gravity = Gravity.CENTER;
            ratingBar.setNumStars(5);
            ratingBar.setStepSize(0.5f);
            ratingBar.setLayoutParams(ratingParams);
            ratingBar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            ratingBar.setRating(car.getRating());
            ratingBar.setIsIndicator(true);
            linearLayout.addView(ratingBar);

            TextView textCar = new TextView(this);
            LinearLayout.LayoutParams textCarParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            String carText = car.getBrand() + " " + car.getModel();
            textCar.setLayoutParams(textCarParams);
            textCar.setText(carText);
            textCar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textCar.setTextSize(24);
            textCar.setTypeface(null, Typeface.BOLD);
            linearLayout.addView(textCar);

            TextView textAbout = new TextView(this);
            String about = car.getPower() + " " + car.getHorsePower()+ "ks";
            textAbout.setText(about);
            textAbout.setLayoutParams(textCarParams);
            textAbout.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            linearLayout.addView(textAbout);

            TextView textViewSeeDetails = new TextView(this);
            textViewSeeDetails.setText("SEE DETAILS");
            LinearLayout.LayoutParams seeParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            seeParams.topMargin = 20*dp;
            seeParams.setMarginEnd(10*dp);
            textViewSeeDetails.setLayoutParams(seeParams);
            textViewSeeDetails.setClickable(true);
            textViewSeeDetails.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textViewSeeDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BuyCarsActivity.this,CardetailsActivity.class);
                    intent.putExtra("user",user);
                    intent.putExtra("car",carbtn);
                    startActivity(intent);
                }
            });
            linearLayout.addView(textViewSeeDetails);



        }
    }

}
