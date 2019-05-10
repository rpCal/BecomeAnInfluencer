package com.s9986.becomeaninfluencer.game;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.s9986.becomeaninfluencer.MainActivity;
import com.s9986.becomeaninfluencer.R;
import com.s9986.becomeaninfluencer.game.fragments.play.PlayFragment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class GameActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    GameData data;

    private String fileName = "data.txt";

    private boolean flagThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.game_activity);

        flagThread = true;

        BottomNavigationView navigation = findViewById(R.id.game_bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        loadJson();

        loadFragment(new PlayFragment());

        Bundle bundle = new Bundle();
        bundle.putString("edttext", "From Activity");


        PlayFragment fragment = new PlayFragment();
        fragment.setArguments(bundle);
    }

    private void threads() {
        //<editor-fold desc="piggyRunnable">
        Runnable piggyRunnable = new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    while (flagThread) {
                        long coinsValue = data.getCoinsValue();

                        if (coinsValue < data.getMaxCoinsValue()) {
                            data.setCoinsValue(coinsValue
                                    + data.getGreedyPiggyNum()
                                    + 10 * data.getLittleTommyNum()
                                    + 50 * data.getBusinessPackNum()
                                    + 200 * data.getSlyMarioNum());
                            try {
                                wait(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else
                            data.setCoinsValue(data.getMaxCoinsValue());

                    }
                }
            }
        };
        //</editor-fold>

        Thread piggyThread = new Thread(piggyRunnable);
        piggyThread.start();
    }

    @Override
    public void onStop() {
        super.onStop();

        flagThread = false;
        saveJson();
    }

    @Override
    public void onStart() {
        super.onStart();

        flagThread = true;
        threads();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(GameActivity.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.game_bottom_navigation_play: {
                fragment = new PlayFragment();
                break;
//                return false;
            }
            case R.id.game_bottom_navigation_upgrades: {
//                fragment = new ShopFragment(data);
//                break;
                return false;
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


    private void saveJson() {

        File file = new File(getApplicationContext().getDir("", Context.MODE_PRIVATE), fileName);
        OutputStream outputStream = null;
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting()
                .create();
        try {
            outputStream = new FileOutputStream(file);
            BufferedWriter bufferedWriter;
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,
                    StandardCharsets.UTF_8));

            gson.toJson(data, GameData.class, bufferedWriter);
            bufferedWriter.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    private void loadJson() {
        data = new GameData();

        File file = new File(getApplicationContext().getDir("", Context.MODE_PRIVATE), fileName);
        InputStream inputStream = null;
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting()
                .create();
        try {
            inputStream = new FileInputStream(file);
            InputStreamReader streamReader;
            streamReader = new InputStreamReader(inputStream,
                    StandardCharsets.UTF_8);

            data = gson.fromJson(streamReader, GameData.class);
            streamReader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
