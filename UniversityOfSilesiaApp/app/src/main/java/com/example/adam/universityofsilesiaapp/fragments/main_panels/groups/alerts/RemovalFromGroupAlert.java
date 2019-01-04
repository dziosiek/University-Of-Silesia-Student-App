package com.example.adam.universityofsilesiaapp.fragments.main_panels.groups.alerts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
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
import com.example.adam.universityofsilesiaapp.MainActivity;
import com.example.adam.universityofsilesiaapp.R;
import com.example.adam.universityofsilesiaapp.fragments.main_panels.groups.Groups;
import com.example.adam.universityofsilesiaapp.fragments.main_panels.groups.recycler_view_components.RecyclerAdapter;
import com.example.adam.universityofsilesiaapp.fragments.main_panels.groups.recycler_view_components.RecyclerViewMyConstants;
import com.example.adam.universityofsilesiaapp.fragments_replacement.FragmentReplacement;
import com.example.adam.universityofsilesiaapp.resources.User;
import com.example.adam.universityofsilesiaapp.resources.UserGroups;
import com.example.adam.universityofsilesiaapp.variables.GlobalVariables;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemovalFromGroupAlert extends AppCompatDialogFragment {
    RecyclerAdapter adapter;
    User me;
    List<UserGroups> userGroups;
    Context context;
    int position;

    public RemovalFromGroupAlert() {
    }

    public void setParams(RecyclerAdapter adapter, User me, List<UserGroups> userGroups, Context context, int position) {
        this.adapter = adapter;
        this.me = me;
        this.userGroups = userGroups;
        this.context = context;
        this.position = position;

    }






    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        UserGroups groups = this.userGroups.get(this.position);


        builder.setTitle("Removal from the group").setMessage("Would you like to delete the group:\n"
                +groups.getSpecialization()+", year "+groups.getYear())
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteGroup();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }




    public void deleteGroup() {

        Toast.makeText(getContext(),adapter.toString(),Toast.LENGTH_SHORT).show();




        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "/jpa/users/" + me.getId()+ "/groups/"+this.userGroups.get(this.position).getId();
        Toast.makeText(context, GlobalVariables.getApiUrl() + url, Toast.LENGTH_SHORT).show();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.DELETE,
                        GlobalVariables.getApiUrl() + url,
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
                            Toast.makeText(context, "You cannot delete this group", Toast.LENGTH_SHORT).show();
                        } else if (networkResponse != null && networkResponse.statusCode == 400) {
                            Toast.makeText(context, "Bad request", Toast.LENGTH_SHORT).show();
                        }
                        else if (networkResponse != null && networkResponse.statusCode == 204) {
                            Toast.makeText(context, "No content", Toast.LENGTH_SHORT).show();
                        }


                        adapter.removeItem(position);
                        
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
