package com.adictosalainformatica.cleanrichnotes.features.image_rest.presentation.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.adictosalainformatica.cleanrichnotes.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

class ImageGalleryListItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.image_gallery_list_item_img)
    ImageView galleryImage;

    public ImageGalleryListItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void decorate(String imageUrl) {
        Glide.with(itemView.getContext())
                .load(imageUrl)
                .apply(new RequestOptions()
                        .override(200, 200)
                        .fitCenter())
                .into(galleryImage);
    }
}
