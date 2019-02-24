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
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.adam.universityofsilesiaapp.MainActivity;
import com.example.adam.universityofsilesiaapp.R;
import com.example.adam.universityofsilesiaapp.fragments.main_panels.events.EventsPanel;
import com.example.adam.universityofsilesiaapp.fragments.main_panels.groups.Groups;
import com.example.adam.universityofsilesiaapp.fragments.main_panels.groups.alerts.NoGroupAlert;
import com.example.adam.universityofsilesiaapp.fragments.main_panels.schedule.Schedule;
import com.example.adam.universityofsilesiaapp.fragments_replacement.FragmentReplacement;
import com.example.adam.universityofsilesiaapp.resources.User;


public class StudentPanel extends Fragment {

    User me;
    Integer selectedGroup;
    WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_groups:
//                Toast.makeText(getContext(),"myGroups",Toast.LENGTH_SHORT).show();
                getGroupsListFragment();
                break;
            case R.id.events:
//                Toast.makeText(getContext(),"Events",Toast.LENGTH_SHORT).show();
                getEvents();
                break;
            case R.id.schedule:
//                Toast.makeText(getContext(),"Schedule",Toast.LENGTH_SHORT).show();
                getScheduleFragment();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_options_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        me = FragmentReplacement.<User>getObjectFromBundle(getArguments(), "me");
        if (me.getGroups().isEmpty()) {
            new NoGroupAlert().show(getFragmentManager(), "NoGroupAlert");
            getGroupsListFragment();
        } else {
            selectedGroup = FragmentReplacement.<Integer>getObjectFromBundle(getArguments(), "selectedGroup");
//            Toast.makeText(getContext(),me.getGroups().get(selectedGroup).getSpecialization(),Toast.LENGTH_SHORT).show();
            ((MainActivity) getContext()).setTitle(me.getGroups().get(selectedGroup).getSpecialization() + ", year " +
                    me.getGroups().get(selectedGroup).getYear());
        }

        //WebView code block

        webView = (WebView) getView().findViewById(R.id.us_website);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.loadUrl("http://www.studenci.us.edu.pl/");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return false;
            }
        });
        //End of code
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_panel, container, false);
    }

    public void getGroupsListFragment() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("me", me);
        FragmentReplacement.pushFragment(getActivity(), R.id.startup_frame_layout_id, new Groups(), bundle);
    }

    public void getEvents() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("me", me);
        bundle.putInt("selectedGroup", selectedGroup);
        FragmentReplacement.pushFragment(getActivity(), R.id.startup_frame_layout_id, new EventsPanel(), bundle);
    }

    private void getScheduleFragment() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("me", me);
        bundle.putInt("selectedGroup", selectedGroup);
        FragmentReplacement.pushFragment(getActivity(), R.id.startup_frame_layout_id, new Schedule(), bundle);
    }
}
