package com.adictosalainformatica.cleanrichnotes.features.add.presentation.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.adictosalainformatica.cleanrichnotes.R;
import com.adictosalainformatica.cleanrichnotes.features.image_camera.TakePhotoActivity;
import com.adictosalainformatica.cleanrichnotes.features.image_rest.presentation.ImageGalleryActivity;
import com.adictosalainformatica.cleanrichnotes.features.list.data.database.Note;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.adictosalainformatica.cleanrichnotes.utils.Constants.NOTE_MODEL;
import static com.adictosalainformatica.cleanrichnotes.utils.Constants.SCREEN_TYPE;
import static com.adictosalainformatica.cleanrichnotes.utils.Constants.SHOW_NOTE;


public class TextNoteFragment extends Fragment {

    @BindView(R.id.note_image)
    ImageView noteImage;

    @BindView(R.id.note_scroll_view)
    ScrollView noteScrollView;

    @BindView(R.id.note_add_title)
    EditText noteAddTitle;

    @BindView(R.id.note_add_text)
    EditText noteAddText;

    private String imagePath = null;
    private int noteId = 0;

    public TextNoteFragment() {
        // Required empty public constructor
    }

    public static TextNoteFragment newInstance(Note note, String screenType) {
        TextNoteFragment fragment = new TextNoteFragment();
        Bundle args = new Bundle();

        if (note !=null) {
            args.putParcelable(NOTE_MODEL, Parcels.wrap(note));
        }

        args.putString(SCREEN_TYPE, screenType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String screenType = getArguments().getString(SCREEN_TYPE);

        if(screenType.equals(SHOW_NOTE)){
            noteAddTitle.setClickable(false);
            noteAddTitle.setCursorVisible(false);
            noteAddTitle.setFocusable(false);
            noteAddTitle.setFocusableInTouchMode(false);
            noteAddTitle.setKeyListener(null);

            noteAddText.setClickable(false);
            noteAddText.setCursorVisible(false);
            noteAddText.setFocusable(false);
            noteAddText.setFocusableInTouchMode(false);
            noteAddText.setKeyListener(null);
        }

        if (getArguments().containsKey(NOTE_MODEL)) {
            Note note = Parcels.unwrap(getArguments().getParcelable(NOTE_MODEL));
            noteId = note.getId();
            noteAddTitle.setText(note.getTitle());
            noteAddText.setText(note.getText());

            String imagePath = note.getImagePath();
            if(imagePath != null) {
                setNoteImage(imagePath);
            }
        }
    }

    public void takePhoto() {
        Intent i = new Intent(getActivity(), TakePhotoActivity.class);
        startActivityForResult(i, 1);
    }

    public void getPhoto() {
        Intent i = new Intent(getActivity(), ImageGalleryActivity.class);
        startActivityForResult(i, 32);
    }

    public void setNoteImage(String path) {
        imagePath = path;
        noteImage.setVisibility(View.VISIBLE);
        Glide.with(getActivity().getApplicationContext())
                .load(path)
                .apply(new RequestOptions()
                        .override(400, 400)
                        .fitCenter())
                .into(noteImage);

        noteScrollView.fullScroll(View.FOCUS_DOWN);
    }

    public Note getNote() {

       return new Note(noteId, noteAddTitle.getText().toString(),
               noteAddText.getText().toString(), false, imagePath, 0);
    }
}
