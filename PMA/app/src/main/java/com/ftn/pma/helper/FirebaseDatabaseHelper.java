package com.ftn.pma.helper;

import android.util.Base64;

import androidx.annotation.NonNull;

import com.ftn.pma.model.Car;
import com.ftn.pma.model.Reservation;
import com.ftn.pma.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public interface DataStatus{
        void DataLoaded(List<Car> cars, List<String> keys);
        void DataInserted();
        void DataUpdated();
        void DataIsDeleted();
        void UserIsAdded();
        void UserLogin(List<User> users);
        void ReservationAdd();
        void ReservationRead(List<Reservation> reservations);
    }


    public FirebaseDatabaseHelper(String base) {
        this.firebaseDatabase = firebaseDatabase.getInstance();
        this.databaseReference = firebaseDatabase.getReference(base);
    }

    public void addUser(User user,final DataStatus dataStatus){
        String key = databaseReference.push().getKey();
        databaseReference.child(key).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.UserIsAdded();
            }
        });
    }

    public void readUser(final DataStatus dataStatus){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> users = new ArrayList<>();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyShot : snapshot.getChildren()){
                    keys.add(keyShot.getKey());
                    User user = keyShot.getValue(User.class);
                    users.add(user);
                }
                dataStatus.UserLogin(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void readCars(final DataStatus dataStatus){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Car> cars = new ArrayList<>();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyShot : snapshot.getChildren()){
                    keys.add(keyShot.getKey());
                    Car car = keyShot.getValue(Car.class);
                    car.setImage(Base64.decode(car.getImageString(),Base64.DEFAULT));
                    cars.add(car);
                }
                dataStatus.DataLoaded(cars,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public  void addCar(Car car, final DataStatus dataStatus){
        String key = databaseReference.push().getKey();
        databaseReference.child(key).setValue(car).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataInserted();
            }
        });
    }

    public void addReservation(Reservation reservation,final DataStatus dataStatus){
        String key = databaseReference.push().getKey();
        databaseReference.child(key).setValue(reservation).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.ReservationAdd();
            }
        });
    }

    public void readReservation(final DataStatus dataStatus){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Reservation> reservations = new ArrayList<>();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyShot : snapshot.getChildren()){
                    keys.add(keyShot.getKey());
                    Reservation reservation = keyShot.getValue(Reservation.class);
                    reservations.add(reservation);
                }
                dataStatus.ReservationRead(reservations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
