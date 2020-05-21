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
import com.ftn.pma.model.User;
import com.ftn.pma.ui.home.HomeFragment;
import com.ftn.pma.view.HomeActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        location = root.findViewById(R.id.switch_geo);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        checkGPS();

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent1);
                    // Reload current fragment

                    List<Fragment> f = getActivity().getSupportFragmentManager().getFragments();

//                    for(Fragment fr : f)
//                    {
//                        System.out.println("NAZIV " +fr.getId());
//                        System.out.println("Ima nesto " +fr.getId());
//                    }
//                    SettingsFragment frg = (SettingsFragment) getActivity().getSupportFragmentManager().findFragmentByTag("settings");

                    final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.detach(f.get(0));
                    ft.attach(f.get(0));
                    ft.commit();
                }else
                {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                    Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent1);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(container.getId(), new SettingsFragment()).commit();
                }
                checkGPS();
            }
        });
        //dark od light BAR
        dark = root.findViewById(R.id.rb_dark);
        light = root.findViewById(R.id.rb_light);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
        {
            dark.setChecked(true);
        }else
            light.setChecked(true);
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

        alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        cb_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_sound.isChecked())
                     showNotification(1);
            }
        });
        cb_vibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_vibrate.isChecked())
                    showNotification(2);
            }
        });
        return root;
    }

    private void showNotification(int id) {
        NotificationCompat.Builder notification = null;

            //zbog verzije androida. Koristio sam metodu za API26 a min je 24
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel notificationChannel = new NotificationChannel("ID", "Name", importance);
                notificationManager.createNotificationChannel(notificationChannel);
                notification = new NotificationCompat.Builder(getActivity().getApplicationContext(), notificationChannel.getId());
            } else {
                notification = new NotificationCompat.Builder(getActivity().getApplicationContext());
            }

        notification.setContentTitle("New Cars");
        notification.setLights(Color.YELLOW,2000,2000);
        notification.setContentText("STIGO JE NOVI AUTO");
        notification.setTicker("New Message Alert!");
        notification.setSmallIcon(R.drawable.ic_person_black_24dp);

        if(id == 1)
            notification.setSound(alarmSound);
        else {
            Vibrator v = (Vibrator) this.getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            v.vibrate(500);

        }
        notificationManager.notify(111,notification.build());
    }

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
        System.out.println("Status GPS: " +GpsStatus);
        if(!GpsStatus)
        {
            System.out.println("ISKLJUCEN");
            location.setChecked(false);

        }else {
            location.setChecked(true);
            System.out.println("UKLJUCEN");
        }
    }

    public void finish()
    {
//        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }


}
