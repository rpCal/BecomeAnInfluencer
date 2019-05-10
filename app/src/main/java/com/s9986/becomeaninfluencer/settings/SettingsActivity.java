package com.s9986.becomeaninfluencer.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.s9986.becomeaninfluencer.game.GameData;
import com.s9986.becomeaninfluencer.MainActivity;
import com.s9986.becomeaninfluencer.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.settings_activity);

        LinearLayout linearLayout = findViewById(R.id.settings);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();

                Toast
                        .makeText(getApplicationContext(), R.string.game_history_deleted, Toast.LENGTH_SHORT)
                        .show();

            }

            void save() {
                File file = new File(getApplicationContext().getDir("", Context.MODE_PRIVATE), "data.txt");
                OutputStream outputStream = null;
                Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting()
                        .create();
                try {
                    outputStream = new FileOutputStream(file);
                    BufferedWriter bufferedWriter;
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                    gson.toJson(new GameData(), GameData.class, bufferedWriter);
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

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        return true;
    }
}
