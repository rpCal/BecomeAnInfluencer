package com.s9986.becomeaninfluencer.persistence;

import android.content.Context;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.Date;
import java.util.List;

public class GameDataRepository {

    private String DB_NAME = "db_game_data";
    private GameDataDatabase gameDataDatabase;

    public GameDataRepository(Context context) {
        gameDataDatabase = Room.databaseBuilder(context, GameDataDatabase.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    public void insertNewIfNeeded(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                GameData gameInProgres = fetchGameInProgress().getValue();
                if(gameInProgres == null){
                    insertDefaultGameData();
                    Log.d("GameDataRepository", "New GameData created");
                }else{
                    Log.d("GameDataRepository", "GameData restored");
                }
                return null;
            }
        }.execute();
    }

    public void insertDefaultGameData() {

        final GameData gameData = new GameData();
        gameData.setGame_finished(0);
        gameData.setIn_progress(1);
        gameData.setCreated_at(getCurrentDateTime());
        gameData.setModified_at(getCurrentDateTime());
        gameData.setPoints_per_second(0.3);
        gameData.setTotal_points(0);
        gameData.setTotal_points_sum(0);
        gameData.setTotal_taps(0);
        gameData.setTotal_spend(0);
        gameData.setTotal_time_in_secound(0);
        gameData.setUpdates_progress("0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                gameDataDatabase.daoAccess().insertGame(gameData);
                return null;
            }
        }.execute();
    }

    public void insertGameData(final GameData gameData) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                gameDataDatabase.daoAccess().insertGame(gameData);
                return null;
            }
        }.execute();
    }


    public void updateGameData(final GameData gameData) {
        gameData.setModified_at(getCurrentDateTime());

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                gameDataDatabase.daoAccess().updateGame(gameData);
                return null;
            }
        }.execute();
    }

    public void deleteGameData(final int id) {
        final LiveData<GameData> gameData = getGameData(id);
        if(gameData != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    gameDataDatabase.daoAccess().deleteGame(gameData.getValue());
                    return null;
                }
            }.execute();
        }
    }

    public void deleteGameData(final GameData gameData) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                gameDataDatabase.daoAccess().deleteGame(gameData);
                return null;
            }
        }.execute();
    }

    public LiveData<GameData> getGameData(int id) {
        return gameDataDatabase.daoAccess().getGame(id);
    }

    public LiveData<GameData> fetchGameInProgress() {
        return gameDataDatabase.daoAccess().fetchGameInProgress();
    }

    public GameData getGameInProgress() {
        return gameDataDatabase.daoAccess().getGameInProgress();
    }


    public LiveData<List<GameData>> fetchAllGames() {
        return gameDataDatabase.daoAccess().fetchAllGames();
    }

    public LiveData<List<GameData>> fetchTop10RankingGames() {
        return gameDataDatabase.daoAccess().fetchTop10RankingGames();
    }

    public void deleteAllGames() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                gameDataDatabase.daoAccess().deleteAllGame();
                return null;
            }
        }.execute();
    }

    private Date getCurrentDateTime(){
        return Calendar.getInstance().getTime();
    }
}
