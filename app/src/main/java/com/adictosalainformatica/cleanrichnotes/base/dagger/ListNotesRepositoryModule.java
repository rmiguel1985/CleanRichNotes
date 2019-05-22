package com.adictosalainformatica.cleanrichnotes.base.dagger;

import com.adictosalainformatica.cleanrichnotes.features.list.data.DataRepository;
import com.adictosalainformatica.cleanrichnotes.features.list.data.database.NotesDao;

import java.util.concurrent.ExecutorService;

import dagger.Module;
import dagger.Provides;

@Module
public class ListNotesRepositoryModule {
    @Provides
    public DataRepository providesDataRepository(NotesDao notesDao, ExecutorService executor) {
        return new DataRepository(notesDao, executor);
    }
}
