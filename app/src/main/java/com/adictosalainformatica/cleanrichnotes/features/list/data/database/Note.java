package com.adictosalainformatica.cleanrichnotes.features.list.data.database;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.parceler.Parcel;

/**
 * A Model class that holds information about the note.
 * Class defines a table for the Room database with primary key the {@see id}.
 */

@Parcel
@Entity(tableName = DataNotesName.TABLE_NAME)
public class Note {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = DataNotesName.COL_ID)
    public int id;

    @ColumnInfo(name = DataNotesName.COL_IS_PINNED)
    public boolean isPinned;

    @ColumnInfo(name = DataNotesName.COL_TEXT)
    public String text;

    @ColumnInfo(name = DataNotesName.COL_TITLE)
    public String title;

    @ColumnInfo(name = DataNotesName.COL_IMAGE_PATH)
    public String imagePath;

    @NonNull
    @ColumnInfo(name = DataNotesName.COL_NOTE_TYPE)
    public int noteTYpe;

    @Ignore
    public Note() { }

    public Note(@NonNull int id, String title, String text, boolean isPinned, String imagePath,@NonNull int noteTYpe) {
        this.id = id;
        this.text = text;
        this.title = title;
        this.isPinned = isPinned;
        this.noteTYpe = noteTYpe;
        this.imagePath = imagePath;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public boolean isPinned() {
        return isPinned;
    }

    public void setIsPinned(boolean isPinned) {
        this.isPinned = isPinned;
    }

    public String getImagePath() {
        return imagePath;
    }
}
