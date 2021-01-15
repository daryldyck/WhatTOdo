package com.denite.whattodo.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.denite.whattodo.R;
import com.denite.whattodo.activities.MainActivity;
import com.denite.whattodo.adapters.AttractionAdapter;
import com.denite.whattodo.models.Attraction;
import com.denite.whattodo.utils.Utils;
import com.denite.whattodo.viewmodels.AttractionFragmentViewModel;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionInflater;

import static com.denite.whattodo.utils.Constants.TYPE_HEADER;
import static com.denite.whattodo.utils.Constants.TYPE_ITEM;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AttractionListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttractionListFragment extends Fragment
{
    private final static String TAG = "AttractionListFragment";
    private View rootView;
    private AttractionFragmentViewModel viewModel;
    private AttractionAdapter attractionPreviewAdapter;
    private RecyclerView recyclerView;
    private int columnWidth;

    public AttractionListFragment()
    {
    }

    public static AttractionListFragment newInstance()
    {
        return new AttractionListFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_attraction_list, container, false);
        setup();
        return rootView;
    }

    // initialize views
    private void setup()
    {
        if (getContext() != null)
        {
            if (viewModel == null)
            {
                viewModel = new ViewModelProvider(this).get(AttractionFragmentViewModel.class);
                viewModel.init();
                viewModel.getAttractionList().observe(getViewLifecycleOwner(), new Observer<List<Attraction>>()
                {
                    @Override
                    public void onChanged(List<Attraction> attraction)
                    {
                        // update recyclerView when attraction list updated
                        attractionPreviewAdapter.notifyDataSetChanged();
                    }
                });
            }

            columnWidth = getResources().getInteger(R.integer.preview_card_width_value);
            attractionPreviewAdapter = new AttractionAdapter((MainActivity) getActivity(), this, viewModel.getAttractionList().getValue());
            // set column qty based on how many fit on the screen
            final int columns = Utils.calculateNoOfColumns(getContext(), columnWidth);
            GridLayoutManager manager = new GridLayoutManager(getContext(), columns);
            // set column span (mainly for header)
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
            {
                @Override
                public int getSpanSize(int position)
                {
                    switch (attractionPreviewAdapter.getItemViewType(position))
                    {
                        case TYPE_HEADER:
                            return columns;
                        case TYPE_ITEM:
                            return 1;
                        default:
                            return 1;
                    }
                }
            });

            recyclerView = rootView.findViewById(R.id.attractionList_recyclerView);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(attractionPreviewAdapter);
        }
    }

    public void savePrefs(Attraction attraction)
    {
        viewModel.savePrefs(attraction);
    }
}