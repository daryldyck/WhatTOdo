package com.denite.whattodo.viewholders;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.denite.whattodo.R;
import com.denite.whattodo.adapters.AttractionAdapter;
import com.denite.whattodo.fragments.AttractionDetailsFragment;
import com.denite.whattodo.models.Attraction;
import com.denite.whattodo.utils.Utils;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import static com.denite.whattodo.utils.Constants.PAGE_ATTRACTION;

public class AttractionPreviewViewHolder extends RecyclerView.ViewHolder
{
    private final String TAG = "AttractionPreviewViewHolder";
    private final View view;
    private ConstraintLayout constraintLayout;
    private ImageButton wishListButton;
    private ImageView photoImageView;
    private TextView nameTextView;
    private TextView addressTextView;

    public AttractionPreviewViewHolder(@NonNull View itemView)
    {
        super(itemView);
        this.view = itemView;

        // initialize views in preview card
        constraintLayout = view.findViewById(R.id.previewMain_constraintLayout);
        wishListButton = view.findViewById(R.id.wishList_button);
        photoImageView = view.findViewById(R.id.previewPhoto_imageView);
        nameTextView = view.findViewById(R.id.previewName_textView);
        addressTextView = view.findViewById(R.id.previewAddress_textView);
    }

    // load all values into preview card for attraction
    @SuppressLint("LongLogTag")
    public void loadFields(final AttractionAdapter attractionAdapter, final Attraction attraction)
    {
        // used for shared element animations
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            wishListButton.setTransitionName("wishListButton" + "_" + attraction.getId());
            photoImageView.setTransitionName("photoImageView" + "_" + attraction.getId());
            nameTextView.setTransitionName("nameTextView" + "_" + attraction.getId());
            addressTextView.setTransitionName("addressTextView" + "_" + attraction.getId());
        }

        // set haptic vibration and listener for onClick
        Utils.setHaptic(constraintLayout);
        constraintLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AttractionDetailsFragment attractionDetailsFragment = AttractionDetailsFragment.newInstance(attraction);

                // used for shared element animations
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                {
                    attractionAdapter.getMainActivity().getSupportFragmentManager().beginTransaction()
                            .addSharedElement(wishListButton, ViewCompat.getTransitionName(wishListButton))
                            .addSharedElement(photoImageView, ViewCompat.getTransitionName(photoImageView))
                            .addSharedElement(nameTextView, ViewCompat.getTransitionName(nameTextView))
                            .addSharedElement(addressTextView, ViewCompat.getTransitionName(addressTextView))
                            .addToBackStack(null)
                            .replace(R.id.main_container, attractionDetailsFragment)
                            .commit();
                }
                else
                {
                    attractionAdapter.getMainActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_container, attractionDetailsFragment)
                            .addToBackStack(null)
                            .commit();
                }
                attractionAdapter.getMainActivity().setCurrentPage(PAGE_ATTRACTION);
            }
        });

        Utils.setHaptic(wishListButton);
        wishListButton.setSelected(attraction.isWishList());
        wishListButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                boolean isOnWishList = attraction.isWishList();

                wishListButton.setSelected(!isOnWishList);
                attraction.setWishList(!isOnWishList);
                attractionAdapter.saveAttractionPref(attraction);
            }
        });

        // load image with Picasso
        Picasso.get()
                .load(attraction.getPhotosList().get(0))
                .placeholder(R.drawable.ic_attraction_default)
                .error(R.drawable.ic_attraction_default)
                .into(photoImageView);

        // set attraction data
        nameTextView.setText(attraction.getName());
        addressTextView.setText(attraction.getAddress());
    }
}
