package com.adictosalainformatica.cleanrichnotes.base.dagger;

import android.content.Context;

import com.adictosalainformatica.cleanrichnotes.features.list.data.database.NotesDao;
import com.adictosalainformatica.cleanrichnotes.features.list.data.database.NotesDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    @Provides
    @Singleton
    public NotesDao providesDatabase(Context context) {
        return NotesDatabase.getInstance(context).notesDao();
    }
}
