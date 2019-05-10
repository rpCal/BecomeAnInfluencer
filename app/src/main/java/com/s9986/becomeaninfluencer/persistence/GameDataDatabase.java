package com.s9986.becomeaninfluencer.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {GameData.class}, version = 1, exportSchema = false)
public abstract class GameDataDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess();
}

