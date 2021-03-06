package com.ay3524.timertest;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimerFragment extends Fragment implements View.OnClickListener, CounterCallbacks {

    private TextView textView;
    private CounterActions counterActions;
    private static final String TIMER_FINISH = "timer_finish";

    public TimerFragment() {
        // Required empty public constructor
    }

    public static TimerFragment newInstance(String data) {
        TimerFragment fragment = new TimerFragment();
        if (data != null) {
            Bundle args = new Bundle();
            args.putString(TIMER_FINISH, data);
            fragment.setArguments(args);
        }

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbarId);
        toolbar.setNavigationOnClickListener(this);
        textView = view.findViewById(R.id.text_timer);

        if (getArguments() != null) {
            String mParam1 = getArguments().getString(TIMER_FINISH);
            textView.setText(mParam1);
        }

        Objects.requireNonNull(((MainActivity) getActivity())).setSupportActionBar(toolbar);
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setTitle("Timer Test");

        ((MainActivity) getActivity()).registerTimerCallbacks(this);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        textView.setText(Utils.formatTime(millisUntilFinished));
    }

    @Override
    public void onFinish() {
        textView.setText("Timer Finished!");
    }

    @Override
    public void onClick(View v) {
        Objects.requireNonNull(((MainActivity) getActivity())).onBackPressed();
    }

    @Override
    public void registerActions(CounterActions counterActions) {
        this.counterActions = counterActions;
    }
}
