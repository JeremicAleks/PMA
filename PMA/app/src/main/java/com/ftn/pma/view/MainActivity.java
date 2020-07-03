package com.ftn.pma.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ftn.pma.R;
import com.ftn.pma.db.User_db;
import com.ftn.pma.helper.FirebaseDatabaseHelper;
import com.ftn.pma.model.Car;
import com.ftn.pma.model.Reservation;
import com.ftn.pma.model.ShoppingCart;
import com.ftn.pma.model.User;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 5000;

    int carsNumber = 0;

    Animation topAnim, bottomAnim;
    ImageView logo;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUIImperativeMode();
        setContentView(R.layout.activity_main);


        ConstraintLayout constraintLayout =  findViewById(R.id.mainLayout);

        //animacije za bootup
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        logo = findViewById(R.id.logo);
        text = findViewById(R.id.textView2);

        logo.setAnimation(topAnim);
        text.setAnimation(bottomAnim);

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("User","User");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                new FirebaseDatabaseHelper("cars").readCars(new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataLoaded(List<Car> cars, List<String> keys) {
                        if(carsNumber<cars.size()){
                            showNotification(1,"novi");
                            carsNumber = cars.size();
                        }
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
                    public void UserIsAdded(Boolean status) {

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
            }
        }, delay);
        super.onResume();
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

    private void showNotification(int id, String text) {
        NotificationCompat.Builder notification = null;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //zbog verzije androida. Koristio sam metodu za API26 a min je 24
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("ID", "Name", importance);
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
            notification = new NotificationCompat.Builder(getApplicationContext(), notificationChannel.getId());
        } else {
            notification = new NotificationCompat.Builder(getApplicationContext());
        }

        notification.setContentTitle("New Cars");
        notification.setLights(Color.YELLOW,2000,2000);
        notification.setContentText("Stigao je auto "+ text);
        notification.setTicker("New Message Alert!");
        notification.setSmallIcon(R.drawable.ic_person_black_24dp);

//        if(id == 1)
//            notification.setSound(alarmSound);
//        else {
//            Vibrator v = (Vibrator) this.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
//            // Vibrate for 500 milliseconds
//            v.vibrate(500);
//
//        }
        notificationManager.notify(111,notification.build());
    }


}
