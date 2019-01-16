package com.example.adam.universityofsilesiaapp.fragments.main_panels.events.recycler_view_components;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adam.universityofsilesiaapp.R;
import com.example.adam.universityofsilesiaapp.fragments.main_panels.events.EventDetailsDialog;
import com.example.adam.universityofsilesiaapp.fragments.main_panels.groups.recycler_view_components.ItemClickListener;
import com.example.adam.universityofsilesiaapp.resources.Event;
import com.example.adam.universityofsilesiaapp.resources.User;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    TextView title, date;
    private ItemClickListener itemClickListener;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.rw_events_title);
        date = (TextView) itemView.findViewById(R.id.rw_events_date);
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

    private List<Event> listData;
    private Context context;
    User me;

    public RecyclerAdapter(List<Event> listData, Context context, User me) {
        this.listData = listData;
        this.context = context;
        this.me = me;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.recycler_view_events_single_object,viewGroup,false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder recyclerViewHolder, int i) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:MM");

        recyclerViewHolder.title.setText(listData.get(i).getTitle());
        recyclerViewHolder.date.setText(listData.get(i).getDate());
        recyclerViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Event event = listData.get(position);
                EventDetailsDialog dialog = new EventDetailsDialog();
                dialog.setParams(event.getTitle(),event.getDescription(),event.getDate(),event.getUser().getFullName());
                dialog.show(((FragmentActivity) context).getSupportFragmentManager(),"EventDetailsDialog");
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void swapItems(List<Event> list){
        this.listData = list;
        notifyDataSetChanged();
    }

    public void removeItem(int position){
        listData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,listData.size());
    }
    public String info(){
        return this.toString();
    }

}

