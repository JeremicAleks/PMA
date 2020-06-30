package com.ftn.pma.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.ftn.pma.R;
import com.ftn.pma.db.User_db;
import com.ftn.pma.model.User;
import com.ftn.pma.view.AboutActivity;
import com.ftn.pma.view.BuyCarsActivity;
import com.ftn.pma.view.ServiceActivity;
import com.ftn.pma.view.UserProfileActivity;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private HomeViewModel homeViewModel;
    private User_db user_db;
    private User user;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ImageButton btnAbout = root.findViewById(R.id.img_btn_about);

        user = (User) getActivity().getIntent().getSerializableExtra("user");

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        ImageButton imageButtonService = root.findViewById(R.id.img_btn_service);

        imageButtonService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ServiceActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        ImageButton imageButtonUserProfile = root.findViewById(R.id.img_btn_profile);
        imageButtonUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserProfileActivity.class);
                intent.putExtra("user",user);
                startActivityForResult(intent,1);
                getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        ImageButton imageButtonBuyCars = root.findViewById(R.id.img_btn_buy_car);
        imageButtonBuyCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BuyCarsActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        return root;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
//        user = (User) data.getSerializableExtra("user");
    }
}
