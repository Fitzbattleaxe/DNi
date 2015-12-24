package com.dane.dni;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Dane on 12/22/2015.
 */
public class HolidayDialogFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.holiday_dialog, container);
        getDialog().setTitle("Holidays");
        ArrayList<DniHoliday> holidays = getArguments().getParcelableArrayList("holidays");
        HolidayListAdapter adapter =
                new HolidayListAdapter(getContext(), R.layout.holiday_list_item, holidays);
        ListView holidayListView = (ListView) view.findViewById(R.id.holidayList);
        holidayListView.setAdapter(adapter);
        return view;
    }
}
