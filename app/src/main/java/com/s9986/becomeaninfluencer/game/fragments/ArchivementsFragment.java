package com.s9986.becomeaninfluencer.game.fragments;

import android.content.Context;
import android.graphics.Color;
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
import com.s9986.becomeaninfluencer.persistence.GameDataConstant;

public class ArchivementsFragment extends Fragment {

    private TextView archivement_0_total_click;
    private TextView archivement_1_total_points;
    private TextView archivement_2_total_secounds;
    private TextView archivement_3_total_spend;
    private DetailViewModel model;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance)
    {
        return inflater.inflate(R.layout.archivement_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        model = ViewModelProviders.of(getActivity()).get(DetailViewModel.class);
        prepareViews();
        addObservables();
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

    private void prepareViews(){
        archivement_0_total_click = getView().findViewById(R.id.archivement_0_total_click);
        archivement_1_total_points = getView().findViewById(R.id.archivement_1_total_points);
        archivement_2_total_secounds = getView().findViewById(R.id.archivement_2_total_secounds);
        archivement_3_total_spend = getView().findViewById(R.id.archivement_3_total_spend);
    }

    private void addObservables(){
        model.fetchGameInProgress().observe(getViewLifecycleOwner(), new Observer<GameData>() {
            @Override
            public void onChanged(@Nullable GameData gameData) {

                Log.d("ArchivementsFragment", "fetchGameInProgress observe run ");

                int colorDone = ContextCompat.getColor(getContext(), R.color.colorDone);
                int colorText = ContextCompat.getColor(getContext(), R.color.colorText);

                if(gameData != null && gameData.getTotal_taps() >= 100){
                    archivement_0_total_click.setTextColor(colorDone);
                }else{
                    archivement_0_total_click.setTextColor(colorText);
                }

                if(gameData != null && gameData.getTotal_points() >= 1000){
                    archivement_1_total_points.setTextColor(colorDone);
                }else{
                    archivement_1_total_points.setTextColor(colorText);
                }

                if(gameData != null && gameData.getTotal_time_in_secound() >= 11){
                    archivement_2_total_secounds.setTextColor(colorDone);
                }else{
                    archivement_2_total_secounds.setTextColor(colorText);
                }

                if(gameData != null && gameData.getTotal_spend() >= 1000){
                    archivement_3_total_spend.setTextColor(colorDone);
                }else{
                    archivement_3_total_spend.setTextColor(colorText);
                }
            }
        });
    }
}
