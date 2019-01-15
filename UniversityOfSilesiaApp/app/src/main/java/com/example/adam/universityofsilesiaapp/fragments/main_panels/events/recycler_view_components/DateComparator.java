package com.example.adam.universityofsilesiaapp.fragments.main_panels.events.recycler_view_components;

import com.example.adam.universityofsilesiaapp.resources.Event;

import java.text.ParseException;
import java.util.Comparator;

public class DateComparator implements Comparator<Event> {

    @Override
    public int compare(Event o1, Event o2) {
        try {
            return o1.getDateAsDateFormat().compareTo(o2.getDateAsDateFormat());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 1;
    }
}