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
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ftn.pma.R;
import com.ftn.pma.db.Reservation_db;
import com.ftn.pma.model.Reservation;
import com.ftn.pma.model.TypeOfService;
import com.ftn.pma.model.User;
import com.google.android.material.navigation.NavigationView;

import java.sql.SQLOutput;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ImageButton shoppingCart;
    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    TextView tv_name;
    TextView tv_email;
    TextView tv_telephone;
    Reservation_db reservation_db;
    TableLayout reservation_table;
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
        setContentView(R.layout.activity_user_profile);

        hideSystemUIImperativeMode();

        shoppingCart = findViewById(R.id.img_btn_shopping_cart);
        shoppingCart.setBackgroundColor(Color.TRANSPARENT);
        Toolbar toolbar = findViewById(R.id.tb_user_profile);
        setSupportActionBar(toolbar);

        final User user = (User) getIntent().getSerializableExtra("user");
        //inicijalizacija baze reservation
        reservation_db = new Reservation_db(this);

        reservation_table = findViewById(R.id.tabela_rezervacija);

        //izlistavanje rezervacija od korinsika
        rezervacijeKorisnika(String.valueOf(user.getId()),reservation_table);

        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        tv_telephone = findViewById(R.id.tv_telephone);

        tv_name.setText(user.getName()+" "+user.getSurname());
        tv_email.setText(user.getEmail());
        tv_telephone.setText(user.getTelephone());

        shoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this,ShoppingCartActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

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

    public void rezervacijeKorisnika(String id, TableLayout t)
    {
        List<Reservation> rezervacije = reservation_db.getAllReservation(id);
        if(rezervacije.size()>0)
        {
            for(Reservation r : rezervacije)
            {
                TableRow tr = new TableRow(this);
                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                /* Create a Button to be the row-content. */
                TextView date = new TextView(this);
                date.setText(r.getDate());
                date.setLines(r.getTypeOfService().size());
                date.setBackgroundColor(Color.parseColor("#f1f1f1"));
                date.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                date.setLayoutParams(new TableRow.LayoutParams(127, TableRow.LayoutParams.WRAP_CONTENT));
                /* Add Button to row. */
                tr.addView(date);
                TextView time = new TextView(this);
                time.setBackgroundColor(Color.parseColor("#f1f1f1"));
                time.setText(r.getTime());
                time.setLines(r.getTypeOfService().size());
                time.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                time.setLayoutParams(new TableRow.LayoutParams(127, TableRow.LayoutParams.WRAP_CONTENT));
                /* Add Button to row. */
                tr.addView(time);
                TextView service = new TextView(this);
                service.setBackgroundColor(Color.parseColor("#f1f1f1"));
                service.setLines(r.getTypeOfService().size());
                service.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                for(int i=0;i<r.getTypeOfService().size()-1;i++)
                {
                    service.append(r.getTypeOfService().get(i).toString());
                    service.append(",\n");
                }
                service.append(r.getTypeOfService().get(r.getTypeOfService().size()-1).toString());
                service.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                /* Add Button to row. */
                tr.addView(service);
                /* Add row to TableLayout. */
                t.addView(tr);
            }
        }
    }
}
