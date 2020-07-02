package com.ftn.pma.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ftn.pma.R;
import com.ftn.pma.db.Car_db;
import com.ftn.pma.helper.FirebaseDatabaseHelper;
import com.ftn.pma.model.Car;
import com.ftn.pma.model.Reservation;
import com.ftn.pma.model.ShoppingCart;
import com.ftn.pma.model.User;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 1000;
    public static final int PERMISSION_CODE = 1001;
    ImageView imageView;
    EditText etBrand;
    EditText etModel;
    EditText etPrice;
    EditText etPower;
    EditText etHorsePower;
    EditText etTorque;
    EditText etMaxPower;
    EditText etWidth;
    EditText etLength;
    EditText etHeight;
    EditText etTransmission;
    RatingBar ratingBar;
    Button btnAddCar;
    ImageButton signOut;
    Uri uri = null;


    private static Uri alarmSound;
    private NotificationManager notificationManager;

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
        setContentView(R.layout.activity_admin);

        //inicijalizacija baze cars
        final Car_db car_db = new Car_db(this);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        //Edit Texts Admin add car
        etBrand = findViewById(R.id.et_adminCarBrand);
        etModel = findViewById(R.id.et_adminCarModel);
        etPrice = findViewById(R.id.et_adminCarPrice);
        etPower = findViewById(R.id.et_adminPower);
        etHorsePower = findViewById(R.id.et_adminHorsePower);
        etTorque = findViewById(R.id.et_adminTorque);
        etMaxPower = findViewById(R.id.et_adminMaxPower);
        etTransmission = findViewById(R.id.et_Transmission);
        etWidth = findViewById(R.id.et_adminWidth);
        etHeight = findViewById(R.id.et_adminHeight);
        etLength = findViewById(R.id.et_adminLenght);
        ratingBar = findViewById(R.id.rb_admin);
        signOut = findViewById(R.id.signOut);
        btnAddCar = findViewById(R.id.btn_adminAddCar);

        alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);



        imageView = findViewById(R.id.img_adminChoose);
        TextView tvChooseImage = findViewById(R.id.tv_chooseImage);

        btnAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    Car car = new Car();
                    car.setBrand(etBrand.getText().toString());
                    car.setModel(etModel.getText().toString());
                    car.setPrice(Double.parseDouble(etPrice.getText().toString()));
                    car.setPower(etPower.getText().toString());
                    car.setHorsePower(etHorsePower.getText().toString());
                    car.setTorque(etTorque.getText().toString());
                    car.setRevAtMaxPower(etMaxPower.getText().toString());
                    car.setTransmission(etTransmission.getText().toString());
                    car.setHeight(Double.parseDouble(etHeight.getText().toString()));
                    car.setWidth(Double.parseDouble(etWidth.getText().toString()));
                    car.setLength(Double.parseDouble(etLength.getText().toString()));
                    car.setRating(ratingBar.getRating());
//                    showNotification(1, car);

                    byte[] img = uriImageToByte(uri);
                    String base = Base64.encodeToString(img,Base64.DEFAULT);
                    car.setImageString(base);
                    car_db.insertDataCar(car, img);
                    new FirebaseDatabaseHelper("cars").addCar(car, new FirebaseDatabaseHelper.DataStatus() {
                        @Override
                        public void DataLoaded(List<Car> cars, List<String> keys) {

                        }

                        @Override
                        public void DataInserted() {
                            Toast.makeText(AdminActivity.this, "Car is successfully added!", Toast.LENGTH_LONG).show();
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

                        }

                        @Override
                        public void ShopingCart(List<ShoppingCart> shoppingCarts) {

                        }

                    });
                }
            }
        });


        tvChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                    String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permission,PERMISSION_CODE);
                }else{
                    pickImage();
                }
            }
        });

        hideSystemUIImperativeMode();

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void pickImage() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE: {
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImage();
                }else{
//                    Toast.makeText(this,"Permission denied!",Toast.LENGTH_SHORT).show();
                    pickImage();
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageView.setImageURI(data.getData());
            uri = data.getData();
        }
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

    private void showNotification(int id,Car car) {
        NotificationCompat.Builder notification = null;

        //zbog verzije androida. Koristio sam metodu za API26 a min je 24
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("ID", "Name", importance);
            notificationManager.createNotificationChannel(notificationChannel);
            notification = new NotificationCompat.Builder(getApplicationContext(), notificationChannel.getId());
        } else {
            notification = new NotificationCompat.Builder(getApplicationContext());
        }

        notification.setContentTitle("New Cars");
        notification.setLights(Color.YELLOW,2000,2000);
        notification.setContentText("Stigao je auto "+ car.getBrand() + " " + car.getModel());
        notification.setTicker("New Message Alert!");
        notification.setSmallIcon(R.drawable.ic_person_black_24dp);

//        if(id == 1)
//            notification.setSound(alarmSound);
//        else {
//            Vibrator v = (Vibrator) this.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
//            // Vibrate for 500 milliseconds
//            v.vibrate(500);
//
//        }
        notificationManager.notify(111,notification.build());
    }

    private byte[] uriImageToByte(Uri uri){
        byte[] data = null;
        try {
            ContentResolver cr = getBaseContext().getContentResolver();
            InputStream inputStream = cr.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //RESIZE SLIKE (BUDE LOSIJEG KVALITETA)
            bitmap = bitmap.createScaledBitmap(bitmap,450,450,false);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            data = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }

    private boolean validation(){
        boolean flag = true;
        if(etBrand.getText().toString().isEmpty()){
            etBrand.setError("Brand is mandatory!");
            flag = false;
        }
        if(etModel.getText().toString().isEmpty()){
            etModel.setError("Model is mandatory!");
            flag = false;
        }
        if(etPrice.getText().toString().isEmpty()){
            etPrice.setError("Price is mandatory!");
            flag = false;
        }
        if(etPower.getText().toString().isEmpty()){
            etPower.setError("Power is mandatory!");
            flag = false;
        }
        if(etHorsePower.getText().toString().isEmpty()){
            etHorsePower.setError("Horse Power is mandatory!");
            flag = false;
        }
        if(etTorque.getText().toString().isEmpty()){
            etTorque.setError("Torque is mandatory!");
            flag = false;
        }
        if(etMaxPower.getText().toString().isEmpty()){
            etMaxPower.setError("Field is mandatory!");
            flag = false;
        }
        if(etTransmission.getText().toString().isEmpty()){
            etTransmission.setText("Transmission is mandatory!");
            flag = false;
        }
        if(etWidth.getText().toString().isEmpty()){
            etWidth.setError("Width is mandatory!");
            flag = false;
        }
        if(etHeight.getText().toString().isEmpty()){
            etHeight.setError("Height is mandatory!");
            flag = false;
        }
        if(etLength.getText().toString().isEmpty()){
            etLength.setError("Length is mandatory!");
            flag = false;
        }
       return flag;
    }



}
