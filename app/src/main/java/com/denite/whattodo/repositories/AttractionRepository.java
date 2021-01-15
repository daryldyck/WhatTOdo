package com.denite.whattodo.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioTrack;
import android.util.Log;


import com.denite.whattodo.models.Attraction;
import com.denite.whattodo.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;

import static com.denite.whattodo.utils.Constants.LOGIN_CURRENT_USER;
import static com.denite.whattodo.utils.Constants.SHARED_PREF_NAME;

public class AttractionRepository
{
    private final String TAG = "AttractionRepository";
    private static SharedPreferences sharedPrefs;
    private static SharedPreferences.Editor prefEditor;
    private Context context;

    private static AttractionRepository instance;
    private ArrayList<Attraction> attractionArrayList = new ArrayList<Attraction>();
    private ArrayList<Attraction> attractionPreferenceArrayList = new ArrayList<Attraction>();

    public static AttractionRepository getInstance()
    {
        if (instance == null)
        {
            instance = new AttractionRepository();
        }
        return instance;
    }

    // get attractionsList
    public MutableLiveData<List<Attraction>> getAttractionArrayList(Context context)
    {
        this.context = context;
        getAttractions();
        sharedPrefs = context.getSharedPreferences(SHARED_PREF_NAME, 0);
        prefEditor = sharedPrefs.edit();

        MutableLiveData<List<Attraction>> data = new MutableLiveData<List<Attraction>>();
        data.setValue(attractionArrayList);
        return data;
    }

    // get attractionsPreferenceList
    public MutableLiveData<List<Attraction>> getAttractionPreferenceArrayList(Context context)
    {
        getAttractionPreferences();

        MutableLiveData<List<Attraction>> data = new MutableLiveData<List<Attraction>>();
        data.setValue(attractionPreferenceArrayList);
        return data;
    }

    /**
     * Get data from json file.
     */
    private void getAttractions()
    {
        // ensure list is empty before loading file.
        attractionArrayList.clear();

        String jsonFile = Utils.loadJsonFromAssets(context, "attractions.json");

        Utils.addAttractionsFromJsonString(false, jsonFile, attractionArrayList);
    }

    /**
     * Get data from json file.
     */
    private void getAttractionPreferences()
    {
        // ensure list is empty before loading file.
        attractionPreferenceArrayList.clear();

        // get user specific attraction preferences
        String fileName = sharedPrefs.getString(LOGIN_CURRENT_USER, null) + "_prefs.json";
        String jsonFile = Utils.loadJsonFromFile(context, fileName);

        Utils.addAttractionsFromJsonString(true, jsonFile, attractionPreferenceArrayList);

        // import preferences into attraction list
        for (Attraction attraction1 : attractionArrayList)
        {
            for (Attraction attraction2 : attractionPreferenceArrayList)
            {
                if (attraction1.getId() == attraction2.getId())
                {
                    attraction1.setWishList(attraction2.isWishList());
                    attraction1.setRating(attraction2.getRating());
                }
            }
        }
    }

    // save user attraction preferences
    public synchronized void savePrefs(Attraction attraction)
    {
        // add to preferences if is in wish list or user rated item
        if (attraction.isWishList() || attraction.getRating() > 0)
        {
            boolean inListAlready = false;

            for (Attraction attraction1 : attractionPreferenceArrayList)
            {
                if (attraction1.getId() == attraction.getId())
                {
                    inListAlready = true;
                    attraction1.setWishList(attraction.isWishList());
                    attraction1.setRating(attraction.getRating());
                }
            }

            if (!inListAlready)
            {
                attractionPreferenceArrayList.add(attraction);
            }
        }
        else // remove if not in wish list or rating not selected
        {
            boolean inListAlready = false;

            for (Attraction attraction1 : attractionPreferenceArrayList)
            {
                if (attraction1.getId() == attraction.getId())
                {
                    inListAlready = true;
                }
            }

            if (inListAlready)
            {
                for (int i = 0; i < attractionPreferenceArrayList.size(); i++)
                {
                    if (attractionPreferenceArrayList.get(i).getId() == attraction.getId())
                    {
                        attractionPreferenceArrayList.remove(i);
                    }
                }
            }
        }
        //save user preferences
        String fileName = sharedPrefs.getString(LOGIN_CURRENT_USER, null) + "_prefs.json";
        Utils.writeToFile(context, fileName, Utils.convertToAttractionsJson(attractionPreferenceArrayList));
    }
}
