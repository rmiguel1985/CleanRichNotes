package com.adictosalainformatica.cleanrichnotes.features.list.data.database;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * A Model class that holds information about the emoji.
 * Class defines a table for the Room database with primary key the {@see #mCode}.
 */

@Entity(tableName = DataNotesName.TABLE_NAME)
public class Note {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = DataNotesName.COL_ID)
    private int id;

    @ColumnInfo(name = DataNotesName.COL_IS_PINNED)
    private boolean isPinned;

    @ColumnInfo(name = DataNotesName.COL_TEXT)
    private String text;

    @ColumnInfo(name = DataNotesName.COL_TITLE)
    private String title;

    public Note(@NonNull int id, String text, String title, boolean isPinned) {
        this.id = id;
        this.text = text;
        this.title = title;
        this.isPinned = isPinned;
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
}
