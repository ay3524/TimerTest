package com.ay3524.timertest;

public class Utils {
    public static String formatTime(long milliSeconds) {
        long seconds = milliSeconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        hours = hours % 60;
        minutes = minutes % 60;
        seconds = seconds % 60;

        String hrs = String.valueOf(minutes);
        String min = String.valueOf(minutes);
        String sec = String.valueOf(seconds);

        if (seconds < 10) {
            sec = "0" + seconds;
        }
        if (minutes < 10) {
            min = "0" + minutes;
        }
        if (hours < 10) {
            hrs = "0" + hours;
        }

        return hrs + ":" + min + ":" + sec;
    }
}
