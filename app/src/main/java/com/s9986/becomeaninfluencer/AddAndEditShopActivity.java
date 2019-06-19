package com.s9986.becomeaninfluencer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.s9986.becomeaninfluencer.models.Shop;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddAndEditShopActivity extends AppCompatActivity {
    private static final String TAG = "AddAndEditShopActivity";
    private EditText shopNameEditText;
    private RadioGroup shopTypeEditText;
    private SeekBar seekBar;
    private TextView shopRadiusText;
    private LocationManager locationManager;
    private EditText shopGeoLong;
    private EditText shopGeoLat;
    private Button resetForm;
    private Button getGeoBtn;
    private Button saveBtn;

    private String paramItemId;
    private String paramUserId;
    private boolean isEditingItem = false;

    private Button btnChoose, btnUpload;
    private ImageView imageView;

    private Uri filePath;
    private String fileRef;

    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_and_edit_shop_activity);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.becomeaninfluencer);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        saveBtn = findViewById(R.id.saveButton);
        getGeoBtn = findViewById(R.id.pobierzGeolokalizacje);
        seekBar = findViewById(R.id.shopRadius);
        shopRadiusText = findViewById(R.id.shopRadiusText);
        resetForm = findViewById(R.id.resetForm);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        shopNameEditText = findViewById(R.id.shopName);
        shopTypeEditText = findViewById(R.id.shopType);
        shopGeoLong = findViewById(R.id.shopSetGeoLong);
        shopGeoLat = findViewById(R.id.shopSetGeoLat);

        btnChoose = (Button) findViewById(R.id.btnChoose);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        imageView = (ImageView) findViewById(R.id.imgView);


        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        getGeoBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                toggleGPSUpdates(v);
            }
        });

        resetForm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                resetForm();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBaraa, int progress, boolean fromUser) {
                shopRadiusText.setText(getRadiusText(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        shopRadiusText.setText(getRadiusText(seekBar.getProgress()));

        Bundle b = getIntent().getExtras();

        if(b != null){
            paramItemId = b.getString("itemId");
            paramUserId = b.getString("userId");

            if(paramItemId != null &&
                    paramItemId.length() > 0 &&
                    paramUserId != null &&
                    paramUserId.length() > 0)
            {
                isEditingItem = true;
            }else{
                isEditingItem = false;
            }
        }else{
            isEditingItem = false;
        }



        if(isEditingItem){
            resetForm();
            handleLoadItem();
        }else{
            saveBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    addNewItem();
                }
            });
        }
    }

    private String getRadiusText(int value){
        return "Promien sklepu to " + String.valueOf(value) + " metrow";
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
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

    private void updateItem(){
        String name = shopNameEditText.getText().toString();
        int selectedId = shopTypeEditText.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedId);
        String type = radioButton.getText().toString();
        String radius = String.valueOf(seekBar.getProgress());
        String longitude = shopGeoLong.getText().toString();
        String latitude = shopGeoLat.getText().toString();
        String myUserId = getUid();
        Log.d(TAG, "Zaktualizuje wartosci. " + "userid: " + paramUserId + ", itemId: " + paramItemId + " name:" + name + ", type" + type + ", radius: " + radius);
        updateShop(paramUserId, paramItemId, name, type, radius, longitude, latitude, fileRef);
    }

    private void addNewItem(){
        String name = shopNameEditText.getText().toString();
        int selectedId = shopTypeEditText.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedId);
        String type = radioButton.getText().toString();
        String radius = String.valueOf(seekBar.getProgress());
        String longitude = shopGeoLong.getText().toString();
        String latitude = shopGeoLat.getText().toString();
        String myUserId = getUid();
        Log.d(TAG, "Dodaje nowy sklep. " + "userid: " + myUserId + ", name:" + name + ", type" + type + ", radius: " + radius);
        saveNewShop(myUserId, name, type, radius, longitude, latitude, fileRef);
    }


    private void handleLoadItem(){
        FirebaseDatabase
        .getInstance()
        .getReference()
        .child("user-shops")
        .child(paramUserId)
        .child(paramItemId)
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(getApplicationContext(),"Odczytano wpis",Toast.LENGTH_SHORT).show();
                Shop shop = dataSnapshot.getValue(Shop.class);
                if(shop == null){
                    Toast.makeText(getApplicationContext(),"Niestety nie udalo sie pobrac wpisu. Blad aplikacji.",Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(AddAndEditShopActivity.this, MainUserListActivity.class);
                    startActivity(loginIntent);
                    return;
                }
                handleEditForm(shop);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Niestety nie udalo sie pobrac wpisu. Blad aplikacji.",Toast.LENGTH_SHORT).show();
                Intent loginIntent = new Intent(AddAndEditShopActivity.this, MainUserListActivity.class);
                startActivity(loginIntent);
            }
        });
    }

    private void handleEditForm(Shop shop){

        if(shop.type.equals("Gastronomia")){
            RadioButton b = (RadioButton) findViewById(R.id.radio0);
            b.setChecked(true);
        }
        if(shop.type.equals("Ubrania")){
            RadioButton b = (RadioButton) findViewById(R.id.radio1);
            b.setChecked(true);
        }
        if(shop.type.equals("Pub")){
            RadioButton b = (RadioButton) findViewById(R.id.radio2);
            b.setChecked(true);
        }
        if(shop.type.equals("RTV")){
            RadioButton b = (RadioButton) findViewById(R.id.radio3);
            b.setChecked(true);
        }
        if(shop.hasImage()){
            fileRef = shop.imageRef;
            loadImageFromFirebase(shop.imageRef);
        }

        saveBtn.setText("Zaktualizuj sklep");
        shopNameEditText.setText(shop.name);
        seekBar.setProgress(Integer.valueOf(shop.radius));
        shopGeoLong.setText(shop.longitude);
        shopGeoLat.setText(shop.latitude);
        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateItem();
            }
        });
    }

    private void resetForm(){
        filePath = null;
        fileRef = null;
        imageView.setImageResource(0);
        shopGeoLong.setText("");
        shopNameEditText.setText("");
        shopGeoLat.setText("");
        seekBar.setProgress(500);
        RadioButton b = (RadioButton) findViewById(R.id.radio0);
        b.setChecked(true);
    }


    private void loadImageFromFirebase(String link){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference = storageReference.child(link);
        photoReference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getApplicationContext()).load(uri.toString()).into(imageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "Nie udalo sie pobrac obrazka z sciezki bo " + exception.getMessage());
                fileRef = null;
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Wybierz obrazek"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Wgrywam plik...");
            progressDialog.show();

            String newRef = "images/"+ UUID.randomUUID().toString();

            StorageReference ref = storageReference.child(newRef);
            Log.d(TAG,  ref.getName() + ", " + ref.getPath() + "," + ref.getBucket());
            UploadTask uploadTask = ref.putFile(filePath);

            Log.d(TAG, "WRZUCAM plik" + ref.getPath());
            fileRef = ref.getPath();

            uploadTask
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AddAndEditShopActivity.this, "Wrzucono plik", Toast.LENGTH_SHORT).show();

                            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                            StorageReference photoReference = storageReference.child(fileRef);
                            photoReference.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Glide.with(getApplicationContext()).load(uri.toString()).into(imageView);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Log.d(TAG, "Nie udalo sie pobrac obrazka z sciezki bo " + exception.getMessage());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddAndEditShopActivity.this, "Nie udalo sie zapisac pliku: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Postęp wrzucania pliku: "+(int)progress+"%");
                        }
                    });
        }
    }


    private void saveNewShop(String userId, String name, String type, String radius, String longitude, String latitude, String imageRef) {
        String key = FirebaseDatabase.getInstance().getReference().child("user-shops").push().getKey();
        Shop shop = new Shop(userId, name, type, radius, longitude, latitude, imageRef);
        Map<String, Object> shopValues = shop.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/user-shops/" + userId + "/" + key, shopValues);
        FirebaseDatabase
        .getInstance()
        .getReference()
        .updateChildren(childUpdates)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Dodano nowy sklep",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateShop(String userId, String itemId, String name, String type, String radius, String longitude, String latitude, String imageRef) {
        Shop shop = new Shop(userId, name, type, radius, longitude, latitude, imageRef);
        Map<String, Object> shopValues = shop.toMap();

        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("user-shops")
                .child(userId)
                .child(itemId)
                .getRef()
                .updateChildren(shopValues)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Zapisano zmiany...",Toast.LENGTH_SHORT).show();
                        Intent loginIntent = new Intent(AddAndEditShopActivity.this, MainUserListActivity.class);
                        startActivity(loginIntent);
                    }
                });

    }



    public String getUid() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent loginIntent = new Intent(AddAndEditShopActivity.this, SignInActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
            return null;
        }
        else{
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
    }


    public void toggleGPSUpdates(View view) {
        if(!isLocationEnabled()){
            return;
        }
        LocationListener locationListener = new AddAndEditShopActivity.AppLocationListener();
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    private class AppLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            double longitude = loc.getLongitude();
            double latitude = loc.getLatitude();
            String text = "Zmieniono lokalizacje na Lat: " + latitude + " Lng: " + longitude;
            Toast.makeText(getBaseContext(),text, Toast.LENGTH_SHORT).show();
            shopGeoLong.setText(String.valueOf(longitude));
            shopGeoLat.setText(String.valueOf(latitude));
        }


        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

}
