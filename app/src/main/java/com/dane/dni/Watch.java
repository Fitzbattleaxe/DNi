package com.dane.dni;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

public class Watch extends FragmentActivity {

    DniDateTime dniDateTime;
    DniDateTime offsetDniDateTime;
    TimeDisplay yearDisplay;
    TimeDisplay monthDisplay;
    TimeDisplay timeDisplay;
    PolarView watch1;
    PolarView watch2;
    ImageButton menuButton;
    final Handler tickHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);

        dniDateTime = new DniDateTime();
        offsetDniDateTime = new DniDateTime();

        yearDisplay = (TimeDisplay) findViewById(R.id.year_display);
        yearDisplay.setClock(dniDateTime);
        List<DniDateTime.Unit> displayUnits = new LinkedList<DniDateTime.Unit>();
        displayUnits.add(DniDateTime.Unit.HAHR);
        yearDisplay.setUnits(displayUnits, "%d DE");

        monthDisplay = (TimeDisplay) findViewById(R.id.month_display);
        monthDisplay.setClock(dniDateTime);
        displayUnits = new LinkedList<DniDateTime.Unit>();
        displayUnits.add(DniDateTime.Unit.NAMED_VAILEE);
        displayUnits.add(DniDateTime.Unit.YAHR);
        displayUnits.add(DniDateTime.Unit.GAHRTAHVOTEE);
        displayUnits.add(DniDateTime.Unit.PAHRTAHVO);
        monthDisplay.setUnits(displayUnits, "%s %02d, %d : %d");

        timeDisplay = (TimeDisplay) findViewById(R.id.time_display);
        timeDisplay.setClock(dniDateTime);
        displayUnits = new LinkedList<DniDateTime.Unit>();
        displayUnits.add(DniDateTime.Unit.TAHVO);
        displayUnits.add(DniDateTime.Unit.GORAHN);
        displayUnits.add(DniDateTime.Unit.PRORAHN);
        timeDisplay.setUnits(displayUnits, "%d : %02d : %02d");

       Float[] easingValues =
                new DampedHarmonicOscillator(-1.0f, 0.01f, 0.00f, 1.0f)
                    .getCachedFunctionValues(0, 800);

        watch2 = (PolarView)  findViewById(R.id.watch2);
        watch2.setUpEasing(easingValues, 800);
        watch2.addClock(dniDateTime);
        watch2.addOffsetClock(offsetDniDateTime);

        watch1 = (PolarView)  findViewById(R.id.watch1);
        watch1.setUpEasing(easingValues, 800);
        watch1.addClock(dniDateTime);
        watch1.addOffsetClock(offsetDniDateTime);

        menuButton = (ImageButton) findViewById(R.id.imageButton);
        menuButton.setOnClickListener(myOnClickListener);

        tickHandler.post(myRunnable);
    }

    final View.OnClickListener myOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fm = getSupportFragmentManager();
            MenuDialogFragment menuDialogFragment = new MenuDialogFragment();
            menuDialogFragment.show(fm, "fragment_menu");
        }
    };

    final Runnable myRunnable = new Runnable() {
        public void run() {
            try {
                long time = System.currentTimeMillis();
                dniDateTime.setTimeInMillis(time);
                offsetDniDateTime.setTimeInMillis(time + 140);
                yearDisplay.updateDisplay();
                monthDisplay.updateDisplay();
                timeDisplay.updateDisplay();
                watch1.updateTime();
                watch2.updateTime();
            } catch (Exception e) {
                System.exit(-1);
            }
            tickHandler.postDelayed(this, 20);
        }
    };
}
