package com.example.adam.universityofsilesiaapp.fragments.main_panels.events;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.adam.universityofsilesiaapp.R;
import com.example.adam.universityofsilesiaapp.fragments.main_panels.events.recycler_view_components.DateComparator;
import com.example.adam.universityofsilesiaapp.fragments_replacement.FragmentReplacement;
import com.example.adam.universityofsilesiaapp.resources.Event;
import com.example.adam.universityofsilesiaapp.resources.User;
import com.example.adam.universityofsilesiaapp.variables.GlobalVariables;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddNewEvent extends Fragment {

    User me;
    Integer selectedGroup=null;
    TextView title, description, date, time;
    Button addBtn;

    public AddNewEvent() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_new_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = getView().findViewById(R.id.add_event_fragment_title);
        description = getView().findViewById(R.id.add_event_fragment_description);
        date = getView().findViewById(R.id.add_event_fragment_date);
        time = getView().findViewById(R.id.add_event_fragment_time);
        addBtn = getView().findViewById(R.id.add_event_fragment_add_btn);
        me = FragmentReplacement.<User>getObjectFromBundle(getArguments(),"me");
        selectedGroup = FragmentReplacement.<Integer>getObjectFromBundle(getArguments(),"selectedGroup");
        Toast.makeText(getContext(),me.toString()+selectedGroup,Toast.LENGTH_SHORT).show();
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent();
            }
        });
    }
    public void addEvent()  {
        RequestQueue queue = Volley.newRequestQueue(getContext());


        Gson gson = new Gson();
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(gson.toJson(new Event(new Date(),"title","desc")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                GlobalVariables.getApiUrl() + "/jpa/student-groups/" + me.getGroups().get(0).getId() + "/events?user_id=" + me.getId(), jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getContext(),response.toString(),Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();

            }
        });

        queue.add(request);
    }
}
