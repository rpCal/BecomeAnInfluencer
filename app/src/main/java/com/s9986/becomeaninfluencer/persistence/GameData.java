package com.s9986.becomeaninfluencer.persistence;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;

@Entity
public class GameData implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "total_points")
    private double total_points;

    @ColumnInfo(name = "total_points_sum")
    private double total_points_sum;

    @ColumnInfo(name = "total_taps")
    private int total_taps;

    @ColumnInfo(name = "total_spend")
    private double total_spend;

    @ColumnInfo(name = "total_time_in_secound")
    private int total_time_in_secound;

    @ColumnInfo(name = "points_per_second")
    private double points_per_second;

    @ColumnInfo(name = "game_finished")
    private int game_finished;

    @ColumnInfo(name = "in_progress")
    private int in_progress;

    @ColumnInfo(name = "updates_progress")
    private String updates_progress;

    @ColumnInfo(name = "created_at")
    @TypeConverters({TimestampConverter.class})
    private Date created_at;

    @ColumnInfo(name = "modified_at")
    @TypeConverters({TimestampConverter.class})
    private Date modified_at;

    public void clockTick(){
        setTotal_points(getTotal_points() + getPoints_per_second());
        setTotal_points_sum(getTotal_points_sum() + getPoints_per_second());
        setTotal_time_in_secound(getTotal_time_in_secound() + 1);

        if(checkFinishGame()){
            finishGame();
        }
    };

    public void click(){
        setTotal_points(getTotal_points() + GameDataConstant.PER_CLICK_VALUE);
        setTotal_points_sum(getTotal_points_sum() + GameDataConstant.PER_CLICK_VALUE);
        setTotal_taps(getTotal_taps() + 1);

        if(checkFinishGame()){
            finishGame();
        }
    }

    public void buyUpgrade(int upgradeIndex, double upgradePrice){
        if(upgradePrice > getTotal_points()){
            return;
        }
        setTotal_points(getTotal_points() - upgradePrice);
        setTotal_spend(getTotal_spend() + upgradePrice);
        //updates_progress

        if(checkFinishGame()){
            finishGame();
        }
    }

    public boolean checkFinishGame(){
        return getTotal_points() >= GameDataConstant.MAX_POINTS_IN_GAME;
    }

    public void finishGame(){
        setGame_finished(GameDataConstant.GAME_FINISHED_VALUE);
        setIn_progress(0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotal_points() {
        return total_points;
    }

    public void setTotal_points(double total_points) {
        this.total_points = total_points;
    }

    public double getTotal_points_sum() {
        return total_points_sum;
    }

    public void setTotal_points_sum(double total_points_sum) {
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

    public double getTotal_spend() {
        return total_spend;
    }

    public void setTotal_spend(double total_spend) {
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

    public String getUpdates_progress() {
        return updates_progress;
    }

    public void setUpdates_progress(String updates_progress) {
        this.updates_progress = updates_progress;
    }

//    public String toString() {
//        StringBuilder result = new StringBuilder();
//        String newLine = System.getProperty("line.separator");
//
//        result.append( this.getClass().getName() );
//        result.append( " Object {" );
//        result.append(newLine);
//
//        //determine fields declared in this class only (no fields of superclass)
//        Field[] fields = this.getClass().getDeclaredFields();
//
//        //print field names paired with their values
//        for ( Field field : fields  ) {
//            result.append("  ");
//            try {
//                result.append( field.getName() );
//                result.append(": ");
//                //requires access to private field:
//                result.append( field.get(this) );
//            } catch ( IllegalAccessException ex ) {
//                System.out.println(ex);
//            }
//            result.append(newLine);
//        }
//        result.append("}");
//
//        return result.toString();
//    }
}

