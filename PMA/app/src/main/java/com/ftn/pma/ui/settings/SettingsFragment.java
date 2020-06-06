package com.ftn.pma.ui.settings;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ftn.pma.R;
import com.ftn.pma.globals.Constants;
import com.ftn.pma.model.User;
import com.ftn.pma.ui.home.HomeFragment;
import com.ftn.pma.view.HomeActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import static android.content.Context.AUDIO_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private Switch location;
    FusedLocationProviderClient fusedLocationProviderClient;
    private LocationManager manager;
    private RadioButton dark,light;
    private CheckBox cb_sound;
    private CheckBox cb_vibrate;
    private static Uri alarmSound;
    private final long[] pattern = {100,500,500,100};
    private NotificationManager notificationManager;
    private  AudioManager audioManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);

        location = root.findViewById(R.id.switch_geo);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        checkGPS();

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent1,100);
                }else
                {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                    Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent1);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.contentContainer, new SettingsFragment());
                    ft.addToBackStack(null);
                    ft.commit();
                }
                checkGPS();
            }
        });
        //dark od light BAR
        dark = root.findViewById(R.id.rb_dark);
        light = root.findViewById(R.id.rb_light);

        settingsViewModel.getDark().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                dark.setChecked(aBoolean);
            }
        });
        settingsViewModel.getLight().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                light.setChecked(aBoolean);
            }
        });


        dark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                restartApp();
            }
        });

        light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                restartApp();
            }
        });

        //sound za notifikaciju
        cb_sound = root.findViewById(R.id.cb_sound);
        cb_vibrate = root.findViewById(R.id.cb_vibration);

        audioManager = (AudioManager) getActivity().getApplicationContext().getSystemService(AUDIO_SERVICE);
        alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        cb_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_sound.isChecked())
                {
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    cb_vibrate.setChecked(false);
                }
            }
        });
        cb_vibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_vibrate.isChecked()) {
                    cb_sound.setChecked(false);
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                }
            }
        });
        return root;
    }

//    private void showNotification(int id) {
//        NotificationCompat.Builder notification = null;
//
//            //zbog verzije androida. Koristio sam metodu za API26 a min je 24
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                int importance = NotificationManager.IMPORTANCE_DEFAULT;
//                NotificationChannel notificationChannel = new NotificationChannel("ID", "Name", importance);
//                notificationManager.createNotificationChannel(notificationChannel);
//                notification = new NotificationCompat.Builder(getActivity().getApplicationContext(), notificationChannel.getId());
//            } else {
//                notification = new NotificationCompat.Builder(getActivity().getApplicationContext());
//            }
//
//        notification.setContentTitle("New Cars");
//        notification.setLights(Color.YELLOW,2000,2000);
//        notification.setContentText("STIGO JE NOVI AUTO");
//        notification.setTicker("New Message Alert!");
//        notification.setSmallIcon(R.drawable.ic_person_black_24dp);
//
//        if(id == 1)
//            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//        else {
//              audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
//        }
//        notificationManager.notify(111,notification.build());
//    }

    private void restartApp() {
        Intent i = new Intent(getActivity().getApplicationContext(), HomeActivity.class);
        User user = (User) getActivity().getIntent().getSerializableExtra("user");
        i.putExtra("user",user);
        startActivity(i);
        getActivity().finish();
    }

    public void checkGPS()
    {
        manager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        boolean GpsStatus = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!GpsStatus)
        {
            location.setChecked(false);

        }else {
            location.setChecked(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Reload current fragment
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.detach(SettingsFragment.this);
        ft.replace(R.id.contentContainer, new SettingsFragment());
        ft.addToBackStack(null);
        ft.commit();
    }


}
