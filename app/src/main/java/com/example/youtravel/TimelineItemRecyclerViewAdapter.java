package com.example.youtravel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TimelineItemRecyclerViewAdapter extends RecyclerView.Adapter<TimelineItemRecyclerViewAdapter.MyViewHolder>{
    Context context;
    ArrayList<TimelineItemModel> timelineItemModel;

    public TimelineItemRecyclerViewAdapter(Context context, ArrayList<TimelineItemModel> timelineItemModel) {
        this.context = context;
        this.timelineItemModel = timelineItemModel;
    }

    @NonNull
    @Override
    public TimelineItemRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.timeline_item,parent,false);
        return new TimelineItemRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineItemRecyclerViewAdapter.MyViewHolder holder, int position) {
        String imageUrl = timelineItemModel.get(position).getImageUrl();
        String imageDate = timelineItemModel.get(position).getImageDate();
        String imageLocation = timelineItemModel.get(position).getImageLocation();
        String imageRating = timelineItemModel.get(position).getImageRating();

        holder.imageDate.setText(imageDate);
        holder.imageLocation.setText(imageLocation);
        holder.imageRating.setText(imageRating);
        Picasso.get().load(imageUrl).fit().into(holder.image);
    }

    @Override
    public int getItemCount() {
        return timelineItemModel.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView imageDate, imageLocation, imageRating;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.itemImage);
            imageDate = itemView.findViewById(R.id.itemDate);
            imageLocation = itemView.findViewById(R.id.itemLocation);
            imageRating = itemView.findViewById(R.id.itemRating);
        }
    }
}
