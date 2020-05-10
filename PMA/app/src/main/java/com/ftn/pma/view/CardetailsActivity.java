package com.ftn.pma.view;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TabHost;

import com.ftn.pma.R;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;

public class CardetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardetails);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        btBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

    }
}
