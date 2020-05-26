package com.ftn.pma.ui.settings;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel {

    private MutableLiveData<Boolean> mRadioButtonDark;
    private MutableLiveData<Boolean> mRadioButtonLight;
    private MutableLiveData<Boolean> mCheckBooxLocation;

    public SettingsViewModel() {
            mRadioButtonDark = new MutableLiveData<>();
            mRadioButtonLight = new MutableLiveData<>();
            mCheckBooxLocation = new MutableLiveData<>();

            if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            mRadioButtonDark.setValue(true);
            mRadioButtonLight.setValue(false);
            }else {
            mRadioButtonLight.setValue(true);
            mRadioButtonDark.setValue(false);
            }
    }

    public LiveData<Boolean> getLight() {
        return mRadioButtonLight;
    }
    public LiveData<Boolean> getDark() {
        return mRadioButtonDark;
    }

    public MutableLiveData<Boolean> getLocation() {
        return mCheckBooxLocation;
    }
}