package com.ftn.pma.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ftn.pma.R;
import com.ftn.pma.model.User;
import com.google.android.material.navigation.NavigationView;

public class ShoppingCartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Animation topAnim, bottomAnim;
    private ImageView logo;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //promena dark ili light modela
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
        {
            System.out.println("DARK MOD");
            setTheme(R.style.darkMode);
        }else
        {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_shopping_cart);

        hideSystemUIImperativeMode();

        Toolbar toolbar = findViewById(R.id.tb_shopping_cart);
        setSupportActionBar(toolbar);

        //animacije za bootup
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        logo = findViewById(R.id.img_car);
        logo.setAnimation(topAnim);

        //meni koji izlazi :D
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        //postavljanje username u navigation View
        User user = (User) getIntent().getSerializableExtra("user");
        TextView txtProfileName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_user_name);
        txtProfileName.setText(user.getName()+" "+user.getSurname());
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

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
            startActivity(intent);}
        break;
            case R.id.nav_settings:
            { Intent intent = new Intent(this,HomeActivity.class);
                intent.putExtra("openFragment",R.id.nav_settings);
                intent.putExtra("openFragmentName",item.getTitle());
                startActivity(intent);
            }
            break;
            case R.id.nav_notifications:
            { Intent intent = new Intent(this,HomeActivity.class);
                intent.putExtra("openFragment",R.id.nav_notifications);
                intent.putExtra("openFragmentName",item.getTitle());
                startActivity(intent);}
            break;
            case R.id.nav_help:
            { Intent intent = new Intent(this,HomeActivity.class);
                intent.putExtra("openFragment",R.id.nav_help);
                intent.putExtra("openFragmentName",item.getTitle());
                startActivity(intent);}
            break;
            case R.id.nav_signOut:
            { Intent intent = new Intent(this,HomeActivity.class);
                intent.putExtra("openFragment",R.id.nav_signOut);
                intent.putExtra("openFragmentName",item.getTitle());
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
}
