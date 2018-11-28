package com.ay3524.timertest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;

public class TimerService extends Service implements CounterActions{

    private final IBinder mTimerBinder = new TimerBinder();
    CountDownTimer countDownTimer;
    ArrayList<CounterCallbacks> timerListeners = new ArrayList<>(0);
    boolean isTimerRunning;
    private LocalBroadcastManager localBroadcastManager;


    public void registerListener(CounterCallbacks timerListener) {
        timerListener.registerActions(this);
        this.timerListeners.add(timerListener);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        return mTimerBinder;
    }

    @Override
    public void onStartTimer() {
        if (isTimerRunning) {
            onStopTimer();
        }
        countDownTimer = new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {

                for(CounterCallbacks counterCallback : timerListeners){
                    counterCallback.onTick(millisUntilFinished);
                }

                isTimerRunning = true;
                Log.e(TimerService.class.getSimpleName(), "seconds remaining: " + (millisUntilFinished / 1000));
            }

            public void onFinish() {

                for(CounterCallbacks counterCallback : timerListeners){
                    counterCallback.onFinish();
                }

                isTimerRunning = false;
                Log.e(TimerService.class.getSimpleName(), "Timer Done...");
                //Start the new Intent

                // Create intent with action
                Intent localIntent = new Intent(MainActivity.timerIntentFilter);
                // Send local broadcast
                localBroadcastManager.sendBroadcast(localIntent);
            }
        }.start();
    }

    @Override
    public void onStopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            isTimerRunning = false;
        }
    }

    public class TimerBinder extends Binder {
        public TimerService getService() {
            return TimerService.this;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public boolean getTimerRunningState() {
        return isTimerRunning;
    }

    public interface TimerListener {
        void onTick(long millisUntilFinished);

        void onFinish();
    }
}
