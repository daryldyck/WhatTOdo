package com.denite.whattodo.viewmodels;

import android.app.Application;


import com.denite.whattodo.models.Attraction;
import com.denite.whattodo.repositories.AttractionRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class AttractionFragmentViewModel extends AndroidViewModel
{
    private final String TAG = "AttractionFragmentViewModel";
    private MutableLiveData<List<Attraction>> attractionList;
    private MutableLiveData<List<Attraction>> attractionListPreferences;
    private AttractionRepository attractionRepository;
    private Application application;

    public AttractionFragmentViewModel(@NonNull Application application)
    {
        super(application);
        this.application = application;
    }

    // load attractions and attraction user preferences from repositories
    public void init()
    {
        if (attractionList != null)
        {
            return;
        }
        attractionRepository = AttractionRepository.getInstance();
        attractionList = attractionRepository.getAttractionArrayList(application);
        attractionListPreferences = attractionRepository.getAttractionPreferenceArrayList(application);
    }

    public LiveData<List<Attraction>> getAttractionList()
    {
        return attractionList;
    }

    public LiveData<List<Attraction>> getAttractionPreferencesList()
    {
        return attractionListPreferences;
    }

    // save user attraction preferences onto device
    public void savePrefs(Attraction attraction)
    {
        attractionRepository.savePrefs(attraction);
    }

}
