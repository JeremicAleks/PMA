package com.ftn.pma.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        });
    }

}
