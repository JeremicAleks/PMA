package com.ftn.pma.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ftn.pma.R;
import com.ftn.pma.db.Reservation_db;
import com.ftn.pma.globals.SendMail;
import com.ftn.pma.helper.FirebaseDatabaseHelper;
import com.ftn.pma.model.Car;
import com.ftn.pma.model.Reservation;
import com.ftn.pma.model.TypeOfService;
import com.ftn.pma.model.User;
import com.google.android.material.navigation.NavigationView;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ServiceActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Reservation_db reservation_db;
    User user;
    EditText et_email_service;
    CheckBox cb_small_service;
    CheckBox cb_big_service;
    CheckBox cb_technical_inspection;
    TextView tvSelectDate;
    TextView tvSelectTime;
    TextView tvCheckValidBox;
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
        setContentView(R.layout.activity_service);
        hideSystemUIImperativeMode();

        Toolbar toolbar = findViewById(R.id.tb_service);
        setSupportActionBar(toolbar);
        //uzimanje uloogvanog User-a
        user = (User) getIntent().getSerializableExtra("user");
        //inicijalizacija baze reservation
        reservation_db = new Reservation_db(this);
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

        et_email_service = findViewById(R.id.et_email_service);
        cb_small_service = findViewById(R.id.cb_small_service);
        cb_big_service = findViewById(R.id.cb_big_service);
        cb_technical_inspection = findViewById(R.id.cb_technical_inspection);
        tvSelectDate = findViewById(R.id.tv_selectDate);
        tvSelectTime = findViewById(R.id.tv_timePicker);
        tvCheckValidBox = findViewById(R.id.tv_serviceCheck);
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;

                String date = dayOfMonth + "/" + month + "/" + year;
                tvSelectDate.setText(date);
            }
        };

        tvSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ServiceActivity.this,dateSetListener,year,month,day);

                datePickerDialog.getWindow();
                datePickerDialog.show();
            }
        });

        final TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                String time = hourOfDay+":"+minute;
                tvSelectTime.setText(time);
            }
        };

        tvSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(ServiceActivity.this,timeSetListener,hour,minute,true);

                timePickerDialog.getWindow();
                timePickerDialog.show();
            }
        });
        Button btnReserve = findViewById(R.id.btn_reserve);
        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validation()) {
                    Reservation r = new Reservation();
                    boolean rezultat = false;
                    if (cb_small_service.isChecked() && cb_big_service.isChecked() && cb_technical_inspection.isChecked()) {
                        rezultat = reservation_db.insertData(et_email_service.getText().toString(), TypeOfService.SMALL_SERVICE + "," + TypeOfService.BIG_SERVICE + "," + TypeOfService.TECHNICAL_INSPECTION, tvSelectDate.getText().toString(), tvSelectTime.getText().toString(), String.valueOf(user.getId()));
                        List<TypeOfService> tp = new ArrayList<>();
                        tp.add(TypeOfService.SMALL_SERVICE);
                        tp.add(TypeOfService.BIG_SERVICE);
                        tp.add(TypeOfService.TECHNICAL_INSPECTION);
                        r.setEmail(et_email_service.getText().toString());
                        r.setTypeOfService(tp);
                        r.setDate(tvSelectDate.getText().toString());
                        r.setTime(tvSelectTime.getText().toString());
                        r.setUserId(user.getId());
                    } else if (cb_small_service.isChecked() && cb_big_service.isChecked()) {
                        rezultat = reservation_db.insertData(et_email_service.getText().toString(), TypeOfService.SMALL_SERVICE + "," + TypeOfService.BIG_SERVICE, tvSelectDate.getText().toString(), tvSelectTime.getText().toString(), String.valueOf(user.getId()));
                        List<TypeOfService> tp = new ArrayList<>();
                        tp.add(TypeOfService.SMALL_SERVICE);
                        tp.add(TypeOfService.BIG_SERVICE);
                        r.setEmail(et_email_service.getText().toString());
                        r.setTypeOfService(tp);
                        r.setDate(tvSelectDate.getText().toString());
                        r.setTime(tvSelectTime.getText().toString());
                        r.setUserId(user.getId());
                    } else if (cb_big_service.isChecked() && cb_technical_inspection.isChecked()) {
                        rezultat = reservation_db.insertData(et_email_service.getText().toString(), TypeOfService.BIG_SERVICE + "," + TypeOfService.TECHNICAL_INSPECTION, tvSelectDate.getText().toString(), tvSelectTime.getText().toString(), String.valueOf(user.getId()));
                        List<TypeOfService> tp = new ArrayList<>();
                        tp.add(TypeOfService.BIG_SERVICE);
                        tp.add(TypeOfService.TECHNICAL_INSPECTION);
                        r.setEmail(et_email_service.getText().toString());
                        r.setTypeOfService(tp);
                        r.setDate(tvSelectDate.getText().toString());
                        r.setTime(tvSelectTime.getText().toString());
                        r.setUserId(user.getId());
                    } else if (cb_small_service.isChecked() && cb_technical_inspection.isChecked()) {
                        rezultat = reservation_db.insertData(et_email_service.getText().toString(), TypeOfService.SMALL_SERVICE + "," + TypeOfService.TECHNICAL_INSPECTION, tvSelectDate.getText().toString(), tvSelectTime.getText().toString(), String.valueOf(user.getId()));
                        List<TypeOfService> tp = new ArrayList<>();
                        tp.add(TypeOfService.SMALL_SERVICE);
                        tp.add(TypeOfService.TECHNICAL_INSPECTION);
                        r.setEmail(et_email_service.getText().toString());
                        r.setTypeOfService(tp);
                        r.setDate(tvSelectDate.getText().toString());
                        r.setTime(tvSelectTime.getText().toString());
                        r.setUserId(user.getId());
                    } else if (cb_small_service.isChecked()) {
                        rezultat = reservation_db.insertData(et_email_service.getText().toString(), TypeOfService.SMALL_SERVICE.toString(), tvSelectDate.getText().toString(), tvSelectTime.getText().toString(), String.valueOf(user.getId()));
                        List<TypeOfService> tp = new ArrayList<>();
                        tp.add(TypeOfService.SMALL_SERVICE);
                        r.setEmail(et_email_service.getText().toString());
                        r.setTypeOfService(tp);
                        r.setDate(tvSelectDate.getText().toString());
                        r.setTime(tvSelectTime.getText().toString());
                        r.setUserId(user.getId());
                    } else if (cb_big_service.isChecked()) {
                        rezultat = reservation_db.insertData(et_email_service.getText().toString(), TypeOfService.BIG_SERVICE.toString(), tvSelectDate.getText().toString(), tvSelectTime.getText().toString(), String.valueOf(user.getId()));
                        List<TypeOfService> tp = new ArrayList<>();
                        tp.add(TypeOfService.BIG_SERVICE);
                        r.setEmail(et_email_service.getText().toString());
                        r.setTypeOfService(tp);
                        r.setDate(tvSelectDate.getText().toString());
                        r.setTime(tvSelectTime.getText().toString());
                        r.setUserId(user.getId());
                    } else if (cb_technical_inspection.isChecked()) {
                        rezultat = reservation_db.insertData(et_email_service.getText().toString(), TypeOfService.TECHNICAL_INSPECTION.toString(), tvSelectDate.getText().toString(), tvSelectTime.getText().toString(), String.valueOf(user.getId()));
                        List<TypeOfService> tp = new ArrayList<>();
                        tp.add(TypeOfService.TECHNICAL_INSPECTION);
                        r.setEmail(et_email_service.getText().toString());
                        r.setTypeOfService(tp);
                        r.setDate(tvSelectDate.getText().toString());
                        r.setTime(tvSelectTime.getText().toString());
                        r.setUserId(user.getId());
                    }
                    final SimpleArcDialog mDialog = new SimpleArcDialog(ServiceActivity.this);
                    ArcConfiguration configuration = new ArcConfiguration(ServiceActivity.this);
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
                    }, 4000);
                    if (rezultat) {
                        try {
                            sendEmail(et_email_service.getText().toString());
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                        new FirebaseDatabaseHelper("service").addReservation(r, new FirebaseDatabaseHelper.DataStatus() {
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
                            public void UserIsAdded() {

                            }

                            @Override
                            public void UserLogin(List<User> users) {

                            }

                            @Override
                            public void ReservationAdd() {
                                Toast.makeText(ServiceActivity.this,"Successful reservation service",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void ReservationRead(List<Reservation> reservations) {

                            }
                        });
                        Toast.makeText(ServiceActivity.this, "Successful reservation service", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ServiceActivity.this, HomeActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ServiceActivity.this, "Problem with reservation", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

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

    public void sendEmail(String emailTo) throws MessagingException {

        Reservation reservation = reservation_db.zadnjaRezervacija(String.valueOf(user.getId()));
        final String email="nijemidosadno@gmail.com";
        final String password="nijemidosadno123";

        //podesavanja za gmail
        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        //Kreiranje sesija za mail i provera autentifikacije
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email,password);
            }
        });

        //kreiranje poruke
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(email));
        message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(emailTo.trim()));
        message.setSubject("[Car Repair] - Rezervacija");
        String poruka = "Poštovani "+ user.getName()+",\n\n"+"Uspešno ste rezervisali termin.\n Informacije o rezervaciji su:\n\t"+"Datum: "+reservation.getDate()+"\n\tVreme: "+reservation.getTime()+"\n\tUsluga: ";
        for(int i=0;i<reservation.getTypeOfService().size()-1;i++)
        {
            poruka+=reservation.getTypeOfService().get(i).toString()+", ";
        }
        poruka+=reservation.getTypeOfService().get(reservation.getTypeOfService().size()-1).toString();
        message.setText(poruka);

        new SendMail().execute(message);
    }

    private boolean validation(){
     boolean flag = true;
     tvCheckValidBox.setText("");

     if(et_email_service.getText().toString().isEmpty()){
         et_email_service.setError("Email is mandatory!");
         flag = false;
     }
     if(!et_email_service.getText().toString().contains("@") && !et_email_service.getText().toString().isEmpty()){
         et_email_service.setError("Email is not valid!");
         flag = false;
     }

     if(!cb_big_service.isChecked() && !cb_small_service.isChecked() && !cb_technical_inspection.isChecked()){
         String serviceCheck = "You must choose service type!";
         tvCheckValidBox.setText(serviceCheck);
         flag = false;
     }

     if(tvSelectDate.getText().equals("Select Date")){
         tvSelectDate.setError("You must choose date");
         flag = false;
     }

     if(tvSelectTime.getText().equals("Select Time")){
         tvSelectTime.setError("You must choose time!");
         flag = false;
     }

     return  flag;
    }
}
