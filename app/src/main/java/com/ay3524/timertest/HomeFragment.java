package com.ay3524.timertest;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, CounterCallbacks{

    Button startButton, stopButton, checkButton;
    TextView timerText;
    CallMainActivity callMainActivity;
    CounterActions counterActions;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setListener(CallMainActivity callMainActivity) {
        this.callMainActivity = callMainActivity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        timerText = view.findViewById(R.id.text_timer);
        timerText.setText("No Timer Set");
        startButton = view.findViewById(R.id.start);
        stopButton = view.findViewById(R.id.stop);
        checkButton = view.findViewById(R.id.check);

        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        checkButton.setOnClickListener(this);

        Toolbar mActionBarToolbar = view.findViewById(R.id.toolbarId);
        ((MainActivity) getActivity()).setSupportActionBar(mActionBarToolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Timer Test");

        ((MainActivity) getActivity()).registerTimerCallbacks(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                onStartButtonPressed();
                break;
            case R.id.stop:
                onStopButtonPressed();
                break;
            case R.id.check:
                onCheckButtonPressed();
                break;
        }
    }

    private void onCheckButtonPressed() {
        callMainActivity.onCheckPressed();
    }

    private void onStopButtonPressed() {
        counterActions.onStopTimer();
    }

    private void onStartButtonPressed() {
        counterActions.onStartTimer();
    }

    @Override
    public void onTick(long millisUntilFinished) {
        timerText.setText(String.valueOf(millisUntilFinished / 1000));
    }

    @Override
    public void onFinish() {
        timerText.setText("Timer Finished!");
    }

    @Override
    public void registerActions(CounterActions counterActions) {
        this.counterActions = counterActions;
    }

    public interface CallMainActivity {
        void onCheckPressed();
    }
}
