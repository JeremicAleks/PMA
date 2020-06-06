package com.ftn.pma.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;

import com.ftn.pma.R;
import com.ftn.pma.db.ShoppingCart_db;
import com.ftn.pma.model.Car;
import com.ftn.pma.model.ShoppingCart;
import com.ftn.pma.model.User;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;

public class CardetailsActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_cardetails);

        final User user = (User) getIntent().getSerializableExtra("user");

        hideSystemUIImperativeMode();
        TabHost tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec spec = tabHost.newTabSpec("Specification");
        spec.setContent(R.id.tab_specification);
        spec.setIndicator("Specification");
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("Dimension");
        spec.setContent(R.id.tab_dimension);
        spec.setIndicator("Dimension");
        tabHost.addTab(spec);

        Button btBuy = findViewById(R.id.btn_buy);

        final ShoppingCart_db shoppingCart_db = new ShoppingCart_db(this);

        btBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean rez = shoppingCart_db.insertData(String.valueOf(user.getId()),null);
                if(rez)
                {
                    final SimpleArcDialog mDialog = new SimpleArcDialog(CardetailsActivity.this);
                    ArcConfiguration configuration = new ArcConfiguration(CardetailsActivity.this);
                    configuration.setAnimationSpeed(SimpleArcLoader.SPEED_MEDIUM);
                    int[] mColors = {Color.parseColor("#0266C8")};
                    configuration.setText("Please wait..");
                    configuration.setColors(mColors);
                    mDialog.setConfiguration(configuration);
                    mDialog.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.cancel();
                        }
                    },4000);
                }else
                {
                    Toast.makeText(CardetailsActivity.this,"Error to buy cars!",Toast.LENGTH_LONG).show();
                }

            }
        });

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
}
