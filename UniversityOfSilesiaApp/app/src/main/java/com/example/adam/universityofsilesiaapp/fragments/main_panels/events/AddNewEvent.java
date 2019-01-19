package com.example.adam.universityofsilesiaapp.fragments.main_panels.events;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.adam.universityofsilesiaapp.R;
import com.example.adam.universityofsilesiaapp.fragments.dialogs.TimePickerFragment;
import com.example.adam.universityofsilesiaapp.fragments_replacement.FragmentReplacement;
import com.example.adam.universityofsilesiaapp.resources.Event;
import com.example.adam.universityofsilesiaapp.resources.User;
import com.example.adam.universityofsilesiaapp.variables.GlobalVariables;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AddNewEvent extends Fragment {

    User me;
    Integer selectedGroup=null;
    EditText titleTV, descriptionTV, dateTV, timeTV;
    Button addBtn;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;

    public AddNewEvent() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Toast.makeText(getContext(),"destroyed",Toast.LENGTH_LONG).show();
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
        titleTV = getView().findViewById(R.id.add_event_fragment_title);
        descriptionTV = getView().findViewById(R.id.add_event_fragment_description);
//        descriptionTV.setMovementMethod(new ScrollingMovementMethod());
        dateTV = getView().findViewById(R.id.add_event_fragment_date);
        timeTV = getView().findViewById(R.id.add_event_fragment_time);
        disableSoftInputFromAppearing(dateTV);
        disableSoftInputFromAppearing(timeTV);
        addBtn = getView().findViewById(R.id.add_event_fragment_add_btn);
        me = FragmentReplacement.<User>getObjectFromBundle(getArguments(),"me");
        selectedGroup = FragmentReplacement.<Integer>getObjectFromBundle(getArguments(),"selectedGroup");
//        Toast.makeText(getContext(),me.toString()+selectedGroup,Toast.LENGTH_SHORT).show();
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent();
            }
        });

        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }
        };
        timeTV.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    TimePickerFragment timePickerFragment = new TimePickerFragment();
                    timePickerFragment.setFieldId(R.id.add_event_fragment_time);
                    timePickerFragment.show(getFragmentManager(),"timePickerFragment");
                    v.clearFocus();
                }
            }
        });
//        timeTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TimePickerFragment timePickerFragment = new TimePickerFragment();
//                timePickerFragment.setFieldId(R.id.add_event_fragment_time);
//                timePickerFragment.show(getFragmentManager(),"timePickerFragment");
//            }
//        });

        dateTV.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    new DatePickerDialog(getContext(), date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
                v.clearFocus();
            }
        });
//        dateTV.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                new DatePickerDialog(getContext(), date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//            }
//        });
    }
    public void updateDate(){
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        dateTV.setText(sdf.format(myCalendar.getTime()));
    }
    public void addEvent()  {
        RequestQueue queue = Volley.newRequestQueue(getContext());


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Gson gson = new Gson();
        JSONObject jsonObject= null;
        String date = dateTV.getText().toString() + " " + timeTV.getText().toString();

        try {
            jsonObject = new JSONObject(gson.toJson(new Event(date, titleTV.getText().toString(), descriptionTV.getText().toString())));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                GlobalVariables.getApiUrl() + "/jpa/student-groups/" + me.getGroups().get(0).getId() + "/events?user_id=" + me.getId(), jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getContext(),"Event added",Toast.LENGTH_SHORT).show();
                FragmentReplacement.pushFragment(getActivity(),R.id.startup_frame_layout_id,new EventsPanel(),getArguments());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();

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

    public static void disableSoftInputFromAppearing(EditText editText) {
        if (Build.VERSION.SDK_INT >= 11) {
            editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
            editText.setTextIsSelectable(true);
        } else {
            editText.setRawInputType(InputType.TYPE_NULL);
            editText.setFocusable(true);
        }
    }
}
