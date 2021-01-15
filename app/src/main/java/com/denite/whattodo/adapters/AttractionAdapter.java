package com.denite.whattodo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.denite.whattodo.R;
import com.denite.whattodo.activities.MainActivity;
import com.denite.whattodo.fragments.AttractionListFragment;
import com.denite.whattodo.models.Attraction;
import com.denite.whattodo.viewholders.AttractionHeaderViewHolder;
import com.denite.whattodo.viewholders.AttractionPreviewViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.denite.whattodo.utils.Constants.TYPE_HEADER;
import static com.denite.whattodo.utils.Constants.TYPE_ITEM;

public class AttractionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<Attraction> attractionsList = new ArrayList<Attraction>();
    private MainActivity mainActivity;
    private AttractionListFragment attractionListFragment;

    public AttractionAdapter(MainActivity mainActivity, AttractionListFragment attractionListFragment, List<Attraction> attractionsList)
    {
        this.mainActivity = mainActivity;
        this.attractionListFragment = attractionListFragment;
        this.attractionsList = attractionsList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if (viewType == TYPE_ITEM)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_attraction_preview, parent, false);
            return new AttractionPreviewViewHolder(view);
        }
        else if (viewType == TYPE_HEADER)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attraction_header, parent, false);
            return new AttractionHeaderViewHolder(view);
        }
        else return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        if (holder instanceof AttractionPreviewViewHolder)
        {
            final AttractionPreviewViewHolder attractionPreviewViewHolder = (AttractionPreviewViewHolder) holder;
            attractionPreviewViewHolder.loadFields(this, attractionsList.get(position - 1)); // -1 due to header}

        }
        else if (holder instanceof AttractionHeaderViewHolder)
        {
            // header
            // no updated required
        }
    }

    @Override
    public int getItemCount()
    {
        return attractionsList.size() + 1; // +1 due to header}
    }

    @Override // used to determine if this is the header, or attraction preview
    public int getItemViewType(int position)
    {
        if (position == 0)
        {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    public MainActivity getMainActivity()
    {
        return mainActivity;
    }

    public void saveAttractionPref(Attraction attraction)
    {
        attractionListFragment.savePrefs(attraction);
    }
}
