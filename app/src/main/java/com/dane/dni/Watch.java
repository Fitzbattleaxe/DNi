package com.dane.dni;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

public class Watch extends Activity {

    DniDateTime dniDateTime;
    DniDateTime offsetDniDateTime;
    TimeDisplay yearDisplay;
    TimeDisplay monthDisplay;
    TimeDisplay timeDisplay;
    PolarView watch1;
    PolarView watch2;
    Typeface dniTypeface;
    final Handler tickHandler = new Handler();

    private String[] drawerOptions;
    private DrawerLayout drawerLayout;
    private ListView drawerList;

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
//        List<DniDateTime.Unit> hands = new LinkedList<DniDateTime.Unit>();
//        hands.add(DniDateTime.Unit.PRORAHN);
//        hands.add(DniDateTime.Unit.GORAHN);
//        hands.add(DniDateTime.Unit.TAHVO);
//        watch2.addHands(hands);
        watch2.setUpEasing(easingValues, 800);
        watch2.addClock(dniDateTime);
        watch2.addOffsetClock(offsetDniDateTime);

        watch1 = (PolarView)  findViewById(R.id.watch1);
//        hands = new LinkedList<DniDateTime.Unit>();
//        hands.add(DniDateTime.Unit.PAHRTAHVO);
//        hands.add(DniDateTime.Unit.GAHRTAHVOTEE);
//        hands.add(DniDateTime.Unit.YAHR);
//        hands.add(DniDateTime.Unit.VAILEE);
//        watch1.addHands(hands);
        watch1.setUpEasing(easingValues, 800);
        watch1.addClock(dniDateTime);
        watch1.addOffsetClock(offsetDniDateTime);

//        Timer myTimer = new Timer();
//        myTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {updateGUI();}
//        }, 0, 50);
        tickHandler.post(myRunnable);


//        drawerOptions = getResources().getStringArray(R.array.drawer_options);
//        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawerList = (ListView) findViewById(R.id.left_drawer);
//
//        // Set the adapter for the list view
//        drawerList.setAdapter(new ArrayAdapter<String>(this,
//                R.layout.drawer_list_item, drawerOptions));
        // Set the list's click listener
     //   drawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    private void updateGUI() {
        tickHandler.post(myRunnable);
    }

    final Runnable myRunnable = new Runnable() {
        public void run() {
            try {
                long time = System.currentTimeMillis();
                dniDateTime.setTimeInMillis(time);
                offsetDniDateTime.setTimeInMillis(time + 275);
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
