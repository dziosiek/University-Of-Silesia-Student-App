package com.example.adam.universityofsilesiaapp.fragments.startup_panels;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.example.adam.universityofsilesiaapp.fragments.startup_panels.Login;
import com.example.adam.universityofsilesiaapp.fragments_replacement.FragmentReplacement;
import com.example.adam.universityofsilesiaapp.variables.GlobalVariables;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends Fragment {

    TextView login, password, firstname, lastname,email;
    Button register;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        login = (TextView) getView().findViewById(R.id.fragment_register_login_id);
        password = (TextView) getView().findViewById(R.id.fragment_register_password_id);
        firstname = (TextView) getView().findViewById(R.id.fragment_register_firstname_id);
        lastname= (TextView) getView().findViewById(R.id.fragment_register_lastname_id);
        email = (TextView) getView().findViewById(R.id.fragment_register_email_id);
        register = (Button) getView().findViewById(R.id.fragment_register_register_btn_id);
        register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        registerIn();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    void registerIn(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("login",login.getText().toString());
            jsonObject.put("password",password.getText().toString());
            jsonObject.put("firstname",firstname.getText().toString());
            jsonObject.put("lastname",lastname.getText().toString());
            jsonObject.put("email",email.getText().toString());
            Toast.makeText(getContext(),email.getText().toString(),Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST,
                        GlobalVariables.getApiUrl()+"/jpa/register",
                        jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getContext(),response.toString(),Toast.LENGTH_SHORT).show();
                        FragmentReplacement.pushFragment(getActivity(), R.id.startup_frame_layout_id,new Login());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 409) {
                            Toast.makeText(getContext(),"User exist",Toast.LENGTH_SHORT).show();
                        }
                        else if(networkResponse != null && networkResponse.statusCode == 400) {
                            Toast.makeText(getContext(),"BadRequest",Toast.LENGTH_SHORT).show();
                        }
                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        queue.add(jsonObjectRequest);
    }
}
