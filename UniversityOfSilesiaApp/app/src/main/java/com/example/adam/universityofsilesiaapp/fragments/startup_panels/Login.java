package com.example.adam.universityofsilesiaapp.fragments.startup_panels;

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
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.adam.universityofsilesiaapp.R;
import com.example.adam.universityofsilesiaapp.fragments.main_panels.StudentPanel;

import com.example.adam.universityofsilesiaapp.fragments_replacement.FragmentReplacement;
import com.example.adam.universityofsilesiaapp.resources.User;
import com.example.adam.universityofsilesiaapp.resources.UserGroups;
import com.example.adam.universityofsilesiaapp.variables.GlobalVariables;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Login extends Fragment {

    TextView tvLogin,tvPassword, btRegister;
    Button btLogin;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        tvLogin = (TextView) getView().findViewById(R.id.fragment_login_login_textview_id);
        tvPassword = (TextView) getView().findViewById(R.id.fragment_login_password_textview_id);
        btRegister = (TextView) getView().findViewById(R.id.fragment_login_register_textview_asbutton_id);
        btLogin = (Button) getView().findViewById(R.id.fragment_login_login_btn_id);
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentReplacement.pushFragment(getActivity(),R.id.startup_frame_layout_id,new Register());
            }
        });
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginM();
            }
        });

    }

    //Hiding Options
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//    }
//
//    @Override
//    public void onPrepareOptionsMenu(Menu main_options_menu) {
//        main_options_menu.findItem(R.id.add_specialization).setVisible(false);
//        super.onPrepareOptionsMenu(main_options_menu);
//    }
    //Hiding Options


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_login, container, false);
    }


    private void loginM() {

        RequestQueue queue = Volley.newRequestQueue(getContext());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("login",tvLogin.getText().toString());
            jsonObject.put("password",tvPassword.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, GlobalVariables.getApiUrl()+"/jpa/login", jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Gson gson = new Gson();
                        try {
                            User user= gson.fromJson(response.getString("user-resource"),User.class);
                           UserGroups[] array = gson.fromJson(response.getString("groups-resource"),UserGroups[].class);
                            List<UserGroups> userGroups = new ArrayList<>(Arrays.asList(array));
                            user.setGroups(userGroups);


                            Bundle bundle = new Bundle();
                            bundle.putSerializable("me",user);
                            bundle.putInt("selectedGroup",0);

                            FragmentReplacement.pushFragment(getActivity(),
                                    R.id.startup_frame_layout_id, new StudentPanel(), bundle);

//                            Toast.makeText(getContext(),user.getGroups().get(0).getSpecialization(),Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }





                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 404) {
                            Toast.makeText(getContext(),"Incorrect user or password",Toast.LENGTH_SHORT).show();
                        }
                        else if(networkResponse != null && networkResponse.statusCode == 400) {
                            Toast.makeText(getContext(),"BadRequest",Toast.LENGTH_SHORT).show();
                        }

                    }


                }){
//
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
//


// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

    }

}

