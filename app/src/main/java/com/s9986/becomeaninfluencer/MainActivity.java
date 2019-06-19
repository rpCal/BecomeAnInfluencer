package com.s9986.becomeaninfluencer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.firebase.ui.auth.viewmodel.AuthViewModelBase;
import com.google.firebase.auth.FirebaseAuth;
import com.s9986.becomeaninfluencer.game.DetailViewModel;
import com.s9986.becomeaninfluencer.game.GameActivity;
import com.s9986.becomeaninfluencer.persistence.GameData;

import java.util.List;

public class MainActivity extends AppCompatActivity {

//    private DetailViewModel model;
    Button gotoLoginScreen;
    Button gotoNewItemScreen;
    Button gotoItemsList;
    Button showRankingButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareViews();
        addEventListeners();
        addObservables();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void prepareViews() {
        setContentView(R.layout.main_activity);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.becomeaninfluencer);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        gotoNewItemScreen = findViewById(R.id.go_to_new_shop_activity);
        gotoLoginScreen = findViewById(R.id.go_to_new_game_activity_button);
        gotoItemsList = findViewById(R.id.go_to_old_game_activity_button);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mAuth.getCurrentUser()==null){
                    Intent loginIntent = new Intent(MainActivity.this, EmailPasswordActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }
            }
        };
    }

    private void addEventListeners() {

        gotoLoginScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ShopMapActivity.class);
                startActivity(i);
            }
        });

        gotoItemsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MainUserListActivity.class);
                startActivity(i);
            }
        });

        gotoNewItemScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddAndEditShopActivity.class);
                startActivity(i);
            }
        });


    }

    private void addObservables() {

//        model.fetchGameInProgress().observe(this, new Observer<GameData>() {
//            @Override
//            public void onChanged(@Nullable GameData gameData) {
//                if (gameData != null) {
//                    startOldGameButton.setVisibility(View.VISIBLE);
//                    startOldGameButton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent i = new Intent(MainActivity.this, GameActivity.class);
//                            startActivity(i);
//                        }
//                    });
//                } else {
//                    startOldGameButton.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        model.fetchTop10RankingGames().observe(this, new Observer<List<GameData>>() {
//            @Override
//            public void onChanged(@Nullable List<GameData> games) {
//                if (games.size() > 0) {
//                    showRankingButton.setVisibility(View.VISIBLE);
//                    showRankingButton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent i = new Intent(MainActivity.this, RankingActivity.class);
//                            startActivity(i);
//                        }
//                    });
//                    showRankingButton.setAlpha(1.0f);
//                } else {
//                    showRankingButton.setVisibility(View.VISIBLE);
//                    showRankingButton.setAlpha(0.5f);
//                }
//            }
//        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;
        }
        return false;
    }

//    private void testAddRankingGameData(){
//
//
//        GameData gameData = new GameData();
//        gameData.setIn_progress(0);
//        gameData.setGame_finished(1);
//        gameData.setTotal_time_in_secound(100);
//        gameData.setTotal_spend(0);
//        gameData.setTotal_taps(100);
//        gameData.setTotal_points_sum(1000);
//        gameData.setTotal_points(1000);
//        gameData.setPoints_per_second(0.3);
//        GameDataRepository gameDataRepository = new GameDataRepository(getApplicationContext());
//        gameDataRepository.insertGameData(gameData);
//    }
}
