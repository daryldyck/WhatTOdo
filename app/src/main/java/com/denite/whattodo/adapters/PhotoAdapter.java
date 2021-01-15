package com.denite.whattodo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denite.whattodo.R;
import com.denite.whattodo.activities.MainActivity;
import com.denite.whattodo.fragments.AttractionDetailsFragment;
import com.denite.whattodo.viewholders.PhotoViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<String> photoList = new ArrayList<String>();
    private AttractionDetailsFragment attractionDetailsFragment;

    public PhotoAdapter(AttractionDetailsFragment attractionDetailsFragment, List<String> photoList)
    {
        this.attractionDetailsFragment = attractionDetailsFragment;
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_attraction_photo, parent, false);
        return new PhotoViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {

        final PhotoViewHolder photoViewHolder = (PhotoViewHolder) holder;
        photoViewHolder.loadFields(this, photoList.get(position));

    }

    @Override
    public int getItemCount()
    {
        return photoList.size();
    }

    public AttractionDetailsFragment getAttractionDetailsFragment()
    {
        return attractionDetailsFragment;
    }
}
