package com.s9986.becomeaninfluencer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.s9986.becomeaninfluencer.MainActivity;
import com.s9986.becomeaninfluencer.R;
import com.s9986.becomeaninfluencer.persistence.GameDataRepository;

public class SettingsActivity extends AppCompatActivity {

    GameDataRepository gameDataRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.settings_activity);

        gameDataRepository = new GameDataRepository(getApplicationContext());

        LinearLayout linearLayout = findViewById(R.id.settings);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameDataRepository.deleteAllGames();
                Toast
                        .makeText(getApplicationContext(), R.string.game_history_deleted, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        return true;
    }
}
