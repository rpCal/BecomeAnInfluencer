package com.s9986.becomeaninfluencer.persistence;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Date;

@Entity
public class GameData implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "total_points")
    private int total_points;

    @ColumnInfo(name = "total_points_sum")
    private int total_points_sum;

    @ColumnInfo(name = "total_taps")
    private int total_taps;

    @ColumnInfo(name = "total_spend")
    private int total_spend;

    @ColumnInfo(name = "total_time_in_secound")
    private int total_time_in_secound;


    @ColumnInfo(name = "points_per_second")
    private double points_per_second;

    @ColumnInfo(name = "game_finished")
    private int game_finished;

    @ColumnInfo(name = "in_progress")
    private int in_progress;

    @ColumnInfo(name = "created_at")
    @TypeConverters({TimestampConverter.class})
    private Date created_at;

    @ColumnInfo(name = "modified_at")
    @TypeConverters({TimestampConverter.class})
    private Date modified_at;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotal_points() {
        return total_points;
    }

    public void setTotal_points(int total_points) {
        this.total_points = total_points;
    }

    public int getTotal_points_sum() {
        return total_points_sum;
    }

    public void setTotal_points_sum(int total_points_sum) {
        this.total_points_sum = total_points_sum;
    }

    public double getPoints_per_second() {
        return points_per_second;
    }

    public void setPoints_per_second(double points_per_second) {
        this.points_per_second = points_per_second;
    }

    public int getTotal_taps() {
        return total_taps;
    }

    public void setTotal_taps(int total_taps) {
        this.total_taps = total_taps;
    }

    public int getTotal_spend() {
        return total_spend;
    }

    public void setTotal_spend(int total_spend) {
        this.total_spend = total_spend;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getModified_at() {
        return modified_at;
    }

    public void setModified_at(Date modified_at) {
        this.modified_at = modified_at;
    }

    public int getTotal_time_in_secound() {
        return total_time_in_secound;
    }

    public void setTotal_time_in_secound(int total_time_in_secound) {
        this.total_time_in_secound = total_time_in_secound;
    }

    public int getGame_finished() {
        return game_finished;
    }

    public void setGame_finished(int game_finished) {
        this.game_finished = game_finished;
    }

    public int getIn_progress() {
        return in_progress;
    }

    public void setIn_progress(int in_progress) {
        this.in_progress = in_progress;
    }
}

