package com.s9986.becomeaninfluencer.game.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.s9986.becomeaninfluencer.R;
import com.s9986.becomeaninfluencer.game.DetailViewModel;
import com.s9986.becomeaninfluencer.persistence.GameData;

import java.util.List;

public class UpgradesFragment extends Fragment {
    private TextView upgrade_0;
    private TextView upgrade_1;
    private TextView upgrade_2;
    private TextView upgrade_3;
    private TextView upgrade_4;
    private TextView upgrade_5;
    private TextView upgrade_6;
    private TextView upgrade_7;
    private TextView upgrade_8;
    private TextView upgrade_9;
    private TextView upgrade_10;
    private TextView upgrade_11;
    private TextView upgrade_12;
    private TextView upgrade_13;
    private TextView upgrade_14;
    private TextView upgrade_15;
    private DetailViewModel model;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {
        return inflater.inflate(R.layout.upgrades_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        model = ViewModelProviders.of(getActivity()).get(DetailViewModel.class);
        prepareViews();
        addObservables();
        addEventListenders();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void addEventListenders() {

        upgrade_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("UpgradesFragment", "game upgraded ----------");

                GameData gameData = model.getGameInProgress();
                if(gameData != null) {
                    gameData.buyUpgrade(0, 100);
                    model.updateGameData(gameData);
                }
            }
        });


        upgrade_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("UpgradesFragment", "game upgraded ----------");

                GameData gameData = model.getGameInProgress();
                if(gameData != null) {
                    gameData.buyUpgrade(1, 100);
                    model.updateGameData(gameData);
                }
            }
        });


        upgrade_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("UpgradesFragment", "game upgraded ----------");

                GameData gameData = model.getGameInProgress();
                if(gameData != null) {
                    gameData.buyUpgrade(2, 100);
                    model.updateGameData(gameData);
                }
            }
        });

        upgrade_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("UpgradesFragment", "game upgraded ----------");

                GameData gameData = model.getGameInProgress();
                if(gameData != null) {
                    gameData.buyUpgrade(3, 100);
                    model.updateGameData(gameData);
                }
            }
        });

        upgrade_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("UpgradesFragment", "game upgraded ----------");

                GameData gameData = model.getGameInProgress();
                if(gameData != null) {
                    gameData.buyUpgrade(4, 100);
                    model.updateGameData(gameData);
                }
            }
        });

        upgrade_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("UpgradesFragment", "game upgraded ----------");

                GameData gameData = model.getGameInProgress();
                if(gameData != null) {
                    gameData.buyUpgrade(5, 100);
                    model.updateGameData(gameData);
                }
            }
        });

        upgrade_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("UpgradesFragment", "game upgraded ----------");

                GameData gameData = model.getGameInProgress();
                if(gameData != null) {
                    gameData.buyUpgrade(6, 100);
                    model.updateGameData(gameData);
                }
            }
        });

        upgrade_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("UpgradesFragment", "game upgraded ----------");

                GameData gameData = model.getGameInProgress();
                if(gameData != null) {
                    gameData.buyUpgrade(7, 100);
                    model.updateGameData(gameData);
                }
            }
        });

        upgrade_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("UpgradesFragment", "game upgraded ----------");

                GameData gameData = model.getGameInProgress();
                if(gameData != null) {
                    gameData.buyUpgrade(8, 100);
                    model.updateGameData(gameData);
                }
            }
        });

        upgrade_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("UpgradesFragment", "game upgraded ----------");

                GameData gameData = model.getGameInProgress();
                if(gameData != null) {
                    gameData.buyUpgrade(9, 100);
                    model.updateGameData(gameData);
                }
            }
        });

        upgrade_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("UpgradesFragment", "game upgraded ----------");

                GameData gameData = model.getGameInProgress();
                if(gameData != null) {
                    gameData.buyUpgrade(10, 100);
                    model.updateGameData(gameData);
                }
            }
        });

        upgrade_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("UpgradesFragment", "game upgraded ----------");

                GameData gameData = model.getGameInProgress();
                if(gameData != null) {
                    gameData.buyUpgrade(11, 100);
                    model.updateGameData(gameData);
                }
            }
        });
    }

    public void prepareViews(){
        upgrade_0 = getView().findViewById(R.id.upgrade_0);
        upgrade_1 = getView().findViewById(R.id.upgrade_1);
        upgrade_2 = getView().findViewById(R.id.upgrade_2);
        upgrade_3 = getView().findViewById(R.id.upgrade_3);
        upgrade_4 = getView().findViewById(R.id.upgrade_4);
        upgrade_5 = getView().findViewById(R.id.upgrade_5);
        upgrade_6 = getView().findViewById(R.id.upgrade_6);
        upgrade_7 = getView().findViewById(R.id.upgrade_7);
        upgrade_8 = getView().findViewById(R.id.upgrade_8);
        upgrade_9 = getView().findViewById(R.id.upgrade_9);
        upgrade_10 = getView().findViewById(R.id.upgrade_10);
        upgrade_11 = getView().findViewById(R.id.upgrade_11);
        upgrade_12 = getView().findViewById(R.id.upgrade_12);
        upgrade_13 = getView().findViewById(R.id.upgrade_13);
        upgrade_14 = getView().findViewById(R.id.upgrade_14);
        upgrade_15 = getView().findViewById(R.id.upgrade_15);
    }

    private void addObservables(){
        model.fetchGameInProgress().observe(getViewLifecycleOwner(), new Observer<GameData>() {
            @Override
            public void onChanged(@Nullable GameData gameData) {

                Log.d("UpgradesFragment", "fetchGameInProgress observe run ");

                if(gameData != null){
                    String[] upgrades =  gameData.getUpdates_progress().split(",");

                    upgrade_0.setText(getString(R.string.upgrade_0) + " " +Integer.valueOf(upgrades[0]));
                    upgrade_1.setText(getString(R.string.upgrade_1) + " " +Integer.valueOf(upgrades[1]));
                    upgrade_2.setText(getString(R.string.upgrade_2) + " " +Integer.valueOf(upgrades[2]));
                    upgrade_3.setText(getString(R.string.upgrade_3) + " " +Integer.valueOf(upgrades[3]));
                    upgrade_4.setText(getString(R.string.upgrade_4) + " " +Integer.valueOf(upgrades[4]));
                    upgrade_5.setText(getString(R.string.upgrade_5) + " " +Integer.valueOf(upgrades[5]));
                    upgrade_6.setText(getString(R.string.upgrade_6) + " " +Integer.valueOf(upgrades[6]));
                    upgrade_7.setText(getString(R.string.upgrade_7) + " " +Integer.valueOf(upgrades[7]));
                    upgrade_8.setText(getString(R.string.upgrade_8) + " " +Integer.valueOf(upgrades[8]));
                    upgrade_9.setText(getString(R.string.upgrade_9) + " " +Integer.valueOf(upgrades[9]));
                    upgrade_10.setText(getString(R.string.upgrade_10) + " " +Integer.valueOf(upgrades[10]));
                    upgrade_11.setText(getString(R.string.upgrade_11) + " " +Integer.valueOf(upgrades[11]));
                    upgrade_12.setText(getString(R.string.upgrade_12) + " " +Integer.valueOf(upgrades[12]));
                    upgrade_13.setText(getString(R.string.upgrade_13) + " " +Integer.valueOf(upgrades[13]));
                    upgrade_14.setText(getString(R.string.upgrade_14) + " " +Integer.valueOf(upgrades[14]));
                    upgrade_15.setText(getString(R.string.upgrade_15) + " " +Integer.valueOf(upgrades[15]));
                }
            }
        });
    }

}
