package com.adictosalainformatica.cleanrichnotes.features.image_rest.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adictosalainformatica.cleanrichnotes.R;

import java.lang.ref.WeakReference;
import java.util.List;

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryListItemViewHolder>{
    private List<String> imageUrlList;
    private WeakReference<OnImageGalleryListItemClickedListener> clickedListenerRef;

    public void setOnImageGalleryListItemClickedListener(OnImageGalleryListItemClickedListener listener) {
        clickedListenerRef = new WeakReference<>(listener);
    }

    public interface OnImageGalleryListItemClickedListener {
        void onImageGalleryListItemClicked(String imageUrl);
    }

    public ImageGalleryAdapter() { }

    @Override
    public ImageGalleryListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.image_gallery_list_item, parent, false);

        final ImageGalleryListItemViewHolder holder = new ImageGalleryListItemViewHolder(itemView);

        itemView.setOnClickListener(view -> {
            if (clickedListenerRef != null) {
                OnImageGalleryListItemClickedListener listener = clickedListenerRef.get();
                if (listener != null) {
                    listener.onImageGalleryListItemClicked(imageUrlList.get(holder.getAdapterPosition()));
                }
            }
        });


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageGalleryListItemViewHolder holder, int position) {
        holder.decorate(imageUrlList.get(position));
    }

    public void setImageUrlList(List<String> imageUrlList){
        this.imageUrlList = imageUrlList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return imageUrlList != null ? imageUrlList.size() : 0;
    }
}
