package com.dane.dni;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Dane on 10/25/2015.
 */
public class MenuDialogFragment extends DialogFragment {
    public MenuDialogFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu, container);
        getDialog().setTitle("Title");
        return view;
    }
}

