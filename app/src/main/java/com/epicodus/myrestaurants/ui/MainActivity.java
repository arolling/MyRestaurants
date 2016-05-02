package com.epicodus.myrestaurants.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.epicodus.myrestaurants.Constants;
import com.epicodus.myrestaurants.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    private ValueEventListener mSearchedLocationRefListener;
    private Firebase mSearchedLocationRef;
    @Bind(R.id.findRestaurantsButton) Button mFindRestaurantsButton;
    @Bind(R.id.locationEditText) EditText mLocationEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSearchedLocationRef = new Firebase(Constants.FIREBASE_URL_SEARCHED_LOCATION);

        mFindRestaurantsButton.setOnClickListener(this);

        mSearchedLocationRefListener = mSearchedLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String locations = dataSnapshot.getValue().toString();
                Log.d("location updated", locations);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mSearchedLocationRef.removeEventListener(mSearchedLocationRefListener);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.findRestaurantsButton:
                String location = mLocationEditText.getText().toString();
                saveLocationToFirebase(location);

                // if(!(location).equals("")) {
                //       addToSharedPreferences(location);
                //   }


                Intent intent = new Intent(MainActivity.this, RestaurantListActivity.class);
                intent.putExtra("location", location);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    public void saveLocationToFirebase(String location){
        Firebase searchedLocationRef = new Firebase(Constants.FIREBASE_URL_SEARCHED_LOCATION);
        searchedLocationRef.push().setValue(location);
    }

    // private void addToSharedPreferences(String location) {
    //    mEditor.putString(Constants.PREFERENCES_LOCATION_KEY, location).apply();
    //  }
}
