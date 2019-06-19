package com.s9986.becomeaninfluencer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.s9986.becomeaninfluencer.models.Shop;

import java.text.DecimalFormat;

public class MainUserListActivity extends BaseActivity {

    private static final String TAG = "MainUserListActivity";

    private FirebaseRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user_list);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.becomeaninfluencer);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        String myUserId = getUid();
        Query myTopPostsQuery = FirebaseDatabase.getInstance().getReference().child("user-shops").child(myUserId)
                .orderByChild("name");


        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Shop>()
                .setQuery(myTopPostsQuery, Shop.class)
                .build();

        adapter = new ShopsAdapter(options);

        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mAuth.getCurrentUser()==null){
                    Intent loginIntent = new Intent(MainUserListActivity.this, SignInActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }}
        };

    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
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



    private class ShopsAdapter extends FirebaseRecyclerAdapter<Shop, ShopViewHolder> {

        public ShopsAdapter(@NonNull FirebaseRecyclerOptions<Shop> options) {
            super(options);
        }

        @Override
        public ShopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_items, parent, false);
            return new ShopViewHolder(view);
        }

        @Override
        protected void onBindViewHolder(ShopViewHolder holder, int position, Shop model) {
            Log.d(TAG, "onBindViewHolder - Przekazuje parametry:" + model.name + " --- "+adapter.getRef(position).getKey());
            holder.setName(model.name);
            holder.setType(model.type);
            holder.setRadius(model.radius);
            holder.setGeo(model.latitude, model.longitude);
            holder.onClickDelete(adapter.getRef(position).getKey(), model.uid);
            holder.onClickEdit(adapter.getRef(position).getKey(), model.uid);
            if(model.hasImage()){
                holder.setImage(model.imageRef);
            }
        }
    }


    private class ShopViewHolder extends RecyclerView.ViewHolder{

        View mView;
        ImageView imageView;
        final long ONE_MEGABYTE = 1024 * 1024;
        DecimalFormat df2 = new DecimalFormat("#.#####");
        public ShopViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            imageView = mView.findViewById(R.id.post_image);
        }
        public void setName(String value){
            TextView item = mView.findViewById(R.id.post_title);
            item.setText("Nazwa sklepu: " +  value);
        }
        public void setType(String value){
            TextView item = mView.findViewById(R.id.post_type);
            item.setText("Rodzaj sklepu: " + value);
        }
        public void setGeo(String lt, String ln){
            TextView item = mView.findViewById(R.id.post_geo);
            if((lt == null || lt.length() == 0) || (ln == null || ln.length() == 0)){
                item.setVisibility(View.GONE);
            }else{
                String one = df2.format(Double.parseDouble(lt));
                String two = df2.format(Double.parseDouble(ln));
                String text = "Geolokalizacja: " + one + ", "  + two;
                item.setText(text);
            }
        }
        public void setRadius(String value){
            TextView item = mView.findViewById(R.id.post_radius);
            item.setText("Promien: " + value + "m");
        }
        public void onClickDelete(String id, String userId){
            TextView item = mView.findViewById(R.id.postDelete);
            item.setOnClickListener(new DeleteButtonListener(id, userId));
        }
        public void onClickEdit(String id, String userId){
            TextView item = mView.findViewById(R.id.postEdit);
            item.setOnClickListener(new EditButtonListener(id, userId));
        }

        public void setImage(String value){
            Log.d(TAG, "pobieram image z sciezki: " + value);
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference photoReference = storageReference.child(value);
            photoReference.getDownloadUrl()
            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getApplicationContext()).load(uri.toString()).into(imageView);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d(TAG, "Nie udalo sie pobrac obrazka z sciezki bo " + exception.getMessage());
                }
            });
        }
    }


    public class DeleteButtonListener implements View.OnClickListener
    {

        String itemId;
        String userId;
        public DeleteButtonListener(String id, String userId) {
            this.itemId = id;
            this.userId = userId;
        }

        @Override
        public void onClick(View v)
        {
            Log.d(TAG, "USUWAM " + itemId);
            FirebaseDatabase.getInstance().getReference()
                    .child("shops").child(itemId).removeValue();
            FirebaseDatabase.getInstance().getReference()
                    .child("user-shops").child(userId).child(itemId).removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"Usunięto sklep",Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "USUNIETO!!");
                            adapter.notifyDataSetChanged();
                        }
                    });
        }
    };

    public class EditButtonListener implements View.OnClickListener
    {

        String itemId;
        String userId;
        public EditButtonListener(String id, String userId) {
            this.itemId = id;
            this.userId = userId;
        }

        @Override
        public void onClick(View v)
        {
            Log.d(TAG, "Edytuje " + ". itemId : "+ itemId + ", userId: " + userId);
            Intent i = new Intent(MainUserListActivity.this, AddAndEditShopActivity.class);
            i.putExtra("itemId", itemId);
            i.putExtra("userId", userId);
            startActivity(i);
        }
    };


    public String getUid() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent loginIntent = new Intent(MainUserListActivity.this, SignInActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
            return null;
        }
        else{
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
    }
}

