package com.dane.dni.watch.views;

import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.dane.dni.common.data.DniHoliday;
import com.dane.dni.R;

import java.util.ArrayList;

/**
 * Created by Dane on 12/22/2015.
 */
public class HolidayDialogFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Typeface typeface =
                Typeface.createFromAsset(getContext().getAssets(), "fonts/EBGaramond-Regular.ttf");

        View view = inflater.inflate(R.layout.holiday_dialog, container);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ArrayList<DniHoliday> holidays = getArguments().getParcelableArrayList("holidays");
        HolidayListAdapter adapter = new HolidayListAdapter(
                getContext(), R.layout.holiday_list_item, holidays, typeface);
        ListView holidayListView = (ListView) view.findViewById(R.id.holidayList);
        holidayListView.setAdapter(adapter);

        TextView holidayHeader = (TextView) view.findViewById(R.id.holidayHeader);
        holidayHeader.setTypeface(typeface);


        return view;
    }
}
