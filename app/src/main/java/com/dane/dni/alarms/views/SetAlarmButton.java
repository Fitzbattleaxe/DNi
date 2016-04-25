package com.dane.dni.alarms.views;

import android.content.Context;
import android.util.AttributeSet;

import com.dane.dni.common.views.CircularButton;

/**
 * Created by Dane on 4/24/2016.
 */
public class SetAlarmButton extends CircularButton {

    public SetAlarmButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setText("\uE858");
        this.setTextAlignment(TEXT_ALIGNMENT_CENTER);
    }
}
