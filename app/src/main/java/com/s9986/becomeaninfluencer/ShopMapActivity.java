package com.s9986.becomeaninfluencer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.s9986.becomeaninfluencer.models.Shop;

public class ShopMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.becomeaninfluencer);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        FirebaseDatabase.getInstance().getReference()
        .child("user-shops").child(getUid())
        .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Shop shop = snapshot.getValue(Shop.class);

                    if(shop.hasLongitude() && shop.hasLatitude()){
                        String title = "Sklep: " + shop.name;
                        LatLng position = new LatLng(shop.getLatitude(), shop.getLongitude());

                        CircleOptions circleOptions = new CircleOptions()
                                .center(position)
                                .radius(shop.getRadius())
                                .fillColor(0x44ff0000)
                                .strokeColor(0xffff0000)
                                .strokeWidth(2);

                        MarkerOptions marker = new MarkerOptions()
                                .position(position)
                                .title(title);

                        mMap.addCircle(circleOptions);
                        mMap.addMarker(marker);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    public String getUid() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent loginIntent = new Intent(ShopMapActivity.this, SignInActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
            return null;
        }
        else{
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, AuthFirebaseActivity.class));
            finish();
            return true;
        } else if (i == R.id.action_add) {
            startActivity(new Intent(this, AddAndEditShopActivity.class));
            finish();
            return true;
        } else if (i == R.id.action_list) {
            startActivity(new Intent(this, MainUserListActivity.class));
            finish();
            return true;
        } else if (i == R.id.action_map) {
            startActivity(new Intent(this, ShopMapActivity.class));
            finish();
            return true;
        } else{
            return super.onOptionsItemSelected(item);
        }
    }

}
