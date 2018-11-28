package com.ay3524.timertest;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements HomeFragment.CallMainActivity {

    public static final String timerIntentFilter = "timer_intent_filter";
    public static final String homeFragmentTag = "tag_home_fragment";
    public static final String timerFragmentTag = "tag_timer_fragment";
    private LocalBroadcastManager localBroadcastManager;
    private FragmentManager mFragmentManager;
    private TimerService mTimerService;
    private boolean isTimerServiceBound;
    private Intent mTimerIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startTimerService();

        mFragmentManager = getSupportFragmentManager();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        localBroadcastManager.registerReceiver(listener, new IntentFilter(timerIntentFilter));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(listener);
        unbindTimerService();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        Fragment currentFragment = getCurrentFragment(mFragmentManager);
        if (!(currentFragment instanceof HomeFragment)) {
            mFragmentManager.popBackStackImmediate();
        }
    }

    private BroadcastReceiver listener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Start the TimerFragment...
            Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(timerFragmentTag);
            if (!(currentFragment instanceof TimerFragment)) {
                replaceFragment(R.id.content, TimerFragment.newInstance(), timerFragmentTag);
            }
        }
    };

    protected void addFragment(@IdRes int containerViewId,
                               @NonNull Fragment fragment,
                               @NonNull String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(containerViewId, fragment, fragmentTag)
                .disallowAddToBackStack()
                .commit();
    }

    protected void replaceFragment(@IdRes int containerViewId,
                                   @NonNull Fragment fragment,
                                   @NonNull String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .addToBackStack(null)
                .commit();
    }

    public static Fragment getCurrentFragment(FragmentManager mFragmentManager) {
        if (mFragmentManager != null) {
            if (mFragmentManager.getBackStackEntryCount() != 0) {
                String fragmentTag = mFragmentManager.getBackStackEntryAt(mFragmentManager.getBackStackEntryCount() - 1).getName();
                return mFragmentManager.findFragmentByTag(fragmentTag);
            }
        }
        return null;
    }

    @Override
    public void onCheckPressed() {
        //Start the TimerFragment...
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(timerFragmentTag);
        if (!(currentFragment instanceof TimerFragment)) {
            replaceFragment(R.id.content, TimerFragment.newInstance(), timerFragmentTag);
        }
    }

    private ServiceConnection mTimerConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            TimerService.TimerBinder binder = (TimerService.TimerBinder) iBinder; // Instantiate Binder
            mTimerService = binder.getService(); // Get Service
            isTimerServiceBound = true;
            //Now launch HomeFragment
            launchHomeFragment();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isTimerServiceBound = false;
        }
    };

    private void launchHomeFragment() {
        HomeFragment homeFragment = HomeFragment.newInstance();
        homeFragment.setListener(MainActivity.this);
        replaceFragment(R.id.content, homeFragment, homeFragmentTag);
    }

    private void startTimerService() {
        if (mTimerIntent == null) {
            // Instantiate Intent.
            mTimerIntent = new Intent(getApplicationContext(), TimerService.class);

            // Bind the Service.
            try {
                bindService(mTimerIntent, mTimerConnection, Context.BIND_AUTO_CREATE);
            } catch (SecurityException e) {
                Log.e(MyApplication.class.getSimpleName(), "Security Exception while binding service!");
            }

            // Start the Service.
            startService(mTimerIntent);
        }
    }

    public void registerTimerCallbacks(CounterCallbacks callback) {
        if (mTimerService != null) {
            mTimerService.registerListener(callback);
        }
    }

    public void unbindTimerService() {
        if (isTimerServiceBound) {
            mTimerService.onUnbind(mTimerIntent);
        }
    }
}