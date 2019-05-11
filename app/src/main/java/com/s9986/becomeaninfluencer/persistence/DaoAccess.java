package com.s9986.becomeaninfluencer.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DaoAccess {

    @Query("SELECT * FROM GameData ORDER BY created_at desc")
    LiveData<List<GameData>> fetchAllGames();

    @Query("SELECT * FROM GameData WHERE in_progress = 0 AND game_finished = 1 ORDER BY total_time_in_secound ASC, created_at desc LIMIT 10")
    LiveData<List<GameData>> fetchTop10RankingGames();

    @Query("SELECT * FROM GameData WHERE in_progress = 1 ORDER BY created_at desc LIMIT 1")
    LiveData<GameData> fetchGameInProgress();

    @Query("SELECT * FROM GameData WHERE in_progress = 1 ORDER BY created_at desc LIMIT 1")
    GameData getGameInProgress();

    @Query("SELECT * FROM GameData WHERE id = :gameId")
    LiveData<GameData> getGame(int gameId);

    @Insert
    Long insertGame(GameData gameData);

    @Update
    void updateGame(GameData gameData);


    @Delete
    void deleteGame(GameData gameData);


    @Query("DELETE FROM GameData")
    void deleteAllGame();
}
