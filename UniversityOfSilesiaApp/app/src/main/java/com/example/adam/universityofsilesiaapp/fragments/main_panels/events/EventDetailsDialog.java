package com.example.adam.universityofsilesiaapp.fragments.main_panels.events;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.method.ScrollingMovementMethod;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.adam.universityofsilesiaapp.R;

public class EventDetailsDialog extends DialogFragment {

    String title, desc, date, addedBy;

    public void setParams(String title, String desc, String date, String addedBy){
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.addedBy = addedBy;
    }

    @Override
    public String toString() {
        return "EventDetailsDialog{" +
                "title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", date='" + date + '\'' +
                ", addedBy='" + addedBy + '\'' +
                '}';
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.EventDialog));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View content = inflater.inflate(R.layout.dialog_events_details, null);
        TextView title = content.findViewById(R.id.dialog_title);
        TextView desc = content.findViewById(R.id.dialog_description);
        TextView date = content.findViewById(R.id.dialog_date);
        TextView addedBy = content.findViewById(R.id.dialog_added_by);
        title.setText(this.title);
        desc.setText(this.desc);
        desc.setMovementMethod(new ScrollingMovementMethod());
        date.setText(this.date);
        addedBy.setText(this.addedBy);
        builder.setView(content)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EventDetailsDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
