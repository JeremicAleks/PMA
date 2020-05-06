package com.ftn.pma.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

import com.ftn.pma.R;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;

public class CardetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardetails);

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
                SimpleArcDialog mDialog = new SimpleArcDialog(CardetailsActivity.this);
                ArcConfiguration configuration = new ArcConfiguration(CardetailsActivity.this);
                configuration.setText("Please wait..");
                mDialog.setConfiguration(configuration);
                mDialog.show();
            }
        });

    }
}
