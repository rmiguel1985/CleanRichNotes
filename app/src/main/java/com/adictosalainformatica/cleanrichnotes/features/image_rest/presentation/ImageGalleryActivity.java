package com.adictosalainformatica.cleanrichnotes.features.image_rest.presentation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adictosalainformatica.cleanrichnotes.R;
import com.adictosalainformatica.cleanrichnotes.features.image_rest.api.ApiClient;
import com.adictosalainformatica.cleanrichnotes.features.image_rest.api.ApiInterface;
import com.adictosalainformatica.cleanrichnotes.features.image_rest.entities.Item;
import com.adictosalainformatica.cleanrichnotes.features.image_rest.entities.RestImages;
import com.adictosalainformatica.cleanrichnotes.features.image_rest.presentation.adapter.ImageGalleryAdapter;
import com.adictosalainformatica.cleanrichnotes.utils.ConnectivityHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.perf.metrics.AddTrace;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.adictosalainformatica.cleanrichnotes.utils.Constants.PHOTO_PATH_DOWNLOADED_KEY;

public class ImageGalleryActivity extends AppCompatActivity implements ImageGalleryAdapter.OnImageGalleryListItemClickedListener {

    private ApiInterface apiService;
    private ImageGalleryAdapter adapter;
    @BindView(R.id.image_gallery_recyclerview)
    RecyclerView imageGalleryRecyclerView;
    @BindView(R.id.getString)
    EditText searchTextView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);
        ButterKnife.bind(this);

        setTitle(R.string.image_gallery_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        adapter = new ImageGalleryAdapter();
        adapter.setOnImageGalleryListItemClickedListener(this);
        imageGalleryRecyclerView.setAdapter(adapter);
        imageGalleryRecyclerView.setLayoutManager(new GridLayoutManager(this,3));

        searchTextView.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                onClick();
                handled = true;
            }
            return handled;
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick(R.id.searchBtn)
    public void onClick() {
        String searchText = searchTextView.getText().toString();
        if(ConnectivityHelper.isConnected() && !searchText.isEmpty()) {
            hideKeyboard(this);
            progressBar.setVisibility(View.VISIBLE);
            getImages(searchText);
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();

        if (view == null) {
            view = new View(activity);
        }

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @AddTrace(name = "downloadImageList")
    private void getImages(String searchText) {
        Call<RestImages> call = apiService.getImages(searchText);
        Timber.d("url: " + call.request().url().toString());
        call.enqueue(new Callback<RestImages>() {
            @Override
            public void onResponse(Call<RestImages> call, Response<RestImages> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    List<String> imageUrlList;
                    imageUrlList = getImagesUrl(response.body().getItems());

                    if (!imageUrlList.isEmpty()) {
                        adapter.setImageUrlList(imageUrlList);
                    }
                } else {
                    //request not successful (like 400,401,403 etc)
                    Timber.e("Error getting images: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<RestImages> call, Throwable t) {
                // Log error here since request failed
                progressBar.setVisibility(View.GONE);
                Timber.e("Error getting images: " + t.getMessage());
            }
        });
    }

    private List<String> getImagesUrl(List<Item> itemList) {
        List<String> imageUrl = new ArrayList<>();
        for (Item item: itemList) {
            imageUrl.add(item.getLink());
        }

        return imageUrl;
    }

    @AddTrace(name = "downloadImage")
    private void downloadImage(String url) {
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        File file=  new File(getCacheDir(), System.currentTimeMillis() + ".jpg");

                        try {
                            FileOutputStream out = new FileOutputStream(
                                    file);
                            resource.compress(
                                    Bitmap.CompressFormat.JPEG,
                                    100, out);
                            out.flush();
                            out.close();

                        } catch (FileNotFoundException e) {
                            progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
                            setResult(Activity.RESULT_CANCELED);
                            finish();
                        } catch (IOException e) {
                            progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
                            setResult(Activity.RESULT_CANCELED);
                            finish();
                        }
                        progressBar.setVisibility(View.GONE);
                        Timber.d("image downloaded");
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(PHOTO_PATH_DOWNLOADED_KEY, file.getAbsoluteFile().toString());
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }



                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    @Override
    public void onImageGalleryListItemClicked(String imageUrl) {
        downloadImage(imageUrl);
    }
}
