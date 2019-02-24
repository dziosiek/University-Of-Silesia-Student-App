package com.example.adam.universityofsilesiaapp.fragments.main_panels.schedule;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.adam.universityofsilesiaapp.R;
import com.example.adam.universityofsilesiaapp.fragments_replacement.FragmentReplacement;
import com.example.adam.universityofsilesiaapp.resources.Timetable;
import com.example.adam.universityofsilesiaapp.resources.User;
import com.example.adam.universityofsilesiaapp.variables.GlobalVariables;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Schedule extends Fragment implements Serializable {

    WebView webView;
    User me;
    Integer selectedGroup;
    Spinner spinner;
    Timetable[] timetables;

    public Schedule() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner = (Spinner) getView().findViewById(R.id.my_spinner);

        webView = (WebView) getView().findViewById(R.id.schedule_web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.loadUrl("http://www.studenci.us.edu.pl/");

        me = FragmentReplacement.<User>getObjectFromBundle(getArguments(), "me");
        selectedGroup = FragmentReplacement.<Integer>getObjectFromBundle(getArguments(), "selectedGroup");

        loadURL();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                webView.loadUrl(timetables[position].getUrl());
//                Toast.makeText(getContext(), timetables[position].getUrl(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void loadURL() {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, GlobalVariables.getApiUrl() + "/jpa/student-groups/" + me.getGroups().get(selectedGroup).getId() + "/timetables/", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
//                Toast.makeText(getContext(),response.toString(),Toast.LENGTH_SHORT).show();
                if (response.length() > 0) {
                    Gson gson = new Gson();
                    timetables = gson.fromJson(response.toString(), Timetable[].class);
                    spinner.setAdapter(new ArrayAdapter<Timetable>(getContext(), R.layout.support_simple_spinner_dropdown_item, timetables));

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "The schedule cannot be loaded. Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {
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
