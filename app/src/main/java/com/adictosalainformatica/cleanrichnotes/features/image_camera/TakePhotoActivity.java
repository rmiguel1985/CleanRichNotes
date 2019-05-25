package com.adictosalainformatica.cleanrichnotes.features.image_camera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.adictosalainformatica.cleanrichnotes.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fotoapparat.Fotoapparat;
import io.fotoapparat.parameter.ScaleType;
import io.fotoapparat.result.PhotoResult;
import io.fotoapparat.view.CameraView;
import timber.log.Timber;

import static com.adictosalainformatica.cleanrichnotes.utils.Constants.PHOTO_PATH_KEY;
import static io.fotoapparat.selector.LensPositionSelectorsKt.back;

public class TakePhotoActivity extends AppCompatActivity {

    @BindView(R.id.camera_view) public CameraView cameraView;

    private Fotoapparat createFotoapparat() {
        return Fotoapparat
                .with(this)
                .into(cameraView)
                .previewScaleType(ScaleType.CenterCrop)
                .lensPosition(back())
                .cameraErrorCallback(e -> {
                    Timber.e("Error with camera:" + e.getMessage());
                })
                .build();
    }

    private Fotoapparat fotoapparat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        ButterKnife.bind(this);

        setTitle(R.string.take_photo_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fotoapparat = createFotoapparat();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        fotoapparat.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        fotoapparat.stop();
    }

    @OnClick(R.id.takePhoto)
    public void onTakePhoto() {
        PhotoResult photoResult = fotoapparat.takePicture();
        File tempFile = new File(getCacheDir(), System.currentTimeMillis() + ".jpg");

        photoResult.saveToFile(tempFile).whenDone(unit -> {
            String absolutePath = tempFile.getAbsolutePath();
            Timber.i("photo path: " + absolutePath);

            Intent returnIntent = new Intent();
            returnIntent.putExtra(PHOTO_PATH_KEY, absolutePath);

            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        });

    }
}
