package com.dane.dni;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Watch extends Activity {

    DniDateTime dniDateTime;
    TimeDisplay yearDisplay;
    TimeDisplay monthDisplay;
    TimeDisplay timeDisplay;
    PolarView watch1;
    PolarView watch2;
    Typeface dniTypeface;
    final Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);

        dniTypeface = Typeface.createFromAsset(getAssets(), "fonts/D_NI_SCR.TTF");

        dniDateTime = new DniDateTime();

        yearDisplay = (TimeDisplay) findViewById(R.id.year_display);
        yearDisplay.setClock(dniDateTime);
        List<DniDateTime.Unit> displayUnits = new LinkedList<DniDateTime.Unit>();
        displayUnits.add(DniDateTime.Unit.HAHR);
        yearDisplay.setUnits(displayUnits, "%s  DE");
        yearDisplay.setTypeface(dniTypeface);

        monthDisplay = (TimeDisplay) findViewById(R.id.month_display);
        monthDisplay.setClock(dniDateTime);
        displayUnits = new LinkedList<DniDateTime.Unit>();
        displayUnits.add(DniDateTime.Unit.NAMED_VAILEE);
        displayUnits.add(DniDateTime.Unit.YAHR);
        displayUnits.add(DniDateTime.Unit.GAHRTAHVOTEE);
        displayUnits.add(DniDateTime.Unit.PAHRTAHVO);
        monthDisplay.setUnits(displayUnits, "%s  %s , %s - %s ");
        monthDisplay.setTypeface(dniTypeface);

        timeDisplay = (TimeDisplay) findViewById(R.id.time_display);
        timeDisplay.setClock(dniDateTime);
        displayUnits = new LinkedList<DniDateTime.Unit>();
        displayUnits.add(DniDateTime.Unit.TAHVO);
        displayUnits.add(DniDateTime.Unit.GORAHN);
        displayUnits.add(DniDateTime.Unit.PRORAHN);
        timeDisplay.setUnits(displayUnits, "%s - %s - %s ");
        timeDisplay.setTypeface(dniTypeface);

        watch2 = (PolarView)  findViewById(R.id.watch2);
        List<DniDateTime.Unit> hands = new LinkedList<DniDateTime.Unit>();
        hands.add(DniDateTime.Unit.PRORAHN);
        hands.add(DniDateTime.Unit.GORAHN);
        hands.add(DniDateTime.Unit.TAHVO);

        watch2.addHands(hands);
        watch2.addClock(dniDateTime);

        watch1 = (PolarView)  findViewById(R.id.watch1);
        hands = new LinkedList<DniDateTime.Unit>();
        hands.add(DniDateTime.Unit.PAHRTAHVO);
        hands.add(DniDateTime.Unit.GAHRTAHVOTEE);
        hands.add(DniDateTime.Unit.YAHR);
        hands.add(DniDateTime.Unit.VAILEE);
        watch1.addHands(hands);
        watch1.addClock(dniDateTime);

        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {updateGUI();}
        }, 0, 100);
    }

    private void updateGUI() {
        myHandler.post(myRunnable);
    }

    final Runnable myRunnable = new Runnable() {
        public void run() {
            dniDateTime.setTimeInMillis(System.currentTimeMillis());
            yearDisplay.updateDisplay();
            monthDisplay.updateDisplay();
            timeDisplay.updateDisplay();
            watch1.updateTime();
            watch2.updateTime();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_watch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
