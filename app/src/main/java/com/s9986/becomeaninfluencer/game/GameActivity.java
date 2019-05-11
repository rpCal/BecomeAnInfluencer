package com.s9986.becomeaninfluencer.game;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.s9986.becomeaninfluencer.MainActivity;
import com.s9986.becomeaninfluencer.R;
import com.s9986.becomeaninfluencer.RankingActivity;
import com.s9986.becomeaninfluencer.game.fragments.ArchivementsFragment;
import com.s9986.becomeaninfluencer.game.fragments.PlayFragment;
import com.s9986.becomeaninfluencer.game.fragments.UpgradesFragment;
import com.s9986.becomeaninfluencer.persistence.GameData;
import com.s9986.becomeaninfluencer.persistence.GameDataConstant;
import com.s9986.becomeaninfluencer.persistence.GameDataRepository;

public class GameActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, LifecycleOwner {

    private Handler mHandler = null;
    private Runnable runnable = null;
    private DetailViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareViews();
        loadFragment(new PlayFragment());
        gameTimer();
    }

    private void prepareViews(){
        getSupportActionBar().hide();
        setContentView(R.layout.game_activity);

        BottomNavigationView navigation = findViewById(R.id.game_bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        model = ViewModelProviders.of(this).get(DetailViewModel.class);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.game_bottom_navigation_play: {
                fragment = new PlayFragment();
                break;
            }
            case R.id.game_bottom_navigation_archivements: {
                fragment = new ArchivementsFragment();
                break;
            }
            case R.id.game_bottom_navigation_upgrades: {
                fragment = new UpgradesFragment();
                break;
            }
            case R.id.game_bottom_navigation_back: {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            }
        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_play, fragment).commit();

            return true;
        }
        return false;
    }


    private void gameTimer(){
        clearGameTick();
        mHandler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            GameData gameData = model.getGameInProgress();
                            if(gameData != null){
                                gameData.clockTick();
                                model.updateGameData(gameData);
                                if(gameData.getGame_finished() == GameDataConstant.GAME_FINISHED_VALUE){
                                    Intent i = new Intent(GameActivity.this, RankingActivity.class);
                                    startActivity(i);
                                    clearGameTick();
                                    model.finishGame();
                                }
//                                Log.d("GameActivity", gameData.toString());
                            }
                            Log.d("GameActivity", "gameData.clockTick ");
                            return null;
                        }
                    }.execute();

                    if(mHandler != null){
                        mHandler.postDelayed(this, 1000);
                    }
                }
            }
        };
        mHandler.post(runnable);
    }

    private void clearGameTick(){
        if(mHandler != null && runnable != null) {
            mHandler.removeCallbacks(runnable);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        clearGameTick();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearGameTick();
    }

    @Override
    public void onStart() {
        super.onStart();
        gameTimer();
    }

    @Override
    public void onBackPressed() {
        clearGameTick();
        Intent i = new Intent(GameActivity.this, MainActivity.class);
        startActivity(i);
    }

}
