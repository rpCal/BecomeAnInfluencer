package com.s9986.becomeaninfluencer.game;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.s9986.becomeaninfluencer.persistence.GameData;
import com.s9986.becomeaninfluencer.persistence.GameDataRepository;

import java.util.List;

public class DetailViewModel extends AndroidViewModel {

    GameDataRepository gameDataRepository;

    private LiveData<GameData> gameData;

    public DetailViewModel(Application application) {
        super(application);
        gameDataRepository = new GameDataRepository(application);
        gameData = gameDataRepository.fetchGameInProgress();
    }
    public void finishGame(){
        gameData = null;
    }

    public LiveData<GameData> fetchGameInProgress() {
        return gameData;
    }

    public LiveData<List<GameData>> fetchTop10RankingGames() {
        return gameDataRepository.fetchTop10RankingGames();
    }

    public void insertNewIfNeeded(){
        gameDataRepository.insertNewIfNeeded();
    }

    public GameData getGameInProgress() {
        if(gameData != null){
            return gameData.getValue();
        }else{
            return null;
        }
    }

    public void updateGameData(final GameData gameData) {
        gameDataRepository.updateGameData(gameData);
    }
}