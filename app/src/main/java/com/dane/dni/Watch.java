package com.dane.dni;

import android.app.Activity;
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
    TextView display;
    PolarView watch1;
    PolarView watch2;
    final Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);

        dniDateTime = new DniDateTime();
        display = (TextView) findViewById(R.id.display);

        watch2 = (PolarView)  findViewById(R.id.watch2);
        List<DniDateTime.Unit> hands = new LinkedList<DniDateTime.Unit>();
        hands.add(DniDateTime.Unit.PRORAHN);
        hands.add(DniDateTime.Unit.GORAHN);
        hands.add(DniDateTime.Unit.TAHVO);
        hands.add(DniDateTime.Unit.PAHRTAHVO);

        watch2.addHands(hands);
        watch2.addClock(dniDateTime);

        watch1 = (PolarView)  findViewById(R.id.watch1);
        hands = new LinkedList<DniDateTime.Unit>();
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
            display.setText(dniDateTime.getFormattedString());
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
