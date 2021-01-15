package com.denite.whattodo.repositories;

import android.content.Context;
import android.util.Log;

import com.denite.whattodo.models.User;
import com.denite.whattodo.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;

public class UserRepository
{
    private final String TAG = "UserRepository";
    private static UserRepository instance;
    private ArrayList<User> userArrayList = new ArrayList<User>();

    public static UserRepository getInstance()
    {
        if (instance == null)
        {
            instance = new UserRepository();
        }
        return instance;
    }

    // get userList
    public MutableLiveData<List<User>> getUserArrayList(Context context)
    {
        getUsers(context);

        MutableLiveData<List<User>> data = new MutableLiveData<List<User>>();
        data.setValue(userArrayList);
        return data;
    }

    /**
     * Get data from json file.
     *
     * @param context needed to read json file from assets
     */
    private void getUsers(Context context)
    {
        // ensure list is empty before loading file.
        userArrayList.clear();

        String jsonFile = Utils.loadJsonFromAssets(context, "logins.json");

        if (jsonFile != null)
        {
            JSONArray jsonArray = Utils.convertToJSONArray(jsonFile);

            if (jsonArray != null)
            {
                try
                {
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        userArrayList.add(new User(
                                jsonObject.getString("username"),
                                jsonObject.getString("password")));
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
