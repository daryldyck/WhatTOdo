package com.denite.whattodo.fragments;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.denite.whattodo.R;
import com.denite.whattodo.utils.ElasticAnimation;

import androidx.fragment.app.Fragment;
import androidx.transition.TransitionInflater;

import static com.denite.whattodo.utils.Constants.INITIAL_FRAGMENT_LOAD;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment
{
    private final static String TAG = "HomeFragment";
    private View rootView;

    private ImageView logo1ImageView;
    private TextView inTextView;
    private TextView torontoTextView;

    // used to track first load of fragment
    private boolean initialFragmentLoad = true;

    public HomeFragment()
    {
    }

    public static HomeFragment newInstance()
    {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // set fragment animations
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.fade));
            setExitTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.fade));
            setReenterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.fade));
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.move));
            setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.move));
        }

        // load values from saved state
        if (savedInstanceState != null)
        {
            initialFragmentLoad = savedInstanceState.getBoolean(INITIAL_FRAGMENT_LOAD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        setup();
        animateIntro();
        initialFragmentLoad = false;
        return rootView;
    }

    @Override // save values state
    public void onSaveInstanceState(Bundle outState)
    {
        outState.clear();
        outState.putBoolean(INITIAL_FRAGMENT_LOAD, initialFragmentLoad);
        super.onSaveInstanceState(outState);
    }

    // initialize views
    private void setup()
    {
        if (getContext() != null)
        {
            logo1ImageView = rootView.findViewById(R.id.logo_imageView);
            inTextView = rootView.findViewById(R.id.in_textView);
            torontoTextView = rootView.findViewById(R.id.toronto_textView);
        }
    }

    // animate logo intro
    private void animateIntro()
    {
        // only load animation on first load of fragment
        if (initialFragmentLoad)
        {
            // setup initial values for animation
            logo1ImageView.setAlpha(0f);
            logo1ImageView.setScaleX(0f);
            logo1ImageView.setScaleY(0f);

            inTextView.setAlpha(0f);
            inTextView.setScaleX(0f);
            inTextView.setScaleY(0f);

            torontoTextView.setAlpha(0f);
            torontoTextView.setScaleX(0f);
            torontoTextView.setScaleY(0f);

            logo1ImageView
                    .animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(1500)
                    .setInterpolator(new ElasticAnimation(0.2, 30))
                    .start();

            inTextView
                    .animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setInterpolator(new ElasticAnimation(0.2, 25))
                    .setDuration(1500)
                    .setStartDelay(500)
                    .start();

            torontoTextView
                    .animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setInterpolator(new ElasticAnimation(0.2, 25))
                    .setDuration(1500)
                    .setStartDelay(1000)
                    .start();
        }
    }
}