package com.adictosalainformatica.cleanrichnotes.features.list.data.database;


import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

/**
 * Room data access object.
 */
@Dao
public interface NotesDao {

    /**
     * Returns all data in table for Paging.
     */
    @Query("SELECT * FROM " + DataNotesName.TABLE_NAME + " ORDER BY " + DataNotesName.COL_IS_PINNED + " DESC")
    DataSource.Factory<Integer, Note> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Note... note);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Note note);

    @Delete
    void delete(Note note);

    @Query("SELECT * FROM " + DataNotesName.TABLE_NAME  +
            " where text LIKE :text or title like :text ORDER BY " +
            DataNotesName.COL_IS_PINNED + " DESC")
    DataSource.Factory<Integer, Note> getFilteredNotes(String text);
}
