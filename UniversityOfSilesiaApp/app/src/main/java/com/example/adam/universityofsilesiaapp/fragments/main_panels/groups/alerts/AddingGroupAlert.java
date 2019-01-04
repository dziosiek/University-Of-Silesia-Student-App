package com.example.adam.universityofsilesiaapp.fragments.main_panels.groups.alerts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.adam.universityofsilesiaapp.fragments.main_panels.groups.Groups;
import com.example.adam.universityofsilesiaapp.variables.GlobalVariables;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AddingGroupAlert extends AppCompatDialogFragment {

    String specialization, year;
    boolean groupAdded;
    Context context;
    Integer userId, groupId;

    public AddingGroupAlert() {
    }

    public void setParams(Context context, String specialization, String year, Integer userId, Integer groupId) {
        this.specialization = specialization;
        this.year = year;
        this.context = context;
        this.userId = userId;
        this.groupId = groupId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public boolean isGroupAdded() {
        return groupAdded;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        builder.setTitle("Joining to group").setMessage("Would you like to join to the group:\n"
                +getSpecialization()+", year "+getYear())
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        groupAdded = true;
                        addUserToGroup();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                groupAdded = false;
            }
        });
        return builder.create();
    }

    public void addUserToGroup() {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "/jpa/users/" + userId + "/groups";
        Toast.makeText(context, GlobalVariables.getApiUrl() + url, Toast.LENGTH_SHORT).show();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST,
                        GlobalVariables.getApiUrl() + url + "?group_id=" + groupId,
                        null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();



                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 409) {
                            Toast.makeText(context, "You are currently in this group", Toast.LENGTH_SHORT).show();
                        } else if (networkResponse != null && networkResponse.statusCode == 400) {
                            Toast.makeText(context, "400", Toast.LENGTH_SHORT).show();
                        }

                    }


                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }


        };

        queue.add(jsonObjectRequest);
    }

}