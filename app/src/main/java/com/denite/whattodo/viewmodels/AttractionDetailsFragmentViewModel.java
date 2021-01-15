package com.denite.whattodo.viewmodels;


import com.denite.whattodo.models.Attraction;

import androidx.lifecycle.ViewModel;

public class AttractionDetailsFragmentViewModel extends ViewModel
{
    private final String TAG = "AttractionDetailsFragmentViewModel";

    private Attraction attraction;
    private int photoPosition = 0;

    public Attraction getAttraction()
    {
        return attraction;
    }

    public void setAttraction(Attraction attraction)
    {
        this.attraction = attraction;
    }

    public int getPhotoPosition()
    {
        return photoPosition;
    }

    public void setPhotoPosition(int photoPosition)
    {
        this.photoPosition = photoPosition;
    }
}
