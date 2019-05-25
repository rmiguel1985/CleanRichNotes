package com.adictosalainformatica.cleanrichnotes.features.list.data;

import android.content.Context;

import androidx.paging.DataSource;

import com.adictosalainformatica.cleanrichnotes.features.list.data.database.Note;
import com.adictosalainformatica.cleanrichnotes.features.list.data.database.NotesDao;
import com.adictosalainformatica.cleanrichnotes.features.list.data.database.NotesDatabase;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Handles data sources and makes sure to execute on the correct thread.
 */
public class DataRepository {

    private final NotesDao mDao;
    private final ExecutorService mIoExecutor;
    private static volatile DataRepository sInstance = null;

    public static DataRepository getInstance(Context application) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    NotesDatabase database = NotesDatabase.getInstance(application);
                    sInstance = new DataRepository(database.notesDao(),
                            Executors.newSingleThreadExecutor());
                }
            }
        }
        return sInstance;
    }

    public DataRepository(NotesDao dao, ExecutorService executor) {
        mIoExecutor = executor;
        mDao = dao;
    }

    public DataSource.Factory<Integer, Note> getNotes() {
        return mDao.getAll();
    }

    public DataSource.Factory<Integer, Note> getFilteredNotes(String text) {
        return mDao.getFilteredNotes(text);
    }

    public List<Note> getWidgetNotes() {
        try {
            return mIoExecutor.submit(mDao::getWidgetNotes).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void delete(Note note) {
        mIoExecutor.execute(() -> mDao.delete(note));
    }

    public void setNote(Note note) {
        mIoExecutor.execute(() -> mDao.insert(note)); }
}
