package com.denite.whattodo.viewmodels;


import android.app.Application;

import com.denite.whattodo.fragments.AttractionListFragment;
import com.denite.whattodo.fragments.HomeFragment;
import com.denite.whattodo.fragments.WebViewFragment;
import com.denite.whattodo.models.Attraction;
import com.denite.whattodo.repositories.AttractionRepository;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import static com.denite.whattodo.utils.Constants.PAGE_HOME;


public class MainActivityViewModel extends ViewModel
{
    private final String TAG = "MainActivityViewModel";

    public boolean initialized = false;
    private int currentPage = PAGE_HOME;

    private HomeFragment homeFragment;
    private AttractionListFragment attractionFragment;

    public boolean isInitialized()
    {
        return initialized;
    }

    public void setInitialized(boolean initialized)
    {
        this.initialized = initialized;
    }

    public int getCurrentPage()
    {
        return currentPage;
    }

    public void setCurrentPage(int currentPage)
    {
        this.currentPage = currentPage;
    }

    public HomeFragment getHomeFragment()
    {
        return homeFragment;
    }

    public void setHomeFragment(HomeFragment homeFragment)
    {
        this.homeFragment = homeFragment;
    }

    public AttractionListFragment getAttractionListFragment()
    {
        return attractionFragment;
    }

    public void setAttractionListFragment(AttractionListFragment attractionFragment)
    {
        this.attractionFragment = attractionFragment;
    }
}
