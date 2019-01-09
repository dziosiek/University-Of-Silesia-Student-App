package com.example.adam.universityofsilesiaapp.fragments.main_panels.groups;


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

import com.example.adam.universityofsilesiaapp.fragments.main_panels.groups.recycler_view_components.RecyclerAdapter;
import com.example.adam.universityofsilesiaapp.fragments.main_panels.groups.recycler_view_components.RecyclerViewMyConstants;
import com.example.adam.universityofsilesiaapp.fragments_replacement.FragmentReplacement;
import com.example.adam.universityofsilesiaapp.resources.User;
import com.example.adam.universityofsilesiaapp.resources.UserGroups;
import com.example.adam.universityofsilesiaapp.variables.GlobalVariables;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Groups extends Fragment {

    List<UserGroups> groupsList;
    User me;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    RecyclerAdapter adapter;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_to_group:
                Toast.makeText(getContext(),"Add to group",Toast.LENGTH_SHORT).show();
                listOfAllGroups();
                break;
            case R.id.choose_a_group:
                listOfMyGroups();
                Toast.makeText(getContext(),"Choose a group",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.groups_options_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_groups, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        me = FragmentReplacement.<User>getObjectFromBundle(getArguments(),"me");
        recyclerView = (RecyclerView) getView().findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(new ArrayList<UserGroups>(),getContext(),me);
        recyclerView.setAdapter(adapter);

        Toast.makeText(getContext(),adapter.toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(me.getGroups().isEmpty())
            listOfAllGroups();
        else
            listOfMyGroups();
    }

    public void listOfMyGroups(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                GlobalVariables.getApiUrl()+"/jpa/users/"+me.getId()+"/groups",
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Bundle bundle = new Bundle();
                Gson gson = new Gson();
                List<UserGroups> list = new ArrayList<UserGroups>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        UserGroups group = gson.fromJson(response.getJSONObject(i).toString(),UserGroups.class);
                        list.add(group);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.swapItems(list,RecyclerViewMyConstants.MY_GROUPS_MODE);
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

    public void listOfAllGroups(){
            RequestQueue queue = Volley.newRequestQueue(getContext());
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, GlobalVariables.getApiUrl()+"/jpa/student-groups", null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Bundle bundle = new Bundle();
                    Gson gson = new Gson();
                    List<UserGroups> list = new ArrayList<UserGroups>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            UserGroups group = gson.fromJson(response.getJSONObject(i).toString(),UserGroups.class);
                            list.add(group);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.swapItems(list,RecyclerViewMyConstants.ALL_GROUPS_MODE);
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
}
