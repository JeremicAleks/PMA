package com.ftn.pma.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TabHost;

import com.ftn.pma.R;

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
    }
}
