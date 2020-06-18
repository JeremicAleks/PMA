package com.ftn.pma.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ftn.pma.R;
import com.ftn.pma.db.User_db;

public class RegistrationActivity extends AppCompatActivity {

    User_db user_db;
    EditText name;
    EditText surname;
    EditText telephone;
    EditText email;
    EditText password;
    Button registracija;
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
        hideSystemUIImperativeMode();
        setContentView(R.layout.activity_registration);

        user_db = new User_db(this);

        name = findViewById(R.id.ed_name);
        surname = findViewById(R.id.ed_surname);
        telephone = findViewById(R.id.ed_telephone);
        email = findViewById(R.id.ed_email);
        password = findViewById(R.id.ed_password);
        registracija = findViewById(R.id.btn_registration);


        registracija.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(chechValidation()){
                boolean rezultat = user_db.insertData(name.getText().toString(),surname.getText().toString(),
                        telephone.getText().toString(),email.getText().toString(),
                        password.getText().toString());
                if(rezultat)
                {
                    Toast.makeText(RegistrationActivity.this,"Successful registration",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else
                {
                    Toast.makeText(RegistrationActivity.this,"Problem with registration",Toast.LENGTH_LONG).show();
                }
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

    private boolean chechValidation(){
        boolean flag = true;

            if(name.getText().toString().isEmpty()) {
                name.setError("Name is mandatory!");
                flag = false;
            }
            if(surname.getText().toString().isEmpty()){
                surname.setError("Surname is mandatory!");
                flag = false;
            }
            if(telephone.getText().toString().isEmpty()){
                telephone.setError("Telephone is mandatory!");
                flag = false;
            }
            if(email.getText().toString().isEmpty()){
                email.setError("Email is mandatory!");
                flag = false;
            }
            if(!email.getText().toString().contains("@") && !email.getText().toString().isEmpty()){
                email.setError("Email is not valid!");
                flag = false;
            }
            if(password.getText().toString().isEmpty()){
                password.setError("Password is mandatory!");
                flag = false;
            }

        return flag;
    }


}
