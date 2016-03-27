package com.dane.dni.watch.views;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.dane.dni.common.data.DniDateTime;
import com.dane.dni.common.data.DniHoliday;
import com.dane.dni.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Dane on 12/22/2015.
 */
public class HolidayDialogFragment extends DialogFragment implements View.OnClickListener {

    public static HolidayDialogFragment newInstance(DniDateTime currentTime,
                                                    ArrayList<DniHoliday> holidayList) {
        Bundle bundle = new Bundle();
        bundle.putLong("currentTimeMillis", currentTime.getSystemTimeInMillis());
        bundle.putParcelableArrayList("holidays", holidayList);
        HolidayDialogFragment frag = new HolidayDialogFragment();
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Typeface typeface =
                Typeface.createFromAsset(getContext().getAssets(), "fonts/EBGaramond-Regular.ttf");

        View view = inflater.inflate(R.layout.holiday_dialog, container);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow()
                .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DniDateTime currentTime =
                DniDateTime.now(getArguments().getLong("currentTimeMillis"));
        final DniDateTime startOfDay = DniDateTime.now(currentTime.getHahrtee(),
                currentTime.getVaileetee() - 1, currentTime.getYahrtee(), 0, 0, 0, 0, 0);
        ArrayList<DniHoliday> holidays = getArguments().getParcelableArrayList("holidays");
        Collections.sort(holidays, new Comparator<DniHoliday>() {
            @Override
            public int compare(DniHoliday lhs, DniHoliday rhs) {
                DniDateTime left = DniDateTime.now(
                        startOfDay.getHahrtee(),
                        lhs.getVailee(),
                        lhs.getYahr(),
                        0,
                        0,
                        0,
                        0,
                        0);
                DniDateTime right = DniDateTime.now(
                        startOfDay.getHahrtee(),
                        rhs.getVailee(),
                        rhs.getYahr(),
                        0,
                        0,
                        0,
                        0,
                        0);
                if (startOfDay.compareTo(left) > 0) {
                    left = DniDateTime.now(
                            startOfDay.getHahrtee() + 1,
                            lhs.getVailee(),
                            lhs.getYahr(),
                            0,
                            0,
                            0,
                            0,
                            0);
                }
                if (startOfDay.compareTo(right) > 0) {
                    right = DniDateTime.now(
                            startOfDay.getHahrtee() + 1,
                            rhs.getVailee(),
                            rhs.getYahr(),
                            0,
                            0,
                            0,
                            0,
                            0);
                }
                return left.compareTo(right);
            }
        });

        HolidayListAdapter adapter = new HolidayListAdapter(
                getContext(), R.layout.holiday_list_item, holidays, typeface);
        ListView holidayListView = (ListView) view.findViewById(R.id.holidayList);
        holidayListView.setDividerHeight(0);
        holidayListView.setAdapter(adapter);

        TextView holidayHeader = (TextView) view.findViewById(R.id.holidayHeader);
        holidayHeader.setTypeface(typeface);

        View closeButton = view.findViewById(R.id.holidayDialogClose);
        closeButton.setOnClickListener(this);
        closeButton.setSoundEffectsEnabled(false);

        return view;
    }

    @Override
    public void onClick(View v) {
        getDialog().dismiss();
    }
}
