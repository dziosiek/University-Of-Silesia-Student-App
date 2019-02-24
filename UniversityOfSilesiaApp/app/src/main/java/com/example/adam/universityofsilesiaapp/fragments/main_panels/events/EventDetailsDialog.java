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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.adam.universityofsilesiaapp.R;
import com.example.adam.universityofsilesiaapp.fragments.main_panels.events.recycler_view_components.RecyclerAdapter;
import com.example.adam.universityofsilesiaapp.variables.GlobalVariables;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EventDetailsDialog extends DialogFragment {

    String title, desc, date, addedBy;
    RecyclerAdapter recyclerAdapter;
    int position, eventId, groupId;

    public void setParams(String title, String desc, String date, String addedBy, int position, int eventId, int groupId) {
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.addedBy = addedBy;
        this.position = position;
        this.eventId = eventId;
        this.groupId = groupId;
    }

    public void setRecyclerAdapter(RecyclerAdapter recyclerAdapter) {
        this.recyclerAdapter = recyclerAdapter;
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
                }).setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recyclerAdapter.removeItem(position);
                RequestQueue queue = Volley.newRequestQueue(getContext());
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE,
                        GlobalVariables.getApiUrl() + "/jpa/student-groups/" + groupId + "/events/" + eventId, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");
                        return headers;

                    }
                };

                queue.add(request);

            }
        });
        return builder.create();
    }
}
