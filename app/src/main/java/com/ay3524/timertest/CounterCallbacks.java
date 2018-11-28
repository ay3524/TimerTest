package com.ay3524.timertest;

public interface CounterCallbacks {
    void onTick(long millisUntilFinished);

    void onFinish();

    void registerActions(CounterActions counterActions);
}
