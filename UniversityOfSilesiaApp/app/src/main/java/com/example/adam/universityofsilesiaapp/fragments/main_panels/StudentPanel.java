package com.example.adam.universityofsilesiaapp.fragments.main_panels;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.example.adam.universityofsilesiaapp.MainActivity;
import com.example.adam.universityofsilesiaapp.R;
import com.example.adam.universityofsilesiaapp.fragments.main_panels.events.EventsPanel;
import com.example.adam.universityofsilesiaapp.fragments.main_panels.groups.Groups;
import com.example.adam.universityofsilesiaapp.fragments.main_panels.groups.alerts.NoGroupAlert;
import com.example.adam.universityofsilesiaapp.fragments_replacement.FragmentReplacement;
import com.example.adam.universityofsilesiaapp.resources.User;
import com.example.adam.universityofsilesiaapp.resources.UserGroups;
import com.example.adam.universityofsilesiaapp.variables.GlobalVariables;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StudentPanel extends Fragment {

    User me;
    Integer selectedGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.my_groups:
                Toast.makeText(getContext(),"myGroups",Toast.LENGTH_SHORT).show();
                getGroupsListFragment();
                break;
            case R.id.events:
                Toast.makeText(getContext(),"Events",Toast.LENGTH_SHORT).show();
                getEvents();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_options_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        me = FragmentReplacement.<User>getObjectFromBundle(getArguments(),"me");
        if(me.getGroups().isEmpty()){
            new NoGroupAlert().show(getFragmentManager(),"NoGroupAlert");
            getGroupsListFragment();
        }
        else {
            selectedGroup= FragmentReplacement.<Integer>getObjectFromBundle(getArguments(),"selectedGroup");
            Toast.makeText(getContext(),me.getGroups().get(selectedGroup).getSpecialization(),Toast.LENGTH_SHORT).show();
            ((MainActivity)getContext()).setTitle(me.getGroups().get(selectedGroup).getSpecialization());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_panel, container, false);
    }

    public void getGroupsListFragment(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("me", me);
        FragmentReplacement.pushFragment(getActivity(),R.id.startup_frame_layout_id, new Groups(), bundle);
    }

    public void getEvents(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("me", me);
        bundle.putInt("selectedGroup",selectedGroup);
        FragmentReplacement.pushFragment(getActivity(),R.id.startup_frame_layout_id,new EventsPanel(),bundle);
    }
}
