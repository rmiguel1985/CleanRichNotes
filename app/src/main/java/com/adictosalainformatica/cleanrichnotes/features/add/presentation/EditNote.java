package com.adictosalainformatica.cleanrichnotes.features.add.presentation;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.adictosalainformatica.cleanrichnotes.R;
import com.adictosalainformatica.cleanrichnotes.features.add.presentation.fragments.TextNoteFragment;
import com.adictosalainformatica.cleanrichnotes.features.list.data.database.Note;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.parceler.Parcels;

import java.util.List;

import timber.log.Timber;

import static android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
import static com.adictosalainformatica.cleanrichnotes.utils.Constants.ADD_NOTE;
import static com.adictosalainformatica.cleanrichnotes.utils.Constants.DELETE_NOTE_KEY;
import static com.adictosalainformatica.cleanrichnotes.utils.Constants.EDIT_NOTE;
import static com.adictosalainformatica.cleanrichnotes.utils.Constants.NOTE_MODEL;
import static com.adictosalainformatica.cleanrichnotes.utils.Constants.PHOTO_PATH_DOWNLOADED_KEY;
import static com.adictosalainformatica.cleanrichnotes.utils.Constants.PHOTO_PATH_KEY;
import static com.adictosalainformatica.cleanrichnotes.utils.Constants.SCREEN_TYPE;
import static com.adictosalainformatica.cleanrichnotes.utils.Constants.SHOW_NOTE;

public class EditNote extends AppCompatActivity {

    private static Fragment fragment1;
    FragmentManager fm = getSupportFragmentManager();
    private static Fragment active = null;

    private boolean isTop = false;
    private String screenType;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (getIntent().getExtras() != null) {
            screenType = getIntent().getStringExtra(SCREEN_TYPE);

            if (screenType!=null) {
                if (screenType.equals(EDIT_NOTE) || screenType.equals(SHOW_NOTE)) {
                    note = Parcels.unwrap(getIntent().getParcelableExtra(NOTE_MODEL));
                    fragment1 = TextNoteFragment.newInstance(note, screenType);
                    fm.beginTransaction().add(R.id.fragment_container,fragment1, "1").commit();
                } else {
                    fragment1 = TextNoteFragment.newInstance(null, screenType);
                    fm.beginTransaction().add(R.id.fragment_container,fragment1, "1").commit();
                }
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.text_note:
                        if (active == null) {
                            fm.beginTransaction().show(fragment1).commit();
                            active = fragment1;
                        }
                        return true;
                    case R.id.take_photo_note:
                        checkCameraAndStoragePermission();

                        return true;
                    case R.id.choose_picture:
                        checkStoragePermission();
                        return true;
                }
                return false;
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(screenType.equals(EDIT_NOTE) || screenType.equals(ADD_NOTE)) {
            getMenuInflater().inflate(R.menu.menu_note_add, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_note_view, menu);
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent bundle = new Intent();
        Note note;
        switch (item.getItemId()) {
            case R.id.note_add_top:
                if (isTop){
                    item.setIcon(R.drawable.ic_menu_notop_material);
                    isTop = false;
                } else {
                    item.setIcon(R.drawable.ic_menu_top_material);
                    isTop = true;
                }

                return true;
            case R.id.note_add_save:
                note = ((TextNoteFragment)fragment1).getNote();
                note.setIsPinned(isTop);
                bundle.putExtra(NOTE_MODEL, Parcels.wrap(note));
                setResult(RESULT_OK, bundle);
                finish();

                return true;
            case R.id.menu_note_share:
                note = ((TextNoteFragment)fragment1).getNote();
                String textToShare =
                        Html.fromHtml("<p><b>" + note.getTitle() + "</b></p>" + "<small><p>" + note.getText() + "</p></small>").toString();
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_SUBJECT, R.string.share_note_title);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, textToShare);
                startActivity(Intent.createChooser(share, getResources().getString(
                        R.string.share_note_with)));
                return true;
            case R.id.menu_note_delete:
                showDeleteAlert();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (data!= null && data.hasExtra(PHOTO_PATH_KEY)) {
                String pothoPath = data.getStringExtra(PHOTO_PATH_KEY);
                ((TextNoteFragment)fragment1).setNoteImage(pothoPath);
            } else if (data != null && data.hasExtra(PHOTO_PATH_DOWNLOADED_KEY)){
                String pothoPath = data.getStringExtra(PHOTO_PATH_DOWNLOADED_KEY);
                ((TextNoteFragment)fragment1).setNoteImage(pothoPath);
            }

        }
    }

    private void showDeleteAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.dialog_delete_title);
        alertDialog.setMessage(getString(R.string.dialog_delete_text));

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.generic_ok), (dialog, which) -> {
            Intent bundle = new Intent();
            note = ((TextNoteFragment)fragment1).getNote();
            note.setIsPinned(isTop);
            bundle.putExtra(NOTE_MODEL, Parcels.wrap(note));
            bundle.putExtra(DELETE_NOTE_KEY, DELETE_NOTE_KEY);
            setResult(RESULT_OK, bundle);
            finish();
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.generic_cancel),(dialog, which) -> {
            alertDialog.dismiss();
        });

        alertDialog.show();
    }


    private void checkCameraAndStoragePermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.isAnyPermissionPermanentlyDenied()) {

                    Timber.d("num: %s", report.getDeniedPermissionResponses().size());

                    for(PermissionDeniedResponse permissionDenied: report.getDeniedPermissionResponses()){
                        if (permissionDenied.getPermissionName().equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                                permissionDenied.getPermissionName().equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                            showSettingsDialog(getString(R.string.dialog_permission_camera_storage_title));
                            break;
                        } else if (permissionDenied.getPermissionName().equals(Manifest.permission.CAMERA)) {
                            showSettingsDialog(getString(R.string.dialog_permission_camera_storage_title));
                            break;
                        }
                    }
                } else if (report.areAllPermissionsGranted()){
                    ((TextNoteFragment)fragment1).takePhoto();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }


    public void checkStoragePermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.isAnyPermissionPermanentlyDenied()) {
                    showSettingsDialog(getString(R.string.dialog_permission_storage_title));
                } else if (report.areAllPermissionsGranted()){
                    ((TextNoteFragment)fragment1).getPhoto();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {

            }
        }).check();
    }

    public void showSettingsDialog(String title) {

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(getString(R.string.dialog_permission_text));

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.generic_ok), (dialog, which) -> {
            dialog.dismiss();
            openSettings();
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.generic_cancel),(dialog, which) -> {
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    public void openSettings() {
        Intent intent = new Intent(ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
}
