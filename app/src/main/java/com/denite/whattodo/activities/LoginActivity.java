package com.denite.whattodo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.denite.whattodo.R;
import com.denite.whattodo.models.User;
import com.denite.whattodo.utils.Utils;
import com.denite.whattodo.viewmodels.LoginActivityViewModel;
import com.google.android.material.textfield.TextInputLayout;

import static com.denite.whattodo.utils.Constants.LOGIN_CURRENT_USER;
import static com.denite.whattodo.utils.Constants.LOGIN_REMEMBER_ME;
import static com.denite.whattodo.utils.Constants.LOGIN_REMEMBER_ME_DEFAULT;
import static com.denite.whattodo.utils.Constants.SHARED_PREF_NAME;
import static com.denite.whattodo.utils.Constants.THEME_PREFERENCE;
import static com.denite.whattodo.utils.Constants.THEME_PREFERENCE_DEFAULT;

public class LoginActivity extends AppCompatActivity
{
    private final String TAG = "MainActivity";
    private static SharedPreferences sharedPrefs;
    private static SharedPreferences.Editor prefEditor;
    private LoginActivityViewModel viewModel;

    private TextInputLayout userNameEditText;
    private TextInputLayout passwordEditText;
    private CheckBox rememberMeCheckBox;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        sharedPrefs = getSharedPreferences(SHARED_PREF_NAME, 0);
        prefEditor = sharedPrefs.edit();
        Utils.applyTheme(sharedPrefs.getInt(THEME_PREFERENCE, THEME_PREFERENCE_DEFAULT));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initiate viewModel and load Users
        viewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);
        viewModel.init();

        // check for remember me and auto login user
        if (sharedPrefs.getBoolean(LOGIN_REMEMBER_ME, LOGIN_REMEMBER_ME_DEFAULT) && sharedPrefs.getString(LOGIN_CURRENT_USER, null) != null)
        {
            login(getCurrentUser(sharedPrefs.getString(LOGIN_CURRENT_USER, null)));
        }
        else
        {
            setup();
        }
    }

    // setup all fields and views
    private void setup()
    {
        userNameEditText = findViewById(R.id.userName_InputLayout);
        Utils.setHaptic(userNameEditText);
        userNameEditText.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus)
                {
                    Utils.clearError(userNameEditText);
                }
            }
        });

        passwordEditText = findViewById(R.id.password_InputLayout);
        Utils.setHaptic(passwordEditText);
        passwordEditText.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus)
                {
                    Utils.clearError(userNameEditText);
                }
            }
        });

        rememberMeCheckBox = findViewById(R.id.rememberMe_checkBox);
        Utils.setHaptic(rememberMeCheckBox);
        rememberMeCheckBox.setChecked(sharedPrefs.getBoolean(LOGIN_REMEMBER_ME, LOGIN_REMEMBER_ME_DEFAULT));
        rememberMeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    prefEditor.putBoolean(LOGIN_REMEMBER_ME, true).commit();
                }
                else
                {
                    prefEditor.putBoolean(LOGIN_REMEMBER_ME, false).commit();
                }
            }
        });

        loginButton = findViewById(R.id.login_button);
        Utils.setHaptic(loginButton);
        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Utils.hideKeyboard(LoginActivity.this, loginButton);

                if (checkForErrors())
                {
                    return;
                }

                if (userExists() && passwordCorrect())
                {
                    login(viewModel.getCurrentUser());
                }
            }
        });
    }

    // check for errors in user input
    private boolean checkForErrors()
    {
        boolean errors = false;

        if (Utils.checkTextInputIsNull(this, userNameEditText))
        {
            errors = true;
        }

        if (Utils.checkTextInputIsNull(this, passwordEditText))
        {
            errors = true;
        }

        // if either editText are null - return before checking for user.
        if (errors)
        {
            return true;
        }

        if (!userExists())
        {
            errors = true;
        }

        return errors;
    }

    // verify user exists in login data
    private boolean userExists()
    {
        boolean userExists = false;
        String userNameToCheck = userNameEditText.getEditText().getText().toString().trim().toLowerCase();

        for (User user : viewModel.getUsersList().getValue())
        {
            if (user.getUserName().contentEquals(userNameToCheck))
            {
                userExists = true;
            }
        }

        if (!userExists)
        {
            Utils.setEditTextError(this, userNameEditText);
            Utils.snackbar(this, getString(R.string.user_does_not_exist));
        }
        return userExists;
    }

    // check for correct password
    private boolean passwordCorrect()
    {
        boolean isCorrect;
        viewModel.setCurrentUser(getCurrentUser(userNameEditText.getEditText().getText().toString().trim().toLowerCase()));
        isCorrect = passwordEditText.getEditText().getText().toString().contentEquals(viewModel.getCurrentUser().getPassword());

        if (!isCorrect)
        {
            Utils.setEditTextError(this, passwordEditText);
            Utils.snackbar(this, getString(R.string.incorrect_password));
        }
        return isCorrect;
    }

    // get specific user from login data
    private User getCurrentUser(String userName)
    {
        Log.d(TAG, "getCurrentUser: " + userName);
        for (User userToCheck : viewModel.getUsersList().getValue())
        {
            if (userToCheck.getUserName().contentEquals(userName))
            {
                return userToCheck;
            }
        }
        return null;
    }

    // log user in and move to MainActivity
    private void login(User user)
    {
        prefEditor.putString(LOGIN_CURRENT_USER, user.getUserName()).commit();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}