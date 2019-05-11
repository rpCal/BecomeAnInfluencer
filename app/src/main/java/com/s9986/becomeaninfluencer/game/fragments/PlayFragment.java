package com.s9986.becomeaninfluencer.game.fragments;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.s9986.becomeaninfluencer.R;
import com.s9986.becomeaninfluencer.RankingActivity;
import com.s9986.becomeaninfluencer.game.DetailViewModel;
import com.s9986.becomeaninfluencer.game.GameActivity;
import com.s9986.becomeaninfluencer.persistence.GameData;
import com.s9986.becomeaninfluencer.persistence.GameDataConstant;

public class PlayFragment extends Fragment {
    private TextView points_textView;
    private TextView points_per_second_textView;
    private VideoView click_element_VideoView;
    private TextView total_time_in_secound_textView;
    private TextView remaining_points_textView;
    private DetailViewModel model;
    private SoundPool soundPool;
    private int soundClickId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance)
    {
        return inflater.inflate(R.layout.play_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        model = ViewModelProviders.of(getActivity()).get(DetailViewModel.class);
        prepareViewItems();
        addEventListenders();
        prepareMedia();
        prepareObservables();
    }

    private void prepareViewItems(){
        points_textView = getView().findViewById(R.id.points_textView);
        points_per_second_textView = getView().findViewById(R.id.points_per_second_textView);
        click_element_VideoView = getView().findViewById(R.id.click_element_VideoView);
        total_time_in_secound_textView = getView().findViewById(R.id.total_time_in_secound_textView);
        remaining_points_textView = getView().findViewById(R.id.remaining_points_textView);
    }

    private void prepareMedia(){

        String path = "android.resource://" +  getActivity().getPackageName() + "/" + R.raw.click_element_action_video;
        click_element_VideoView.setVideoURI(Uri.parse(path));
        click_element_VideoView.seekTo( 1 );

        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .setMaxStreams(10)
                .build();

        soundClickId = soundPool.load(getContext(), R.raw.click_element_action_sound_fast, 1);
    }

    private void prepareObservables(){
        model.fetchGameInProgress().observe(getViewLifecycleOwner(), new Observer<GameData>() {
            @Override
            public void onChanged(@Nullable GameData gameData) {

                Log.d("PlayFragment", "fetchGameInProgress observe run ");

                String points_textView_Text = "";
                String points_per_second_textView_Text = "";
                String total_time_in_secound_textView_Text = "";
                String remaining_points_textView_Text = "";

                if(gameData != null) {
                    points_textView_Text = String.format( "%.2f", gameData.getTotal_points())   + " " + getString(R.string.points_label);
                    points_per_second_textView_Text = String.format( "%.2f", gameData.getPoints_per_second()) + " " + getString(R.string.points_per_second_label);
                    total_time_in_secound_textView_Text = gameData.getTotal_time_in_secound() + "" + getString(R.string.total_time_in_secound_label);
                    double remainingPoints = GameDataConstant.MAX_POINTS_IN_GAME - gameData.getTotal_points();
                    remaining_points_textView_Text = getString(R.string.remaining_points_label) + ": " + String.format("%.2f", remainingPoints);
                }

                remaining_points_textView.setText(remaining_points_textView_Text);
                points_textView.setText(points_textView_Text);
                points_per_second_textView.setText(points_per_second_textView_Text);
                total_time_in_secound_textView.setText(total_time_in_secound_textView_Text);
            }
        });
    }

    private void addEventListenders(){

        click_element_VideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                click_element_VideoView.seekTo( 1 );
                click_element_VideoView.start();

                soundPool.play(soundClickId, 1, 1, 0, 0, 1);

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        GameData gameData = model.getGameInProgress();
                        if(gameData != null){
                            gameData.click();
                            model.updateGameData(gameData);

                            if(gameData.getGame_finished() == GameDataConstant.GAME_FINISHED_VALUE){
                                Intent i = new Intent(getActivity(), RankingActivity.class);
                                startActivity(i);
                                model.finishGame();
                            }
//                            Log.d("PlayFragment", gameData.toString());
                        }
                        Log.d("PlayFragment", "game state updated ");
                        return null;
                    }
                }.execute();
            }
        });
    }
}
