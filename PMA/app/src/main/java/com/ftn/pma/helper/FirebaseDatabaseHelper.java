package com.ftn.pma.helper;

import android.provider.ContactsContract;
import android.util.Base64;

import androidx.annotation.NonNull;

import com.ftn.pma.model.Car;
import com.ftn.pma.model.Reservation;
import com.ftn.pma.model.ShoppingCart;
import com.ftn.pma.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
        void UserLogin(User user);
        void ReservationAdd();
        void ReservationRead(List<Reservation> reservations);
        void ReservationUser(List<Reservation> reservations);
        void ShopingCart(List<ShoppingCart> shoppingCarts);
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

    public void userIsLogin(String email, final String pass, final DataStatus dataStatus){
        Query query = databaseReference.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot keyShot : snapshot.getChildren()){
                    User user = keyShot.getValue(User.class);
                    if(user.getPassword().equals(pass)){
                        user.setKey(keyShot.getKey());
                        dataStatus.UserLogin(user);
                        break;
                    }
                }
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

    public void addShopping(ShoppingCart shoppingCart, final DataStatus dataStatus){
        String key = databaseReference.push().getKey();
        databaseReference.child(key).setValue(shoppingCart).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataInserted();
            }
        });
    }

    public void readShoppingCart(final DataStatus dataStatus){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<ShoppingCart> shoppingCarts = new ArrayList<>();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyShot : snapshot.getChildren()){
                    keys.add(keyShot.getKey());
                    ShoppingCart shoppingCart = keyShot.getValue(ShoppingCart.class);
                    shoppingCarts.add(shoppingCart);
                }
                dataStatus.ShopingCart(shoppingCarts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    public void readReservationOfUser(int userID, final DataStatus dataStatus){
        Query query = databaseReference.orderByChild("userId").equalTo(userID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Reservation> reservations = new ArrayList<>();
                snapshot.getChildren();
                for (DataSnapshot keyShot : snapshot.getChildren()) {
                    Reservation reservation = keyShot.getValue(Reservation.class);
                    reservations.add(reservation);
                }
                dataStatus.ReservationUser(reservations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void editUser(final User user, final DataStatus dataStatus){
        databaseReference.child(user.getKey()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataUpdated();
            }
        });
    }

}
