package com.adictosalainformatica.cleanrichnotes.base.dagger;

import com.adictosalainformatica.cleanrichnotes.NotesApplication;
import com.adictosalainformatica.cleanrichnotes.features.list.presentation.ListNotesActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        NotesModule.class,
        NotesNetworkModule.class,
        ViewModelModule.class,
        DatabaseModule.class,
        ListNotesRepositoryModule.class,

})
public interface NotesComponent {
    void inject(NotesApplication avengersApplication);
    void inject(ListNotesActivity listNotesActivity);
}
