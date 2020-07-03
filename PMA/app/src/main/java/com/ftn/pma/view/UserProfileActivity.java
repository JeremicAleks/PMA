package com.ftn.pma.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.ftn.pma.R;
import com.ftn.pma.db.Reservation_db;
import com.ftn.pma.db.User_db;
import com.ftn.pma.helper.FirebaseDatabaseHelper;
import com.ftn.pma.model.Car;
import com.ftn.pma.model.Reservation;
import com.ftn.pma.model.ShoppingCart;
import com.ftn.pma.model.TypeOfService;
import com.ftn.pma.model.User;
import com.ftn.pma.ui.home.HomeFragment;
import com.google.android.material.navigation.NavigationView;

import java.sql.SQLOutput;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ImageButton shoppingCart;
    private ImageButton edit;
    private ImageButton saveEditText;
    private AlertDialog changeName;
    private AlertDialog changeEmail;
    private AlertDialog changeNumber;
    private EditText editName;
    private EditText editEmail;
    private EditText editNumber;
    private AppBarConfiguration mAppBarConfiguration;
    private int editUserProfile = 0;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    TextView tv_name;
    TextView tv_email;
    TextView tv_telephone;
    Reservation_db reservation_db;
    TableLayout reservation_table;
    User user;
    User_db user_db;
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
        setContentView(R.layout.activity_user_profile);

        hideSystemUIImperativeMode();

        shoppingCart = findViewById(R.id.img_btn_shopping_cart);
        shoppingCart.setBackgroundColor(Color.TRANSPARENT);
        Toolbar toolbar = findViewById(R.id.tb_user_profile);
        setSupportActionBar(toolbar);

        user = (User) getIntent().getSerializableExtra("user");
        //inicijalizacija baze reservation
        reservation_db = new Reservation_db(this);
        reservation_table = findViewById(R.id.tabela_rezervacija);

        //izlistavanje rezervacija od korinsika
        rezervacijeKorisnika(user.getKey(),reservation_table);

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
        final TextView txtProfileName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_user_name);
        txtProfileName.setText(user.getName()+" "+user.getSurname());
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //edit dugme za user profile
        edit = findViewById(R.id.img_btn_edit);
        edit.setBackgroundColor(Color.TRANSPARENT);
        saveEditText = findViewById(R.id.img_btn_save);
        saveEditText.setBackgroundColor(Color.TRANSPARENT);
        editName = new EditText(this);
//        editEmail = new EditText(this);
        editNumber = new EditText(this);
        changeName = new AlertDialog.Builder(this).create();
//        changeEmail = new AlertDialog.Builder(this).create();
        changeNumber = new AlertDialog.Builder(this).create();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.setVisibility(View.GONE);
                saveEditText.setVisibility(View.VISIBLE);
                changeName.setTitle("Edit Name and Surname");
                changeName.setView(editName);
//                changeEmail.setTitle("Edit email");
//                changeEmail.setView(editEmail);
                changeNumber.setTitle("Edit number");
                changeNumber.setView(editNumber);
                editUserProfile = 1;
            }
        });
        saveEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEditText.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);
                editUserProfile=0;
                String[] split = tv_name.getText().toString().split(" ");
                if(split.length > 1)
                {
                    user.setName(split[0]);
                    user.setSurname(split[1]);
                    user.setTelephone(tv_telephone.getText().toString());
                    new FirebaseDatabaseHelper("users").editUser(user,new FirebaseDatabaseHelper.DataStatus() {
                        @Override
                        public void DataLoaded(List<Car> cars, List<String> keys) {
                        }

                        @Override
                        public void DataInserted() {

                        }

                        @Override
                        public void DataUpdated() {
                            Toast.makeText(UserProfileActivity.this,"Successful edit profile",Toast.LENGTH_SHORT).show();
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
                }else
                {
                    Toast.makeText(UserProfileActivity.this,"Error edit profile",Toast.LENGTH_SHORT).show();
                }
                txtProfileName.setText(user.getName()+" "+user.getSurname());
            }
        });
        tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editUserProfile == 1)
                {
                    editName.setText(tv_name.getText());
                    changeName.show();
                }
            }
        });
        changeName.setButton(DialogInterface.BUTTON_POSITIVE, "SAVE TEXT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tv_name.setText(editName.getText());
            }
        });
//        tv_email.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(editUserProfile == 1)
//                {
//                    editEmail.setText(tv_email.getText());
//                    changeEmail.show();
//                }
//            }
//        });
//        changeEmail.setButton(DialogInterface.BUTTON_POSITIVE, "SAVE TEXT", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                tv_email.setText(editEmail.getText());
//            }
//        });
        tv_telephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editUserProfile == 1)
                {
                    editNumber.setText(tv_telephone.getText());
                    changeNumber.show();
                }
            }
        });
        changeNumber.setButton(DialogInterface.BUTTON_POSITIVE, "SAVE TEXT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tv_telephone.setText(editNumber.getText());
            }
        });
    }
//    @Override
//    public void finish()
//    {
//        //Starting the previous Intent
//        Intent previousScreen = new Intent(getApplicationContext(), HomeFragment.class);
//        //Sending the data to Activity_A
//        previousScreen.putExtra("user",user);
//        setResult(1, previousScreen);
//        super.finish();
//
//        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
//    }

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

    public void rezervacijeKorisnika(String key, final TableLayout t)
    {
        new FirebaseDatabaseHelper("service").readReservationOfUser(key,new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataLoaded(List<Car> cars, List<String> keys) {
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
                reservation_db.deleteTable();
                for(Reservation r : reservations)
                {
                    String s="";
                    for(int i=0;i<r.getTypeOfService().size()-1;i++)
                    {
                        s += r.getTypeOfService().get(i) + ",";
                    }
                    s+= r.getTypeOfService().get(r.getTypeOfService().size()-1);

                    reservation_db.insertData(r.getEmail(),s,r.getDate(),r.getTime(),r.getUserKey());
                }

                displayReservation(reservations,t);
            }

            @Override
            public void ShopingCart(List<ShoppingCart> shoppingCarts) {

            }

        });

        List<Reservation> rezervacije = reservation_db.getAllReservation(user.getKey());

        if(rezervacije!= null)
        {
            for(Reservation r : rezervacije)
            {
                TableRow tr = new TableRow(this);
                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                /* Date button */
                TextView date = new TextView(this);
                date.setText(r.getDate());
                TableRow.LayoutParams params = new TableRow.LayoutParams(127, TableRow.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,0,0,10);
                date.setLayoutParams(params);
                date.setLines(r.getTypeOfService().size());
                date.setBackgroundColor(Color.parseColor("#f1f1f1"));
                date.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tr.addView(date);
                /* TIME button */
                TextView time = new TextView(this);
                time.setBackgroundColor(Color.parseColor("#f1f1f1"));
                time.setText(r.getTime());
                time.setLines(r.getTypeOfService().size());
                time.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                time.setLayoutParams(params);
                tr.addView(time);
                /* SERVICE button */
                TextView service = new TextView(this);
                service.setBackgroundColor(Color.parseColor("#f1f1f1"));
                service.setLines(r.getTypeOfService().size());
                service.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                for(int i=0;i<r.getTypeOfService().size()-1;i++)
                {
                        service.append(r.getTypeOfService().get(i).toString().replace("_"," "));
                        service.append(",\n");
                }

                service.append(r.getTypeOfService().get(r.getTypeOfService().size()-1).toString().replace("_"," "));

                service.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tr.addView(service);

                /* Add row to TableLayout. */
                t.addView(tr);
            }
        }
    }

    public void displayReservation(List<Reservation> reservations, final TableLayout t)
    {
        if(reservations!= null)
        {
            for(Reservation r : reservations)
            {
                TableRow tr = new TableRow(this);
                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                /* Date button */
                TextView date = new TextView(this);
                date.setText(r.getDate());
                TableRow.LayoutParams params = new TableRow.LayoutParams(127, TableRow.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,0,0,10);
                date.setLayoutParams(params);
                date.setLines(r.getTypeOfService().size());
                date.setBackgroundColor(Color.parseColor("#f1f1f1"));
                date.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tr.addView(date);
                /* TIME button */
                TextView time = new TextView(this);
                time.setBackgroundColor(Color.parseColor("#f1f1f1"));
                time.setText(r.getTime());
                time.setLines(r.getTypeOfService().size());
                time.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                time.setLayoutParams(params);
                tr.addView(time);
                /* SERVICE button */
                TextView service = new TextView(this);
                service.setBackgroundColor(Color.parseColor("#f1f1f1"));
                service.setLines(r.getTypeOfService().size());
                service.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                for(int i=0;i<r.getTypeOfService().size()-1;i++)
                {
                    service.append(r.getTypeOfService().get(i).toString().replace("_"," "));
                    service.append(",\n");
                }

                service.append(r.getTypeOfService().get(r.getTypeOfService().size()-1).toString().replace("_"," "));

                service.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tr.addView(service);

                /* Add row to TableLayout. */
                t.addView(tr);
            }
        }
    }
}
