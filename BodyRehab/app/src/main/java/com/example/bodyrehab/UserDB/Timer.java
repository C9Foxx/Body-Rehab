package com.example.bodyrehab.UserDB;

import androidx.room.ColumnInfo;

public class Timer {
    private long activeTimer;
    private long restTimer;
    private int repetitions;
    private boolean flag;

    public long getActiveTimer() {
        return activeTimer;
    }

    public void setActiveTimer(long activeTimer) {
        this.activeTimer = activeTimer;
    }

    public long getRestTimer() {
        return restTimer;
    }

    public void setRestTimer(long restTimer) {
        this.restTimer = restTimer;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public Timer(long activeTimer, long restTimer, int repetitions, boolean flag) {
        this.activeTimer = activeTimer;
        this.restTimer = restTimer;
        this.repetitions = repetitions;
        this.flag = flag;
    }
}
