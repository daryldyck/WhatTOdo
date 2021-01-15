package com.denite.whattodo.viewholders;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.denite.whattodo.R;
import com.denite.whattodo.adapters.AttractionAdapter;
import com.denite.whattodo.adapters.PhotoAdapter;
import com.denite.whattodo.fragments.AttractionDetailsFragment;
import com.denite.whattodo.models.Attraction;
import com.denite.whattodo.utils.Utils;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import static com.denite.whattodo.utils.Constants.PAGE_ATTRACTION;

public class PhotoViewHolder extends RecyclerView.ViewHolder
{
    private final String TAG = "PhotoViewHolder";
    private final View view;
    private ImageView photoImageView;

    public PhotoViewHolder(@NonNull View itemView)
    {
        super(itemView);
        this.view = itemView;

        // initialize views in preview card
        photoImageView = view.findViewById(R.id.previewPhoto_imageView);
    }

    public void loadFields(final PhotoAdapter adapter, final String photoUrl)
    {
        Utils.setHaptic(photoImageView);
        photoImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // set image on larger main image
                adapter.getAttractionDetailsFragment().setImage(getAdapterPosition());
            }
        });

        // load image with Picasso
        Picasso.get()
                .load(photoUrl)
                .placeholder(R.drawable.ic_attraction_default)
                .error(R.drawable.ic_attraction_default)
                .into(photoImageView);
    }
}
