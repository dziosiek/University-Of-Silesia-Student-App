package com.example.adam.universityofsilesiaapp.fragments.main_panels.events;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.adam.universityofsilesiaapp.R;
import com.example.adam.universityofsilesiaapp.fragments.main_panels.events.recycler_view_components.DateComparator;
import com.example.adam.universityofsilesiaapp.fragments.main_panels.events.recycler_view_components.RecyclerAdapter;
import com.example.adam.universityofsilesiaapp.fragments.main_panels.groups.recycler_view_components.RecyclerViewMyConstants;
import com.example.adam.universityofsilesiaapp.fragments_replacement.FragmentReplacement;
import com.example.adam.universityofsilesiaapp.resources.Event;
import com.example.adam.universityofsilesiaapp.resources.User;
import com.example.adam.universityofsilesiaapp.resources.UserGroups;
import com.example.adam.universityofsilesiaapp.variables.GlobalVariables;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EventsPanel extends Fragment {

    User me;
    Integer selectedGroup;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    RecyclerAdapter adapter;

    public EventsPanel() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_events_panel, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                Toast.makeText(getContext(),"Settings",Toast.LENGTH_SHORT).show();
                break;
            case R.id.add_event:
                Toast.makeText(getContext(),"Add Event",Toast.LENGTH_SHORT).show();
                addNewEvent();
                break;
            case R.id.all_events:
                getEventsList();
                Toast.makeText(getContext(),"All Events",Toast.LENGTH_SHORT).show();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.events_panel_options_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        me = FragmentReplacement.<User>getObjectFromBundle(getArguments(),"me");
        selectedGroup = FragmentReplacement.<Integer>getObjectFromBundle(getArguments(),"selectedGroup");

        recyclerView = (RecyclerView) getView().findViewById(R.id.events_recycler_view_id);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(new ArrayList<Event>(),getContext(),me);
        recyclerView.setAdapter(adapter);
        getEventsList();
    }
    public void getEventsList(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, GlobalVariables.getApiUrl()+"/jpa/student-groups/"+me.getGroups().get(selectedGroup).getId()+"/events/", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Bundle bundle = new Bundle();
                Gson gson = new Gson();
                List<Event> list = new ArrayList<Event>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Event event= gson.fromJson(response.getJSONObject(i).toString(),Event.class);
//                        Toast.makeText(getContext(),event.toString(),Toast.LENGTH_SHORT).show();
                        list.add(event);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Collections.sort(list, new DateComparator());
                adapter.swapItems(list);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"error",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map headers = new HashMap();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        queue.add(request);
    }
    public void addNewEvent(){
        FragmentReplacement.pushFragment(getActivity(),R.id.startup_frame_layout_id,new AddNewEvent(),getArguments());
    }
}
