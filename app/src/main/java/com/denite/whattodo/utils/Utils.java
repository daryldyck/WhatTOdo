package com.denite.whattodo.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.denite.whattodo.R;
import com.denite.whattodo.models.Attraction;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatDelegate;

public class Utils
{
    private static final String TAG = "Utils";

    // apply user selected theme
    public static void applyTheme(int mode)
    {
        if (AppCompatDelegate.getDefaultNightMode() != mode)
        {
            AppCompatDelegate.setDefaultNightMode(mode);
        }
    }

    // set haptic vibration on view
    public synchronized static void setHaptic(View view)
    {
        view.setHapticFeedbackEnabled(true);
        view.setOnTouchListener(new HapticListner());
    }

    // display snackBar
    public static void snackbar(Activity activity, String string)
    {
        try
        {
            Snackbar snackbar = Snackbar.make(activity.findViewById(R.id.main_coordinatorLayout), string, Snackbar.LENGTH_SHORT);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundResource(R.drawable.snack_bar);
            TextView textView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(activity.getResources().getColor(R.color.white));
            textView.setMaxLines(5);
            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
            snackbar.show();
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // clear error on textView
    public synchronized static void clearError(TextInputLayout textInputLayout)
    {
        textInputLayout.setError(null);
        textInputLayout.setErrorEnabled(false);
    }

    // check if editText is null
    public synchronized static boolean checkTextInputIsNull(Context context, TextInputLayout textInputLayout)
    {
        if (textInputLayout.getEditText().getText() == null || textInputLayout.getEditText().length() == 0)
        {
            setEditTextError(context, textInputLayout);
            return true;
        }
        return false;
    }

    // set error on editText
    public synchronized static void setEditTextError(Context context, TextInputLayout textInputLayout)
    {
        textInputLayout.setError(context.getString(R.string.mandatory_field));
        textInputLayout.setErrorEnabled(true);
    }

    // hide keyboard
    public synchronized static void hideKeyboard(Activity activity, View view)
    {
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Load json file from assets
     *
     * @param context app context
     * @param fileName - json file name
     * @return
     */
    public synchronized static String loadJsonFromAssets(Context context, String fileName)
    {
        String jsonString;

        try
        {
            InputStream fileData = context.getAssets().open(fileName);
            int fileSize = fileData.available();
            byte[] buffer = new byte[fileSize];
            fileData.read(buffer);
            fileData.close();
            jsonString = new String(buffer, "UTF-8");

            return jsonString;
        }
        catch (IOException e)
        {
            Log.d(TAG, "Error opening file.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Load json file from user specified file name (within internal app directory)
     *
     * @param context - app context
     * @param fileName - json file name
     * @return
     */
    public synchronized static String loadJsonFromFile(Context context, String fileName)
    {
        String jsonString;
        File folderPath = new File(Environment.getDataDirectory() + "/data/" + context.getPackageName() + "/");
        final File file = new File(folderPath, fileName);

        if (file.exists())
        {
            try
            {
                FileInputStream fileInputStream = new FileInputStream(file);
                int fileSize = fileInputStream.available();
                byte[] buffer = new byte[fileSize];
                fileInputStream.read(buffer);
                fileInputStream.close();
                jsonString = new String(buffer, "UTF-8");

                return jsonString;
            }
            catch (IOException e)
            {
                Log.d(TAG, "Error opening file.");
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    // convert String to JSONArray
    public synchronized static JSONArray convertToJSONArray(String fileData)
    {
        JSONArray jsonData;
        try
        {
            jsonData = new JSONArray(fileData);
            return jsonData;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    // convert String to JSONObject
    public synchronized static JSONObject convertToJSONObject(String fileData)
    {
        JSONObject jsonData;
        try
        {
            jsonData = new JSONObject(fileData);
            return jsonData;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Add attractions to ArrayList from json String
     *
     * @param isPrefFile - is loading from original attractions json file, or from user attraction preferences
     * @param jsonFile
     * @param attractionArrayList - json String from file
     */
    public synchronized static void addAttractionsFromJsonString(boolean isPrefFile, String jsonFile, ArrayList<Attraction> attractionArrayList)
    {
        if (jsonFile != null)
        {
            try
            {
                JSONArray jsonArray = Utils.convertToJSONArray(jsonFile);

                if (jsonArray != null)
                {
                    for (int index = 0; index < jsonArray.length(); index++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(index);

                        List<String> photoList = new ArrayList<>();
                        JSONArray jsonPhotoArray = jsonObject.getJSONArray("photosList");

                        for (int j = 0; j < jsonPhotoArray.length(); j++)
                        {
                            String photo = jsonPhotoArray.getString(j);
                            photoList.add(jsonPhotoArray.getString(j));
                        }

                        // pref includes wishList & rating
                        if (isPrefFile)
                        {
                            // create attraction from json object
                            Attraction attraction = new Attraction(
                                    jsonObject.getInt("id"),
                                    jsonObject.getString("name"),
                                    jsonObject.getString("address"),
                                    photoList,
                                    jsonObject.getLong("phone"),
                                    jsonObject.getString("website"),
                                    jsonObject.getString("description"),
                                    jsonObject.getString("price"),
                                    jsonObject.getInt("rating"),
                                    jsonObject.getBoolean("wishList"));
                            attractionArrayList.add(attraction);
                        }
                        else
                        {
                            // create attraction from json object
                            Attraction attraction = new Attraction(
                                    jsonObject.getInt("id"),
                                    jsonObject.getString("name"),
                                    jsonObject.getString("address"),
                                    photoList,
                                    jsonObject.getLong("phone"),
                                    jsonObject.getString("website"),
                                    jsonObject.getString("description"),
                                    jsonObject.getString("price"));
                            attractionArrayList.add(attraction);
                        }
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     *  Convert ArrayList of attractions into JSON String
     *
     * @param attractionList - list of attractions
     * @return json String json String of attractions
     */
    public synchronized static String convertToAttractionsJson(List<Attraction> attractionList)
    {
        String jsonString = "";
        try
        {
            JSONArray jsonArray = new JSONArray();
            for (Attraction attraction : attractionList)
            {
                JSONArray photoList = new JSONArray();

                for (String photo : attraction.getPhotosList())
                {
                    photoList.put(photo);
                }

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", attraction.getId());
                jsonObject.put("name", attraction.getName());
                jsonObject.put("address", attraction.getAddress());
                jsonObject.put("photosList", photoList);
                jsonObject.put("phone", attraction.getPhone());
                jsonObject.put("website", attraction.getWebsite());
                jsonObject.put("description", attraction.getDescription());
                jsonObject.put("price", attraction.getPrice());
                jsonObject.put("rating", attraction.getRating());
                jsonObject.put("wishList", attraction.isWishList());

                jsonArray.put(jsonObject);
            }

            jsonString = jsonArray.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return jsonString;
    }

    // write json string to internal app storage
    public synchronized static void writeToFile(Context context, String fileName, String data)
    {
        Log.d(TAG, "writeToFile: data: " + data);
        File folderPath = new File(Environment.getDataDirectory() + "/data/" + context.getPackageName() + "/");

        // Make sure the path directory exists.
        if (!folderPath.exists())
        {
            // Make it, if it doesn't exit
            folderPath.mkdirs();
        }

        final File file = new File(folderPath, fileName);

        try
        {
            file.createNewFile();

            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);

            myOutWriter.close();

            fOut.flush();
            fOut.close();
            Log.d(TAG, "File: " + file.exists());
            Log.d(TAG, "File: " + file.getAbsolutePath());
        }
        catch (IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * calculate column qty for recyclerView
     *
     * @param context - app context to get app Resources
     * @param width   - column width
     * @return - qty of columns
     */
    public synchronized static int calculateNoOfColumns(Context context, float width)
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / width);
        return noOfColumns;
    }

    // remove unnecessary text from link for user display
    public synchronized static String getShorterUrl(String url)
    {
        return url.toLowerCase()
                .replace("https://", "")
                .replace("http://", "")
                .replace("www.", "");
    }

    // format long into phone String
    public synchronized static String formatPhoneNumber(Long number)
    {
        if (number != 0)
        {
            if (String.valueOf(number).length() == 11)
            {
                String firstDigit = String.valueOf(String.valueOf(number).charAt(0));
                String remainingNumber = String.valueOf(number).substring(1);
                return String.valueOf(remainingNumber).
                        replaceFirst("(\\d{3})(\\d{3})(\\d+)", firstDigit + " ($1) $2-$3");
            }
            else
            {
                return String.valueOf(number).replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
            }
        }
        else
        {
            return "No Phone";
        }
    }
}