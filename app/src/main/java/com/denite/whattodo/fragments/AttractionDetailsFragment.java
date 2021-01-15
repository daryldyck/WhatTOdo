package com.denite.whattodo.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.denite.whattodo.R;
import com.denite.whattodo.activities.MainActivity;
import com.denite.whattodo.adapters.PhotoAdapter;
import com.denite.whattodo.models.Attraction;
import com.denite.whattodo.utils.Utils;
import com.denite.whattodo.viewmodels.AttractionDetailsFragmentViewModel;
import com.denite.whattodo.viewmodels.AttractionFragmentViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionInflater;

import static com.denite.whattodo.utils.Constants.ACTION_DISPLAY_BACK_BUTTON;
import static com.denite.whattodo.utils.Constants.ACTION_LOAD_WEBVIEW;
import static com.denite.whattodo.utils.Constants.EXTRA_ATTRACTION;
import static com.denite.whattodo.utils.Constants.EXTRA_URL_STRING;


@SuppressLint("LongLogTag")
public class AttractionDetailsFragment extends Fragment
{
    private final String TAG = "AttractionDetailsFragment";
    private View rootView;
    private AttractionDetailsFragmentViewModel viewModel;
    private ImageView photoImageView;

    public AttractionDetailsFragment()
    {
    }

    public static AttractionDetailsFragment newInstance(Attraction attraction)
    {
        AttractionDetailsFragment fragment = new AttractionDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_ATTRACTION, attraction);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AttractionDetailsFragmentViewModel.class);

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
            viewModel.setAttraction((Attraction) getArguments().getSerializable(EXTRA_ATTRACTION));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_attraction_details, container, false);
        setup();
        setupBackButton();
        return rootView;
    }

    // setup and load fields with attraction data
    private void setup()
    {
        photoImageView = rootView.findViewById(R.id.photo_imageView);

        final ImageButton wishListButton = rootView.findViewById(R.id.wishList_button);
        Utils.setHaptic(wishListButton);
        wishListButton.setSelected(viewModel.getAttraction().isWishList());
        wishListButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                boolean isOnWishList = viewModel.getAttraction().isWishList();

                wishListButton.setSelected(!isOnWishList);
                viewModel.getAttraction().setWishList(!isOnWishList);

                try
                {
                    ((MainActivity) getActivity()).getViewModel().getAttractionListFragment().savePrefs(viewModel.getAttraction());
                }
                catch (NullPointerException e)
                {
                    e.printStackTrace();
                }
            }
        });

        TextView nameTextView = rootView.findViewById(R.id.name_textView);

        TextView addressTextView = rootView.findViewById(R.id.address_textView);
        addressTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });

        TextView phoneTextView = rootView.findViewById(R.id.phone_textView);
        phoneTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + viewModel.getAttraction().getPhone()));
                startActivity(intent);
            }
        });

        TextView websiteTextView = rootView.findViewById(R.id.website_textView);
        websiteTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d(TAG, "onClick: Website");
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.setAction(ACTION_LOAD_WEBVIEW);
                intent.putExtra(EXTRA_URL_STRING, viewModel.getAttraction().getWebsite());
                startActivity(intent);
            }
        });

        TextView priceTextView = rootView.findViewById(R.id.price_textView);
        TextView detailsTextView = rootView.findViewById(R.id.detailsValue_textView);

        RatingBar ratingBar = rootView.findViewById(R.id.ratingBar);
        ratingBar.setRating(viewModel.getAttraction().getRating());
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
        {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float value, boolean b)
            {
                viewModel.getAttraction().setRating((int) value);
                try
                {
                    ((MainActivity) getActivity()).getViewModel().getAttractionListFragment().savePrefs(viewModel.getAttraction());
                }
                catch (NullPointerException e)
                {
                    e.printStackTrace();
                }
            }
        });

        RecyclerView photosRecyclerView = rootView.findViewById(R.id.photos_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        photosRecyclerView.setLayoutManager(linearLayoutManager);
        PhotoAdapter photoAdapter = new PhotoAdapter(this, viewModel.getAttraction().getPhotosList());
        photosRecyclerView.setAdapter(photoAdapter);


        // used for shared element animations
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            wishListButton.setTransitionName("wishListButton" + "_" + viewModel.getAttraction().getId());
            photoImageView.setTransitionName("photoImageView" + "_" + viewModel.getAttraction().getId());
            nameTextView.setTransitionName("nameTextView" + "_" + viewModel.getAttraction().getId());
            addressTextView.setTransitionName("addressTextView" + "_" + viewModel.getAttraction().getId());
        }

        setImage(viewModel.getPhotoPosition());

        // load attraction details
        nameTextView.setText(viewModel.getAttraction().getName());
        addressTextView.setText(viewModel.getAttraction().getAddress());
        phoneTextView.setText(Utils.formatPhoneNumber(viewModel.getAttraction().getPhone()));
        websiteTextView.setText(Utils.getShorterUrl(viewModel.getAttraction().getWebsite()));
        priceTextView.setText(viewModel.getAttraction().getPrice());
        detailsTextView.setText(viewModel.getAttraction().getDescription());
    }

    // turn on back button in toolbar from MainActivity
    private void setupBackButton()
    {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setAction(ACTION_DISPLAY_BACK_BUTTON);
        startActivity(intent);
    }

    // load image with Picasso
    public void setImage(int position)
    {
        Picasso.get()
                .load(viewModel.getAttraction().getPhotosList().get(position))
                .placeholder(R.drawable.ic_attraction_default)
                .error(R.drawable.ic_attraction_default)
                .into(photoImageView);
        viewModel.setPhotoPosition(position);
    }

    @Override
    public void onDestroy()
    {
        // set image back to first image for transition back
        if (viewModel.getPhotoPosition() != 0)
        {
            setImage(0);
        }
        super.onDestroy();
    }
}