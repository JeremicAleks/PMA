package com.ftn.pma.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ftn.pma.R;
import com.ftn.pma.db.Car_db;
import com.ftn.pma.db.ShoppingCart_db;
import com.ftn.pma.model.Car;
import com.ftn.pma.model.ShoppingCart;
import com.ftn.pma.model.User;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class ShoppingCartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Animation topAnim, bottomAnim;
    private ImageView logo;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    ShoppingCart_db shoppingCart_db;
    Car_db car_db;
    TableLayout shopping_table;
    User user;
    @SuppressLint("RestrictedApi")
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
        user = (User) getIntent().getSerializableExtra("user");
        TextView txtProfileName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_user_name);
        txtProfileName.setText(user.getName()+" "+user.getSurname());
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //iscrtavanje automobila u tabeli
        shoppingCart_db = new ShoppingCart_db(this);
        car_db = new Car_db(this);
        shopping_table = findViewById(R.id.shoppingTable);
        getAllCars(String.valueOf(user.getId()),shopping_table);

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

    public void getAllCars(String user_id, TableLayout t)
    {

        List<ShoppingCart> shopiShoppingCartList = shoppingCart_db.getAllBuyCars(user_id);
        List<Car> carList = car_db.getAllCars();
        if(shopiShoppingCartList != null)
        {
            for(ShoppingCart shop : shopiShoppingCartList)
            {
                for(Car c : carList)
                {
                    System.out.println("IZBOR KOLA");
                    if(c.getId() == Integer.parseInt(shop.getCars_id()))
                    {
                        System.out.println("NASAO AUTO");
                        TableRow tr = new TableRow(this);
                        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                        /* Marka + model button */
                        TextView marka_model = new TextView(this);
                        marka_model.setText(c.getBrand() + " "+ c.getModel());
                        TableRow.LayoutParams params = new TableRow.LayoutParams(127, TableRow.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0,0,10,10);
                        marka_model.setLayoutParams(params);
                        marka_model.setLines(1);
                        marka_model.setBackgroundColor(Color.parseColor("#f1f1f1"));
                        marka_model.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        tr.addView(marka_model);
                        /* PRICE button */
                        TextView price = new TextView(this);
                        price.setBackgroundColor(Color.parseColor("#f1f1f1"));
                        price.setText(String.valueOf(c.getPrice())+"e");
                        price.setLines(1);
                        price.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        price.setLayoutParams(params);
                        tr.addView(price);

                        /* Add row to TableLayout. */
                        t.addView(tr);
                    }
                }
            }
        }
    }
}
