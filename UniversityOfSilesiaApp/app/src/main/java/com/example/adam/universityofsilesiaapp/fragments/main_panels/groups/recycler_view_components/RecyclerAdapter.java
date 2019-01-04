package com.example.adam.universityofsilesiaapp.fragments.main_panels.groups.recycler_view_components;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adam.universityofsilesiaapp.R;
import com.example.adam.universityofsilesiaapp.fragments.main_panels.StudentPanel;
import com.example.adam.universityofsilesiaapp.fragments.main_panels.groups.alerts.AddingGroupAlert;
import com.example.adam.universityofsilesiaapp.fragments.main_panels.groups.alerts.RemovalFromGroupAlert;
import com.example.adam.universityofsilesiaapp.fragments_replacement.FragmentReplacement;
import com.example.adam.universityofsilesiaapp.resources.User;
import com.example.adam.universityofsilesiaapp.resources.UserGroups;

import java.util.List;

class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {



    TextView specialization,year;

    private ItemClickListener itemClickListener;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        specialization = (TextView) itemView.findViewById(R.id.rw_students_group_specialization);
        year = (TextView) itemView.findViewById(R.id.rw_students_group_year);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;

    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v,getAdapterPosition(),false);
    }

    @Override
    public boolean onLongClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),true);
        return true;
    }
}


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{

    private List<UserGroups> listData;
    private Context context;
    User me;
    private int mode;




    public RecyclerAdapter(List<UserGroups> listData, Context context, User me) {
        this.listData = listData;
        this.context = context;
        this.me = me;

    }




    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.recycler_view_students_groups,viewGroup,false);
        return new RecyclerViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder recyclerViewHolder, int i) {

        recyclerViewHolder.specialization.setText(listData.get(i).getSpecialization());
        recyclerViewHolder.year.setText("year "+listData.get(i).getYear());

        recyclerViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {



                if(isLongClick){
                    UserGroups userGroups = listData.get(position);

                    if(mode == RecyclerViewMyConstants.MY_GROUPS_MODE){
                        RemovalFromGroupAlert alert = new RemovalFromGroupAlert();
                        alert.setParams
                                (RecyclerAdapter.this,
                                RecyclerAdapter.this.me,
                                RecyclerAdapter.this.listData,
                                RecyclerAdapter.this.context,
                                        position);
                        alert.show(((FragmentActivity) context).getSupportFragmentManager(),"RemovalGroupAlert");

                    }
                    else if(mode == RecyclerViewMyConstants.ALL_GROUPS_MODE){
                        AddingGroupAlert alert = new AddingGroupAlert();
                        alert.setParams(context,userGroups.getSpecialization(),
                                userGroups.getYear(),me.getId(),listData.get(position).getId());
                        alert.show(((FragmentActivity) context).getSupportFragmentManager(),"AddingGroupAlert");
                    }


                }
                else if(mode ==RecyclerViewMyConstants.MY_GROUPS_MODE){

                    Bundle bundle = new Bundle();
                    me.setGroups(listData);
                    bundle.putSerializable("me",me);
                    bundle.putInt("selectedGroup",position);
                    Toast.makeText(context,Integer.toString(position),Toast.LENGTH_SHORT).show();
                    FragmentReplacement.pushFragment((Activity) context,
                            R.id.startup_frame_layout_id, new StudentPanel(),bundle);
                }



            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void swapItems(List<UserGroups> list, int mode){
        this.mode = mode;
        this.listData = list;
        notifyDataSetChanged();
    }

    public void removeItem(int position){
        listData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,listData.size());
    }



}