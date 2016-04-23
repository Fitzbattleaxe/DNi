package com.dane.dni.watch.views;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dane.dni.common.data.DniHoliday;
import com.dane.dni.R;

import java.util.List;

/**
 * Created by Dane on 12/23/2015.
 */
public class HolidayListAdapter extends ArrayAdapter<DniHoliday> {

    private final LayoutInflater mInflater;

    private int mResource;

    private Typeface typeface;

    public HolidayListAdapter(Context context, int resource, List<DniHoliday> objects,
                              Typeface typeface) {
        super(context, resource, objects);
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        this.typeface = typeface;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(mInflater, position, convertView, parent, mResource);
    }

    private View createViewFromResource(LayoutInflater inflater, int position, View convertView,
                                        ViewGroup parent, int resource) {
        View view;
        TextView nameView;
        TextView dateView;

        if (convertView == null) {
            view = inflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        nameView = (TextView) view.findViewById(R.id.holidayName);
        dateView = (TextView) view.findViewById(R.id.holidayDate);

        DniHoliday item = getItem(position);
        nameView.setText(item.getName());
        nameView.setTypeface(typeface);

        String dateString = (item.getVailee() + 1) + " : " + item.getYahr() + 1;
        SpannableStringBuilder finalDateString = new SpannableStringBuilder(dateString);
        int colonIndex = dateString.indexOf(':');
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(nameView.getCurrentTextColor());
        finalDateString.setSpan(colorSpan, colonIndex, colonIndex + 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan((int) nameView.getTextSize());
        finalDateString.setSpan(sizeSpan, colonIndex, colonIndex + 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        dateView.setText(finalDateString);
        dateView.setTypeface(typeface);

        return view;
    }
}
