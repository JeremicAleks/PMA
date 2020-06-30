package com.ftn.pma.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ftn.pma.R;
import com.ftn.pma.db.User_db;
import com.ftn.pma.helper.FirebaseDatabaseHelper;
import com.ftn.pma.model.Car;
import com.ftn.pma.model.Reservation;
import com.ftn.pma.model.User;

import java.util.List;

public class LoginActivity extends AppCompatActivity{

    EditText etEmail;
    EditText etPassword;

    User_db user_db;
    User user;

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
        setContentView(R.layout.activity_login);

        hideSystemUIImperativeMode();

        TextView tvSignUp = findViewById(R.id.tv_signUp);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        user_db = new User_db(this);
        Button btnLogin = findViewById(R.id.btn_login);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    final String email = etEmail.getText().toString();
                    final String pass = etPassword.getText().toString();
//                    user = user_db.login(email, pass);
                    new FirebaseDatabaseHelper("users").readUser(new FirebaseDatabaseHelper.DataStatus() {
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
                            for(User u : users)
                            {
                                if (u.getEmail().equals(email) && u.getPassword().equals(pass)) {
                                    user_db.createTable();
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    intent.putExtra("user", u);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    finish();
                                } else if (email.equalsIgnoreCase("admin") && pass.equalsIgnoreCase("admin")) {
                                    Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Failed Login", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void ReservationAdd() {

                        }

                        @Override
                        public void ReservationRead(List<Reservation> reservations) {

                        }
                    });

                }
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
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

    private boolean validation(){
        boolean flag = true;

        if(etEmail.getText().toString().isEmpty()){
            etEmail.setError("Email is mandatory!");
            flag = false;
        }

        if(etPassword.getText().toString().isEmpty()){
            etPassword.setError("Password is mandatory!");
            flag = false;
        }

        return flag;

    }

}
