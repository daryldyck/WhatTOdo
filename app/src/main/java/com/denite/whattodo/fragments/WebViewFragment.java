package com.denite.whattodo.fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.denite.whattodo.R;

import androidx.fragment.app.Fragment;
import androidx.transition.TransitionInflater;

import static com.denite.whattodo.utils.Constants.EXTRA_URL_STRING;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WebViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebViewFragment extends Fragment
{
    private final static String TAG = "MapFragment";
    private View rootView;
    private String url;

    public WebViewFragment()
    {
    }

    public static WebViewFragment newInstance(String url)
    {
        WebViewFragment fragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_URL_STRING, url);
        fragment.setArguments(bundle);
        return fragment;
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

        if (getArguments() != null)
        {
            url = getArguments().getString(EXTRA_URL_STRING);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
        {
            url = savedInstanceState.getString(EXTRA_URL_STRING);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.clear();
        outState.putString(EXTRA_URL_STRING, url);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_webview, container, false);
        setup();
        return rootView;
    }


    // initialize views
    @SuppressLint("SetJavaScriptEnabled")
    private void setup()
    {
        if (getContext() != null)
        {
            final ProgressBar loadingProgressBar = rootView.findViewById(R.id.loading_progressBar);

            WebView webView = rootView.findViewById(R.id.webView);
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(url);
            webView.setWebViewClient(new WebViewClient()
            {
                public void onPageFinished(WebView view, String url)
                {
                    // turn off loading progressBar
                    loadingProgressBar.setVisibility(View.GONE);
                }
            });
        }
    }
}